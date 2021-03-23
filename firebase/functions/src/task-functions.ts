import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import { Statistics, Task } from "./model";

export const onTaskCreate = functions.firestore
  .document("users/{userId}/tasks/{taskId}")
  .onCreate(async (snapshot, context) => {
    const task = snapshot.data() as Task;
    const statisticsRef = await admin
      .firestore()
      .doc(`users/${context.params.userId}/statistics/task`);
    const statistics = (await statisticsRef.get()).data() as Statistics;

    if (statistics.updatedAt >= task.updatedAt) {
      return;
    }
    await statisticsRef.update({
      numberOfActiveTasks: admin.firestore.FieldValue.increment(1),
      updatedAt: task.createdAt,
    });
  });

export const onTaskUpdate = functions.firestore
  .document("users/{userId}/tasks/{taskId}")
  .onUpdate(async (change, context) => {
    const oldTask = change.before.data() as Task;
    const newTask = change.after.data() as Task;
    if (newTask.completed == oldTask.completed) {
      return;
    }

    const statisticsRef = await admin
      .firestore()
      .doc(`users/${context.params.userId}/statistics/task`);

    await admin.firestore().runTransaction(async (transaction) => {
      const statistics = (
        await transaction.get(statisticsRef)
      ).data() as Statistics;
      console.log(`statistics: ${JSON.stringify(statistics)}`);
      if (statistics.updatedAt >= newTask.updatedAt) {
        return;
      }

      const { numberOfActiveTasks, numberOfCompletedTask } = statistics;
      let newStatistics: Partial<Statistics>;
      if (newTask.completed) {
        newStatistics = {
          numberOfActiveTasks: Math.max(numberOfActiveTasks - 1, 0),
          numberOfCompletedTask: numberOfCompletedTask + 1,
        };
        console.log("numberOfActiveTasks:-1, numberOfCompletedTask:+1");
      } else {
        newStatistics = {
          numberOfActiveTasks: numberOfActiveTasks + 1,
          numberOfCompletedTask: Math.max(numberOfCompletedTask - 1, 0),
        };
        console.log("numberOfActiveTasks:+1, numberOfCompletedTask:-1");
      }

      await transaction.set(statisticsRef, {
        ...newStatistics,
        updatedAt: newTask.updatedAt,
      } as Partial<Statistics>);
    });
  });

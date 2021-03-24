import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import { DocumentData, Statistics, Task } from "./model";

export const onCreateTask = functions.firestore
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
    } as DocumentData<Statistics>);
  });

export const onUpdateTask = functions.firestore
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

      const {
        numberOfActiveTasks,
        numberOfCompletedTasks: numberOfCompletedTask,
      } = statistics;
      let newStatistics: DocumentData<Statistics>;
      if (newTask.completed) {
        newStatistics = {
          numberOfActiveTasks: Math.max(numberOfActiveTasks - 1, 0),
          numberOfCompletedTasks: numberOfCompletedTask + 1,
        };
        console.log("numberOfActiveTasks:-1, numberOfCompletedTask:+1");
      } else {
        newStatistics = {
          numberOfActiveTasks: numberOfActiveTasks + 1,
          numberOfCompletedTasks: Math.max(numberOfCompletedTask - 1, 0),
        };
        console.log("numberOfActiveTasks:+1, numberOfCompletedTask:-1");
      }

      transaction.set(statisticsRef, {
        ...newStatistics,
        updatedAt: newTask.updatedAt,
      } as DocumentData<Statistics>);
    });
  });


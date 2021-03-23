import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import { Statistics, Task } from "./model";

export const onTaskCreate = functions.firestore
  .document("users/{userId}/tasks/{taskId}")
  .onCreate(async (snapshot, context) => {
    const statisticsRef = await admin
      .firestore()
      .doc(`users/${context.params.userId}/statistics/task`);

    const statistics = (await statisticsRef.get()).data() as Statistics;
    const task = snapshot.data() as Task;
    if (statistics.updatedAt > task.updatedAt) {
      return;
    }
    statisticsRef.update({
      numberOfActiveTasks: admin.firestore.FieldValue.increment(1),
      updatedAt: task.createdAt,
    } as Partial<Statistics>);
  });

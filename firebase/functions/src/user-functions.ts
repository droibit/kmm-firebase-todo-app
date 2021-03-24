import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import { DocumentData, Statistics, User } from "./model";

export const onCreateAuthUser = functions.auth
  .user()
  .onCreate(async (user, context) => {
    const batch = admin.firestore().batch();

    const newUserRef = admin.firestore().doc(`users/${user.uid}`);
    const newStatisticsRef = newUserRef.collection("statistics").doc("task");
    batch
      .create(newUserRef, {
        name: user.displayName!,
        photoURL: user.photoURL,
      } as DocumentData<User>)
      .create(newStatisticsRef, {
        numberOfActiveTasks: 0,
        numberOfCompletedTasks: 0,
        updatedAt: admin.firestore.FieldValue.serverTimestamp(),
      } as DocumentData<Statistics>);

    await batch.commit();
  });

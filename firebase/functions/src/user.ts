import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import { Statistics, User } from "./model";

export const onAuthUserCreate = functions.auth.user().onCreate(async (user) => {
  const batch = admin.firestore().batch();

  const newUser: User = {
    name: user.displayName!,
    photoURL: user.photoURL,
  };
  const newUserRef = admin.firestore().doc(`users/${user.uid}`);
  newUserRef.set(newUser);

  const initialStatistics: Statistics = {
    numberOfActiveTasks: 0,
    numberOfCompletedTask: 0,
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
  };
  newUserRef.collection("statistics").doc("task").set(initialStatistics);

  await batch.commit();
});

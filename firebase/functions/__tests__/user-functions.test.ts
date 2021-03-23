import * as testInitializer from "firebase-functions-test";
import * as admin from "firebase-admin";
import { Statistics, User } from "../src/model";
import * as userFunctions from "../src/user-functions";

// ref. https://github.com/firebase/quickstart-testing/tree/master/unit-test-cloud-functions

const functionsTest = testInitializer({
  projectId: process.env.TEST_FIREBASE_PROJECt_ID,
});

beforeAll(() => {
  const app = admin.initializeApp();
  console.log(`projectId: ${app.options.projectId}`);
});

afterAll(() => {
  functionsTest.cleanup();
});

describe("#onAuthUserCreate", () => {
  test("Firebaseユーザ生成時にユーザの初期設定を行うこと", async () => {
    const wrapped = functionsTest.wrap(userFunctions.onAuthUserCreate);
    const uid = `${new Date().getTime()}`;
    const displayName = "Taro Tanaka";
    const photoURL = "https://example.com/profile.jpg";
    const user = functionsTest.auth.makeUserRecord({
      uid,
      displayName,
      photoURL,
    });

    await wrapped(user);

    const userSnapshot = await admin.firestore().doc(`users/${uid}`).get();
    expect(userSnapshot.data()).toEqual({
      name: displayName,
      photoURL,
    } as User);

    const statisticsSnapshot = await admin
      .firestore()
      .doc(`users/${uid}/statistics/task`)
      .get();
    const actualStatistics = statisticsSnapshot.data() as Statistics;
    expect(actualStatistics.numberOfActiveTasks).toBe(0);
    expect(actualStatistics.numberOfCompletedTask).toBe(0);
  });
});

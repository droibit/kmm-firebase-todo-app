import * as testInitializer from "firebase-functions-test";
import * as admin from "firebase-admin";
import { Statistics, Task } from "../src/model";
import * as taskFunctions from "../src/task-functions";

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

test("Taskの書き込み操作により統計が更新されること", async () => {
  const auth = { uid: `${new Date().getTime()}` };
  const statisticsRef = admin
    .firestore()
    .doc(`users/${auth.uid}/statistics/task`);

  await statisticsRef.set({
    numberOfActiveTasks: 0,
    numberOfCompletedTask: 0,
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
  } as Statistics);

  const onCreateWrapped = functionsTest.wrap(taskFunctions.onTaskCreate);

  // Create/Update at 2021-03-23 00:00:00.
  const task: Task = {
    title: "Test Task",
    description: "",
    completed: false,
    createdAt: FirebaseFirestore.Timestamp.fromMillis(1616425200),
    updatedAt: FirebaseFirestore.Timestamp.fromMillis(1616425200),
  };

  await onCreateWrapped(task);

  const actualStatistics1 = (await statisticsRef.get()).data() as Statistics;
  expect(actualStatistics1).toEqual({
    numberOfActiveTasks: 1,
    numberOfCompletedTask: 0,
    updatedAt: task.createdAt,
  } as Statistics);
});

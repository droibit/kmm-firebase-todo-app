import * as admin from "firebase-admin";
import * as testInitializer from "firebase-functions-test";
import { ContextOptions } from "firebase-functions-test/lib/main";
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

test("Task作成時に統計が更新されること", async () => {
  const auth = { uid: `${new Date().getTime()}` };
  const statisticsRef = admin
    .firestore()
    .doc(`users/${auth.uid}/statistics/task`);

  await statisticsRef.set({
    numberOfActiveTasks: 0,
    numberOfCompletedTask: 0,
    updatedAt: admin.firestore.Timestamp.fromMillis(1616425199),
  } as Statistics);

  const onCreateWrapped = functionsTest.wrap(taskFunctions.onTaskCreate);

  // Create/Update at 2021-03-23 00:00:00.
  const initialTask: Task = {
    title: "Test Task",
    description: "",
    completed: false,
    createdAt: admin.firestore.Timestamp.fromMillis(1616425200),
    updatedAt: admin.firestore.Timestamp.fromMillis(1616425200),
  };
  const taskSnapshot = functionsTest.firestore.makeDocumentSnapshot(
    initialTask,
    `users/${auth.uid}/tasks/${auth.uid}`
  );

  const context: ContextOptions = {
    params: {
      userId: auth.uid,
      taskId: auth.uid,
    },
  };
  await onCreateWrapped(taskSnapshot, context);
  await onCreateWrapped(taskSnapshot, context);

  const actualStatistics1 = (await statisticsRef.get()).data() as Statistics;
  expect(actualStatistics1).toEqual({
    numberOfActiveTasks: 1,
    numberOfCompletedTask: 0,
    updatedAt: initialTask.createdAt,
  } as Statistics);
});

describe("#onUpdateTask", () => {
  // Create/Update at 2021-03-23 00:00:00.
  const initialTask: Task = {
    title: "Test Task",
    description: "",
    completed: false,
    createdAt: admin.firestore.Timestamp.fromMillis(1616425200),
    updatedAt: admin.firestore.Timestamp.fromMillis(1616425200),
  };
  const taskId = "task-test";

  test("Taskの完了状態更新時に統計が更新されること", async () => {
    const userId = `${new Date().getTime()}`;
    const context: ContextOptions = {
      params: { userId, taskId },
    };

    const statisticsRef = admin
      .firestore()
      .doc(`users/${userId}/statistics/task`);
    await statisticsRef.set({
      numberOfActiveTasks: 1,
      numberOfCompletedTask: 0,
      updatedAt: initialTask.updatedAt,
    } as Statistics);

    const onUpdateWrapped = functionsTest.wrap(taskFunctions.onTaskUpdate);

    const completedTask: Task = {
      ...initialTask,
      completed: true,
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425300),
    };
    const changeToBeCompleted = functionsTest.makeChange(
      functionsTest.firestore.makeDocumentSnapshot(
        initialTask,
        `users/${userId}/tasks/${taskId}`
      ),
      functionsTest.firestore.makeDocumentSnapshot(
        completedTask,
        `users/${userId}/tasks/${taskId}`
      )
    );
    await onUpdateWrapped(changeToBeCompleted, context);
    await onUpdateWrapped(changeToBeCompleted, context);

    expect((await statisticsRef.get()).data() as Statistics).toEqual({
      numberOfActiveTasks: 0,
      numberOfCompletedTask: 1,
      updatedAt: completedTask.updatedAt,
    } as Statistics);

    const activeTask: Task = {
      ...initialTask,
      completed: false,
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425400),
    };
    const changeToBeActive = functionsTest.makeChange(
      functionsTest.firestore.makeDocumentSnapshot(
        completedTask,
        `users/${userId}/tasks/${taskId}`
      ),
      functionsTest.firestore.makeDocumentSnapshot(
        activeTask,
        `users/${userId}/tasks/${taskId}`
      )
    );
    await onUpdateWrapped(changeToBeActive, context);

    expect((await statisticsRef.get()).data() as Statistics).toEqual({
      numberOfActiveTasks: 1,
      numberOfCompletedTask: 0,
      updatedAt: activeTask.updatedAt,
    } as Statistics);
  });

  test("Taskの完了状態未更新時は統計が更新されないこと", async () => {
    const userId = `${new Date().getTime()}`;
    const statisticsRef = admin
      .firestore()
      .doc(`users/${userId}/statistics/task`);
    await statisticsRef.set({
      numberOfActiveTasks: 1,
      numberOfCompletedTask: 0,
      updatedAt: initialTask.updatedAt,
    } as Statistics);

    const onUpdateWrapped = functionsTest.wrap(taskFunctions.onTaskUpdate);

    const updateTask: Task = {
      ...initialTask,
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425300),
    };
    const changeToBeCompleted = functionsTest.makeChange(
      functionsTest.firestore.makeDocumentSnapshot(
        initialTask,
        `users/${userId}/tasks/${taskId}`
      ),
      functionsTest.firestore.makeDocumentSnapshot(
        updateTask,
        `users/${userId}/tasks/${taskId}`
      )
    );

    await onUpdateWrapped(changeToBeCompleted, {
      params: { userId, taskId },
    });

    expect((await statisticsRef.get()).data() as Statistics).toEqual({
      numberOfActiveTasks: 1,
      numberOfCompletedTask: 0,
      updatedAt: initialTask.updatedAt,
    } as Statistics);
  });
});

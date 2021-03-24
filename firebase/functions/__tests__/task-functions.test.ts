import * as admin from "firebase-admin";
import * as testInitializer from "firebase-functions-test";
import { ContextOptions } from "firebase-functions-test/lib/main";
import { DocumentData, Statistics, Task } from "../src/model";
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

describe("#onUpdateTask", () => {
  test("Task作成時に統計が更新されること", async () => {
    const auth = { uid: `${new Date().getTime()}` };
    const statisticsRef = admin
      .firestore()
      .doc(`users/${auth.uid}/statistics/task`);

    await statisticsRef.set({
      numberOfActiveTasks: 0,
      numberOfCompletedTasks: 0,
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425199),
    } as DocumentData<Statistics>);

    const onCreateWrapped = functionsTest.wrap(taskFunctions.onCreateTask);

    // Create/Update at 2021-03-23 00:00:00.
    const initialTask: DocumentData<Task> = {
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

    const actualStatistics = (await statisticsRef.get()).data() as Statistics;
    expect(actualStatistics).toEqual({
      numberOfActiveTasks: 1,
      numberOfCompletedTasks: 0,
      updatedAt: initialTask.createdAt,
    } as Statistics);
  });
});

describe("#onUpdateTask", () => {
  // Create/Update at 2021-03-23 00:00:00.
  const initialTask: DocumentData<Task> = {
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
      numberOfCompletedTasks: 0,
      updatedAt: initialTask.updatedAt,
    } as DocumentData<Statistics>);

    const onUpdateWrapped = functionsTest.wrap(taskFunctions.onUpdateTask);

    const completedTask: DocumentData<Task> = {
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
      numberOfCompletedTasks: 1,
      updatedAt: completedTask.updatedAt,
    } as Statistics);

    const activeTask: DocumentData<Task> = {
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
      numberOfCompletedTasks: 0,
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
      numberOfCompletedTasks: 0,
      updatedAt: initialTask.updatedAt,
    } as DocumentData<Statistics>);

    const onUpdateWrapped = functionsTest.wrap(taskFunctions.onUpdateTask);

    const updateTask: DocumentData<Task> = {
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
      numberOfCompletedTasks: 0,
      updatedAt: initialTask.updatedAt,
    } as Statistics);
  });
});

describe("#onDeleteTask", () => {
  test("タスク削除時に統計が更新されること", async () => {
    const userId = `${new Date().getTime()}`;
    // Create/Update at 2021-03-23 00:00:00.
    const activeTask: DocumentData<Task> = {
      title: "Task",
      description: "",
      completed: false,
      createdAt: admin.firestore.Timestamp.fromMillis(1616425200),
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425200),
    };
    const activeTaskId = `${new Date().getTime()}`;
    await admin
      .firestore()
      .doc(`users/${userId}/tasks/${activeTaskId}`)
      .set(activeTask);

    const completedTask: DocumentData<Task> = {
      ...activeTask,
      completed: true,
    };
    const completedTaskId = `${new Date().getTime()}`;
    await admin
      .firestore()
      .doc(`users/${userId}/tasks/${completedTaskId}`)
      .set(completedTask);

    const statisticsRef = admin
      .firestore()
      .doc(`users/${userId}/statistics/task`);
    await statisticsRef.set({
      numberOfActiveTasks: 1,
      numberOfCompletedTasks: 1,
      updatedAt: admin.firestore.Timestamp.fromMillis(1616425199),
    } as DocumentData<Statistics>);

    const onDeleteWrapped = functionsTest.wrap(taskFunctions.onDeleteTask);

    const activeTaskSnapshot = functionsTest.firestore.makeDocumentSnapshot(
      activeTask,
      `users/${userId}/tasks/${activeTaskId}`
    );
    await onDeleteWrapped(activeTaskSnapshot, {
      params: {
        userId,
        taskId: activeTaskId,
      },
    });

    let actualStatistics = (await statisticsRef.get()).data() as Statistics;
    expect(actualStatistics.numberOfActiveTasks).toBe(0);
    expect(actualStatistics.numberOfCompletedTasks).toBe(1);

    const completedTaskSnapshot = functionsTest.firestore.makeDocumentSnapshot(
      completedTask,
      `users/${userId}/tasks/${completedTaskId}`
    );
    await onDeleteWrapped(completedTaskSnapshot, {
      params: {
        userId,
        taskId: completedTaskId,
      },
    });
    actualStatistics = (await statisticsRef.get()).data() as Statistics;
    expect(actualStatistics.numberOfActiveTasks).toBe(0);
    expect(actualStatistics.numberOfCompletedTasks).toBe(0);

    const deletedTaskRef = await admin
      .firestore()
      .doc(`users/${userId}/tasks/deleted`);
    await onDeleteWrapped(deletedTaskRef.get(), {
      params: {
        userId,
        taskId: "deleted",
      },
    });
    actualStatistics = (await statisticsRef.get()).data() as Statistics;
    expect(actualStatistics.numberOfActiveTasks).toBe(0);
    expect(actualStatistics.numberOfCompletedTasks).toBe(0);
  });
});

import * as firebase from "@firebase/rules-unit-testing";
import { TokenOptions } from "@firebase/rules-unit-testing/dist/src/api";
import * as fs from "fs";
import * as http from "http";
import {
  PROJECT_ID,
  COVERAGE_URL,
  authedFirestore,
  adminFirestore,
} from "./firestore-utils";

beforeAll(async () => {
  // Load the rules file before the tests begin
  const rules = fs.readFileSync("firestore.rules", "utf8");
  await firebase.loadFirestoreRules({ projectId: PROJECT_ID, rules });
});

beforeEach(async () => {
  // Clear the database between tests
  await firebase.clearFirestoreData({ projectId: PROJECT_ID });
});

afterAll(async () => {
  // Delete all the FirebaseApp instances created during testing
  // Note: this does not affect or clear any data
  await Promise.all(firebase.apps().map((app) => app.delete()));

  // Write the coverage report to a file
  const coverageFile = "firestore-coverage.html";
  const fstream = fs.createWriteStream(coverageFile);
  await new Promise((resolve, reject) => {
    http.get(COVERAGE_URL, (res) => {
      res.pipe(fstream, { end: true });

      res.on("end", resolve);
      res.on("error", reject);
    });
  });

  console.log(`View firestore rule coverage information at ${coverageFile}\n`);
});

describe("Test `users` collection", () => {
  const testAuth: TokenOptions = { uid: "alice" };

  describe("Read operation", () => {
    test("未認証ユーザはユーザ情報を取得できないこと", async () => {
      const db = authedFirestore(null);
      const userRef = db.doc(`users/${testAuth.uid}`);
      await firebase.assertFails(userRef.get());
    });

    test("他ユーザのユーザ情報を取得できないこと", async () => {
      const admin = adminFirestore();
      await firebase.assertSucceeds(
        admin.doc(`users/${testAuth.uid}`).set({ name: "Alice" })
      );

      const db = authedFirestore({ uid: "bob" });
      const userRef = db.doc(`users/${testAuth.uid}`);
      await firebase.assertFails(userRef.get());
    });

    test("認証済みユーザは自分のユーザ情報を取得できること", async () => {
      const admin = adminFirestore();
      await firebase.assertSucceeds(
        admin.doc(`users/${testAuth.uid}`).set({ name: "Alice" })
      );

      const db = authedFirestore(testAuth);
      const userRef = db.doc(`users/${testAuth.uid}`);
      await firebase.assertSucceeds(userRef.get());
    });
  });
});

describe("Test `task` collection", () => {
  const testAuth: TokenOptions = { uid: "alice" };

  describe("Read operation", () => {
    test("未認証ユーザはタスクを取得できないこと", async () => {
      const db = authedFirestore(null);

      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);
      await firebase.assertFails(tasksRef.get());

      const taskRef = tasksRef.doc("task-1");
      await firebase.assertFails(taskRef.get());
    });

    test("他ユーザのタスクを取得できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });

      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);
      await firebase.assertFails(tasksRef.get());

      const taskRef = tasksRef.doc("task-1");
      await firebase.assertFails(taskRef.get());
    });

    test("自ユーザのタスクを取得できること", async () => {
      const db = authedFirestore(testAuth);

      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);
      await firebase.assertSucceeds(tasksRef.get());

      const taskRef = tasksRef.doc("task-1");
      await firebase.assertSucceeds(taskRef.get());
    });
  });

  describe("Create operation", () => {
    describe("Schema verification", () => {
      test("ドキュメントのキーが規定数ではない場合エラーとなること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "1",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
        await firebase.assertFails(
          tasksRef.add({
            title: "1",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
            unknownField: "Error",
          })
        );
      });

      test("titleのがstring型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: 1,
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionがstring型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: 1,
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("completedがbool型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: "false",
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("createdAtがtimestamp型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: false,
            createdAt: "0",
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("updatedAtがtimestamp型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: "0",
          })
        );
      });
    });

    describe("Data validation", () => {
      test("titleが1文字以上100以下ではない場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
        await firebase.assertFails(
          tasksRef.add({
            title: "a".repeat(101),
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionが0文字以上500以下ではない場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "a".repeat(501),
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("completedがtrueの場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: true,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      // TODO: Is timestamp validation possible?
    });

    test("未認証ユーザはタスクを作成できないこと", async () => {
      const db = authedFirestore(null);
      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

      await firebase.assertFails(
        tasksRef.add({
          title: "Error",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("他ユーザのタスクを作成できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

      await firebase.assertFails(
        tasksRef.add({
          title: "Error",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("正常なタスクの場合は作成に成功すること", async () => {
      const db = authedFirestore(testAuth);
      const tasksRef = db.collection(`users/${testAuth.uid}/tasks`);

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "1",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "a".repeat(100),
          description: "b".repeat(500),
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });
  });

  describe("Update Operation", () => {
    const testTaskId = "task-1";

    beforeEach(async () => {
      const db = authedFirestore(testAuth);
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertSucceeds(
        taskRef.set({
          title: "Task-1",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    describe("Schema verification", () => {
      test("ドキュメントのキーが規定数ではない場合エラーとなること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            title: "Task-1",
            description: "",
            completed: false,
            unknownField1: "",
            unknownField2: "",
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("titleのがstring型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            title: 1,
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionがstring型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            description: 1,
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("completedがbool型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            completed: "false",
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("createdAtがtimestamp型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            createdAt: "0",
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("updateddAtがtimestamp型では無い場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(taskRef.update({ updatedAt: "0" }));
      });
    });

    describe("Data verification", () => {
      test("titleが1文字以上100以下ではない場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            title: "",
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
        await firebase.assertFails(
          taskRef.update({
            title: "a".repeat(101),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionが0文字以上500以下ではない場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            description: "a".repeat(501),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("createdAtが変更されている場合エラーになること", async () => {
        const db = authedFirestore(testAuth);
        const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

        await firebase.assertFails(
          taskRef.update({
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });
    });

    test("未認証ユーザはタスクを更新できないこと", async () => {
      const db = authedFirestore(null);
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertFails(
        taskRef.update({
          completed: true,
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("他ユーザのタスクを更新できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertFails(
        taskRef.update({
          completed: true,
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("正常なタスクの場合は更新に成功すること", async () => {
      const db = authedFirestore(testAuth);
      const tasksRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertSucceeds(
        tasksRef.update({
          title: "a",
          description: "",
          completed: true,
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );

      await firebase.assertSucceeds(
        tasksRef.update({
          title: "a".repeat(100),
          description: "b".repeat(500),
          completed: false,
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });
  });

  describe("Delete Operation", () => {
    const testTaskId = "task-1";

    beforeEach(async () => {
      const db = authedFirestore(testAuth);
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertSucceeds(
        taskRef.set({
          title: "Task-1",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("未認証ユーザはタスクを削除できないこと", async () => {
      const db = authedFirestore(null);
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertFails(taskRef.delete());
    });

    test("他ユーザのタスクを削除できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const taskRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertFails(taskRef.delete());
    });

    test("正常なタスクの場合は削除に成功すること", async () => {
      const db = authedFirestore(testAuth);
      const tasksRef = db.doc(`users/${testAuth.uid}/tasks/${testTaskId}`);

      await firebase.assertSucceeds(tasksRef.delete());
    });
  });
});

describe("Test `statistics` document", () => {
  const testAuth: TokenOptions = { uid: "alice" };

  describe("Read operation", () => {
    test("未認証ユーザはタスク統計を取得できないこと", async () => {
      const db = authedFirestore(null);
      const statisticsRef = db.doc("users/alice/statistics/task");
      await firebase.assertFails(statisticsRef.get());
    });

    test("他ユーザのタスク統計を取得できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const statisticsRef = db.doc(`users/${testAuth.uid}/statistics/task`);
      await firebase.assertFails(statisticsRef.get());
    });

    test("タスク統計以外取得できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const statisticsRef = db.doc(`users/${testAuth.uid}/statistics/task`);
      await firebase.assertFails(statisticsRef.get());
    });

    test("自ユーザのタスク統計を取得できること", async () => {
      const db = authedFirestore(testAuth);
      const statisticsRef = db.doc(`users/${testAuth.uid}/statistics/task`);
      await firebase.assertSucceeds(statisticsRef.get());
    });
  });
});

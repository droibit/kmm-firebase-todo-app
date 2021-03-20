import * as firebase from "@firebase/rules-unit-testing";
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
  describe("Read operation", () => {
    test("未認証ユーザはユーザ情報を取得できないこと", async () => {
      const db = authedFirestore(null);
      const user = db.collection("users").doc("alice");
      await firebase.assertFails(user.get());
    });

    test("他ユーザのユーザ情報を取得できないこと", async () => {
      const admin = adminFirestore();
      await firebase.assertSucceeds(
        admin.collection("users").doc("alice").set({ name: "Alice" })
      );

      const db = authedFirestore({ uid: "bob" });
      const user = db.collection("users").doc("alice");
      await firebase.assertFails(user.get());
    });

    test("認証済みユーザは自分のユーザ情報を取得できること", async () => {
      const auth = { uid: "alice" };
      const admin = adminFirestore();
      await firebase.assertSucceeds(
        admin.collection("users").doc(auth.uid).set({ name: "Alice" })
      );

      const db = authedFirestore(auth);
      const user = db.collection("users").doc(auth.uid);
      await firebase.assertSucceeds(user.get());
    });
  });
});

describe("Test `task` collection", () => {
  describe("Read operation", () => {
    test("未認証ユーザはタスクを取得できないこと", async () => {
      const db = authedFirestore(null);
      const tasksRef = db.collection("users").doc("alice").collection("tasks");
      await firebase.assertFails(tasksRef.get());

      const task = tasksRef.doc("task-1");
      await firebase.assertFails(task.get());
    });

    test("他ユーザのタスクを取得できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const tasksRef = db.collection("users").doc("alice").collection("tasks");
      await firebase.assertFails(tasksRef.get());

      const task = tasksRef.doc("task-1");
      await firebase.assertFails(task.get());
    });

    test("自ユーザのタスクを取得できること", async () => {
      const auth = { uid: "alice" };
      const db = authedFirestore(auth);
      const tasksRef = db.collection("users").doc(auth.uid).collection("tasks");
      await firebase.assertSucceeds(tasksRef.get());

      const task = tasksRef.doc("task-1");
      await firebase.assertSucceeds(task.get());
    });
  });

  describe("Create operation", () => {
    describe("Schema verification", () => {
      test("ドキュメントのキーが4つではない場合エラーとなること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
          })
        );
        await firebase.assertFails(
          tasksRef.add({
            title: "1",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
            unknownField: "Error",
          })
        );
      });

      test("ドキュメントに未定義のキーが含まれている場合エラーとなること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "1",
            description: "",
            completed: false,
            unknownField: "Error",
          })
        );
      });

      test("titleのがstring型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: 1,
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionがstring型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: 1,
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("completedがbool型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: "false",
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("createdAtがtimestamp型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: false,
            createdAt: "0",
          })
        );
      });
    });

    describe("Data validation", () => {
      test("titleが1文字以上100以下ではない場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "",
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
        await firebase.assertFails(
          tasksRef.add({
            title: "a".repeat(101),
            description: "",
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("descriptionが0文字以上500以下ではない場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "a".repeat(501),
            completed: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("completedがtrueの場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const tasksRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks");
        await firebase.assertFails(
          tasksRef.add({
            title: "Error",
            description: "",
            completed: true,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      // TODO: Is createdAt data validation possible?
    });

    test("未認証ユーザはタスクを作成できないこと", async () => {
      const db = authedFirestore(null);
      const tasksRef = db.collection("users").doc("alice").collection("tasks");

      await firebase.assertFails(
        tasksRef.add({
          title: "Error",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("他ユーザのタスクを作成できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const tasksRef = db.collection("users").doc("alice").collection("tasks");

      await firebase.assertFails(
        tasksRef.add({
          title: "Error",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    test("正常なタスクの場合は作成に成功すること", async () => {
      const auth = { uid: "alice" };
      const db = authedFirestore(auth);
      const tasksRef = db.collection("users").doc(auth.uid).collection("tasks");

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "1",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "a".repeat(100),
          description: "b".repeat(500),
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });
  });

  describe("Update Operation", () => {
    const auth = { uid: "alice" };
    const testTaskId = "task-1";

    beforeEach(async () => {
      const db = authedFirestore(auth);
      const taskRef = db
        .collection("users")
        .doc(auth.uid)
        .collection("tasks")
        .doc(testTaskId);
      await firebase.assertSucceeds(
        taskRef.set({
          title: "Task-1",
          description: "",
          completed: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });

    describe("Schema verification", () => {
      test("ドキュメントのキーが4つではない場合エラーとなること", async () => {
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(
          taskRef.update({
            title: "Task-1",
            description: "",
            completed: false,
            unknownField1: "",
            unknownField2: "",
          })
        );
      });

      test("ドキュメントに未定義のキーが含まれている場合エラーとなること", async () => {
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(
          taskRef.update({
            title: "Task-1",
            description: "",
            completed: false,
            unknownField: "",
          })
        );
      });

      test("titleのがstring型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(taskRef.update({ title: 1 }));
      });

      test("descriptionがstring型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(taskRef.update({ description: 1 }));
      });

      test("completedがbool型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(taskRef.update({ completed: "false" }));
      });

      test("createdAtがtimestamp型では無い場合エラーになること", async () => {
        const auth = { uid: "alice" };
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(taskRef.update({ createdAt: "0" }));
      });
    });

    describe("Data verification", () => {
      test("titleが1文字以上100以下ではない場合エラーになること", async () => {
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(taskRef.update({ title: "" }));
        await firebase.assertFails(taskRef.update({ title: "a".repeat(101) }));
      });

      test("descriptionが0文字以上500以下ではない場合エラーになること", async () => {
        const db = authedFirestore(auth);
        const taskRef = db
          .collection("users")
          .doc(auth.uid)
          .collection("tasks")
          .doc(testTaskId);
        await firebase.assertFails(
          taskRef.update({ description: "a".repeat(501) })
        );
      });
    });

    test("未認証ユーザはタスクを更新できないこと", async () => {
      const db = authedFirestore(null);
      const taskRef = db
        .collection("users")
        .doc(auth.uid)
        .collection("tasks")
        .doc(testTaskId);

      await firebase.assertFails(taskRef.update({ completed: true }));
    });

    test("他ユーザのタスクを更新できないこと", async () => {
      const db = authedFirestore({ uid: "bob" });
      const taskRef = db
        .collection("users")
        .doc(auth.uid)
        .collection("tasks")
        .doc(testTaskId);

      await firebase.assertFails(taskRef.update({ completed: true }));
    });

    test("正常なタスクの場合は更新に成功すること", async () => {
      const db = authedFirestore(auth);
      const tasksRef = db
        .collection("users")
        .doc(auth.uid)
        .collection("tasks")
        .doc(testTaskId);

      await firebase.assertSucceeds(
        tasksRef.update({
          title: "a",
          description: "",
          completed: true,
        })
      );

      await firebase.assertSucceeds(
        tasksRef.update({
          title: "a".repeat(100),
          description: "b".repeat(500),
          completed: false,
        })
      );
    });
  });
});

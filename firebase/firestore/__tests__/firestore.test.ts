import * as firebase from "@firebase/rules-unit-testing";
import * as fs from "fs";
import * as http from "http";

// ref. https://github.com/firebase/quickstart-testing/blob/master/unit-test-security-rules/test/firestore.spec.js

/**
 * The emulator will accept any project ID for testing.
 */
const PROJECT_ID = "firebase-todo";

/**
 * The FIRESTORE_EMULATOR_HOST environment variable is set automatically
 * by "firebase emulators:exec"
 */
const COVERAGE_URL = `http://${process.env.FIRESTORE_EMULATOR_HOST}/emulator/v1/projects/${PROJECT_ID}:ruleCoverage.html`;

function authedFirestore(auth: any) {
  return firebase
    .initializeTestApp({ projectId: PROJECT_ID, auth })
    .firestore();
}

function adminFirestore() {
  return firebase.initializeAdminApp({ projectId: PROJECT_ID }).firestore();
}

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

// // TODO: Share types with functions project.
// interface Task {
//   title: string;
//   description: string;
//   isCompleted: boolean;
//   createdAt: any;
// }

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
            title1: "Error",
            title2: "Error",
            title3: "Error",
            title4: "Error",
            title5: "Error",
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
            isCompleted: false,
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
            isCompleted: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("isCompletedがbool型では無い場合エラーになること", async () => {
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
            isCompleted: "false",
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
            isCompleted: false,
            createdAt: 0,
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
            isCompleted: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
        await firebase.assertFails(
          tasksRef.add({
            title: "a".repeat(101),
            description: "",
            isCompleted: false,
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
            isCompleted: false,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      test("isCompletedがtrueの場合エラーになること", async () => {
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
            isCompleted: true,
            createdAt: firebase.firestore.FieldValue.serverTimestamp(),
          })
        );
      });

      // TODO: Is createdAt data validation possible?
    });

    test("正常なタスクの場合は作成に成功すること", async () => {
      const auth = { uid: "alice" };
      const db = authedFirestore(auth);
      const tasksRef = db.collection("users").doc(auth.uid).collection("tasks");

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "1",
          description: "",
          isCompleted: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );

      await firebase.assertSucceeds(
        tasksRef.add({
          title: "a".repeat(100),
          description: "b".repeat(500),
          isCompleted: false,
          createdAt: firebase.firestore.FieldValue.serverTimestamp(),
        })
      );
    });
  });
});

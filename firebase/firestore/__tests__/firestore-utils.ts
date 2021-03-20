import * as firebase from "@firebase/rules-unit-testing";

// ref. https://github.com/firebase/quickstart-testing/blob/master/unit-test-security-rules/test/firestore.spec.js

/**
 * The emulator will accept any project ID for testing.
 */
export const PROJECT_ID = "firebase-todo";

/**
 * The FIRESTORE_EMULATOR_HOST environment variable is set automatically
 * by "firebase emulators:exec"
 */
export const COVERAGE_URL = `http://${process.env.FIRESTORE_EMULATOR_HOST}/emulator/v1/projects/${PROJECT_ID}:ruleCoverage.html`;

export function authedFirestore(auth: any) {
  return firebase
    .initializeTestApp({ projectId: PROJECT_ID, auth })
    .firestore();
}

export function adminFirestore() {
  return firebase.initializeAdminApp({ projectId: PROJECT_ID }).firestore();
}

import * as admin from "firebase-admin";

export interface Statistics {
  readonly numberOfActiveTasks: number;
  readonly numberOfCompletedTask: number;
  readonly updatedAt: admin.firestore.FieldValue;
}

export interface Task {
  readonly title: string;
  readonly description: string;
  readonly completed: boolean;
  readonly createdAt: admin.firestore.FieldValue;
}

export interface User {
  readonly name: string;
  readonly photoURL?: string;
}

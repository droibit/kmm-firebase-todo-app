export type DocumentData<T> = {
  [P in keyof T]?: T[P] | FirebaseFirestore.FieldValue;
};

export interface Statistics {
  readonly numberOfActiveTasks: number;
  readonly numberOfCompletedTasks: number;
  readonly updatedAt: FirebaseFirestore.Timestamp;
}

export interface Task {
  readonly title: string;
  readonly description: string;
  readonly completed: boolean;
  readonly createdAt: FirebaseFirestore.Timestamp;
  readonly updatedAt: FirebaseFirestore.Timestamp;
}

export interface User {
  readonly name: string;
  readonly photoURL?: string;
}

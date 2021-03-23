export interface Statistics {
  readonly numberOfActiveTasks: number;
  readonly numberOfCompletedTask: number;
  readonly updatedAt: FirebaseFirestore.FieldValue;
}

export interface Task {
  readonly title: string;
  readonly description: string;
  readonly completed: boolean;
  readonly createdAt: FirebaseFirestore.FieldValue;
}

export interface User {
  readonly name: string;
  readonly photoURL?: string;
}

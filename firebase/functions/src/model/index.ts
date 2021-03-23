export interface Statistics {
  readonly numberOfActiveTasks: FirebaseFirestore.FieldValue | number;
  readonly numberOfCompletedTask: FirebaseFirestore.FieldValue | number;
  readonly updatedAt:
    | FirebaseFirestore.Timestamp
    | FirebaseFirestore.FieldValue;
}

export interface Task {
  readonly title: string;
  readonly description: string;
  readonly completed: boolean;
  readonly createdAt:
    | FirebaseFirestore.Timestamp
    | FirebaseFirestore.FieldValue;
  readonly updatedAt:
    | FirebaseFirestore.Timestamp
    | FirebaseFirestore.FieldValue;
}

export interface User {
  readonly name: string;
  readonly photoURL?: string;
}

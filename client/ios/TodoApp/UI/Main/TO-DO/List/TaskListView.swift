//
//  TaskListView.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import SwiftUI

struct TaskListView: View {
    var body: some View {
        Text("Task List")
    }
}

// MARK: - Builder

extension TaskListView {
    struct Builder: View {
        var body: some View {
            TaskListView()
        }
    }
}

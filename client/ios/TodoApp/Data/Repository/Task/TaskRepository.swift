//
//  TaskRepository.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/31.
//

import Combine
import Shared

protocol TaskRepository {
    var taskFilter: AnyPublisher<TaskFilter, Never> { get }

    func setTaskFilter(_ taskFilter: TaskFilter) -> AnyPublisher<Void, Never>
}

//
//  TaskRepositoryCombineAdapter.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/31.
//

import Combine
import Shared

class TaskRepositoryCombineAdapter: TaskRepository {
    // swiftlint:disable weak_delegate
    private let delegate: Shared.TaskRepository
    // swiftlint:enable weak_delegate

    init(delegate: Shared.TaskRepository) {
        self.delegate = delegate
    }

    var taskFilter: AnyPublisher<TaskFilter, Never> {
        let sink = PassthroughSubject<TaskFilter, Never>()
        let closable = delegate.taskFilter.watch { taskFilter, error in
            assert(error == nil)
            sink.send(taskFilter!)
        }

        return sink.handleEvents(receiveCancel: {
            closable.close()
        }).eraseToAnyPublisher()
    }

    func setTaskFilter(_ taskFilter: TaskFilter) -> AnyPublisher<Void, Never> {
        Deferred {
            Future { promise in
                self.delegate.setTaskFilter(taskFilter: taskFilter) { _, error in
                    assert(error == nil)
                    promise(.success(()))
                }
            }
        }
        .eraseToAnyPublisher()
    }
}

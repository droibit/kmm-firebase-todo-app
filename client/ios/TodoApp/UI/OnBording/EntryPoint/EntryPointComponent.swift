//
//  EntryPointComponent.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import Combine
import NeedleFoundation
import Shared

protocol EntryPointDependency: Dependency {
    var userRepository: UserRepository { get }
}

class EntryPointComponent: Component<EntryPointDependency>, ObservableObject {
    var viewModel: EntryPointViewModel {
        shared {
            EntryPointViewModel(
                userRepostory: dependency.userRepository
            )
        }
    }

    func makeView() -> EntryPointView {
        EntryPointView(viewModel: viewModel)
    }
}

extension EntryPointComponent {
    static func make() -> EntryPointComponent {
        SingletonComponent.sharedInstance.entryPoint
    }
}

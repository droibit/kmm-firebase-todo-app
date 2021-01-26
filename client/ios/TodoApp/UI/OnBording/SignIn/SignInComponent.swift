//
//  SignInComponent.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/26.
//

import Combine
import CombineSchedulers
import Foundation
import NeedleFoundation
import Shared

protocol SignInDependency: Dependency {
    var mainScheduler: AnySchedulerOf<DispatchQueue> { get }
    var userRepository: UserRepository { get }
}

class SignInComponent: Component<SignInDependency>, ObservableObject {
    var viewModel: SignInViewModel {
        shared {
            SignInViewModel(
                userRepository: dependency.userRepository,
                mainScheduler: dependency.mainScheduler
            )
        }
    }

    func makeView() -> SignInView {
        SignInView(viewModel: viewModel)
    }
}

extension SignInComponent {
    static func make() -> SignInComponent {
        SingletonComponent.sharedInstance.signIn
    }
}

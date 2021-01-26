//
//  AppComponent.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import CombineSchedulers
import NeedleFoundation
import Shared

class SingletonComponent: BootstrapComponent {
    static let sharedInstance: SingletonComponent = .init()
}

extension SingletonComponent {
    var mainScheduler: AnySchedulerOf<DispatchQueue> {
        shared {
            DispatchQueue.main.eraseToAnyScheduler()
        }
    }
}

// MARK: - Provide view's component

extension SingletonComponent {
    var signIn: SignInComponent {
        SignInComponent(parent: self)
    }
}

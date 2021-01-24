//
//  AppComponent.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import CombineSchedulers
import NeedleFoundation
import Shared

class AppComponent: BootstrapComponent {
    static let sharedInstance: AppComponent = .init()
}

extension AppComponent {
    var mainScheduler: AnySchedulerOf<DispatchQueue> {
        shared {
            DispatchQueue.main.eraseToAnyScheduler()
        }
    }
}

// MARK: - Provide view's component

extension AppComponent {}

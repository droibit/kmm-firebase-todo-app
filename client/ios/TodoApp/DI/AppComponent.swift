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
    var mainScheduler: AnySchedulerOf<DispatchQueue> {
        shared {
            DispatchQueue.main.eraseToAnyScheduler()
        }
    }
}

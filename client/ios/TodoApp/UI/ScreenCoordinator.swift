//
//  ScreenCoordinator.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import SwiftUI

class ScreenCoordinator: ObservableObject {
    @Published var screen: ScreenView = .entryPoint
}

extension ScreenCoordinator {
    enum ScreenView {
        case entryPoint
        case signIn
        case main
    }
}

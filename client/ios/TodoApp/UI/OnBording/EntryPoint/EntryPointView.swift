//
//  EntryPointView.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import SwiftUI

struct EntryPointView: View {
    @ObservedObject var viewModel: EntryPointViewModel

    @EnvironmentObject private var screenCoordinator: ScreenCoordinator

    var body: some View {
        Image(uiImage: Asset.Images.LaunchScreen.doneOutline.image)
            .onAppear(perform: viewModel.onAppear)
            .onReceive(viewModel.launchDestination) { launchDestination in
                switch launchDestination {
                case .signIn:
                    screenCoordinator.screen = .signIn
                case .main:
                    screenCoordinator.screen = .main
                }
            }
    }
}

// MARK: - Builder

extension EntryPointView {
    struct Builder: View {
        @StateObject private var component: EntryPointComponent = .make()

        var body: some View {
            component.makeView()
        }
    }
}

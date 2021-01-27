//
//  SignInView.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import SwiftUI

struct SignInView: View {
    @ObservedObject var viewModel: SignInViewModel

    var body: some View {
        SignInContentView(
            signInWithGoogleHandler: viewModel.onGoogleSignIn
        ).onReceive(viewModel.$uiModel) { uiModel in
            if uiModel.showProgress {
                showProgressHUD()
            } else {
                hideProgressHUD()
            }

            if let errorMessage = uiModel.showError {
                flashLabeledHUD(message: errorMessage)
            }

            if uiModel.showSuccess {
                // TODO:
            }
        }
    }
}

// MARK: - Builder

extension SignInView {
    struct Builder: View {
        @StateObject private var component = SignInComponent.make()

        var body: some View {
            component.makeView()
        }
    }
}

//
//  SignInViewModel.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Combine
import CombineSchedulers
import GoogleSignIn
import Shared

class SignInViewModel: ObservableObject {
    private let userRepository: UserRepository

    private let mainScheduler: AnySchedulerOf<DispatchQueue>

    private var cancellables: Set<AnyCancellable> = []

    @Published private(set) var uiModel: SignInUiModel

    init(userRepository: UserRepository,
         mainScheduler: AnySchedulerOf<DispatchQueue>,
         uiModel: SignInUiModel = .init())
    {
        self.userRepository = userRepository
        self.mainScheduler = mainScheduler
        self.uiModel = uiModel
    }

    func onGoogleSignIn(WithReult result: Result<GIDGoogleUser, Error>) {
        switch result {
        case let .success(user):
            signInWithGoogle(withAuthentication: user.authentication)
        case let .failure(error):
            if let error = GIDSignInErrorCode(rawValue: (error as NSError).code), error != .canceled {
                uiModel = SignInUiModel(
                    showProgress: false,
                    showError: L10n.SignIn.failed
                )
            }
            Napier.d("Google sign in failed: \(error)")
        }
    }

    private func signInWithGoogle(withAuthentication authentication: GIDAuthentication) {
        guard !uiModel.showProgress else {
            return
        }

        userRepository.signInWithGoogle(
            idToken: authentication.idToken,
            accessToken: authentication.accessToken
        )
        .receive(on: mainScheduler)
        .sink(receiveCompletion: { [unowned self] completion in
            if case let .failure(error) = completion {
                self.uiModel = SignInUiModel(
                    showProgress: false,
                    showError: L10n.SignIn.failed
                )
                Napier.e("Authentication failed", throwable: error)
            }
        }, receiveValue: { [unowned self] _ in
            self.uiModel = SignInUiModel(
                showProgress: false,
                showSuccess: true
            )
        })
        .store(in: &cancellables)
    }
}

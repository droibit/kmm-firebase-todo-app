//
//  SignInViewModel.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Combine
import CombineSchedulers
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
}

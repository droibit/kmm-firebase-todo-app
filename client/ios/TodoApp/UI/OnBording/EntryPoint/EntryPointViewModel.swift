//
//  EntryPointViewModel.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import Combine
import Shared

class EntryPointViewModel: ObservableObject {
    private let userRepostory: UserRepository

    private let launchDestinationSink: PassthroughSubject<LaunchDestination, Never>

    var launchDestination: AnyPublisher<LaunchDestination, Never> {
        launchDestinationSink.eraseToAnyPublisher()
    }

    init(userRepostory: UserRepository,
         launchDestinationSink: PassthroughSubject<LaunchDestination, Never> = .init())
    {
        self.userRepostory = userRepostory
        self.launchDestinationSink = launchDestinationSink
    }

    func onAppear() {
        let launchDestination: LaunchDestination
        if userRepostory.isSignedIn {
            launchDestination = .main
        } else {
            launchDestination = .signIn
        }
        launchDestinationSink.send(launchDestination)
    }
}

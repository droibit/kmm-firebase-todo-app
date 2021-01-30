//
//  UserRepositoryCombineAdapter.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Combine
import Shared

class UserRepositoryCombineAdapter: UserRepository {
    // swiftlint:disable weak_delegate
    private let delegate: Shared.UserRepository
    // swiftlint:enable weak_delegate

    init(delegate: Shared.UserRepository) {
        self.delegate = delegate
    }

    var isSignedIn: Bool {
        delegate.isSignedIn
    }

    var currentUser: User? {
        delegate.currentUser
    }

    func signInWithGoogle(idToken: String, accessToken: String) -> AnyPublisher<User, AuthException> {
        Deferred {
            Future { promise in
                self.delegate.signInWithGoogle(idToken: idToken, accessToken: accessToken) { user, error in
                    Napier.d("\(currentQueueName()): signInWithGoogle -> user:\(String(describing: user)), error: \(String(describing: error))")
                    if let error = error?.kotlinException {
                        promise(.failure(error as! AuthException))
                    } else {
                        promise(.success(user!))
                    }
                }
            }
        }
        .eraseToAnyPublisher()
    }
}

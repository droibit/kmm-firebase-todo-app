//
//  UserRepositoryCombineAdapter.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Combine
import Shared

class UserRepositoryCombineAdapter: UserRepository {
    private let real: Shared.UserRepository

    init(realRepository: Shared.UserRepository) {
        real = realRepository
    }

    var isSignedIn: Bool {
        real.isSignedIn
    }

    var currentUser: User? {
        real.currentUser
    }

    func signInWithGoogle(idToken: String, accessToken: String) -> AnyPublisher<User, AuthException> {
        Deferred {
            Future { promise in
                self.real.signInWithGoogle(idToken: idToken, accessToken: accessToken) { user, error in
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

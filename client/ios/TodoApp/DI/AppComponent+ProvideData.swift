//
//  AppComponent+Data.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import NeedleFoundation
import Shared

// MARK: - Provide Repositories

extension AppComponent {
    var userRepository: UserRepository {
        shared {
            UserRepositoryCombineAdapter(
                realRepository: UserRepositoryFactory().make(dataSource: userDetaSource)
            )
        }
    }
}

// MARK: - Provide Data sources

extension AppComponent {
    var firebaseAuth: Firebase_authFirebaseAuth {
        shared {
            FirebaseAuthProvider().auth
        }
    }

    var userDetaSource: UserDataSource {
        shared {
            UserDataSource(auth: firebaseAuth)
        }
    }
}

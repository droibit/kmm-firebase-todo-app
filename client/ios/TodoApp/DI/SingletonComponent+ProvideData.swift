//
//  SingletonComponent+ProvideData.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import NeedleFoundation
import Shared

// MARK: - Provide Repositories

extension SingletonComponent {
    var userRepository: UserRepository {
        shared {
            UserRepositoryCombineAdapter(
                realRepository: UserRepositoryFactory().make(dataSource: userDetaSource)
            )
        }
    }
}

// MARK: - Provide Data sources

extension SingletonComponent {
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

    var userSettingsDataSource: UserSettingsDataSource {
        shared {
            // If the setting name is null, `UserDefaults.standard` is used internally.
            UserSettingsDataSource(
                settings: UserSettingsFactory().make(name: nil)
            )
        }
    }
}

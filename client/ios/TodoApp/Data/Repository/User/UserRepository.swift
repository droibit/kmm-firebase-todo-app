//
//  UserRepository.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Combine
import Shared

protocol UserRepository {
    var isSignedIn: Bool { get }

    var currentUser: User? { get }

    func signInWithGoogle(idToken: String, accessToken: String) -> AnyPublisher<User, AuthException>
}

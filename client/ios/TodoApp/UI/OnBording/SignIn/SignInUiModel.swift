//
//  SignInUiModel.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Foundation

struct SignInUiModel {
    typealias Success = Void

    let showProgress: Bool
    let showError: String?
    let showSuccess: Bool

    init(showProgress: Bool = false,
         showError: String? = nil,
         showSuccess: Bool = false)
    {
        self.showProgress = showProgress
        self.showError = showError
        self.showSuccess = showSuccess
    }
}

//
//  SignInUiModel.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Foundation

struct SignInUiModel {
    typealias Completed = Void

    let showProgress: Bool
    let showError: String?
    let showSuccess: Completed?

    init(showProgress: Bool = false,
         showError: String? = nil,
         showSuccess: Completed? = nil)
    {
        self.showProgress = showProgress
        self.showError = showError
        self.showSuccess = showSuccess
    }
}

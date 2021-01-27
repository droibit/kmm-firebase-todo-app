//
//  HUD.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/27.
//

import PKHUD

func showProgressHUD() {
    HUD.show(.progress)
}

func hideProgressHUD() {
    HUD.hide()
}

func flashLabeledHUD(message: String, completionHandler: (() -> Void)? = nil) {
    HUD.flash(.label(message), delay: 2.0) { _ in
        completionHandler?()
    }
}

//
//  AppDelegate.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/18.
//

import Shared
import UIKit

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        #if DEBUG
            BootstrapKt.bootstrap(debuggable: true)
        #else
            BootstrapKt.bootstrap(debuggable: false)
        #endif        
        return true
    }
}

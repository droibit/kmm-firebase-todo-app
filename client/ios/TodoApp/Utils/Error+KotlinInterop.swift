//
//  Error+KotlinInterop.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Foundation
import Shared

extension Error {
    var kotlinException: KotlinException? {
        (self as NSError).kotlinException as? KotlinException
    }
}

extension KotlinThrowable: Error {}

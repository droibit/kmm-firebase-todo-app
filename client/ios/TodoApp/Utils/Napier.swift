//
//  Napier.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/24.
//

import Shared

extension Napier {
    private static let napier = Napier()

    static func v(_ message: String) {
        napier.v(message: message, throwable: nil, tag_: "")
    }

    static func i(_ message: String) {
        napier.i(message: message, throwable: nil, tag_: "")
    }

    static func d(_ message: String) {
        napier.d(message: message, throwable: nil, tag_: "")
    }

    static func w(_ message: String) {
        napier.w(message: message, throwable: nil, tag_: "")
    }

    static func e(_ message: String, throwable: KotlinThrowable? = nil) {
        napier.e(message: message, throwable: throwable, tag_: "")
    }
}

func currentQueueName() -> String {
    // ref. https://stackoverflow.com/questions/39553171/how-to-get-the-current-queue-name-in-swift-3/39809760
    if let name = String(cString: __dispatch_queue_get_label(nil), encoding: .utf8) {
        return name
    }
    return "unknown"
}

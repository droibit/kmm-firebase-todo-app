import SwiftUI

@main
struct TodoApp: App {
    // swiftlint:disable weak_delegate
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    // swiftlint:enable weak_delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

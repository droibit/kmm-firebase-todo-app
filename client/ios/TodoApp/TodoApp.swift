import SwiftUI

@main
struct TodoApp: App {
    // swiftlint:disable weak_delegate
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    // swiftlint:enable weak_delegate

    @StateObject var screenCoordinator = ScreenCoordinator()

    var body: some Scene {
        WindowGroup {
            Group {
                switch screenCoordinator.screen {
                case .entryPoint:
                    EntryPointView.Builder()
                case .signIn:
                    SignInView.Builder()
                case .main:
                    MainView()
                }
            }.environmentObject(screenCoordinator)
        }
    }
}

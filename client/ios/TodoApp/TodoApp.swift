import SwiftUI

@main
struct TodoApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

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

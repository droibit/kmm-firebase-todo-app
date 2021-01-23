//
//  GoogleSignInButton.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/23.
//

import GoogleSignIn
import SwiftUI

// typealias GoogleSignInHandler = (Result<GIDGoogleUser, Error>) -> Void

struct GoogleSignInButton: View {
    let signInHandler: (Result<GIDGoogleUser, Error>) -> Void

    var body: some View {
        GoogleSignInButtonController(signInHandler: signInHandler)
            .onOpenURL { url in
                GIDSignIn.sharedInstance().handle(url)
            }
            // TODO: Delete fixed size because
            .frame(height: 48)
    }
}

struct GoogleSignInButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            GoogleSignInButton { _ in
            }
            .previewLayout(.sizeThatFits)
            .environment(\.colorScheme, .light)

            GoogleSignInButton { _ in
            }
            .previewLayout(.sizeThatFits)
            .environment(\.colorScheme, .dark)
        }
    }
}

// MARK: - Controller

private struct GoogleSignInButtonController: UIViewRepresentable {
    typealias UIViewType = UIView

    let signInHandler: (Result<GIDGoogleUser, Error>) -> Void

    @Environment(\.colorScheme) var colorScheme

    func makeUIView(context: Context) -> UIView {
        GIDSignIn.sharedInstance()?.presentingViewController = UIWindow.keyWindow?.rootViewController

        let signInButton = GIDSignInButton()
        signInButton.style = .wide
        signInButton.colorScheme = (colorScheme == .dark) ? .dark : .light
        return signInButton
    }

    func updateUIView(_ uiView: UIView, context: Context) {}

    static func dismantleUIView(_ uiView: UIView, coordinator: Coordinator) {
        GIDSignIn.sharedInstance()?.presentingViewController = nil
    }
}

extension GoogleSignInButtonController {
    class Coordinator: NSObject, GIDSignInDelegate {
        private let parent: GoogleSignInButtonController

        init(_ parent: GoogleSignInButtonController) {
            self.parent = parent
        }

        func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
            if let error = error {
                parent.signInHandler(.failure(error))
            } else {
                parent.signInHandler(.success(user))
            }
        }
    }

    func makeCoordinator() -> Coordinator {
        let coordinator = Coordinator(self)
        GIDSignIn.sharedInstance()?.delegate = coordinator
        return coordinator
    }
}

private extension UIWindow {
    static var keyWindow: UIWindow? {
        UIApplication.shared.windows.first { $0.isKeyWindow }
    }
}

//
//  SignInContentView.swift
//  TodoApp
//
//  Created by Shinya Kumagai on 2021/01/26.
//

import GoogleSignIn
import SwiftUI

struct SignInContentView: View {
    let signInWithGoogleHandler: (Result<GIDGoogleUser, Error>) -> Void

    var body: some View {
        VStack {
            // TODO: Introduce text appearance like MDC.
            Text(L10n.SignIn.title)
                .font(.system(size: 80, weight: .bold))
                .multilineTextAlignment(.center)
                .padding()

            Spacer().frame(height: 128)

            VStack {
                GoogleSignInButton(signInHandler: signInWithGoogleHandler)
            }
            .padding(.horizontal, 32)
            .padding(.vertical)
        }
    }
}

struct SignContentInView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            SignInContentView { _ in
            }
            .environment(\.colorScheme, .light)
            .previewDevice(.init(stringLiteral: "iPhone 12 mini"))

            SignInContentView { _ in
            }
            .environment(\.colorScheme, .light)
            .previewDevice(.init(stringLiteral: "iPhone SE (2nd generation)"))
        }
    }
}

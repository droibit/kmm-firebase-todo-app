import Shared
import SwiftUI

func greet() -> String {
    Greeting().greeting()
}

struct ContentView: View {
    var body: some View {
        VStack(spacing: 8) {
            GoogleSignInButton { _ in
            }.padding(.horizontal)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

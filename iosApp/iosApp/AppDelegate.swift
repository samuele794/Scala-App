//
//  AppDelegate.swift
//  iosApp
//
//  Created by ABSTRACT on 22/04/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Firebase

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

  var window: UIWindow?

  func application(_ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions:
                   [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    FirebaseApp.configure()

    return true
  }
}

package com.kirson.googlebooks.util

import com.kirson.googlebooks.entity.MapLocation

interface AppLauncher {
  fun openWebsite(url: String)
  fun openDialer(phoneNumber: String)
  fun openAppInGooglePlay(packageName: String)
  fun openMapLocation(location: MapLocation)
  fun openNotificationSettings()
}

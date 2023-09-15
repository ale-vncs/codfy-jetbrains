package com.ale.vncs.codfy.notification

import com.ale.vncs.codfy.utils.Constants
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType


object Notification {
    fun notifyInfo(title: String?, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(Constants.APP_NAME)
            .createNotification(content, NotificationType.INFORMATION)
            .setTitle("${Constants.APP_NAME} $title")
            .notify(null)
    }

    fun notifyError(title: String?, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(Constants.APP_NAME)
            .createNotification(content, NotificationType.ERROR)
            .setTitle(title)
            .notify(null)
    }

}

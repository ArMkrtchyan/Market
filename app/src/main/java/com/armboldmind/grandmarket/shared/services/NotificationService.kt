package com.armboldmind.grandmarket.shared.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.armboldmind.grandmarket.R
import com.armboldmind.grandmarket.shared.enums.ActionsEnum
import com.armboldmind.grandmarket.shared.enums.BundleKeysEnum
import com.armboldmind.grandmarket.shared.enums.UserRoleEnum
import com.armboldmind.grandmarket.shared.managers.PreferencesManager
import com.armboldmind.grandmarket.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(messsage: RemoteMessage) {
        if (PreferencesManager(applicationContext).findByKey<UserRoleEnum>(BundleKeysEnum.USER_ROLE.key) == UserRoleEnum.USER) {
            val intent = Intent()
            intent.action = ActionsEnum.RECEIVE_GLOBAL_NOTIFICATION_ACTION.action
            Log.i("NotificationTag", messsage.data.toString())
            var notificationType: Int? = null
            var actionType: Int? = null
            var actionId: Long? = null
            messsage.data["notificationType"]?.let { notificationType = it.toInt() }
            messsage.data["actionType"]?.let { actionType = it.toInt() }?: run { actionType = 0 }
            messsage.data["actionId"]?.let { actionId = it.toLong() }
            notificationType?.let { intent.putExtra(BundleKeysEnum.NOTIFICATION_TYPE.key, it) }
            actionType?.let { intent.putExtra(BundleKeysEnum.ACTION_TYPE.key, it) }
            actionId?.let { intent.putExtra(BundleKeysEnum.ACTION_ID.key, it) }
            sendBroadcast(intent)
            sendNotification(messsage.data["title"], messsage.data["description"], intent)
        }
    }

    private fun sendNotification(title: String?, body: String?, intent: Intent) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        intent.setClass(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "Channel_1"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVibrate(longArrayOf(1000, 1000))
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)


        // Since android Oreo notification channel is needed.
        // https://developer.android.com/training/notify-user/build-notification#Priority
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_HIGH)

            val attributes: AudioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

            channel.description = body
            channel.setShowBadge(true)
            channel.enableVibration(true)
            channel.setSound(defaultSoundUri, attributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
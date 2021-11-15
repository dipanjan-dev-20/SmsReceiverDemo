package com.android.smsdemo.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.smsdemo.R
import com.android.smsdemo.activity.MainActivity
import com.android.smsdemo.activity.SmsDetailsActivity
import com.android.smsdemo.data.Sms
import com.android.smsdemo.data.SmsRepository
import com.android.smsdemo.di.IoDispatcher
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

object NotificationID {
    private val c: AtomicInteger = AtomicInteger(0)
    val iD: Int
        get() = c.incrementAndGet()
}

class MySmsReceiver : DaggerBroadcastReceiver() {
    val pdu_type = "pdus"

    @Inject
    lateinit var smsRepository: SmsRepository

    @IoDispatcher
    @Inject
    lateinit var dispatchers: CoroutineDispatcher

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(context == null || intent == null || intent.action == null){
            return
        }
        if (intent.action != (Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            return
        }

        //val contentResolver = context.contentResolver
        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (message in smsMessages) {

            GlobalScope.launch(dispatchers) {
                smsRepository.insert(Sms(System.currentTimeMillis(), message.displayOriginatingAddress, message.messageBody))
            }

            createNotificationChannel(context)
            showNotification(context, message.displayOriginatingAddress, message.messageBody)
        }
//        // Get the SMS message.
//        // Get the SMS message.
//        val bundle = intent!!.extras
//        val msgs: Array<SmsMessage?>
//        val format = bundle!!.getString("format")
//        // Retrieve the SMS message received.
//        // Retrieve the SMS message received.
//        val pdus = bundle[pdu_type] as Array<Any>?
//        if (pdus != null) {
//            msgs = arrayOfNulls(pdus.size)
//            for (i in msgs.indices) {
//                // If Android version M or newer:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
//                } else {
//                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
//                }
//
//                // Build the message to show.
//                val creator = msgs[i]?.originatingAddress ?: ""
//                val messageText: String = msgs[i]?.messageBody ?: ""
//                GlobalScope.launch(dispatchers) {
//                    smsRepository.insert(Sms(System.currentTimeMillis(), creator, messageText))
//                }
//
//                context?.let {
//                    createNotificationChannel(it)
//                    showNotification(it, creator, messageText)
//                }
//            }
//        }
    }

    companion object {
        private const val CHANNEL_ID = "smsdemoChannelId"
        private const val CHANNEL_NAME = "smsdemoChannelName"
        private const val CHANNEL_DESC = "testing demo sms"
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            // Register the channel with the system
            val notificationManager: NotificationManager? =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, sender: String, text: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtras(Bundle().also { it.putString(SmsDetailsActivity.SMS_SENDER, sender) })
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("SMS from ${sender}")
            .setContentText(text)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(NotificationID.iD, builder.build())
        }

    }
}
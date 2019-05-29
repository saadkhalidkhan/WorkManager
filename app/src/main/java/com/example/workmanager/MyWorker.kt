package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_TEXT = "text"
        const val EXTRA_OUTPUT_MESSAGE = "output_message"
    }

    override fun doWork(): Result {

        val title = inputData.getString(EXTRA_TITLE)

        val text = inputData.getString(EXTRA_TEXT)

        //in case of cancelling the request
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        sendNotification("Simple Work Manager", "I have been send by WorkManager.")

        val output = Data.Builder()
            .putString(EXTRA_OUTPUT_MESSAGE, "Testing from MyWorker!")
            .build()

        return Result.success(output)
    }

    fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1, notification.build())
    }
}
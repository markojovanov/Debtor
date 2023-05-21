package com.markojovanov.debtor.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.markojovanov.debtor.R
import com.markojovanov.debtor.data.db.DebtorDao
import com.markojovanov.debtor.data.model.entities.Debtor
import com.markojovanov.debtor.ui.MainActivity
import com.markojovanov.debtor.utils.DateConverter.convertDateToSimpleFormatString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class NotificationWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val debtorDao: DebtorDao
): CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val todayDate = getTodaysDateAsString()
        val debtors = debtorDao.getAllDebtors()
            for (debtor in debtors) {
                if (todayDate == debtor.dueDate && !debtor.isPayed) {
                    Log.d("WORK MANAGER", "Dates ---------->: ${debtor.dueDate} ")
                    createNotificationChannel()
                    showNotification(debtor)
                }
            }

        return  Result.success()
    }

    private fun getTodaysDateAsString() = convertDateToSimpleFormatString(Calendar.getInstance().time)

    private fun showNotification(debtor: Debtor) {
        val notification = NotificationCompat.Builder(applicationContext, "channelID")
        notification.apply {
            setSmallIcon(R.drawable.credit_card_24)
                setContentTitle("Deadline payment for ${debtor.personName}")
                setContentText("Remaining amount of ${debtor.remainingAmountMoney}")
            setContentIntent(setPendingIntent())
                priority = NotificationCompat.PRIORITY_DEFAULT
        }
        NotificationManagerCompat.from(applicationContext).also { it.notify(debtor.debtorId!!, notification.build()) } //this id should be unique
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "channel_1"
            val description = "This is test channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channelID", name, importance)
            channel.description = description
            val notificationManager: NotificationManager? = getSystemService(applicationContext, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun setPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

}
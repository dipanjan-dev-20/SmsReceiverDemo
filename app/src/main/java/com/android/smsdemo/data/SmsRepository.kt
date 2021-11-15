package com.android.smsdemo.data

import android.app.Application
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.smsdemo.di.IoDispatcher
import com.android.smsdemo.ui.MainController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmsRepositoryImp @Inject constructor(private val smsDao :SmsDao, private val dispatcher: CoroutineDispatcher,private val application: Application):SmsRepository {

    companion object {
        const val  PAGE_SIZE = 20
    }

    override fun getSmsPost():Flow<PagingData<Sms>> = Pager(config = PagingConfig(PAGE_SIZE)){
        smsDao.smsDataSource.asPagingSourceFactory(fetchDispatcher = dispatcher).invoke()
    }.flow

    override fun getSmsBySender(sender: String): Flow<PagingData<Sms>> = Pager(config = PagingConfig(PAGE_SIZE)){
        smsDao.senderDataSource(sender).asPagingSourceFactory(fetchDispatcher = dispatcher).invoke()
    }.flow

    override suspend fun getAllSms():Boolean  = withContext(dispatcher) {
        val cr = application.contentResolver
        val c: Cursor? = cr.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.BODY
            ),  // Select body text
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        ) // Default
        // sort
        // order);
        val totalSMS: Int = c?.count ?: 0
        if (c?.moveToFirst() == true) {
            for (i in 0 until totalSMS) {
                val data = Sms(c.getLong(0),c.getString(1),c.getString(2))
                smsDao.insert(data)
                c.moveToNext()
            }
        }
        c?.close()
        return@withContext true
    }



    override suspend fun getCount() = withContext(Dispatchers.IO) {
        smsDao.getCount()
    }

    override suspend fun insert(sms: Sms) = withContext(Dispatchers.IO){
        smsDao.insert(sms)
    }
}

interface SmsRepository{

    fun getSmsPost():Flow<PagingData<Sms>>
    fun getSmsBySender(sender:String):Flow<PagingData<Sms>>
    suspend fun getAllSms():Boolean
    suspend fun getCount():Int
    suspend fun insert(sms:Sms)

}
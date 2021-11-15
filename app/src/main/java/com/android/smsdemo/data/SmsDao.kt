package com.android.smsdemo.data

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface SmsDao {
    @get:Query("SELECT * FROM sms ORDER BY date DESC")
    val smsDataSource:DataSource.Factory<Int,Sms>



    @Query("SELECT * FROM sms WHERE creator = :smsCreator ORDER BY date DESC")
    fun senderDataSource(smsCreator:String):DataSource.Factory<Int,Sms>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sms:Sms)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(sms:List<Sms>)

    @Query("SELECT COUNT(*) FROM sms")
    suspend fun getCount():Int



}



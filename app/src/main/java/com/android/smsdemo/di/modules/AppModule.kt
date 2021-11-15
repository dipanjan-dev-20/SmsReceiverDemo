package com.android.smsdemo.di.modules

import androidx.room.Room
import com.android.smsdemo.application.SmsDemoApplication
import com.android.smsdemo.data.SmsDao
import com.android.smsdemo.data.SmsDatabase
import com.android.smsdemo.data.SmsRepository
import com.android.smsdemo.data.SmsRepositoryImp
import com.android.smsdemo.di.ApplicationScope
import com.android.smsdemo.di.IoDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideRoom(application: SmsDemoApplication): SmsDatabase {
        return Room.databaseBuilder(application, SmsDatabase::class.java, "sms-db").build()
    }

    @ApplicationScope
    @Provides
    fun smsDao(smsDatabase: SmsDatabase): SmsDao {
        return smsDatabase.userDao()
    }

    @ApplicationScope
    @Provides
    fun provideSmsRepository(
        application: SmsDemoApplication,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        smsDao: SmsDao
    ): SmsRepository =
        SmsRepositoryImp(application = application, dispatcher = dispatcher, smsDao = smsDao)
}
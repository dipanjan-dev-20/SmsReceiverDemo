package com.android.smsdemo.di.modules

import com.android.smsdemo.receiver.MySmsReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSmsReceiver(): MySmsReceiver

}
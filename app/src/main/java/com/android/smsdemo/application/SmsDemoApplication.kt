package com.android.smsdemo.application

import com.android.smsdemo.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class SmsDemoApplication:DaggerApplication() {

    private val appComponent =
        DaggerAppComponent.builder().application(this)
            .build()
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent
}
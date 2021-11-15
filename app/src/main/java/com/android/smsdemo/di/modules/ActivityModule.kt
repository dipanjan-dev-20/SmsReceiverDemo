package com.android.smsdemo.di.modules

import com.android.smsdemo.activity.MainActivity
import com.android.smsdemo.activity.SmsDetailsActivity
import com.android.smsdemo.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun contributeOnboardingActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SmsDetailsModule::class])
    internal abstract fun contributeSmsDetailsActivity(): SmsDetailsActivity


}

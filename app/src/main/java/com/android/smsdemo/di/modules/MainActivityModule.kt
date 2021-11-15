package com.android.smsdemo.di.modules

import androidx.lifecycle.ViewModelProvider
import com.android.smsdemo.activity.MainActivity
import com.android.smsdemo.viewmodel.MainViewModel
import com.android.smsdemo.di.ActivityScope
import com.android.smsdemo.di.AppViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @ActivityScope
    @Provides
    fun provideMainViewModel(mainActivity: MainActivity, appViewModelFactory: AppViewModelFactory): MainViewModel {
        return ViewModelProvider(mainActivity,appViewModelFactory)[MainViewModel::class.java]
    }
}
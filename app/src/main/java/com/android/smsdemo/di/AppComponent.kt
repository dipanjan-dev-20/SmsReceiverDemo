package com.android.smsdemo.di

import com.android.smsdemo.application.SmsDemoApplication
import com.android.smsdemo.di.modules.*
import com.android.smsdemo.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(modules = [AndroidSupportInjectionModule::class, DispatcherModule::class,AppModule::class,ViewModelModule::class,ActivityModule::class, ReceiverModule::class])
interface AppComponent : AndroidInjector<SmsDemoApplication>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: SmsDemoApplication): Builder
        fun build(): AppComponent
    }
}
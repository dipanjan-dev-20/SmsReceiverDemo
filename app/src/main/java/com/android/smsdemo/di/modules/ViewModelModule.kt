package com.android.smsdemo.di.modules

import androidx.lifecycle.ViewModel
import com.android.smsdemo.viewmodel.MainViewModel
import com.android.smsdemo.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindOnBoardingViewModel(mainViewModel: MainViewModel): ViewModel


}
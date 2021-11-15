package com.android.smsdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.android.smsdemo.data.Sms
import com.android.smsdemo.data.SmsRepository
import com.android.smsdemo.ui.SmsUiItem
import com.android.smsdemo.ui.UIStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val smsRepository: SmsRepository) : ViewModel() {

    val uiStatesLiveData: LiveData<UIStates> by lazy {
        uiStatesMutableLiveData
    }

    private val uiStatesMutableLiveData by lazy {
        MutableLiveData<UIStates>()
    }

    fun getMessageBySender(sender:String):Flow<PagingData<SmsUiItem>> {
        return smsRepository.getSmsBySender(sender).map { pagingData: PagingData<Sms> ->
            pagingData.map { sms ->
                SmsUiItem.Item(sms)
            }
        }
    }

     val dataSource: Flow<PagingData<SmsUiItem>> by lazy {
        smsRepository.getSmsPost().map { pagingData: PagingData<Sms> ->
            pagingData.map { sms ->
                SmsUiItem.Item(sms)
            }.insertSeparators { before, after ->
                when {
                    before == null && after != null -> SmsUiItem.Seperator(getSeperatorString(after.sms))
                    after == null -> null
                    before!=null ->{
                        val afterHours = getKey(after.sms)
                        val beforeHours = getKey(before.sms)
                        if(afterHours - beforeHours>=1){
                            return@insertSeparators SmsUiItem.Seperator(getSeperatorString(after.sms))
                        } else {
                            return@insertSeparators null
                        }
                    }
                    else -> null
                }
            }
        }
    }

    fun getSeperatorString(smsData: Sms):String{
        val hours = getKey(smsData)
        if(hours==24L){
            return "1 day ago"
        } else {
            return "$hours hours ago"
        }
    }

    fun getKey(smsData: Sms): Long {
        var diffHours = (System.currentTimeMillis() - smsData.date) / 3600000
        return when (diffHours) {
            in 0..3 -> diffHours
            in 3..6 -> 3
            in 6..12 -> 6
            in 12..24 -> 12
            else -> 24
        }
    }


    fun startSync() {
        uiStatesMutableLiveData.value = UIStates.LOADING
        viewModelScope.launch {
            if (smsRepository.getCount() != 0) {
                uiStatesMutableLiveData.postValue(UIStates.SUCCESS)
                smsRepository.getAllSms()
            }else {
                smsRepository.getAllSms()
                uiStatesMutableLiveData.postValue(UIStates.SUCCESS)
            }

        }
    }


}


package com.android.smsdemo.data

import androidx.room.Entity

@Entity(tableName = "sms" ,primaryKeys = ["date","creator"])
data class Sms(val date:Long,val creator:String,val data:String)

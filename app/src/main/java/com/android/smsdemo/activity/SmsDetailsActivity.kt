package com.android.smsdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.smsdemo.R
import com.android.smsdemo.databinding.ActivityMainBinding
import com.android.smsdemo.databinding.ActivitySmsDetailsBinding
import com.android.smsdemo.ui.MainController
import com.android.smsdemo.ui.SmsDetailsController
import com.android.smsdemo.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SmsDetailsActivity : DaggerAppCompatActivity() {

    companion object{
        const val SMS_SENDER= "sms_sender"
        fun openActivity(context: Context,sender:String){
            val intent = Intent(context,SmsDetailsActivity::class.java)
            intent.putExtra(SMS_SENDER,sender)
            context.startActivity(intent)
        }
    }



    @Inject
    lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivitySmsDetailsBinding
    private val mainController by lazy { SmsDetailsController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getStringExtra(SMS_SENDER)?.let {
            supportActionBar?.setTitle(it)
            initRecycler(it)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    fun initRecycler(sender:String){
        binding.recyclerView.setController(mainController)
        lifecycleScope.launch {
            mainViewModel.getMessageBySender(sender).collectLatest {
                mainController.submitData(it)
            }
        }

    }
}
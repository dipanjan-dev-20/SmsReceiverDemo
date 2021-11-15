package com.android.smsdemo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.smsdemo.databinding.ActivityMainBinding
import com.android.smsdemo.ui.MainController
import com.android.smsdemo.ui.SmsClickListener
import com.android.smsdemo.ui.UIStates
import com.android.smsdemo.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), SmsClickListener {

    companion object {
        const val MY_PERMISSIONS_REQUEST_SMS = 101
    }


    @Inject
    lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivityMainBinding
    private val mainController by lazy { MainController(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sender = intent.getStringExtra(SmsDetailsActivity.SMS_SENDER)
        if (sender!=null){
            SmsDetailsActivity.openActivity(this, sender = sender)
        }
        init()


    }

    fun init(){
        if (isDefaultSmsApp()){
            checkPermission()
        }else {
            setDefaultApp()
        }

        binding.recyclerView.setController(mainController)
    }

    fun isDefaultSmsApp(): Boolean {
        return packageName.equals(Telephony.Sms.getDefaultSmsPackage(this))
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_WAP_PUSH) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_MMS,
                    Manifest.permission.RECEIVE_WAP_PUSH),
                MY_PERMISSIONS_REQUEST_SMS)
        }else {
            initRecycler()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if(checkPermissionGranted(requestCode, permissions, grantResults)){
            initRecycler()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkPermissionGranted(requestCode: Int,
                               permissions: Array<String>, grantResults: IntArray): Boolean{
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SMS -> {
                // If request is cancelled, the result arrays are empty.
                return (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
        return false
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK ) {
            checkPermission()
        }
    }

    private fun setDefaultApp(){
        val setSmsAppIntent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        resultLauncher.launch(setSmsAppIntent)
    }





    @ObsoleteCoroutinesApi
    fun initRecycler() {
        mainViewModel.startSync()
        mainViewModel.uiStatesLiveData.observe(this) {
            when (it) {
                UIStates.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                UIStates.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    lifecycleScope.launch {
                        mainViewModel.dataSource.collectLatest {
                            mainController.submitData(it)
                        }
                    }

                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun smsClicked(sender: String) {
        SmsDetailsActivity.openActivity(this, sender = sender)
    }


}
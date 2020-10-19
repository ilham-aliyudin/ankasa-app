package com.arkademy.ankasa.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.ankasa.MainActivity
import com.arkademy.ankasa.R
import com.arkademy.ankasa.booking.DetailBookingActivity
import com.arkademy.ankasa.databinding.ActivityLoginBinding
import com.arkademy.ankasa.forgot.ForgotPassActivity
import com.arkademy.ankasa.utils.api.ApiClient
import com.arkademy.ankasa.utils.api.AuthApiService
import com.arkademy.ankasa.utils.sharedpreferences.Constants
import com.arkademy.ankasa.utils.sharedpreferences.PreferenceHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var sharepref: PreferenceHelper
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.tvReset.setOnClickListener(this)

        sharepref = PreferenceHelper(this)
        val service = ApiClient.getApiClientToken(this)?.create(AuthApiService::class.java)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setSharedPreference(sharepref)

        if (service != null) {
            viewModel.setLoginService(service)
        }

        binding.btnLogin.setOnClickListener{
            viewModel.callAuthApi(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        subscribeLiveData()
    }

    private fun moveIntent() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (sharepref.getBoolean(Constants.PREF_IS_LOGIN)!!) {
            moveIntent()
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLoginLiveData.observe(this, Observer {
            Log.d("android1", "$it")

            if (it) {
                Toast.makeText(this, "Login Succcess", Toast.LENGTH_SHORT).show()
                moveIntent()
                finish()
            } else {
                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.tvReset -> {
                val intent = Intent(this, ForgotPassActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogin -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
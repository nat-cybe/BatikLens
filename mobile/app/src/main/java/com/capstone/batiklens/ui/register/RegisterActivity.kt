package com.capstone.batiklens.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.batiklens.MainActivity
import com.capstone.batiklens.data.Result
import com.capstone.batiklens.databinding.ActivityRegisterBinding
import com.capstone.batiklens.ui.AuthViewModel
import com.capstone.batiklens.utils.ViewModelFactory
import com.capstone.batiklens.utils.dataStore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonRegister.setOnClickListener{
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                registerAccount(name, email, password)
            }
        }
    }

    private fun registerAccount(name: String, email: String, password: String) {
        registerViewModel.registerEmailAndPassword(name, email, password).observe(this@RegisterActivity){result ->
            when(result){
                is Result.Loading -> {
                    Log.d("RegisterActivity", "Loading")
                }
                is Result.Success -> {
                    Log.d("RegisterActivity", result.data)
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    val user = registerViewModel.currentUser()
                    if(user != null){
                        goToMainActivity()
                    }
                }
                is Result.Error -> {
                    Log.d("RegisterActivity", "${result.error} Error")

                    Toast.makeText(this, result.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }}
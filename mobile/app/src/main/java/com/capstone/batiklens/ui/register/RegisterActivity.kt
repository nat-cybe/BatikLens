package com.capstone.batiklens.ui.register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.batiklens.R
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
                }
                is Result.Error -> {
                    Log.d("RegisterActivity", "${result.error} Error")

                    Toast.makeText(this, "${result.error.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.capstone.batiklens.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.batiklens.data.Result
import com.capstone.batiklens.databinding.ActivityAccountBinding
import com.capstone.batiklens.ui.AuthViewModel
import com.capstone.batiklens.ui.history.HistoryActivity
import com.capstone.batiklens.ui.welcome.WelcomeActivity
import com.capstone.batiklens.utils.ViewModelFactory
import com.capstone.batiklens.utils.dataStore

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val authViewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Account"

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setting1.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            userLogout()
        }

//        binding.tvEmail.text = authViewModel.currentUser()?.email

        authViewModel.getUserData().observe(this){result ->
            when(result){
                is Result.Loading -> {}
                is Result.Success -> {
                    binding.tvEmail.text = result.data.username
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun goToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun userLogout() {
        authViewModel.signOut()
        val user = authViewModel.currentUser()
        if (user == null){
            goToWelcomeActivity()
            Toast.makeText(this,"You have been Logged Out", Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
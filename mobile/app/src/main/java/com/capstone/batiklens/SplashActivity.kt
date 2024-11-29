package com.capstone.batiklens

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.capstone.batiklens.ui.welcome.WelcomeActivity
import com.capstone.batiklens.utils.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var isNavigating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            dataStore.data.map {preferences ->
                preferences[booleanPreferencesKey("theme_setting")] ?: false
            }.collect {isDarkMode ->
                Log.d("isdark", "$isDarkMode")
                AppCompatDelegate.setDefaultNightMode(
                    if(isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }



        installSplashScreen()
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()


        val splash = findViewById<ImageView>(R.id.icon)

        val avd = AnimationUtils.loadAnimation(this,R.anim.scal_anim)
        splash.startAnimation(avd)


//        Handler(Looper.getMainLooper()).postDelayed({
//            if(!isNavigating) {
//                Log.d("splashMove", "moving")
//                isNavigating = true
//                startMainActivity()
//            }},2000) // 2-second delay
        if (savedInstanceState == null) {
//            isNavigating = true// Only schedule navigation on the first creation
            lifecycleScope.launch {
                delay(2000)
                startMainActivity()
            }
//            Handler(Looper.getMainLooper()).postDelayed({
//                startMainActivity()
//            }, 2000)
        }else{
            Log.d("move", "$savedInstanceState")
            lifecycleScope.launch {
                delay(2000)
                startMainActivity()
            }
        }

    }

//    override fun onStart() {
//        super.onStart()
//
//
//    }

    private fun startMainActivity() {
        Log.d("splashMove","move")
//            .apply {
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            }
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
    }
}
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.capstone.batiklens.ui.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val splash = findViewById<ImageView>(R.id.icon)

        val avd = AnimationUtils.loadAnimation(this,R.anim.scal_anim)
        splash.startAnimation(avd)
//            avd.setTarget(splash)
//            avd.start()
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity() },
            2000) // 2-second delay
    }

    private fun startMainActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
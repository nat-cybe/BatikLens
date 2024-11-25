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
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.capstone.batiklens.ui.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Log.d("Splash","custom splash active")
            installSplashScreen()
            Log.d("Splash","custom splash done")
        }
        super.onCreate(savedInstanceState)




        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            setContentView(R.layout.activity_splash)
            val splash = findViewById<ImageView>(R.id.icon)

            val avd = AnimatorInflater.loadAnimator(this, R.animator.animation)
            avd.setTarget(splash)
            avd.start()

//            splash?.post{
//                avd?.start()
//                Log.d("icon", "splash")
//            }

            Handler(Looper.getMainLooper()).postDelayed({
                startMainActivity()
            }, 2000) // 2-second delay
        } else {
            startMainActivity()
        }

    }

    private fun startMainActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
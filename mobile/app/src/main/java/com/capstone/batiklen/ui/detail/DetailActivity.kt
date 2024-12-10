package com.capstone.batiklen.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.batiklen.R
import com.capstone.batiklen.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val batikName = intent.getStringExtra("BATIK_NAME")
        val batikDescription = intent.getStringExtra("BATIK_DESCRIPTION")
        val batikImageRes = intent.getIntExtra("BATIK_IMAGE_RES", R.drawable.default_image) // Gambar default jika tidak ada data

        // Set data ke tampilan
        binding.tvBatikName.text = batikName
        binding.tvBatikDescription.text = batikDescription
        binding.imgBatik.setImageResource(batikImageRes)
    }
}
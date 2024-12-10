package com.capstone.batiklen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.batiklen.databinding.ItemBatikBinding

data class Batik(val name: String, val description: String, val imageRes: Int)

class BatikAdapter(
    private val batikList: List<Batik>,
    private val onItemClick: (Batik) -> Unit
) : RecyclerView.Adapter<BatikAdapter.BatikViewHolder>() {

    inner class BatikViewHolder(private val binding: ItemBatikBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(batik: Batik) {
            binding.textTitle.text = batik.name
            binding.textDescription.text = batik.description
            binding.imageIcon.setImageResource(batik.imageRes)

            // Set click listener
            binding.root.setOnClickListener { onItemClick(batik) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatikViewHolder {
        val binding = ItemBatikBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatikViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatikViewHolder, position: Int) {
        holder.bind(batikList[position])
    }

    override fun getItemCount() = batikList.size
}

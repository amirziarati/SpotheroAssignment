package com.spothero.challenge.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spothero.challenge.data.model.Spot
import com.spothero.challenge.databinding.ItemRowBinding

class SpotsAdapter(private val spots: List<Spot>, private val onItemClicked: (spot: Spot) -> Unit) :
    RecyclerView.Adapter<SpotsAdapter.ViewHolder>() {

    private lateinit var binding: ItemRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.binding.apply {
            tvAddress.text = spot.address.street
            tvDistance.text = spot.distance
            tvPrice.text = "${spot.price}$"
            Glide.with(root.context)
                .load(Uri.parse("file:/" + spot.facilityPhoto))
                .into(imgSpot)
            root.setOnClickListener { onItemClicked(spot) }
        }
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    inner class ViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)
}
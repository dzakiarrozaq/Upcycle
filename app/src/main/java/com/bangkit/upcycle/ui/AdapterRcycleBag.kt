package com.bangkit.upcycle.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.upcycle.R
import com.bangkit.upcycle.repository.PredictionData

class AdapterRcycleBag(private val predictionDataList: List<PredictionData>) :
    RecyclerView.Adapter<AdapterRcycleBag.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView = itemView.findViewById(R.id.tvUser)
        val img: ImageView = itemView.findViewById(R.id.img_user_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_trash, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data ke tampilan di sini
        val predictionData = predictionDataList[position]
        holder.labelTextView.text = predictionData.label
        val bitmap = BitmapFactory.decodeFile(predictionData.image.localPath)
        holder.img.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return predictionDataList.size
    }
}
package com.example.flickerphotos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_ph.view.*

class adapterPh(val activity:MainActivity ,val photo:ArrayList<photoClass>) : RecyclerView.Adapter<adapterPh.itemViewHolder>() {

 class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {

        holder.itemView.apply {
            tv.text=photo[position].title
            Glide.with(context).load(photo[position].url).override(600,200).into(imV)

            imV.setOnClickListener {
                    activity.fullImage(photo[position].url)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        return itemViewHolder(

            LayoutInflater.from(activity).inflate(
                R.layout.adapter_ph,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = photo.size
}
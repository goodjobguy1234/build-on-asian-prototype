package com.example.awsvmsguild.homeActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.awsvmsguild.R
import com.example.awsvmsguild.data.VideoContent
import kotlinx.android.synthetic.main.view_recycler_video_list.view.*

class ThumbnailAdapter(val data: ArrayList<VideoContent> = ArrayList(),
                        val callBack: (VideoContent) -> Unit): RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callBack(data[adapterPosition])
            }

        }
        fun bind(position: Int) {
            val image = data[position].url
            val title = data[position].text_result
            val id = data[position].id

            with(itemView) {
                iv_vdo.apply {
                    Glide.with(this).load(image).centerCrop().placeholder(R.drawable.drawable_vdo_loading).into(this)
                }
                tv_title.text = title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_video_list, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
package com.example.awsvmsguild.resultActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.awsvmsguild.R
import com.example.awsvmsguild.data.ResultContent
import kotlinx.android.synthetic.main.view_recycler_result_item.view.*

class ResultAdapter(val data: ArrayList<ResultContent> = ArrayList()): RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            tv_creator.text = data[position].creatorId
            tv_score.text = data[position].score.toString()
            tv_text_result.text = data[position].text_result
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
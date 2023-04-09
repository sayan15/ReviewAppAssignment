package com.example.reviewapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAllRepliesAdapter(private val data:MutableList<ReplyDetails>):RecyclerView.Adapter<ViewAllRepliesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAllRepliesAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_rlies_custom,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewAllRepliesAdapter.ViewHolder, position: Int) {
        holder.rly.text=data[position].reply
        holder.username.text=data[position].userName
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val rly=view.findViewById<TextView>(R.id.userRlies_txtView)
        val username=view.findViewById<TextView>(R.id.userName_txtView)
    }
}
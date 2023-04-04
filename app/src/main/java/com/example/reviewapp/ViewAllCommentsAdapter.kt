package com.example.reviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAllCommentsAdapter(private val data:MutableList<Comments>):RecyclerView.Adapter<ViewAllCommentsAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val cmnt=view.findViewById<TextView>(R.id.userCmnt_txtView)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAllCommentsAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_comments_custom,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewAllCommentsAdapter.ViewHolder, position: Int) {
        holder.cmnt.text=data[position].cmnts
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface onItemClickListner{
        fun onClickListner(position: Int)
    }
}
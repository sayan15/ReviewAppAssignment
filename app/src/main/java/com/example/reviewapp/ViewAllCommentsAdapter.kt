package com.example.reviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAllCommentsAdapter(private val data:MutableList<Comments>,
                             private val listner:onItemClickListner
):RecyclerView.Adapter<ViewAllCommentsAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val cmnt=view.findViewById<TextView>(R.id.userCmnt_txtView)
        val totalLike_tctView=view.findViewById<TextView>(R.id.totallikes_txtView)
        val likeBtn=view.findViewById<ImageButton>(R.id.likeBtn)
        val total_disLike_tctView=view.findViewById<TextView>(R.id.total_dislikes_txtView)
        val unlikeBtn=view.findViewById<ImageButton>(R.id.dislikeBtn)
        val rlyBtn=view.findViewById<ImageButton>(R.id.repliesBtn)
        val total_replies=view.findViewById<TextView>(R.id.total_replies)

        init {
            //perform like
            likeBtn!!.setOnClickListener {
                val pos=adapterPosition
                if(pos!=RecyclerView.NO_POSITION){
                    listner.onClickListnerLike(pos)
                }
            }
            //perform unlike
            unlikeBtn!!.setOnClickListener {
                val pos=adapterPosition
                if(pos!=RecyclerView.NO_POSITION){
                    listner.onClickListnerDisLike(pos)
                }
            }

            //perform reply
            rlyBtn!!.setOnClickListener {
                val pos=adapterPosition
                if(pos!=RecyclerView.NO_POSITION){
                    listner.onClickReply(pos)
                }
            }
        }

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
        holder.totalLike_tctView.text=data[position].total_likes
        holder.total_disLike_tctView.text=data[position].total_unlikes
        holder.total_replies.text=data[position].total_replies
        //if user liked it previously
        if(data[position].likeOrNot=="like"){
            holder.unlikeBtn.setImageResource(R.drawable.before_dislike_thump)
            holder.likeBtn.setImageResource(R.drawable.thump_up)
        }else if(data[position].likeOrNot=="unlike"){
            holder.likeBtn.setImageResource(R.drawable.before_like_thump)
            holder.unlikeBtn.setImageResource(R.drawable.thump_down)
        }else{
            holder.likeBtn.setImageResource(R.drawable.before_like_thump)
            holder.unlikeBtn.setImageResource(R.drawable.before_dislike_thump)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface onItemClickListner{
        fun onClickListnerLike(position: Int)
        fun onClickListnerDisLike(position: Int)
        fun onClickReply(position: Int)
    }
}
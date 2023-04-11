package com.example.reviewapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReplyDialogFragment(private var data:MutableList<ReplyDetails>,private val userId:Int,private val username:String,private val cmnt_id:Int,private val context: Context):DialogFragment() {
    lateinit var adapter:ViewAllRepliesAdapter

    val db =DbConnector(context)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.dialog_fragment_custom,container,false)

        //impose recycler view
        val listView = view.findViewById<RecyclerView>(R.id.repliesCustomRecycleView)
        val layoutManager= LinearLayoutManager(context)

        adapter=ViewAllRepliesAdapter(data)
        listView.adapter=adapter
        listView.layoutManager=layoutManager

        //click reply btn
        val rlytext=view.findViewById<EditText>(R.id.replies_edttext)
        val rlyBtn=view.findViewById<ImageButton>(R.id.rly_btn)
        //submit reply

        rlyBtn!!.setOnClickListener {
                if (rlytext.text.isNullOrEmpty()) {
                    Toast.makeText(context,"Please provide valid text",Toast.LENGTH_SHORT).show()
                }
                else
                {
                db.addNewReply(userId,cmnt_id,rlytext.text.toString()){replyId,result->
                    if(result)
                    {
                        data.add(ReplyDetails(replyId,rlytext.text.toString(),username))
                        adapter.notifyDataSetChanged()
                        rlytext.text.clear()
                    }
                    else
                    {
                        Toast.makeText(context,"Sorry for the inconvenience unable to add your reply at the moment",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
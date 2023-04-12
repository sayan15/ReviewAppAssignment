package com.example.reviewapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Update_Delete(private val cmnt_id:Int,private val position:Int,private val listner:onClickListner):BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.update_delete_custom,container,false)

        //delete action
        val delete_btn=view.findViewById<Button>(R.id.delete_btn)
        delete_btn!!.setOnClickListener {
            if (cmnt_id.toString().isNullOrEmpty())
            {
                Toast.makeText(context,"unable to delete this comment",Toast.LENGTH_SHORT).show()
            }
            else
            {
                listner.onDeleteClick(cmnt_id,position)
            }
        }
        return view
    }

    interface onClickListner{
        fun onDeleteClick(cmnt_id:Int,pos:Int)
    }


}
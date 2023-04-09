package com.example.reviewapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReplyDialogFragment(private var data:MutableList<ReplyDetails>):DialogFragment() {
    lateinit var adapter:ViewAllRepliesAdapter



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
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
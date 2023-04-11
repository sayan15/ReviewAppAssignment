package com.example.reviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomePage : AppCompatActivity(), ViewAllCommentsAdapter.onItemClickListner {

    lateinit var adapter:ViewAllCommentsAdapter
    lateinit var data:MutableList<Comments>
    var menuBtn:Button?=null
    var btn_AddCmnt:Button?=null
    val db =DbConnector(this)

    var userId:Int=0
    var userName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        //initialize button
        btn_AddCmnt=findViewById<Button>(R.id.addCmntButton)


        //get userid
        val extras = intent.extras
        userId=extras!!.getInt("UserId")
        userName=extras!!.getString("UserName")

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        // Invalidate the options menu to make sure the custom layout is displayed
        invalidateOptionsMenu()

        //impose recycler view
        val listView = findViewById<RecyclerView>(R.id.CustomRecycleView)


        //get the data and inflate to recycler view
        readAllComments(userId,listView)

        //add comment button clicked
        btn_AddCmnt!!.setOnClickListener {
            //create edit text pop up
            val builder=AlertDialog.Builder(this)
            val inflater:LayoutInflater=layoutInflater
            val myView = inflater.inflate(R.layout.add_comment_edittext,null)
            val editText:EditText=myView.findViewById<EditText>(R.id.cmnt_editTxt)
            var cmnt=""
            with(builder){
                setTitle("Submit your review here")
                setPositiveButton("Submit"){dialog,which->
                    cmnt=editText.text.toString()
                    //if cmnt is not null only insert review
                    if(cmnt.isNullOrEmpty()!=true){
                        userId?.let { id ->
                            db.addNewComment(id,cmnt) { response ->
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()
                                if (response) {
                                    readAllComments(userId, listView)
                                    Toast.makeText(context,"Thanks  for your review",Toast.LENGTH_SHORT)
                                }else{
                                    Toast.makeText(context,"Sorry for the inconvenience we unable to add your review at the moment!",Toast.LENGTH_SHORT)
                                }
                            }
                        }
                    }else{
                        Toast.makeText(context,"we expect some valid review! Thanks.",Toast.LENGTH_SHORT).show()
                    }
                }
                setNegativeButton("Cancel"){dialog,which->
                    Toast.makeText(context,"we expect your review soon",Toast.LENGTH_SHORT).show()
                }
                setView(myView)
                show()
            }
        }

    }

    //create menu btn and activities
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.myReviw->{
                val myReviewIntent= Intent(this,MyReviews::class.java)
                myReviewIntent.putExtra("UserId",userId)
                myReviewIntent.putExtra("UserName",userName)
                startActivity(myReviewIntent)
                true
            }else->return super.onOptionsItemSelected(item)
        }
    }
    //created this function to recall it again
    fun readAllComments(userId:Int,listView:RecyclerView){
        val layoutManager= LinearLayoutManager(this)

        //get all comments and total likes and dis likes
        db.readAllComments(userId) { cmnts->
            data=cmnts
            adapter = ViewAllCommentsAdapter(data,this)

            listView.adapter=adapter
            listView.layoutManager=layoutManager
        }
    }



    override fun onClickListnerLike(position: Int) {
        var clickedItem:Comments=data[position]
        var likes=0

        if(clickedItem.likeOrNot=="like"){
            Toast.makeText(this,"You have already like this review",Toast.LENGTH_SHORT).show()
        }else if(clickedItem.likeOrNot=="unlike"){
            //update unlike to like
            db.updateLikeOrDislike(userId,clickedItem.cmnt_id,1) { result ->
                if (result) {
                    if (clickedItem.total_unlikes.toString().toInt() > 0) {
                        //reduce un likes
                        likes = clickedItem.total_unlikes.toString().toInt() - 1
                        clickedItem.total_unlikes = likes.toString()
                        //increase likes
                        likes = clickedItem.total_likes.toString().toInt() + 1
                        clickedItem.total_likes = likes.toString()
                    }else{
                        //increase likes
                        likes = clickedItem.total_likes.toString().toInt() + 1
                        clickedItem.total_likes = likes.toString()
                    }
                    clickedItem.likeOrNot = "like"
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(
                        this,
                        "Unable to perform this action at this moment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }else{
            db.addLikeOrDislike(userId,clickedItem.cmnt_id,1) { result ->
                if (result) {
                    likes=clickedItem.total_likes.toString().toInt()+1
                    clickedItem.total_likes = likes.toString()
                    clickedItem.likeOrNot = "like"
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(
                        this,
                        "Unable to perform this action at this moment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onClickListnerDisLike(position: Int) {
        var clickedItem:Comments=data[position]
        var likes=0

        if(clickedItem.likeOrNot=="unlike"){
            Toast.makeText(this,"You have already unlike this review",Toast.LENGTH_SHORT).show()
        }else if(clickedItem.likeOrNot=="like"){
            //update likw to unlike
            db.updateLikeOrDislike(clickedItem.user_id,clickedItem.cmnt_id,0) { result ->
                if (result) {
                    if (clickedItem.total_likes.toString().toInt() > 0) {
                        // decrease likes
                        likes = clickedItem.total_likes.toString().toInt() - 1
                        clickedItem.total_likes = likes.toString()
                        //increase unlikes
                        likes = clickedItem.total_unlikes.toString().toInt() + 1
                        clickedItem.total_unlikes = likes.toString()
                    }else{
                        //increase unlikes
                        likes = clickedItem.total_unlikes.toString().toInt() + 1
                        clickedItem.total_unlikes = likes.toString()
                    }
                    clickedItem.likeOrNot = "unlike"
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(
                        this,
                        "Unable to perform this action at this moment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }else{
            db.addLikeOrDislike(userId,clickedItem.cmnt_id,0) { result ->
                if (result) {
                    likes=clickedItem.total_unlikes.toString().toInt() + 1
                    clickedItem.total_unlikes = likes.toString()
                    clickedItem.likeOrNot = "unlike"
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(
                        this,
                        "Unable to perform this action at this moment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onClickReply(position: Int) {
        var clickedItem:Comments=data[position]
        db.readAllreplies(clickedItem.cmnt_id){data->
            val dialogFragment = ReplyDialogFragment(data,userId,userName.toString(),clickedItem.cmnt_id,this)
            dialogFragment.show(supportFragmentManager, "ReplyDialogFragment")
        }

    }
}
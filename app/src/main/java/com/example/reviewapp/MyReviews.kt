package com.example.reviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyReviews : AppCompatActivity(), ViewAllCommentsAdapter.onItemClickListner {

    lateinit var adapter:ViewAllCommentsAdapter
    lateinit var data:MutableList<Comments>
    val db =DbConnector(this)

    var userId:Int=0
    var userName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reviews)

        //initialize toolbar
        val toolbar=findViewById<Toolbar>(R.id.myReview_toolbar)
        setSupportActionBar(toolbar)

        // Invalidate the options menu to make sure the custom layout is displayed
        invalidateOptionsMenu()

        //get userid
        val extras = intent.extras
        userId=extras!!.getInt("UserId")
        userName=extras!!.getString("UserName")

        //impose recycler view
        val listView = findViewById<RecyclerView>(R.id.myReview_CustomRecycleView)


        //get the data and inflate to recycler view
        readAllMyComments(userId,listView)


    }

    //created this function to recall it again
    fun readAllMyComments(userId:Int,listView:RecyclerView){
        val layoutManager= LinearLayoutManager(this)

        //get all comments and total likes and dis likes
        db.readAllMyComments(userId) { cmnts->
            data=cmnts
            adapter = ViewAllCommentsAdapter(data,this)

            listView.adapter=adapter
            listView.layoutManager=layoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.allReviw->{
                val allReviewIntent= Intent(this,HomePage::class.java)
                allReviewIntent.putExtra("UserId",userId)
                allReviewIntent.putExtra("UserName",userName)
                startActivity(allReviewIntent)
                true
            }else->return super.onOptionsItemSelected(item)
        }
    }

    override fun onClickListnerLike(position: Int) {
        var clickedItem:Comments=data[position]
        var likes=0

        if(clickedItem.likeOrNot=="like"){
            Toast.makeText(this,"You have already like this review", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"You have already unlike this review", Toast.LENGTH_SHORT).show()
        }else if(clickedItem.likeOrNot=="like"){
            //update likw to unlike
            db.updateLikeOrDislike(userId,clickedItem.cmnt_id,0) { result ->
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
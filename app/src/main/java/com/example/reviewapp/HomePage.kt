package com.example.reviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomePage : AppCompatActivity() {

    lateinit var adapter:ViewAllCommentsAdapter
    lateinit var data:MutableList<Comments>
    var btn_AddCmnt:Button?=null
    val db =DbConnector(this)

    var userId:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        //initialize button
        btn_AddCmnt=findViewById<Button>(R.id.addCmntButton)


        //get userid
        val extras = intent.extras
        userId=extras?.getInt("UserId")

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        //impose recycler view
        val listView = findViewById<RecyclerView>(R.id.CustomRecycleView)


        //get the data and inflate to recycler view
        readAllComments(listView)

                //add comment button clicked
        btn_AddCmnt!!.setOnClickListener {
            userId?.let { it1 ->
                db.addNewComment(it1,"test",{ response->
                    Toast.makeText(this,response.toString(),Toast.LENGTH_SHORT).show()
                    if(response){
                        readAllComments(listView)
                    }
                })
            }
        }

    }
    //created this function to recall it again
    fun readAllComments(listView:RecyclerView){
        val layoutManager= LinearLayoutManager(this)
        db.readAllComments { cmnts->
            data=cmnts
            adapter = ViewAllCommentsAdapter(data)

            listView.adapter=adapter
            listView.layoutManager=layoutManager
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                Toast.makeText(this, "logout button clicked", Toast.LENGTH_SHORT).show()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }
}
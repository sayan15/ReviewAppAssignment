package com.example.reviewapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class HomePage : AppCompatActivity(){


    var userId:Int=0
    var userName:String?=null
    var userType:String?=null

    var allReviewBtn:ImageButton?=null
    var myReviewBtn:ImageButton?=null

    val db =DbConnector(this)
    lateinit var data:MutableList<Comments>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //get userid
        val extras = intent.extras
        userId=extras!!.getInt("UserId")
        userName=extras!!.getString("UserName")
        userType=extras!!.getString("UserType")

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        // Invalidate the options menu to make sure the custom layout is displayed
        invalidateOptionsMenu()


        //Create barchart data set
        // get all comments and total likes and dis likes
        db.readAllComments(userId) { cmnts->
            data=cmnts
            val entries = listOf(
                BarEntry(1f, data.count{it.user_id==userId}.toFloat()),
                BarEntry(2f, data.count().toFloat()),
            )
            val labels = listOf("All Reviews", "My Review")
            val barchart=findViewById<BarChart>(R.id.barChart)
            val barDataSet=BarDataSet(entries,"Number of Reviews")
            barDataSet.setColors(Color.RED,Color.BLUE)
            barDataSet
            val data=BarData(barDataSet)
            // Set the value formatter to display the labels for each bar
            data!!.setValueFormatter(object:ValueFormatter(){
                override fun getBarLabel(barEntry: BarEntry?): String {
                    if (barEntry != null) {
                        return labels[barEntry.x.toInt() % labels.size]
                    }
                    return super.getBarLabel(barEntry)
                }
            })
            barchart.data=data
            barchart.invalidate()
        }

        //Navigate to all Review
        allReviewBtn=findViewById(R.id.allReview_btn)
        allReviewBtn!!.setOnClickListener {
            val allReviewIntent= Intent(this,AllReviews::class.java)
            allReviewIntent.putExtra("UserId",userId)
            allReviewIntent.putExtra("UserName",userName)
            allReviewIntent.putExtra("UserType",userType)
            allReviewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(allReviewIntent)
        }

        //Navigate to my Review
        myReviewBtn=findViewById(R.id.myReview_btn)
        myReviewBtn!!.setOnClickListener {
            val myReviewIntent= Intent(this,MyReviews::class.java)
            myReviewIntent.putExtra("UserId",userId)
            myReviewIntent.putExtra("UserName",userName)
            myReviewIntent.putExtra("UserType",userType)
            myReviewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(myReviewIntent)
        }

    }

    //create menu btn and activities
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.logout->{
                true
            }else->return super.onOptionsItemSelected(item)
        }
    }

}
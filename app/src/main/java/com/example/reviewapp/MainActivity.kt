package com.example.reviewapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

var v_btnLogin:TextView?=null
var v_btnSignUp:TextView?=null
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        v_btnLogin=findViewById<TextView>(R.id.loginBtn)
        v_btnSignUp=findViewById<TextView>(R.id.SignupBtn)
        val v_SignPageIntent=Intent(this,SignUp::class.java)


        v_btnLogin!!.setOnClickListener {
            v_btnSignUp!!.setBackgroundResource(0)
            v_btnLogin!!.setBackgroundResource(R.drawable.switchbtn)
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show()
        }
        v_btnSignUp!!.setOnClickListener {
            v_btnLogin!!.setBackgroundResource(0)
            v_btnSignUp!!.setBackgroundResource(R.drawable.switchbtn)
            startActivity(v_SignPageIntent)
        }

    }
}
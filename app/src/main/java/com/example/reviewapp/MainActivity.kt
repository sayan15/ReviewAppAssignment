package com.example.reviewapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {
    var v_btnLogin:TextView?=null
    var v_btnSignUp:TextView?=null
    var v_edtUsername:EditText?=null
    var v_edtPassword:EditText?=null
    val dbConnector=DbConnector(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        v_btnLogin=findViewById<TextView>(R.id.loginBtn)
        v_btnSignUp=findViewById<TextView>(R.id.SignupBtn)
        v_edtUsername=findViewById<EditText>(R.id.username)
        v_edtPassword=findViewById<EditText>(R.id.password)



        v_btnLogin!!.setOnClickListener {
            v_btnSignUp!!.setBackgroundResource(0)
            v_btnLogin!!.setBackgroundResource(R.drawable.switchbtn)
            dbConnector.checkUsernameAndPassword(v_edtUsername!!.text.toString(),v_edtPassword!!.text.toString()) { verified, userDetails ->
                if (verified) {
                    Toast.makeText(
                        this,
                        "successfully login " + userDetails[0].username,
                        Toast.LENGTH_SHORT
                    ).show()
                    val v_MainPageIntent = Intent(this, HomePage::class.java)
                    v_MainPageIntent.putExtra("UserName", userDetails[0].username)
                    v_MainPageIntent.putExtra("UserId", userDetails[0].userId)
                    v_MainPageIntent.putExtra("UserType", userDetails[0].type)
                    startActivity(v_MainPageIntent)
                } else {
                    Toast.makeText(this, "unable to login", Toast.LENGTH_SHORT).show()
                }
            }

        }
        v_btnSignUp!!.setOnClickListener {
            v_btnLogin!!.setBackgroundResource(0)
            v_btnSignUp!!.setBackgroundResource(R.drawable.switchbtn)
            val v_SignPageIntent=Intent(this,SignUp::class.java)
            startActivity(v_SignPageIntent)
        }

    }
}
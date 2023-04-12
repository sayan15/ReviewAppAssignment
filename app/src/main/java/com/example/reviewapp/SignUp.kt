package com.example.reviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignUp : AppCompatActivity() {
    var edt_UsrName:EditText?=null
    var edt_Password:EditText?=null
    var txt_mail:TextView?=null
    var txt_userName:TextView?=null
    var txt_passwordValidity:TextView?=null
    var edt_email:EditText?=null
    var edt_reTypePass:EditText?=null
    var v_btnSignUp: TextView?=null
    val dbConnector=DbConnector(this);
    val cmnFunc=CommonFunctions()

    var passwordValid:Boolean?=null
    var emailValid:Boolean?=null
    var mess= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edt_UsrName=findViewById<EditText>(R.id.signUp_username)
        txt_userName=findViewById<TextView>(R.id.userValidty)
        v_btnSignUp=findViewById<TextView>(R.id.SignPage_Btn)
        edt_email=findViewById<EditText>(R.id.signUp_email)
        edt_Password=findViewById<EditText>(R.id.SignUp_password)
        edt_reTypePass=findViewById<EditText>(R.id.confirm_password)
        txt_mail=findViewById<TextView>(R.id.emailValidty)
        txt_passwordValidity=findViewById<TextView>(R.id.passwordValidty)



        //check for email validation
        edt_email!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(cmnFunc.isEmailValid(s.toString())!=true){
                    txt_mail!!.isEnabled=true
                    txt_mail!!.setText("*Inavlid Email address")
                    emailValid=false
                }
                else
                {
                    txt_mail!!.setText("")
                    txt_mail!!.isEnabled=false
                    emailValid=true
                }
            }

        })
        //check for password confirmation after password field entered ro re changed
        edt_Password!!.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(passwordValid==true && edt_reTypePass!!.text.toString()!=s.toString())
                {
                    passwordValid=false
                    txt_passwordValidity!!.isEnabled=true
                    txt_passwordValidity!!.setText("Password is not matching")
                }else if(passwordValid!=true && edt_reTypePass!!.text.isNullOrEmpty())
                {
                    passwordValid=false
                    txt_passwordValidity!!.isEnabled=false
                    txt_passwordValidity!!.setText("")
                }
            }

        })
        //check for the password confirmation
        edt_reTypePass!!.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(edt_Password!!.text.toString()==s.toString())
                {
                    passwordValid=true
                    txt_passwordValidity!!.setText("")
                    txt_passwordValidity!!.isEnabled=false
                }else
                {
                    passwordValid=false
                    txt_passwordValidity!!.isEnabled=true
                    txt_passwordValidity!!.setText("Password is not matching")
                }
            }

        })

        v_btnSignUp!!.setOnClickListener {

            dbConnector.checkUser(edt_UsrName!!.text.toString(),{ message ->
                    mess = message
                    if (mess.toBoolean()) {
                        txt_userName!!.setText("* Username already exist")
                    } else if (mess.toBoolean()==false){
                        txt_userName!!.setText("")
                        dbConnector.inserUser(edt_UsrName!!.text.toString(),edt_Password!!.text.toString(),edt_email!!.text.toString(),"user",{response->
                            if (response){
                                Toast.makeText(this,"Registered Success fully",Toast.LENGTH_LONG).show()
                                edt_UsrName!!.editableText.clear()
                                edt_email!!.editableText.clear()
                                edt_Password!!.editableText.clear()
                                edt_reTypePass!!.editableText.clear()
                                val v_SignInIntent= Intent(this,MainActivity::class.java)
                                startActivity(v_SignInIntent)

                            }
                            else{
                                Toast.makeText(this,"Unable to register try again",Toast.LENGTH_LONG).show()
                                edt_UsrName!!.editableText.clear()
                                edt_email!!.editableText.clear()
                                edt_Password!!.editableText.clear()
                                edt_reTypePass!!.editableText.clear()
                            }
                        })
                    }
                })

        }

    }
}
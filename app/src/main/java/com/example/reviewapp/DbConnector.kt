package com.example.reviewapp


import com.android.volley.toolbox.Volley
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject

class DbConnector(private val context: Context) {
     fun inserUser( username: String,password:String,email:String,type:String) {
         val Url = "http://192.168.1.127/reviewapp/rank-data-information.php?op=1"
         // Instantiate the RequestQueue.
         val queue = Volley.newRequestQueue(context)
         val stringRequest = object : StringRequest(Request.Method.POST,
             Url, Response.Listener { response ->
                 try {
                     val obj = JSONObject(response)
                     Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                 } catch (e: JSONException) {
                     e.printStackTrace()
                 }
             }, Response.ErrorListener { error ->
                 if (error != null) {
                     Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                 };
             }){
             // Override the getParams() method to specify the data you want to send in the request body.
                 override fun getParams(): MutableMap<String, String>? {
                     val paras=HashMap<String,String>()
                     paras["username"]=username
                     paras["password"]=password
                     paras["email"]=email
                     paras["type"]=type
                     return paras
                 }
         }
         // Add the request to the RequestQueue.
         queue.add(stringRequest)

     }
}
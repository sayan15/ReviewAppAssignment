package com.example.reviewapp


import android.content.ComponentCallbacks
import com.android.volley.toolbox.Volley
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject

class DbConnector(private val context: Context) {
    val Url = "http://192.168.1.127/reviewapp/"
    val insertUrl=Url+"rank-data-information.php?op=1"
    val checkUserUrl=Url+"CheckUserExist.php?op=1"
     fun inserUser( username: String,password:String,email:String,type:String,callback: (Boolean) -> Unit) {

         // Instantiate the RequestQueue.
         val queue = Volley.newRequestQueue(context)
         val stringRequest = object : StringRequest(Request.Method.POST,
             insertUrl, Response.Listener { response ->
                 try {
                     val obj = JSONObject(response)
                     callback(obj.getBoolean("message"))
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
    //check whether user exist already
    fun checkUser(username: String,callback:(String)->Unit){
        var message=""
        val queue=Volley.newRequestQueue(context)
        val stringRequest=object :StringRequest(Request.Method.POST,
            checkUserUrl,
            Response.Listener { response ->
                try{
                    val obj = JSONObject(response)
                    message=obj.getString("message")
                    callback(message)
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            },Response.ErrorListener { error ->
                if (error != null) {
                    Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                };
            }){
            override fun getParams(): MutableMap<String, String>? {
                val paras=HashMap<String,String>()
                paras["username"]=username
                return paras
            }

        }
        queue.add(stringRequest)

    }
}
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
    val Url = "http://192.168.1.130//reviewappPhpScripts/"
    val insertUrl=Url+"rank-data-information.php?op=1"
    val checkUserUrl=Url+"CheckUserExist.php?op=1"
    val checkUserPassUrl=Url+"usernameAndPassword.php?op=1"
    val readAllCommentsUrl=Url+"viewAllComments.php?op=1"
    val readAllMyCommentsUrl=Url+"viewAllMyComments.php?op=1"
    val addCommentsUrl=Url+"addComments.php?op=1"
    val addLikeDislikeUrl=Url+"addLikeDisLike.php?op=1"
    val updateLikeDislikeUrl=Url+"updateLikeDislike.php?op=1"
    val readAllReplyUrl=Url+"viewAllReplies.php?op=1"
    val addReplyUrl=Url+"addReplies.php?op=1"
    val deleteReviewUrl=Url+"deleteReviews.php?op=1"
    val updateReviewUrl=Url+"updateReviews.php?op=1"
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

    //check username and password is correct
    fun checkUsernameAndPassword(username: String,password:String,callback:(Boolean,MutableList<UserDetails>)->Unit){
        val data= mutableListOf<UserDetails>()
        val queue=Volley.newRequestQueue(context)
        val stringRequest=object :StringRequest(Request.Method.POST,
            checkUserPassUrl,
            Response.Listener { response ->
                try{

                    val obj = JSONObject(response)
                    if(obj.getBoolean("verified")){
                        val value=UserDetails(obj.getInt("user_id"),obj.getString("user_name"),obj.getString("user_type"))
                        data.add(value)
                    }
                    callback(obj.getBoolean("verified"),data)
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
                paras["password"]=password
                return paras
            }

        }
        queue.add(stringRequest)

    }

    //Read All comments
    fun readAllComments(user_id:Int,callback:(MutableList<Comments>)->Unit){
        val data= mutableListOf<Comments>()
        val queue=Volley.newRequestQueue(context)
        val stringRequest=object :StringRequest(Request.Method.POST,
            readAllCommentsUrl,
            { response ->
                try{
                    val obj = JSONObject(response)
                    val rows = obj.getJSONArray("AllComments")
                    for (i in 0 until rows.length()){
                        val row = rows.getJSONObject(i)
                        val values=Comments(row.getInt("cmnt_id"),row.getString("user_cmnts"),row.getInt("user_id"),row.getString("userName"),row.getString("total_likes"),row.getString("total_unlikes"),row.getString("by_user"),row.getString("reply_count"))
                        data.add(values)
                    }
                    callback(data)
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            }, { error ->
                if (error != null) {
                    Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                };
            }
        ){override fun getParams(): MutableMap<String, String>? {
                val paras=HashMap<String,String>()
                paras["user_id"]=user_id.toString()
                return paras
            }}
        queue.add(stringRequest)
    }

    //readAllmyComments
    fun readAllMyComments(user_id:Int,callback:(MutableList<Comments>)->Unit){
        val data= mutableListOf<Comments>()
        val queue=Volley.newRequestQueue(context)
        val stringRequest=object :StringRequest(Request.Method.POST,
            readAllMyCommentsUrl,
            { response ->
                try{
                    val obj = JSONObject(response)
                    val rows = obj.getJSONArray("AllComments")
                    for (i in 0 until rows.length()){
                        val row = rows.getJSONObject(i)
                        val values=Comments(row.getInt("cmnt_id"),row.getString("user_cmnts"),row.getInt("user_id"),row.getString("userName"),row.getString("total_likes"),row.getString("total_unlikes"),row.getString("by_user"),row.getString("reply_count"))
                        data.add(values)
                    }
                    callback(data)
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            }, { error ->
                if (error != null) {
                    Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                };
            }
        ){override fun getParams(): MutableMap<String, String>? {
            val paras=HashMap<String,String>()
            paras["user_id"]=user_id.toString()
            return paras
        }}
        queue.add(stringRequest)
    }
    //read all my replies
    fun readAllreplies(cmnt_id:Int,callback:(MutableList<ReplyDetails>)->Unit){
        val data= mutableListOf<ReplyDetails>()
        val queue=Volley.newRequestQueue(context)
        val stringRequest=object :StringRequest(Request.Method.POST,
            readAllReplyUrl,
            { response ->
                try{
                    val obj = JSONObject(response)
                    val rows = obj.getJSONArray("AllReplies")
                    if(rows.length()>0){
                        for (i in 0 until rows.length()){
                            val row = rows.getJSONObject(i)
                            val values=ReplyDetails(row.getInt("reply_id"),row.getString("reply"),row.getString("userName"))
                            data.add(values)
                        }
                    }
                    else{
                        Toast.makeText(context,"There is no replies for this review",Toast.LENGTH_SHORT).show()
                    }

                    callback(data)
                }
                catch (e: JSONException)
                {
                    e.printStackTrace()
                }
            }, { error ->
                if (error != null) {
                    Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                };
            }
        ){override fun getParams(): MutableMap<String, String>? {
            val paras=HashMap<String,String>()
            paras["cmnt_id"]=cmnt_id.toString()
            return paras
        }}
        queue.add(stringRequest)
    }

    //Add comments
    fun addNewComment( user_id: Int,comment:String,callback: (Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            addCommentsUrl, Response.Listener { response ->
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
                paras["user_id"]=user_id.toString()
                paras["comment"]=comment
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }


    //add like or dislike reviews
    fun addLikeOrDislike( user_id: Int,comnt_id:Int,like_status:Int,callback: (Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            addLikeDislikeUrl, Response.Listener { response ->
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
                paras["user_id"]=user_id.toString()
                paras["cmnt_id"]=comnt_id.toString()
                paras["like_status"]=like_status.toString()
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    //update like or dislike reviews
    fun updateLikeOrDislike( user_id: Int,comnt_id:Int,like_status:Int,callback: (Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            updateLikeDislikeUrl, Response.Listener { response ->
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
                paras["user_id"]=user_id.toString()
                paras["cmnt_id"]=comnt_id.toString()
                paras["like_status"]=like_status.toString()
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    //add new reply
    fun addNewReply( user_id: Int,cmnt_id: Int,reply:String,callback: (Int,Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            addReplyUrl, Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    callback(obj.getInt("reply_id"),obj.getBoolean("message"))
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
                paras["user_id"]=user_id.toString()
                paras["cmnt_id"]=cmnt_id.toString()
                paras["reply"]=reply
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    //delete review
    fun deleteReview( comnt_id:Int,callback: (Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            deleteReviewUrl, Response.Listener { response ->
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
                paras["cmnt_id"]=comnt_id.toString()
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    //update review
    fun updateReview( comnt_id:Int,comment: String,callback: (Boolean) -> Unit) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Request.Method.POST,
            updateReviewUrl, Response.Listener { response ->
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
                paras["cmnt_id"]=comnt_id.toString()
                paras["comment"]=comment
                return paras
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

}
package com.example.reviewapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AddCommentFragment(private val context: Context,private val userId:Int):DialogFragment() {
    val db =DbConnector(context)
    var result=false;
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.add_comment_edittext, null)
        val editText = view.findViewById<EditText>(R.id.cmnt_editTxt)

        return AlertDialog.Builder(requireContext())
            .setTitle("Enter your review")
            .setView(view)
            .setPositiveButton("Submit") { dialog, which ->
                // Handle the positive button click here
                val review = editText.text.toString()
                //if review is not null only insert review
                if(review.isNullOrEmpty()!=true){
                    userId?.let { id ->
                        db.addNewComment(id,review) { response ->
                            if (response) {
                                Toast.makeText(context,"Thanks  for your review", Toast.LENGTH_SHORT)
                                dismiss()
                            }else{
                                Toast.makeText(context,"Sorry for the inconvenience we unable to add your review at the moment!",
                                    Toast.LENGTH_SHORT)
                            }
                        }
                    }
                }else{
                    Toast.makeText(context,"we expect some valid review! Thanks.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle the negative button click here
                Toast.makeText(context,"we expect your review soon", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .create()
    }

    //overide the dismiss function
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Notify the listener that the dialog has been dismissed
        onDialogDismissedListener?.onDialogDismissed()
    }

    //created the interface implement on button click
    interface OnDialogDismissedListener {
        fun onDialogDismissed()
    }

    private var onDialogDismissedListener: OnDialogDismissedListener? = null

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener) {
        onDialogDismissedListener = listener
    }

}
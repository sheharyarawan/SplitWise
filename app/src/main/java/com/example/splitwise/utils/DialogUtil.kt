package com.example.splitwise.utils
import android.content.Context
import androidx.appcompat.app.AlertDialog
object DialogUtils {
    fun showErrorDialog(
        context: Context,
        message: String
    ) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
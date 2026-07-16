package com.theword.app.ui.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

object ShareUtils {

    fun copyVerse(context: Context, reference: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("verse", "$text - $reference"))
        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    fun shareVerse(context: Context, reference: String, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "\"$text\" - $reference")
            putExtra(Intent.EXTRA_SUBJECT, reference)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share verse"))
    }
}

package com.github.droibit.firebase_todo.ui.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.github.droibit.firebase_todo.R

class CheckableSingleLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val checkImageView: ImageView = itemView.findViewById(R.id.check)
    private val textView: TextView = itemView.findViewById(R.id.text)

    fun bindTo(text: String, checked: Boolean) {
        textView.text = text
        checkImageView.isInvisible = !checked
    }
}
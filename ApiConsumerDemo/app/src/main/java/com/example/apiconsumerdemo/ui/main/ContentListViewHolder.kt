package com.example.apiconsumerdemo.ui.main

import android.os.SystemClock
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiconsumerdemo.R
import com.example.apiconsumerdemo.domain.DemoContent
import com.squareup.picasso.Picasso

internal class ContentListViewHolder(view: View, private val picasso: Picasso): RecyclerView.ViewHolder(view) {

    companion object {
        fun onClick(holder: RecyclerView.ViewHolder, action: (String) -> Unit) = object :
            View.OnClickListener {
            val debounceThreshold = 1000L
            private var lastClickTime = 0L
            override fun onClick(p0: View?) {
                val elapsedTime = SystemClock.elapsedRealtime()
                if (elapsedTime - lastClickTime < debounceThreshold) return
                val itemId = holder.itemView.tag.toString()
                action(itemId)
            }
        }
    }

    private val image = itemView.findViewById<AppCompatImageView?>(R.id.list_item_image)
    private val title = itemView.findViewById<AppCompatTextView?>(R.id.list_item_title)

    fun bind(content: DemoContent) {
        itemView.tag = content.id
        image?.let {
            picasso
                .load(content.image)
                .noFade()
                .noPlaceholder()
                .centerCrop()
                .fit() //some images are unnecessarily big - performance impact
                .tag(it)
                .into(it)
        }
        title?.text = content.title
    }

    fun reset() {
        itemView.tag = null
    }

}
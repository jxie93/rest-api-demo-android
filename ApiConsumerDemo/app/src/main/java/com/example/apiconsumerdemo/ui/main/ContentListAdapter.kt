package com.example.apiconsumerdemo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apiconsumerdemo.R
import com.example.apiconsumerdemo.domain.DemoContent
import com.squareup.picasso.Picasso

internal interface ContentListDelegate {
    fun onListItemPressed(content: DemoContent)
}

internal class ContentListAdapter(
    private val delegate: ContentListDelegate,
    private val picasso: Picasso
): RecyclerView.Adapter<ContentListViewHolder>() {

    private var data: List<DemoContent> = emptyList()

    enum class ContentListItemViewType {
        NORMAL, PLACEHOLDER
    }

    init {
        setHasStableIds(true)
    }

    fun updateData(data: List<DemoContent>, useDiff: Boolean = false) {
        val oldData = this.data
        this.data = data
        if (useDiff) {
            dispatchDiffUpdate(oldData)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            if (viewType == ContentListItemViewType.PLACEHOLDER.ordinal)
                R.layout.item_view_list_content_placeholder else
                R.layout.item_view_list_content,
            parent,
            false
        )
        return ContentListViewHolder(view, picasso)
    }

    override fun onBindViewHolder(holder: ContentListViewHolder, position: Int) {
        val entity = data[position]
        if (holder.itemView.tag == entity.id) return //already bound
        holder.bind(entity)
    }

    override fun getItemViewType(position: Int): Int {
        val entity = data[position]
        return if (entity.isPlaceholder)
            ContentListItemViewType.PLACEHOLDER.ordinal else
            ContentListItemViewType.NORMAL.ordinal
    }

    override fun onBindViewHolder(
        holder: ContentListViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val coffee = data[position]
        holder.bind(coffee)
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewDetachedFromWindow(holder: ContentListViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun onViewAttachedToWindow(holder: ContentListViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener(
            ContentListViewHolder.onClick(holder) { itemId ->
                val dataItem = data
                    .firstOrNull { content -> content.id == itemId }
                    .takeIf { content -> content?.isPlaceholder == false } ?: return@onClick
                delegate.onListItemPressed(dataItem)
            }
        )
    }

    override fun onViewRecycled(holder: ContentListViewHolder) {
        super.onViewRecycled(holder)
        holder.reset()
    }

    override fun getItemCount(): Int = data.size

    private fun dispatchDiffUpdate(oldData: List<DemoContent>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldData.size

            override fun getNewListSize(): Int = data.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[newItemPosition].hashCode() == oldData[oldItemPosition].hashCode()
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[newItemPosition].hashCode() == oldData[oldItemPosition].hashCode()
            }
        })
        diffResult.dispatchUpdatesTo(this)
    }
}
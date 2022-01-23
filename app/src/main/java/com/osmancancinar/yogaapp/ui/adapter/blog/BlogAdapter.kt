package com.osmancancinar.yogaapp.ui.adapter.blog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.databinding.BlogListRowBinding

class BlogAdapter(private val clickListener: BlogListener) :
    ListAdapter<Blog, BlogAdapter.BlogViewHolder>(BlogDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder =
        BlogViewHolder.from(parent)

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class BlogViewHolder private constructor(val binding: BlogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Blog, clickListener: BlogListener) {
            binding.blog = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BlogViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BlogListRowBinding.inflate(layoutInflater, parent, false)
                return BlogViewHolder(binding)
            }
        }
    }

    class BlogDiffCallBack : DiffUtil.ItemCallback<Blog>() {
        override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
            return oldItem == newItem
        }
    }
}

class BlogListener(val listener: (postId: Int) -> Unit) {
    fun onClick(blog: Blog) = listener(blog.postId!!)
}
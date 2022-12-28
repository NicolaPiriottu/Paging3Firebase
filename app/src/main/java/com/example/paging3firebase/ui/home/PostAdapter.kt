package com.example.paging3firebase.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class PostAdapter(val listener: Listener) : ListAdapter<PostModel, PostViewHolder>(DIFF_CALLBACK) {

    interface Listener{
        fun onPostClicked(postModel: PostModel)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PostViewHolder {

        return PostViewHolder(
            binding = PostViewHolder.getBinding(parent),
            listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel) =
                oldItem == newItem
        }
    }
}

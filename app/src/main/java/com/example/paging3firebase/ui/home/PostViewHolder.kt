package com.example.paging3firebase.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3firebase.databinding.LayoutPostItemBinding

class PostViewHolder(
    private val binding: LayoutPostItemBinding,
    var listener: PostAdapter.Listener,
) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var itemRef: PostModel

    init {
        binding.root.setOnClickListener {
            listener.onPostClicked(itemRef)
        }
    }

    fun bind(item: PostModel) {
        itemRef = item
        binding.titolo.text = itemRef.title
        binding.descrizione.text = item.message
        binding.like.text = "like ${itemRef.likeCount}"
    }

    companion object {
        fun getBinding(parent: ViewGroup): LayoutPostItemBinding {
            val inflater = LayoutInflater.from(parent.context)

            return LayoutPostItemBinding.inflate(
                inflater,
                parent,
                false
            )
        }
    }
}
package com.harissabil.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.harissabil.storyapp.R
import com.harissabil.storyapp.data.remote.response.ListStoryItem
import com.harissabil.storyapp.databinding.ItemStoriesBinding
import com.harissabil.storyapp.ui.detail.DetailActivity
import com.harissabil.storyapp.ui.detail.DetailActivity.Companion.EXTRA_DATA

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.ivPhoto.load(data.photoUrl) {
                placeholder(R.color.dim)
            }
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description

            binding.cvStory.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivPhoto, "photo"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "description")
                    )

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, data)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(list)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
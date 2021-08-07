package com.example.noteapp.ui.folder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemFolderBinding

class FolderAdapter(private val listener: onItemClickListener) :
    ListAdapter<Pair<Int, String>, FolderAdapter.FoldersViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoldersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoldersViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, holder)
    }

    inner class FoldersViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val pair = getItem(position)
                        listener.onItemClick(pair.second)
                    }
                }
            }
        }


        fun bind(pairData: Pair<Int, String>, holder: FoldersViewHolder) {
            binding.apply {
                folderName.text = pairData.second

                folderImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        pairData.first
                    )
                )
                folderImage.imageTintList= ColorStateList.valueOf(Color.BLACK)
            }

        }
    }


    interface onItemClickListener {
        fun onItemClick(folderName: String)
    }


    class DiffCallback : DiffUtil.ItemCallback<Pair<Int, String>>() {

        override fun areItemsTheSame(
            oldItem: Pair<Int, String>,
            newItem: Pair<Int, String>
        ) =
            oldItem.second == newItem.second


        override fun areContentsTheSame(
            oldItem: Pair<Int, String>,
            newItem: Pair<Int, String>
        ) =
            oldItem == newItem


    }
}


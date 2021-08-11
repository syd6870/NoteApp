package com.example.noteapp.ui.nearbyLocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.ItemNearbyCardBinding
import com.example.noteapp.ui.note.NoteAdapter
import com.example.noteapp.util.round


class NearbyLocationAdapter(private val listener: onItemClickListener) :
    ListAdapter<Note, NearbyLocationAdapter.NearbyLocationViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyLocationViewHolder {
        val binding =
            ItemNearbyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NearbyLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyLocationViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class NearbyLocationViewHolder(private val binding: ItemNearbyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                buttonNearbyCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val note = getItem(position)
                        listener.onItemClick(note)
                    }
                }
            }
        }


        fun bind(note: Note) {
            binding.apply {
                textviewNearbyTitle.text = note.title
                textviewNearbyBody.text = note.content
                val string = String.format("%s\t%s", note.createdTime, note.createdDate)
                textviewNearbyDate.text = string
                textviewNearbyDistance.text = "≈ ${note.distanceFromUser.round(3)} Km"
            }

        }


    }


    interface onItemClickListener {
        fun onItemClick(note: Note)
    }


    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem

    }

}


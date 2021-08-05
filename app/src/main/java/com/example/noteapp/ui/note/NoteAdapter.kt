package com.example.noteapp.ui.note

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.ItemNoteBinding

class NoteAdapter(private val listener: onItemClickListener) : ListAdapter<Note, NoteAdapter.NotesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

   inner class NotesViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener{
                    val position=adapterPosition
                    if(position!=RecyclerView.NO_POSITION){
                        val note=getItem(position)
                        listener.onItemClick(note)
                    }
                }
            }
        }



        fun bind(note: Note) {
            binding.apply {
                imageViewLineDesign.imageTintList = ColorStateList.valueOf(Color.RED)
                textviewNoteDate.text = note.createdDate
                textviewNoteTitle.text = note.title
                textviewNoteBody.text = note.content
            }

        }
    }


    interface onItemClickListener{
        fun onItemClick(note: Note)
    }


    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem

    }
}


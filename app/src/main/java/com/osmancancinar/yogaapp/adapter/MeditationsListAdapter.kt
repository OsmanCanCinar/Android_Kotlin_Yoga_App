package com.osmancancinar.yogaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.databinding.MeditationListRowBinding

class MeditationsListAdapter(val clickListener: MeditationCategoryListener) :
    ListAdapter<MeditationCategory, MeditationsListAdapter.MeditationsListViewHolder>(
        MeditationsListDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationsListViewHolder =
        MeditationsListViewHolder.from(parent)

    override fun onBindViewHolder(holder: MeditationsListViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class MeditationsListViewHolder private constructor(val binding: MeditationListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MeditationCategory, clickListener: MeditationCategoryListener) {
            binding.meditationCategory = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MeditationsListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MeditationListRowBinding.inflate(layoutInflater, parent, false)
                return MeditationsListViewHolder(binding)
            }
        }
    }

    class MeditationsListDiffCallBack : DiffUtil.ItemCallback<MeditationCategory>() {
        override fun areItemsTheSame(
            oldItem: MeditationCategory,
            newItem: MeditationCategory
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MeditationCategory,
            newItem: MeditationCategory
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class MeditationCategoryListener(val listener : (categoryId : Int) -> Unit) {
    fun onClick(meditationCategory: MeditationCategory) = listener(meditationCategory.id)
}
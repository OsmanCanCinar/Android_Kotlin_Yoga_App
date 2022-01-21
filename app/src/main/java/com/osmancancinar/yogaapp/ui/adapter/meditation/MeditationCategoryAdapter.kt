package com.osmancancinar.yogaapp.ui.adapter.meditation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.databinding.MeditationCatagoryRowBinding

class MeditationCategoryAdapter(private val clickListener: MeditationCategoryListener) :
    ListAdapter<MeditationCategory, MeditationCategoryAdapter.MeditationCategoryViewHolder>(
        MeditationCategoryDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationCategoryViewHolder =
        MeditationCategoryViewHolder.from(parent)

    override fun onBindViewHolder(holder: MeditationCategoryViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class MeditationCategoryViewHolder private constructor(val binding: MeditationCatagoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MeditationCategory, clickListener: MeditationCategoryListener) {
            binding.meditationCategory = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup):MeditationCategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MeditationCatagoryRowBinding.inflate(layoutInflater, parent, false)
                return MeditationCategoryViewHolder(binding)
            }
        }
    }

    class MeditationCategoryDiffCallBack : DiffUtil.ItemCallback<MeditationCategory>() {
        override fun areItemsTheSame(
            oldItem: MeditationCategory,
            newItem: MeditationCategory
        ): Boolean {
            return oldItem.meditationId == newItem.meditationId
        }

        override fun areContentsTheSame(
            oldItem: MeditationCategory,
            newItem: MeditationCategory
        ): Boolean {
            return oldItem == newItem
        }

    }
}

class MeditationCategoryListener(val listener : (meditationId : Int) -> Unit) {
    fun onClick(meditationCategory: MeditationCategory) = listener(meditationCategory.meditationId!!)
}

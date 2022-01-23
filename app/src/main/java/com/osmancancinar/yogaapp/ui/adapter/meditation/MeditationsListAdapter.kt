package com.osmancancinar.yogaapp.ui.adapter.meditation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.databinding.MeditationListRowBinding

class MeditationsListAdapter(private val clickListener: MeditationCategoriesListListener) :
    ListAdapter<MeditationCategoriesList, MeditationsListAdapter.MeditationsListViewHolder>(
        MeditationsListDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationsListViewHolder =
        MeditationsListViewHolder.from(parent)

    override fun onBindViewHolder(holder: MeditationsListViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class MeditationsListViewHolder private constructor(val binding: MeditationListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MeditationCategoriesList, clickListener: MeditationCategoriesListListener) {
            binding.meditationCategoriesList = item
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

    class MeditationsListDiffCallBack : DiffUtil.ItemCallback<MeditationCategoriesList>() {
        override fun areItemsTheSame(
            oldItem: MeditationCategoriesList,
            newItem: MeditationCategoriesList
        ): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(
            oldItem: MeditationCategoriesList,
            newItem: MeditationCategoriesList
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class MeditationCategoriesListListener(val listener: (categoryId: Int) -> Unit) {
    fun onClick(meditationCategoriesList: MeditationCategoriesList) =
        listener(meditationCategoriesList.categoryId!!)
}
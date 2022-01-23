package com.osmancancinar.yogaapp.ui.adapter.yoga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.data.model.Yoga
import com.osmancancinar.yogaapp.databinding.YogaListRowBinding

class YogaAdapter(private val clickListener: YogaListener) :
    ListAdapter<Yoga, YogaAdapter.YogaViewHolder>(YogaDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YogaViewHolder =
        YogaViewHolder.from(parent)

    override fun onBindViewHolder(holder: YogaViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class YogaViewHolder private constructor(val binding: YogaListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Yoga, clickListener: YogaListener) {
            binding.yoga = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): YogaViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = YogaListRowBinding.inflate(layoutInflater, parent, false)
                return YogaViewHolder(binding)
            }
        }

    }

    class YogaDiffCallBack : DiffUtil.ItemCallback<Yoga>() {
        override fun areItemsTheSame(oldItem: Yoga, newItem: Yoga): Boolean {
            return oldItem.yogaId == newItem.yogaId
        }

        override fun areContentsTheSame(oldItem: Yoga, newItem: Yoga): Boolean {
            return oldItem == newItem
        }
    }

}

class YogaListener(val listener: (yogaId: Int) -> Unit) {
    fun onClick(yoga: Yoga) = listener(yoga.yogaId!!)
}

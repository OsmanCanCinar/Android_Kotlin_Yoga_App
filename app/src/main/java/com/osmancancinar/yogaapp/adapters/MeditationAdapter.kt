package com.osmancancinar.yogaapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.MeditationListRowBinding
import com.osmancancinar.yogaapp.models.Meditation

class MeditationAdapter(private val meditations: ArrayList<Meditation>) : RecyclerView.Adapter<MeditationAdapter.MeditationViewHolder>(), ListItemClickListener {

    inner class MeditationViewHolder(var binding: MeditationListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        /*
        fun bind(meditation: Meditation?) {

            var colorCode = meditation?.colorCode.toString()
            var colorInt = Color.parseColor(colorCode)

            binding.linearMeditation.setCardBackgroundColor(colorInt)
            binding.meditationRowTitle.text = meditation?.meditationTitle
            binding.linearMeditation.setOnClickListener {
                onItemClickListener(it,meditation?.id!!.toInt())
            }
        }
         */
    }
/*
    private val differCallBack = object : DiffUtil.ItemCallback<Meditation>() {
        override fun areItemsTheSame(oldItem: Meditation, newItem: Meditation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meditation, newItem: Meditation): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
 */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationViewHolder {
        val binding = MeditationListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //val binding = DataBindingUtil.inflate<MeditationListRowBinding>(LayoutInflater.from(parent.context), R.layout.meditation_list_row, parent, false)
        return MeditationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeditationViewHolder, position: Int) {
        //val meditation = differ.currentList[position]
        //holder.bind(meditation)

        val meditation = meditations[position]
        var colorCode = meditation.colorCode.toString()
        var colorInt = Color.parseColor(colorCode)
        holder.binding.linearMeditation.setCardBackgroundColor(colorInt)
        holder.binding.meditationRowTitle.text = meditation.meditationTitle
        holder.binding.linearMeditation.setOnClickListener {
            onItemClickListener(it,meditation.id!!.toInt())
        }

    }

    override fun getItemCount(): Int {
        return meditations.size
        //return differ.currentList.size
    }

    override fun onItemClickListener(view: View, itemId: Int) {
        println(itemId)
    }
}
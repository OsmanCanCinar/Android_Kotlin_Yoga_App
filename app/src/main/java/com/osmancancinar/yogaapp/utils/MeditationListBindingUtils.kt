package com.osmancancinar.yogaapp.utils

import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.osmancancinar.yogaapp.data.model.MeditationCategory

@BindingAdapter("categoryName")
fun TextView.setMeditationCategoryName(item: MeditationCategory?) {
    item?.let {
        text = it.meditationTitle
    }
}

@BindingAdapter("categoryColor")
fun CardView.setMeditationCategoryBackGroundColor(item: MeditationCategory?) {
    item?.let {
        val colorCode = it.colorCode.toString()
        val colorInt = Color.parseColor(colorCode)
        setCardBackgroundColor(colorInt)
    }
}
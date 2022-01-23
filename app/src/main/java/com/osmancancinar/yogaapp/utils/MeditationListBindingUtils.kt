package com.osmancancinar.yogaapp.utils

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.Yoga

@BindingAdapter("categoryName")
fun TextView.setMeditationCategoryName(item: MeditationCategoriesList?) {
    item?.let {
        text = it.meditationCategoryTitle
    }
}

@BindingAdapter("categoryColor")
fun CardView.setMeditationCategoriesListBackGroundColor(item: MeditationCategoriesList?) {
    item?.let {
        val colorCode = it.colorCode.toString()
        val colorInt = Color.parseColor(colorCode)
        setCardBackgroundColor(colorInt)
    }
}

@BindingAdapter("meditationTitle")
fun TextView.setMeditationCategoryTitle(item: MeditationCategory?) {
    item?.let {
        text = it.meditationTitle
    }
}

@BindingAdapter("meditationLength")
fun TextView.setMeditationCategoryLength(item: MeditationCategory?) {
    item?.let {
        //val number = BigDecimal(it.length!!).setScale(2, RoundingMode.HALF_EVEN)
        text = it.length.toString()
    }
}

@BindingAdapter("meditationdesc")
fun TextView.setMeditationCategoryDesc(item: MeditationCategory?) {
    item?.let {
        text = it.meditationDescription
    }
}

@BindingAdapter("meditationId")
fun TextView.setMeditationId(item: MeditationCategory?) {
    item?.let {
        text = it.meditationId.toString()
    }
}

@BindingAdapter("meditationColor")
fun CardView.setMeditationCategoryBackGroundColor(item: MeditationCategory?) {
    item?.let {
        val colorCode = it.colorCode.toString()
        val colorInt = Color.parseColor(colorCode)
        setCardBackgroundColor(colorInt)
    }
}

fun ImageView.downloadCover(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("downloadMeditationCover")
fun downloadMeditationCover(image: ImageView, item: MeditationCategory?) {
    item?.let {
        image.downloadCover(item.imageURL, placeHolderProgressBar(image.context))
    }
}

@BindingAdapter("yogaName")
fun TextView.setYogaName(item: Yoga?) {
    item?.let {
        text = it.yogaTitle
    }
}

@BindingAdapter("yogaDesc")
fun TextView.setYogaDesc(item: Yoga?) {
    item?.let {
        text = it.yogaDescription
    }
}

@BindingAdapter("yogaLength")
fun TextView.setYogaLength(item: Yoga?) {
    item?.let {
        text = it.length.toString()
    }
}

@BindingAdapter("downloadYogaCover")
fun downloadYogaCover(image: ImageView, item: Yoga?) {
    item?.let {
        image.downloadCover(item.imageURL, placeHolderProgressBar(image.context))
    }
}
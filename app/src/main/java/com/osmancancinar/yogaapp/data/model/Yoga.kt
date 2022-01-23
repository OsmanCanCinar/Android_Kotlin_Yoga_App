package com.osmancancinar.yogaapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yoga_table")
data class Yoga(

    @ColumnInfo(name = "videoURL")
    var videoURL: String? = null,

    @ColumnInfo(name = "imageURL")
    var imageURL: String? = null,

    @ColumnInfo(name = "yogaTitle")
    var yogaTitle: String? = null,

    @ColumnInfo(name = "yogaDescription")
    var yogaDescription: String? = null,

    @ColumnInfo(name = "yogaId")
    var yogaId: Int? = null,

    @ColumnInfo(name = "length")
    var length: Float? = null,

    @ColumnInfo(name = "category")
    var category: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}
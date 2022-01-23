package com.osmancancinar.yogaapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meditation_category_table")
data class MeditationCategory(

    @ColumnInfo(name = "colorCode")
    var colorCode: String? = null,

    @ColumnInfo(name = "audioURL")
    var audioURL: String? = null,

    @ColumnInfo(name = "imageURL")
    var imageURL: String? = null,

    @ColumnInfo(name = "meditationTitle")
    var meditationTitle: String? = null,

    @ColumnInfo(name = "meditationDescription")
    var meditationDescription: String? = null,

    @ColumnInfo(name = "meditationId")
    var meditationId: Int? = null,

    @ColumnInfo(name = "length")
    var length: Float? = null
) {
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}
package com.osmancancinar.yogaapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meditation_categories_list_table")
data class MeditationCategoriesList(

    @ColumnInfo(name = "colorCode")
    var colorCode: String? = null,

    @ColumnInfo(name = "imageURL")
    var imageURL: String? = null,

    @ColumnInfo(name = "meditationCategoryTitle")
    var meditationCategoryTitle: String? = null,

    @ColumnInfo(name = "categoryId")
    var categoryId: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}
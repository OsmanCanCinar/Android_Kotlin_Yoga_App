package com.osmancancinar.yogaapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_table")
data class Blog(

    @ColumnInfo(name = "firstContent")
    var firstContent: String? = null,

    @ColumnInfo(name = "secondContent")
    var secondContent: String? = null,

    @ColumnInfo(name = "firstImageURL")
    var firstImageURL: String? = null,

    @ColumnInfo(name = "secondImageURL")
    var secondImageURL: String? = null,

    @ColumnInfo(name = "postTitle")
    var postTitle: String? = null,

    @ColumnInfo(name = "postId")
    var postId: Int? = null,

    ) {
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}
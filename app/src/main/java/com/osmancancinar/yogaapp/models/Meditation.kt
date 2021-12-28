package com.osmancancinar.yogaapp.models

var meditationList = mutableListOf<Meditation>()

data class Meditation(
    var colorCode: String? = null,
    var meditationTitle: String? = null,
    var id: Int? = meditationList.size
)
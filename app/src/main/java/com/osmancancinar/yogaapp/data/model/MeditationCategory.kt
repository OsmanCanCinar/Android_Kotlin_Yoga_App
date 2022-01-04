package com.osmancancinar.yogaapp.data.model

var meditationsList = mutableListOf<MeditationCategory>()

data class MeditationCategory(
    var colorCode: String? = null,
    var meditationTitle: String? = null,
    var id: Int = meditationsList.size
)
package com.osmancancinar.yogaapp.vm.home

import android.app.Application
import com.osmancancinar.yogaapp.vm.BaseViewModel

class MeditationVM(private val app: Application) : BaseViewModel(app) {
    val address : List<String?>? = listOf(null,null)
    var a  = address?.size
    val amount : Double? = 10.0
}
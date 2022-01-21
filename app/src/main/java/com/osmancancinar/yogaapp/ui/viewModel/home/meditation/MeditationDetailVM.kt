package com.osmancancinar.yogaapp.ui.viewModel.home.meditation

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Named

class MeditationDetailVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val firebaseRepositories: FirebaseRepositories
) : BaseViewModel(app, firebaseRepositories) {

    private val dao = AppDatabase.invoke(app.applicationContext).appDao()
    private val roomRepositories = RoomRepositories(dao)
    private var totalTime: Int = 0
    private var audioURL: String? = null

    val selectedMeditation = MutableLiveData<MeditationCategory>()
    private lateinit var theMeditation: MeditationCategory
    var mp: MediaPlayer? = null


    fun getSelectedMeditation(id: Int) {
        launch {
            val meditation = roomRepositories.getSelectedMeditation(id)
            selectedMeditation.value = meditation
        }
    }

    private fun prepareMP() {
        theMeditation = selectedMeditation.value!!
        audioURL = theMeditation.audioURL

        mp = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            isLooping = false
            setVolume(0.5f, 0.5f)
            setDataSource(audioURL)

            try {
                prepare()
                totalTime = duration
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun callOnDestroy() {
        if (mp != null) {
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }
    }

    fun seekToPositionBar(progress: Int) {
        mp!!.seekTo(progress)
    }

    fun seekToVolumeBar(progress: Int) {
        val volumeNum = progress / 100.0f
        mp!!.setVolume(volumeNum, volumeNum)
    }

    fun playButton(): Int {
        if (mp == null) {
            prepareMP()
        }
        mp?.start()
        return totalTime
    }

    fun pauseButton() {
        if (mp != null)
            mp?.pause()
    }

    fun stopButton(): String {
        if (mp != null) {
            mp?.stop()
            mp?.reset()
            mp?.release()
            mp = null
        }

        return createTimeLabel(totalTime)
    }

   fun setTimeLabels(currentPosition: Int): ArrayList<String> {
        val list: ArrayList<String> = arrayListOf()

        val elapsedTime = createTimeLabel(currentPosition)
        list.add(elapsedTime)

        val remainingTime = createTimeLabel(totalTime - currentPosition)
        list.add("-$remainingTime")

        return list
    }
}
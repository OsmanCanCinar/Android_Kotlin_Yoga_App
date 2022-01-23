package com.osmancancinar.yogaapp.ui.view.home.yoga

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.ActivityTemplateBinding
import com.osmancancinar.yogaapp.ui.viewModel.home.yoga.VideoActivityVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_video_controller.*

@AndroidEntryPoint
class TemplateActivity : AppCompatActivity() {

    private var isFullScreen = false
    private var isLocked = false

    private lateinit var binding: ActivityTemplateBinding
    private lateinit var mediaItem: MediaItem
    private lateinit var btn_fullScreen: ImageView
    private lateinit var btn_lockScreen: ImageView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private val viewModel: VideoActivityVM by viewModels()
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra("id", 0)
        viewModel.getSelectedYoga(id)

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()

        binding.player.apply {
            player = simpleExoPlayer
            keepScreenOn = true
        }

        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBarVideo.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBarVideo.visibility = View.GONE
                }
            }
        })

        viewModel.uri().observe(this, Observer {
            mediaItem = MediaItem.fromUri(it)
            simpleExoPlayer.setMediaItem(mediaItem)
        })

        simpleExoPlayer.prepare()
        simpleExoPlayer.play()

        btn_fullScreen = bt_fullscreen
        btn_fullScreen.setOnClickListener {
            if (!isFullScreen) {
                btn_fullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_exit
                    )
                )
                //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                btn_fullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            isFullScreen = !isFullScreen
        }

        btn_lockScreen = exo_lock
        btn_lockScreen.setOnClickListener {
            if (!isLocked) {
                btn_lockScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_video
                    )
                )
            } else {
                btn_lockScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_open
                    )
                )
            }
            isLocked = !isLocked
            handleLockScreen(isLocked)
        }
    }

    private fun handleLockScreen(isLocked: Boolean) {
        val sec_mid = sec_controlvid1
        val sec_bottom = sec_controlvid2

        if (isLocked) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        } else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (isLocked) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btn_fullScreen.performClick()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

}
package com.osmancancinar.yogaapp.ui.view.home.meditation

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.databinding.FragmentMeditationDetailBinding
import com.osmancancinar.yogaapp.ui.viewModel.home.meditation.MeditationDetailVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.os.Looper

@AndroidEntryPoint
class MeditationDetailFragment : Fragment() {

    private lateinit var binding: FragmentMeditationDetailBinding
    private val args: MeditationDetailFragmentArgs by navArgs()
    private val viewModel: MeditationDetailVM by viewModels()
    private var id: Int? = null
    private var isPlaying: Boolean = false

    @Inject
    lateinit var glide: RequestManager
    //glide.load(song.imageUrl).into(ivSongImage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.meditationId
        viewModel.getSelectedMeditation(id!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMeditationDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setPositionBar()
        controlSound()
    }

    private fun controlSound() {
        binding.playBtn.setOnClickListener {
            if (!isPlaying) {
                val totalTime = viewModel.playButton()
                binding.positionBar.max = totalTime
                binding.positionBar.visibility = View.VISIBLE
                initializePositionBar(viewModel.mp)
                binding.playBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_pause)
                binding.meditationDetailProgress.visibility = View.INVISIBLE
            } else {
                binding.playBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_play)
                viewModel.pauseButton()
            }
            isPlaying = !isPlaying
        }
    }

    private fun observeLiveData() {
        viewModel.selectedMeditation.observe(viewLifecycleOwner, Observer { meditation ->
            meditation?.let {
                binding.selectedMeditation = it
            }
        })

        viewModel.meditationLoading.observe(viewLifecycleOwner, Observer { readiness ->
            readiness?.let {
                if (it) {
                    binding.meditationDetailProgress.visibility = View.VISIBLE

                } else {
                    binding.meditationDetailProgress.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setPositionBar() {
        binding.positionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    if (viewModel.mp != null) {
                        viewModel.seekToPositionBar(progress)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        }
        )
    }

    private fun initializePositionBar(mp: MediaPlayer?) {
        var list: ArrayList<String>
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (mp != null) {
                        binding.positionBar.progress = mp!!.currentPosition
                        list = viewModel.setTimeLabels(mp!!.currentPosition)
                        binding.elapsedTimeLabel.text = list[0]
                        binding.remainingTimeLabel.text = list[1]
                        handler.postDelayed(this, 1000)
                    }
                } catch (e: Exception) {
                    binding.positionBar.progress = 0
                }
            }
        }, 0)
    }

    override fun onPause() {
        super.onPause()
        binding.playBtn.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_play)
        viewModel.pauseButton()
        isPlaying = false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.callOnDestroy()
    }

}



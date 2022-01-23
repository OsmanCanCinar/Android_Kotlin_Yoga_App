package com.osmancancinar.yogaapp.ui.view.home.meditation

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

@AndroidEntryPoint
class MeditationDetailFragment : Fragment() {

    private lateinit var binding: FragmentMeditationDetailBinding
    private val args: MeditationDetailFragmentArgs by navArgs()
    private val viewModel: MeditationDetailVM by viewModels()
    private var id: Int? = null

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
        setVolumeBar()
        controlSound()
    }

    private fun observeLiveData() {
        viewModel.selectedMeditation.observe(viewLifecycleOwner, Observer { meditation ->
            meditation?.let {
                binding.selectedMeditation = it
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.callOnDestroy()
    }

    private fun setPositionBar() {
        binding.positionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekToPositionBar(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        }
        )
    }

    private fun setVolumeBar() {
        binding.volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekToVolumeBar(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        )
    }

    private fun controlSound(){
        binding.playBtn.setOnClickListener {
            val totalTime = viewModel.playButton()
            binding.positionBar.max = totalTime
            initializePositionBar(viewModel.mp)
        }

        binding.pauseBtn.setOnClickListener {
            viewModel.pauseButton()
        }

        binding.stopBtn.setOnClickListener {
            val totalTime = viewModel.stopButton()
            binding.volumeBar.progress = 50
            binding.elapsedTimeLabel.text = getString(R.string.zero_zero)
            binding.remainingTimeLabel.text = totalTime

        }
    }

    private fun initializePositionBar(mp: MediaPlayer?) {
        var list: ArrayList<String>
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.positionBar.progress = mp!!.currentPosition
                    list = viewModel.setTimeLabels(mp!!.currentPosition)
                    binding.elapsedTimeLabel.text = list[0]
                    binding.remainingTimeLabel.text = list[1]
                    handler.postDelayed(this, 1000)

                } catch (e: Exception) {
                    binding.positionBar.progress = 0
                }
            }
        }, 0)
    }

}

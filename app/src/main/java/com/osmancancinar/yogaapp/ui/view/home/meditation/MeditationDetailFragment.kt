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
    private lateinit var handler: Handler

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
        hideSystemBars()
        observeLiveData()
        setPositionBar()
        controlSound()
    }

    private fun observeLiveData() {
        viewModel.selectedMeditation.observe(viewLifecycleOwner, Observer { meditation ->
            meditation?.let {
                binding.selectedMeditation = it
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

    private fun controlSound() {
        binding.playBtn.setOnClickListener {
            if (!isPlaying) {
                val totalTime = viewModel.playButton()
                binding.positionBar.max = totalTime
                binding.positionBar.visibility = View.VISIBLE
                initializePositionBar(viewModel.mp)
                binding.playBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_pause)
                isPlaying = true
            } else {
                binding.playBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_play)
                viewModel.pauseButton()
                isPlaying = false
            }

        }
    }


    override fun onPause() {
        super.onPause()
        binding.playBtn.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_play)
        viewModel.pauseButton()
        isPlaying = false
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

    private fun hideSystemBars() {
       hideIt()

        requireActivity().window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                handler.postDelayed({
                    hideIt()
                },2000)
                // adjustments to your UI, such as showing the action bar or
                // other navigational controls.
            } else {

                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
            }
        }


        val windowInsetsController =
            ViewCompat.getWindowInsetsController(requireActivity().window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    private fun hideIt() {
        handler = Handler(Looper.getMainLooper())
        activity?.window!!.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
    }

    private fun unHideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(requireActivity().window.decorView) ?: return
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

        activity?.window?.decorView?.apply {
            // Calling setSystemUiVisibility() with a value of 0 clears
            // all flags.
            systemUiVisibility = 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.callOnDestroy()
        unHideSystemBars()
        handler.removeCallbacksAndMessages(null);
    }

}



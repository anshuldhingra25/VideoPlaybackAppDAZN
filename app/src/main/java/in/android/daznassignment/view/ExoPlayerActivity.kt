package `in`.android.daznassignment.view

import `in`.android.daznassignment.constants.BACKWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.FORWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.PAUSE_BUTTON_CLICK
import `in`.android.daznassignment.databinding.ActivityExoPlayerBinding
import `in`.android.daznassignment.helper.ExoPlayerHelper
import `in`.android.daznassignment.viewmodel.ExoPlayerViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.Player

class ExoPlayerActivity : AppCompatActivity(), Player.Listener {
    private val exoPlayerVM by viewModels<ExoPlayerViewModel>()
    private lateinit var mBinding: ActivityExoPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExoPlayerBinding.inflate(layoutInflater)
        mBinding.apply {
            lifecycleOwner = this@ExoPlayerActivity
            exoPlayerViewModel = exoPlayerVM
        }
        setContentView(mBinding.root)
        mBinding.exoPlayerView.player = ExoPlayerHelper.preparePlayer(applicationContext)
        mBinding.exoPlayerView.player?.addListener(this)
        setDefaultValue()
    }

    private fun setDefaultValue() {
        exoPlayerVM.handleButtonClick(PAUSE_BUTTON_CLICK, -1)
        exoPlayerVM.handleButtonClick(FORWARD_BUTTON_CLICK, -1)
        exoPlayerVM.handleButtonClick(BACKWARD_BUTTON_CLICK, -1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.exoPlayerView.player?.release()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) return
        exoPlayerVM.handleButtonClick(PAUSE_BUTTON_CLICK, exoPlayerVM.pauseClickCount.value)
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        exoPlayerVM.onControlsClick(newPosition)
    }
}
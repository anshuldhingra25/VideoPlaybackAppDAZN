package `in`.android.daznassignment.viewmodel

import `in`.android.daznassignment.constants.BACKWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.FORWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.MPD_FILE_LINK
import `in`.android.daznassignment.constants.PAUSE_BUTTON_CLICK
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.firebase.analytics.FirebaseAnalytics

class ExoPlayerViewModel(application: Application) : AndroidViewModel(application),
    Player.Listener {

    private var exoPlayer: ExoPlayer? = null
    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    private val _pauseClickCount = MutableLiveData<Int>()
    val pauseClickCount: LiveData<Int>
        get() = _pauseClickCount

    private val _forwardClickCount = MutableLiveData<Int>()
    val forwardClickCount: LiveData<Int>
        get() = _forwardClickCount

    private val _backwardClickCount = MutableLiveData<Int>()
    val backwardClickCount: LiveData<Int>
        get() = _backwardClickCount
    private var lastPlaybackPosition = 0L
    private fun getPlayer(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer as ExoPlayer
    }

    fun preparePlayer(): ExoPlayer? {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(Uri.parse(MPD_FILE_LINK))
        val mediaSource =
            DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)

        exoPlayer = getPlayer(getApplication())
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
        exoPlayer?.addListener(this)
        return exoPlayer
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) return
        onPauseButtonClick()
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        val currentPlaybackPosition = newPosition.positionMs
        if (currentPlaybackPosition > lastPlaybackPosition) {
            onForwardButtonClick()
        } else if (currentPlaybackPosition < lastPlaybackPosition) {
            onBackwardButtonClick()
        }
        lastPlaybackPosition = currentPlaybackPosition
    }

    private fun onPauseButtonClick() {
        _pauseClickCount.value = getTotalCount(pauseClickCount.value)
        (pauseClickCount.value ?: 0) + 1
        analytics.logEvent(PAUSE_BUTTON_CLICK, null)
    }

    private fun onForwardButtonClick() {
        _forwardClickCount.value = getTotalCount(forwardClickCount.value)

        analytics.logEvent(FORWARD_BUTTON_CLICK, null)
    }

    private fun onBackwardButtonClick() {
        _backwardClickCount.value = getTotalCount(backwardClickCount.value)
        analytics.logEvent(BACKWARD_BUTTON_CLICK, null)
    }

    private fun getTotalCount(value: Int?) = (value ?: 0) + 1
}

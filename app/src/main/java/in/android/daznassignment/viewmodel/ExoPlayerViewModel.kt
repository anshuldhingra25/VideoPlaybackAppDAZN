package `in`.android.daznassignment.viewmodel

import ExoPlayerEventListener
import `in`.android.daznassignment.R
import `in`.android.daznassignment.constants.MPD_FILE_LINK
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.firebase.analytics.FirebaseAnalytics

class ExoPlayerViewModel(application: Application) : AndroidViewModel(application),
    ExoPlayerEventListener {

    private var exoPlayer: ExoPlayer? = null
    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    private val _pauseClickCount = MutableLiveData<String>()
    val pauseClickCount: LiveData<String>
        get() = _pauseClickCount

    private val _forwardClickCount = MutableLiveData<String>()
    val forwardClickCount: LiveData<String>
        get() = _forwardClickCount

    private val _backwardClickCount = MutableLiveData<String>()
    val backwardClickCount: LiveData<String>
        get() = _backwardClickCount

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

    override fun onPauseButtonClick() {
        _pauseClickCount.value = getApplication<Application?>().getString(R.string.pauseCount)
            .plus(pauseClickCount.value ?: 0) + 1
        analytics.logEvent("pause_button_click", null)
    }

    override fun onForwardButtonClick() {
        _forwardClickCount.value = getApplication<Application?>().getString(R.string.forwardCount)
            .plus(forwardClickCount.value ?: 0) + 1
        analytics.logEvent("forward_button_click", null)
    }

    override fun onBackwardButtonClick() {
        _backwardClickCount.value = getApplication<Application?>().getString(R.string.backwardCount)
            .plus(backwardClickCount.value ?: 0) + 1
        analytics.logEvent("backward_button_click", null)
    }

}

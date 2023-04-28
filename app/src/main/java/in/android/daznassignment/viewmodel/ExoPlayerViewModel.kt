package `in`.android.daznassignment.viewmodel

import `in`.android.daznassignment.constants.BACKWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.FORWARD_BUTTON_CLICK
import `in`.android.daznassignment.constants.PAUSE_BUTTON_CLICK
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.Player
import com.google.firebase.analytics.FirebaseAnalytics

class ExoPlayerViewModel(application: Application) : AndroidViewModel(application) {

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

    fun onControlsClick(newPosition: Player.PositionInfo) {
        val currentPlaybackPosition = newPosition.positionMs
        if (currentPlaybackPosition > lastPlaybackPosition) {
            handleButtonClick(FORWARD_BUTTON_CLICK, forwardClickCount.value)
        } else if (currentPlaybackPosition < lastPlaybackPosition) {
            handleButtonClick(BACKWARD_BUTTON_CLICK, backwardClickCount.value)
        }
        lastPlaybackPosition = currentPlaybackPosition
    }

    fun handleButtonClick(event: String, value: Int?) {
        when (event) {
            PAUSE_BUTTON_CLICK -> {
                _pauseClickCount.value = getTotalCount(value)
                analytics.logEvent(event, null)
            }
            FORWARD_BUTTON_CLICK -> {
                _forwardClickCount.value = getTotalCount(value)
                analytics.logEvent(event, null)
            }
            BACKWARD_BUTTON_CLICK -> {
                _backwardClickCount.value = getTotalCount(value)
                analytics.logEvent(event, null)
            }
        }
    }

    private fun getTotalCount(value: Int?) =
        if (value == -1) 0
        else (value ?: 0) + 1
}

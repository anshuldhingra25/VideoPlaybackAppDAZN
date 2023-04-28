import com.google.android.exoplayer2.Player

interface ExoPlayerEventListener : Player.Listener {
    fun onPauseButtonClick()
    fun onForwardButtonClick()
    fun onBackwardButtonClick()
}
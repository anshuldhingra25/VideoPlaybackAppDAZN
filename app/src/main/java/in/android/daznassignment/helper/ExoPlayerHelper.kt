package `in`.android.daznassignment.helper

import `in`.android.daznassignment.constants.MPD_FILE_LINK
import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

object ExoPlayerHelper {

    private var exoPlayer: ExoPlayer? = null

    fun preparePlayer(context: Context): ExoPlayer? {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(Uri.parse(MPD_FILE_LINK))
        val mediaSource =
            DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)

        exoPlayer = getPlayer(context)
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
        return exoPlayer
    }

    private fun getPlayer(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer as ExoPlayer
    }
}
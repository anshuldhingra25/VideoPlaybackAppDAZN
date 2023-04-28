package `in`.android.daznassignment.view

import `in`.android.daznassignment.databinding.ActivityExoPlayerBinding
import `in`.android.daznassignment.viewmodel.ExoPlayerViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.firebase.analytics.FirebaseAnalytics

class ExoPlayerActivity : AppCompatActivity() {
    private var exoPlayer: ExoPlayer? = null
    private val exoPlayerViewModel by viewModels<ExoPlayerViewModel>()
    lateinit var mBinding: ActivityExoPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExoPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.exoPlayerView.player = exoPlayer
        mBinding.exoPlayerView.player = exoPlayerViewModel.preparePlayer()
        FirebaseAnalytics.getInstance(this).logEvent("event1", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerViewModel.releasePlayer()
    }
}
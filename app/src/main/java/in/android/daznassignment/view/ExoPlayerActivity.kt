package `in`.android.daznassignment.view

import `in`.android.daznassignment.databinding.ActivityExoPlayerBinding
import `in`.android.daznassignment.viewmodel.ExoPlayerViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.Player

class ExoPlayerActivity : AppCompatActivity() {
    private val exoPlayerVM by viewModels<ExoPlayerViewModel>()
    lateinit var mBinding: ActivityExoPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExoPlayerBinding.inflate(layoutInflater)
        mBinding.apply {
            lifecycleOwner = this@ExoPlayerActivity
            exoPlayerViewModel = exoPlayerVM
        }
        setContentView(mBinding.root)
        mBinding.exoPlayerView.player = exoPlayerVM.preparePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerVM.releasePlayer()
    }
}
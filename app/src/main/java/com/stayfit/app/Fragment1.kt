package com.stayfit.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment


class Fragment1 : Fragment(), YouTubePlayer.OnInitializedListener {

    private var mPlayer1: YouTubePlayer? = null
    private var mVideoId1: String? = null
    private lateinit var playerFragment1: YouTubePlayerFragment
    private val RECOVERY_DIALOG_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView1: View = inflater.inflate(R.layout.tab_fragment_1, container, false)

        val title1 = rootView1.findViewById<View>(R.id.title1) as TextView
        title1.visibility = View.VISIBLE

        title1.setText(R.string.tab1Text)
        mVideoId1 = getString(R.string.tab1VideoID)
        Log.d("mVideoId1 is ", mVideoId1!!)

        playerFragment1 = childFragmentManager.findFragmentById(R.id.moviePlayer1) as YouTubePlayerFragment
        playerFragment1.setUserVisibleHint(true)

        return rootView1
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (!isVisibleToUser && mPlayer1 != null) {
            mPlayer1!!.release()
        }
        if (isVisibleToUser) {
            playerFragment1.initialize(getString(R.string.google_maps_key), this)
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer1: YouTubePlayer, restored: Boolean) {

        mPlayer1 = youTubePlayer1

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer1!!.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION


        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer1!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)


        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer1!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)


        if (mVideoId1 != null) {
            if (restored) {
                mPlayer1!!.play()
            } else {
                mPlayer1!!.loadVideo(mVideoId1)
            }
        }

    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(activity!!, RECOVERY_DIALOG_REQUEST).show()
        } else {
            //Handle the failure
            Toast.makeText(activity, "onInitializationFailure", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        fun newInstance(): Fragment1 {
            val fragment = Fragment1()
            val args = Bundle()

            return fragment
        }
    }
}


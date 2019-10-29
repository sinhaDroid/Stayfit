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

class Fragment2 : Fragment(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_DIALOG_REQUEST = 1
    internal var mPlayer2: YouTubePlayer? = null
    internal var playerFragment2: YouTubePlayerFragment? = null


    internal var mVideoId2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView2: View

        //getActivity().setTitle("Movie Show");
        rootView2 = inflater.inflate(R.layout.tab_fragment_2, container, false)
        val title2 = rootView2.findViewById<View>(R.id.title2) as TextView
        title2.visibility = View.VISIBLE

        title2.setText(R.string.tab2Text)
        mVideoId2 = getString(R.string.tab2VideoID)
        Log.d("mVideoId2 is ", mVideoId2!!)

        playerFragment2 = childFragmentManager.findFragmentById(R.id.moviePlayer2) as YouTubePlayerFragment?
        playerFragment2!!.setUserVisibleHint(true)

        return rootView2
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (!isVisibleToUser && mPlayer2 != null) {
            mPlayer2!!.release()
        }
        if (isVisibleToUser && playerFragment2 != null) {
            playerFragment2!!.initialize(getString(R.string.google_maps_key), this)
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer2: YouTubePlayer, restored: Boolean) {

        mPlayer2 = youTubePlayer2

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer2!!.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION


        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer2!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)


        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer2!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)


        if (mVideoId2 != null) {
            if (restored) {
                mPlayer2!!.play()
            } else {
                mPlayer2!!.loadVideo(mVideoId2)
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

        fun newInstance(): Fragment2 {
            val fragment = Fragment2()
            val args = Bundle()

            return fragment
        }
    }
}
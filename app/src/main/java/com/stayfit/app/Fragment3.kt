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

class Fragment3 : Fragment(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_DIALOG_REQUEST = 1
    internal var mPlayer3: YouTubePlayer? = null
    internal var playerFragment3: YouTubePlayerFragment? = null


    internal var mVideoId3: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView3: View

        //getActivity().setTitle("Movie Show");
        rootView3 = inflater.inflate(R.layout.tab_fragment_3, container, false)
        val title3 = rootView3.findViewById<View>(R.id.title3) as TextView
        title3.visibility = View.VISIBLE

        title3.setText(R.string.tab3Text)
        mVideoId3 = getString(R.string.tab3VideoID)
        Log.d("mVideoId3 is ", mVideoId3!!)

        playerFragment3 = childFragmentManager.findFragmentById(R.id.moviePlayer3) as YouTubePlayerFragment
        playerFragment3!!.setUserVisibleHint(true)

        return rootView3
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (!isVisibleToUser && mPlayer3 != null) {
            mPlayer3!!.release()
        }
        if (isVisibleToUser && playerFragment3 != null) {
            playerFragment3!!.initialize(getString(R.string.google_maps_key), this)
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer3: YouTubePlayer, restored: Boolean) {

        mPlayer3 = youTubePlayer3

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer3!!.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION


        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer3!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)


        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer3!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)


        if (mVideoId3 != null) {
            if (restored) {
                mPlayer3!!.play()
            } else {
                mPlayer3!!.loadVideo(mVideoId3)
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

        fun newInstance(): Fragment3 {
            val fragment = Fragment3()
            val args = Bundle()

            return fragment
        }
    }
}
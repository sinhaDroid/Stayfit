package com.stayfit.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.account_fragment, container, false)

        val changeGoalImg = rootView.findViewById<View>(R.id.settingsIcon) as ImageView
        val changeGoalsText = rootView.findViewById<View>(R.id.settingsText) as TextView

        val mGoalListener: OnChangeGoalListener
        try {
            mGoalListener = context as OnChangeGoalListener
            Log.d("mContext is ", context!!.toString())
        } catch (ex: ClassCastException) {
            throw ClassCastException("The hosting activity of the fragment" + "forgot to implement onFragmentInteractionListener")
        }

        changeGoalImg.setOnClickListener { mGoalListener.onChangeGoalClicked() }

        changeGoalsText.setOnClickListener { mGoalListener.onChangeGoalClicked() }


        return rootView
    }

    interface OnChangeGoalListener {
        fun onChangeGoalClicked()
    }

    companion object {

        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }

}// Required empty public constructor

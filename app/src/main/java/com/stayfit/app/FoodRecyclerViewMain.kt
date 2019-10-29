package com.stayfit.app


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.speech.RecognizerIntent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import org.json.JSONException

import java.lang.ref.WeakReference
import java.util.Locale

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator

/**
 * Created by maddi on 3/21/2016.
 */
class FoodRecyclerViewMain : Fragment() {
    private val REQ_CODE_SPEECH_INPUT = 100
    internal lateinit var foodData: FoodDataJson
    private var voice: FloatingActionButton? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: FoodMyRecyclerViewAdapter
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
        foodData = FoodDataJson()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_recyclerview_activity, container, false)
        mRecyclerView = rootView.findViewById<View>(R.id.cardList) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerViewAdapter = FoodMyRecyclerViewAdapter(requireContext(), foodData.foodList)
        mRecyclerView.adapter = mRecyclerViewAdapter
        // Animation
        mRecyclerView.itemAnimator = ScaleInBottomAnimator()
        mRecyclerView.itemAnimator!!.addDuration = 100
        mRecyclerView.itemAnimator!!.removeDuration = 1000
        mRecyclerView.itemAnimator!!.moveDuration = 100
        mRecyclerView.itemAnimator!!.changeDuration = 100
        val animator = ScaleInBottomAnimator()
        mRecyclerView.itemAnimator = animator
        // Adapter Animation
        mRecyclerView.adapter = ScaleInAnimationAdapter(mRecyclerViewAdapter)
        val alphaAdapter = ScaleInAnimationAdapter(mRecyclerViewAdapter)
        alphaAdapter.setDuration(500)
        mRecyclerView.adapter = alphaAdapter
        voice = rootView.findViewById<View>(R.id.vsfb) as FloatingActionButton
        voice!!.setOnClickListener { promptSpeechInput() }
        return rootView
    }

    /**
     * Showing google speech input dialog
     */
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(context, "Not Supported",
                    Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Receiving speech input
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    voice_query = result!![0]
                    Log.d("voice", voice_query)
                }
            }
        }
    }


    // Search action menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_actionview, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.action_search).actionView as SearchView
        if (search != null) {
            search.setIconifiedByDefault(true)
            search.setQuery(voice_query, true)
            // search.setQueryHint(voice_query);
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    var food: String = query
                    /*if (voice_query != "") {
                        food = voice_query;
                        search.setQueryHint(voice_query);
                        Log.d("searchvoice",voice_query);
                    }
                    else*/
                    food = food.replace(" ", "")
                    //String f_url = "https://api.nutritionix.com/v1_1/search/"+food+"?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id&appId=42e8cbe9&appKey=a4e373fe0f10ab1de40cffbffb9db544";
                    val f_url = "https://api.nutritionix.com/v1_1/search/$food?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Citem_description%2Cnf_protein%2Cnf_calories%2Cnf_total_carbohydrate%2Cnf_total_fat&appId=42e8cbe9&appKey=a4e373fe0f10ab1de40cffbffb9db544"
                    val downloadJson = MyDownloadJsonAsyncTask(mRecyclerViewAdapter)
                    Log.d("RahulMaddineni", f_url)
                    downloadJson.execute(f_url)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return super.onOptionsItemSelected(item)
    }

    // Load Async Food Data from Nutrionix.com
    private inner class MyDownloadJsonAsyncTask(adapter: FoodMyRecyclerViewAdapter) : AsyncTask<String, Void, FoodDataJson>() {
        private val adapterReference: WeakReference<FoodMyRecyclerViewAdapter>?

        init {
            adapterReference = WeakReference(adapter)
        }

        override fun doInBackground(vararg urls: String): FoodDataJson {
            val threadMovieData = FoodDataJson()
            for (url in urls) {
                try {
                    threadMovieData.downloadFoodDataJson(url)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return threadMovieData
        }

        override fun onPostExecute(threadMovieData: FoodDataJson) {
            foodData.foodList.clear()
            for (i in 0 until threadMovieData.size)
                foodData.foodList.clear()
            for (k in 0 until threadMovieData.size)
                foodData.foodList.add(threadMovieData.foodList[k])
            if (adapterReference != null) {
                val adapter = adapterReference.get()
                adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {

        private val ARG_MOVIE = "R.id.mdf_main_replace"
        var voice_query = ""

        fun newInstance(): FoodRecyclerViewMain {
            val fragment = FoodRecyclerViewMain()
            val args = Bundle()
            args.putSerializable(ARG_MOVIE, "R.id.rcmain")
            fragment.arguments = args
            return fragment
        }
    }

}// Constructor

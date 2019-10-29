package com.stayfit.app

import android.util.Log
import android.widget.TextView

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by maddi on 3/29/2016.
 */
class FoodDataJson {
    internal var foodList: MutableList<Map<String, *>> = ArrayList()
    var t: TextView? = null

    val moviesList: List<Map<String, *>>
        get() = foodList

    val size: Int
        get() = foodList.size

    fun getItem(i: Int): HashMap<*, *>? {
        return if (i >= 0 && i < foodList.size) {
            foodList[i] as HashMap<*, *>
        } else
            null
    }

    fun removeItem(i: Int) {
        foodList.removeAt(i)
    }

    fun addItem(position: Int, clone: HashMap<String, *>) {
        foodList.add(position, clone)
    }

    @Throws(JSONException::class)
    fun downloadFoodDataJson(json_url: String) {
        foodList.clear() // clear the list

        var foodArray = MyUtility.downloadJSONusingHTTPGetRequest(json_url)
        foodArray = foodArray!!.toString()
        longInfo(foodArray)
        Log.d("FoodArray", foodArray)

        if (foodArray == null) {
            Log.d("MyDebugMsg", "Having trouble loading URL: $json_url")
            return
        }

        val json = "Assuming that here is your JSON response"
        try {
            val parentObject = JSONObject(foodArray)
            val hitsJsonArray = parentObject.getJSONArray("hits")
            Log.d("hits", hitsJsonArray.toString())
            Log.d("hits length", hitsJsonArray.length().toString())
            for (i in 0 until hitsJsonArray.length()) {
                val f = hitsJsonArray.getJSONObject(i)
                val fi = f.getJSONObject("fields")
                Log.d("Hits array item:", fi.toString())
                run {
                    val iid = fi.getString("item_id")
                    val iname = fi.getString("item_name")
                    val bid = fi.getString("brand_id")
                    val bname = fi.getString("brand_name")
                    val ical = fi.getString("nf_calories")
                    val idesc = fi.getString("item_description")
                    val ifat = fi.getString("nf_total_fat")
                    val iprotein = fi.getString("nf_protein")
                    val icarbs = fi.getString("nf_total_carbohydrate")
                    foodList.add(createFood_brief(iid, iname, bid, bname, ical, idesc, ifat, iprotein, icarbs))
                }
            }

        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            Log.d("MyDebugMsg", "JSONException in downloadFoodDataJson")
            e.printStackTrace()
        }

    }

    companion object {

        private fun createFood_brief(iid: String, iname: String, bid: String, bname: String, ical: String, idesc: String, ifat: String, iprotein: String, icarbs: String): Map<String, *> {
            val fd = HashMap<String, String>()

            fd["iid"] = iid
            fd["iname"] = iname
            fd["bid"] = bid
            fd["bname"] = bname
            fd["ical"] = ical
            fd["idesc"] = idesc
            fd["ifat"] = ifat
            fd["icarbs"] = icarbs
            fd["iprotein"] = iprotein
            return fd
        }

        fun longInfo(str: String) {
            if (str.length > 4000) {
                Log.i("FoodArray1:", str.substring(0, 4000))
                longInfo(str.substring(4000))
            } else
                Log.i("FoodArray2", str)
        }
    }
}

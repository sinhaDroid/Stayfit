package com.stayfit.app

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import org.json.JSONArray

/**
 * Created by maddi on 3/21/2016.
 */
class FoodMyRecyclerViewAdapter// Constructor
(private val mContext: Context, private val mDataset: List<Map<String, *>>) : RecyclerView.Adapter<FoodMyRecyclerViewAdapter.ViewHolder>() {

    // Using View Holder
    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int): FoodMyRecyclerViewAdapter.ViewHolder {
        val v: View
        v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_food, parent, false)
        return ViewHolder(v)
    }

    // Filling Data into ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = mDataset[position]
        holder.bindMovieData(food)
    }

    // No of items in dataset
    override fun getItemCount(): Int {
        return mDataset.size
    }

    // ViewHolder Class
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var vTitle: TextView
        var vType: TextView
        var vCal: TextView
        var vAdd: Button
        // Set Popup Window
        var mRelativeLayout: RelativeLayout? = null
        var mPopupWindow: PopupWindow? = null

        private val mAuth: FirebaseAuth
        private val mDatabase: DatabaseReference

        init {
            vTitle = v.findViewById<View>(R.id.title) as TextView
            vType = v.findViewById<View>(R.id.type) as TextView
            vCal = v.findViewById<View>(R.id.calories) as TextView
            vAdd = v.findViewById<View>(R.id.addfood) as Button
            //mRelativeLayout = (RelativeLayout) v.findViewById(R.id.recyclr_frag_pop);
            mAuth = FirebaseAuth.getInstance()
            mDatabase = FirebaseDatabase.getInstance().reference
        }

        private fun getCaloriesRef(ref: String): DatabaseReference {
            val user = mAuth.currentUser
            val userId = user!!.uid
            return mDatabase.child("Calories").child(userId).child(ref)
        }

        fun bindMovieData(fooditem: Map<String, *>) {

            vTitle.text = fooditem["iname"] as String?
            vType.text = fooditem["bname"] as String?
            // vDesc.setText((String) fooditem.get("idesc"));
            vCal.text = fooditem["ical"] as String?
            caloriecount = FoodRecyclerFragMain.calRef1
            totalcarbs = FoodRecyclerFragMain.user_carbs1
            totalprotein = FoodRecyclerFragMain.user_protein1
            totalfat = FoodRecyclerFragMain.user_fat1
            vAdd.setOnClickListener {
                count++
                Log.d("Before Adding", caloriecount.toString() + totalcarbs.toString() + totalfat.toString() + totalprotein.toString())
                caloriecount = caloriecount + java.lang.Float.parseFloat(fooditem["ical"].toString())
                totalcarbs = totalcarbs + java.lang.Float.parseFloat(fooditem["icarbs"].toString())
                totalfat = totalfat + java.lang.Float.parseFloat(fooditem["ifat"].toString())
                totalprotein = totalprotein + java.lang.Float.parseFloat(fooditem["iprotein"].toString())
                Log.d("After Adding", caloriecount.toString() + totalcarbs.toString() + totalfat.toString() + totalprotein.toString())
                Log.d("Adapter", FoodRecyclerFragMain.user_fat1.toString() + FoodRecyclerFragMain.user_carbs1.toString() + FoodRecyclerFragMain.user_protein1.toString() + FoodRecyclerFragMain.calRef1.toString())

                getCaloriesRef("totalcalories").setValue(caloriecount)
                getCaloriesRef("totalfat").setValue(totalfat)
                getCaloriesRef("totalcarbs").setValue(totalcarbs)
                getCaloriesRef("totalprotein").setValue(totalprotein)

                if (count == 1) {
                    val toast1 = count.toString() + "item added"
                    Toast.makeText(mContext, toast1, Toast.LENGTH_SHORT).show()
                } else if (count > 1) {
                    val toast2 = count.toString() + "items added"
                    Toast.makeText(mContext, toast2, Toast.LENGTH_SHORT).show()
                }
            }
            val j: JSONArray? = null

        }
    }

    companion object {
        var caloriecount = 0f
        var totalfat = 0f
        var totalcarbs = 0f
        var totalprotein = 0f
        var count = 0
    }


}



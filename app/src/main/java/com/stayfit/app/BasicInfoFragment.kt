package com.stayfit.app

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BasicInfoFragment : Fragment() {

    internal var position: Int = 0
    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private fun getUsersRef(ref: String): DatabaseReference {
        val user = mAuth!!.currentUser
        val userId = user!!.uid
        return mDatabase!!.child("Users").child(userId).child(ref)
    }

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_basicinfo, container, false)
        val mListener: OnFloatingButtonClickListener

        try {
            mListener = context as OnFloatingButtonClickListener
            Log.d("mContext is ", context!!.toString())
        } catch (ex: ClassCastException) {
            throw ClassCastException("The hosting activity of the fragment" + "forgot to implement onFragmentInteractionListener")
        }

        val next = rootView.findViewById<View>(R.id.nextbutton) as Button

        val fab1 = activity!!.findViewById<View>(R.id.next) as FloatingActionButton
        val nameET = rootView.findViewById<View>(R.id.nameInput) as EditText
        val phoneET = rootView.findViewById<View>(R.id.phoneInput) as EditText
        val ageET = rootView.findViewById<View>(R.id.ageInput) as EditText
        val weightET = rootView.findViewById<View>(R.id.weightInput) as EditText
        val heightET = rootView.findViewById<View>(R.id.heightInput) as EditText

        val myRadioGroup = rootView.findViewById<View>(R.id.genderGroup) as RadioGroup
        myRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            position = myRadioGroup.indexOfChild(rootView.findViewById(checkedId))
            if (position == 0) {
                Log.d("Gender is ", "Male")
                getUsersRef("gender").setValue("Male")
            } else {
                Log.d("Gender is ", "Female")
                getUsersRef("gender").setValue("Female")
            }
        }

        val userPhoto = activity!!.findViewById<View>(R.id.userPhoto) as ImageView
        userPhoto.setImageResource(R.drawable.runimage)

        fab1.setOnClickListener(View.OnClickListener {
            if (nameET.text.toString().length == 0) {
                nameET.error = "Name is required!"
                return@OnClickListener
            }
            mListener.onFloatingButtonClicked()
            getUsersRef("name").setValue(nameET.text.toString())
            getUsersRef("phone").setValue(phoneET.text.toString())
            getUsersRef("age").setValue(ageET.text.toString())
            getUsersRef("height").setValue(heightET.text.toString())
            getUsersRef("weight").setValue(weightET.text.toString())
        })

        next.setOnClickListener(View.OnClickListener {
            if (nameET.text.toString().length == 0) {
                nameET.error = "Name is required!"
                return@OnClickListener
            }
            mListener.onFloatingButtonClicked()
            getUsersRef("name").setValue(nameET.text.toString())
            getUsersRef("phone").setValue(phoneET.text.toString())
            getUsersRef("age").setValue(ageET.text.toString())
            getUsersRef("height").setValue(heightET.text.toString())
            getUsersRef("weight").setValue(weightET.text.toString())
        })

        /* ---- BMI Calculation ---- */
        //        final TextView bmicalc = (TextView) rootView.findViewById(R.id.bmi);
        //        final TextView bmires = (TextView) rootView.findViewById(R.id.bmiresult);
        //        ImageView bmiImage = (ImageView) rootView.findViewById(R.id.bmiimage);
        //        bmiImage.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                try {
        //                    if(weightET.getText().toString() != "" || heightET.getText().toString() != "") {
        //                        Log.d("In try Block","");
        //                        Float bmi = ((Float.parseFloat(weightET.getText().toString())) * 10000) / ((Float.parseFloat(heightET.getText().toString())) * (Float.parseFloat(heightET.getText().toString())));
        //                        bmi = Float.parseFloat(String.format("%.2f", bmi));
        //                        Log.d("BMI is", bmi.toString());
        //                        bmicalc.setText(bmi.toString());
        //                        if (bmi < 18.5) {
        //                            bmires.setText("Under Weight");
        //                        } else if (bmi > 18.5 && bmi < 24.99) {
        //                            bmires.setText("Normal Weight");
        //                        } else if (bmi > 25 && bmi < 29.99) {
        //                            bmires.setText("Over Weight");
        //                        } else {
        //                            bmires.setText("Obesity");
        //                        }
        //                    }
        //                }
        //                catch (Exception ex){
        //                    Log.d("In Catch Block","");
        //                    Toast toast = Toast.makeText(getActivity(), "Forgot to enter height or weight?", Toast.LENGTH_LONG);
        //                    toast.show();
        //                }
        //            }
        //        });

        return rootView
    }

    interface OnFloatingButtonClickListener {
        fun onFloatingButtonClicked()
    }

    companion object {

        fun newInstance(): BasicInfoFragment {
            return BasicInfoFragment()
        }
    }
}


package com.stayfit.app

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.Locale

class AskLocationActivity : AppCompatActivity() {

    private lateinit var mResultReceiver: AddressResultReceiver

    private lateinit var addressEdit: EditText

    internal lateinit var infoText: TextView

    private var fetchAddress: Boolean = false
    private var fetchType = Constants.USE_ADDRESS_LOCATION
    private lateinit var invite: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asklocation)
        invite = findViewById<View>(R.id.invite) as Button
        invite.visibility = View.INVISIBLE
        // Go to Map Load page
        invite.setOnClickListener {
            val intent = Intent(this@AskLocationActivity, LoadMapActivity::class.java)
            //Intent intent = new Intent(AskLocationActivity.this, LatLongActivity.class);
            startActivity(intent)
        }

        // Send Data to LoadMapActivity Lat and Long
        /*Intent intent = null;
        intent.putExtra("Latitude",address1.getLatitude());
        intent.putExtra("Longitude",address1.getLongitude());
        startActivity(intent);*/
        // placename = (TextView) findViewById(R.id.place_name);

        /* ---- Auto Complete Search for Google Maps ---- */
        //        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
        //                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        //
        //        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
        //            @Override
        //            public void onPlaceSelected(Place place) {
        //                // TODO: Get info about the selected place.
        //                Log.i(TAG1, "Place: " + place.getName());
        //                // placename.setText(place.getName());
        //                place1 = place.getName().toString();
        //                addressEdit.setText(place.getName().toString());
        //            }
        //
        //            @Override
        //            public void onError(Status status) {
        //                // TODO: Handle the error.
        //                Log.i(TAG1, "An error occurred: " + status);
        //            }
        //
        //            //  @Override
        //            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //                if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
        //                    if (resultCode == RESULT_OK) {
        //                        Place place = PlaceAutocomplete.getPlace(AskLocationActivity.this, data);
        //                        Log.i(TAG1, "Place: " + place.getName());
        //                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
        //                        Status status = PlaceAutocomplete.getStatus(AskLocationActivity.this, data);
        //                        // TODO: Handle the error.
        //                        Log.i(TAG1, status.getStatusMessage());
        //
        //                    } else if (resultCode == RESULT_CANCELED) {
        //                        // The user canceled the operation.
        //                    }
        //                }
        //            }
        //        });
        //        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
        //                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
        //                        //.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
        //                .build();
        //
        //        autocompleteFragment.setFilter(typeFilter);

        /* ----- Get Lat and Long from Place Name ----- */
        addressEdit = findViewById<View>(R.id.addressEdit) as EditText
        infoText = findViewById<View>(R.id.infoText) as TextView
        mResultReceiver = AddressResultReceiver(null)
        fetchAddress = false
        fetchType = Constants.USE_ADDRESS_NAME
        addressEdit.isEnabled = true
        addressEdit.requestFocus()

    }

    fun onButtonClicked(view: View) {
        val intent = Intent(this, GeocodeAddressIntentService::class.java)
        intent.putExtra(Constants.RECEIVER, mResultReceiver)
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType)
        if (fetchType == Constants.USE_ADDRESS_NAME) {
            if (addressEdit.text.isEmpty()) {
                Toast.makeText(this, "Please enter an address name", Toast.LENGTH_LONG).show()
                return
            }
            invite.visibility = View.VISIBLE
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, addressEdit.text.toString())
        }
        Log.e(TAG, "Starting Service")
        startService(intent)
    }

    internal inner class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
        private var address: Address? = null

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                address = resultData.getParcelable(Constants.RESULT_ADDRESS)
                runOnUiThread {
                    infoText.visibility = View.VISIBLE
                    infoText.text = "Latitude: " + address!!.latitude + "\n" +
                            "Longitude: " + address!!.longitude + "\n" +
                            "Address: " + resultData.getString(Constants.RESULT_DATA_KEY)
                    infoText.visibility = View.INVISIBLE
                    address1.latitude = address!!.latitude
                    address1.longitude = address!!.longitude
                }
            } else {
                runOnUiThread {
                    infoText.visibility = View.VISIBLE
                    infoText.text = resultData.getString(Constants.RESULT_DATA_KEY)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
        var place1: String? = null
        var address1 = Address(Locale.getDefault())
    }
}


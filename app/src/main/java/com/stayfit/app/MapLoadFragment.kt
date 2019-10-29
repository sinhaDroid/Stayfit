package com.stayfit.app


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

//import com.google.android.gms.maps.MapFragment;


/**
 * A simple [Fragment] subclass.
 */
class MapLoadFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /***define Parameters here */
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null // Might be null if Google Play services APK is not available.
    private var mLastLocation: Location? = null

    internal var latLng_Prev: LatLng? = null

    /** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState == null) {
            buildGoogleApiClient()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapFragment = childFragmentManager.findFragmentById(R.id.googlemap) as SupportMapFragment?
        if (mMapFragment != null) {
            mMapFragment!!.getMapAsync(this)
        }
    }

    private fun buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
        val mLocationRequest = createLocationRequest()
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.LOCATION_HARDWARE, Manifest.permission_group.LOCATION),
                    PERMISSION_LOCATION_REQUEST_CODE)
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("onConnectionFailed ", "Connection failed: ConnectionResult.getErrorCode() = " + result.errorCode)
    }

    override fun onConnectionSuspended(cause: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    override fun onStop() {
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
        super.onStop()
    }

    /*
     * set location request: frequency, priority
     * */
    private fun createLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return mLocationRequest
    }

    override fun onLocationChanged(location: Location) {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(AskLocationActivity.address1.latitude, AskLocationActivity.address1.longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title(AskLocationActivity.place1))
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //move camera when location changed
        val latLng_Now = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder()
                .target(sydney)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(17f)                   // Sets the zoom
                .bearing(90f)                // Sets the orientation of the camera to east
                .tilt(45f)                   // Sets the tilt of the camera to 45 degrees
                .build()                   // Creates a CameraPosition from the builder
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        if (latLng_Prev == null) {
            latLng_Prev = latLng_Now
        }
        //draw line between two locations:
        /* Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(latLng_Prev, latLng_Now)
                .width(5)
                .color(Color.RED));
        latLng_Prev = latLng_Now;*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        // mMap.setMyLocationEnabled(true);

        mMap!!.setOnMapClickListener { lat -> Toast.makeText(context, "Latitude: " + lat.latitude + "\nLongitude: " + lat.longitude, Toast.LENGTH_SHORT).show() }
        mMap!!.setOnMapLongClickListener { lat ->
            val marker = mMap!!.addMarker(MarkerOptions()
                    .title("self defined marker")
                    .snippet("Hello!")
                    .position(lat).visible(true)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
            )
        }
        mMap!!.setOnMarkerClickListener { marker ->
            Toast.makeText(context, marker.title.toString(), Toast.LENGTH_SHORT).show()
            true
        }


    }

    companion object {

        private val PERMISSION_LOCATION_REQUEST_CODE = 1

        fun newInstance(): MapLoadFragment {
            return MapLoadFragment()
        }
    }
}// Required empty public constructor

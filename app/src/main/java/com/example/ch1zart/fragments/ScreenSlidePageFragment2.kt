package com.example.ch1zart.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ch1zart.dbconnector.MyDatabaseOpenHelper
import com.example.ch1zart.fitnessapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import java.util.*


class ScreenSlidePageFragment2 : Fragment() , OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    lateinit var mMapView: MapView
    lateinit var mGoogleMap: GoogleMap
    lateinit var mLocationRequest: LocationRequest
    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var mLastLocation: LatLng

    lateinit var mDbHelper: MyDatabaseOpenHelper
    lateinit var dbW: SQLiteDatabase

    var i_time = 0
    var i_speed = 0

    var user_GetHeight = 0
    var user_GetWeigth = 0

    val speed_v: MutableList<Float> = arrayListOf()

    val time_v : MutableList<String> = arrayListOf()

    val to = TransferObject

    private lateinit var start_point: LatLng
    private var all_sum_km = 0f
    private var all_step_sum = 0
    private var all_energy_sum = 0f

    var cicle = 0
    var first_run = false
    var rectOptions = PolylineOptions().color(Color.rgb(0,128,255))

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mGoogleApiClient = GoogleApiClient.Builder(this.context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        var rootView = inflater?.inflate(R.layout.activity_screen_slide_page_fragment2, container, false) as ViewGroup
        to.clearVariables()
        mMapView = rootView.find<MapView>(R.id.mapView)
        mMapView?.onCreate(savedInstanceState)

        mDbHelper = MyDatabaseOpenHelper(activity)

        mMapView?.onResume()
        mMapView?.getMapAsync { mMap ->
            mGoogleMap = mMap
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            mGoogleMap.isMyLocationEnabled = true
        }

           return rootView
    }


    fun checkUserStats()
    {
             doAsync   {
           dbW = mDbHelper.writableDatabase
            user_GetHeight = mDbHelper.GetHeight(dbW)
            user_GetWeigth = mDbHelper.GetWeight(dbW)

           dbW.close()

            uiThread {
              if(user_GetHeight > 0 && user_GetWeigth > 0)
                {
                to.HaveUser.set(true)
                }
             }
        }
    }

    fun EnergyMeter(meters: Float)
    {
        val user_weight:Int = user_GetHeight

        val m_to_km = meters / 1000

        var energy_formula:Float = (user_weight * m_to_km)

        all_energy_sum += energy_formula
        to.all_energy.set(all_energy_sum)
    }

    fun stepMeter(meters: Float)
    {
        val user_height_m:Double = user_GetWeigth / 100.0

        var step_formula:Double = (user_height_m/ 4.1)+0.37
        var step_in_step = (meters / step_formula).toInt()

        all_step_sum+=step_in_step
        to.all_steps.set(all_step_sum)
    }

    override fun onMapReady(googleMap: GoogleMap) {
              mGoogleMap = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
              buildGoogleApiClient()
                mGoogleMap.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mGoogleMap.isMyLocationEnabled = true
        }
    }

    @Synchronized protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this.context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }
    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()

        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 5000

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionSuspended(i: Int) {
    }

    fun CameraMove(location: Location)
    {
        mLastLocation = LatLng(location.latitude, location.longitude)

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLastLocation))
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location) {

        mLastLocation = LatLng(location.latitude, location.longitude)

        to.UserLatitude.set(location.latitude.toFloat())
        to.UserLongitute.set(location.longitude.toFloat())

         if (first_run != true) {
            CameraMove(location)
            cicle = 0

            val latLng = LatLng(location.latitude, location.longitude)
            start_point = latLng
            rectOptions.add(start_point)

            CalculationByDistance( mLastLocation, mLastLocation, true)
        }

        if (to.start_btn.get()) {


            if (rectOptions.points.last() != mLastLocation) {
                cicle = 0
                to.bIsStop.set(false)
                CalculationByDistance(rectOptions.points.last(), mLastLocation, false)
                rectOptions.add(mLastLocation)
            }

            if (rectOptions.points.last() == mLastLocation)
            {
                if(cicle >= 3) {
                    to.bIsStop.set(true)

                    CalculationByDistance(mLastLocation, mLastLocation, true)
                }
                cicle++
            }
        }

        if (to.finish_btn.get()) {

            restartInfo()
            CameraMove(location)

            closeTraining()
        }
        first_run = true
    }

    fun restartInfo()
    {
        to.all_steps.set(0)
        all_step_sum = 0
        all_sum_km = 0f
        to.all_sum_km.set(0)
    }

    fun closeTraining() {
        speed_v.add(i_speed, 0f)
        time_v.add(i_time, to.get_current_time.get())
        i_speed++
        i_time++
        mGoogleMap.addPolyline(rectOptions)
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)

        toast("Тренировка завершена")
    }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng, StopPoint: Boolean): Double {
        val Radius = 6371// radius of earth in Km
        val c = 0.0
        var meter = 0f
        val res = Radius * c
        if (StopPoint) {
            speed_v.add(i_speed, meter)
            i_speed++

            time_v.add(i_time," ")
            i_time++
        }
        else {

            val lat1 = StartP.latitude
            val lat2 = EndP.latitude
            val lon1 = StartP.longitude
            val lon2 = EndP.longitude

            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.asin(Math.sqrt(a))
            val valueResult = Radius * c.toFloat()

            meter = valueResult * 1000f

            all_sum_km += meter
            to.all_sum_km.set(all_sum_km.toLong())

            val speed = (meter / 5f)
            speed_v.add(i_speed, speed)
            i_speed++

            time_v.add(i_time," ")
            i_time++

            if(to.HaveUser.get())
            {
              stepMeter(meter)
              EnergyMeter(meter)
            }

        }
        return res
    }

  override  fun onStart() {
        super.onStart()

     checkUserStats()
      mGoogleApiClient.connect()
  }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
         super.onDestroy()
    mMapView!!.onDestroy()
    }
}
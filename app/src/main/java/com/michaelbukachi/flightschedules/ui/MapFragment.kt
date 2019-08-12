package com.michaelbukachi.flightschedules.ui


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.michaelbukachi.flightschedules.R


class MapFragment : Fragment() {

    private val args by navArgs<MapFragmentArgs>()

    private lateinit var mMap: GoogleMap
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        var origin: LatLng? = null
        var destination: LatLng? = null
        val lastIndex = args.route.airports.lastIndex
        var lines = PolylineOptions()

        for (i in args.route.airports.indices) {
            val airport = args.route.airports[i]
            val coord = LatLng(airport.latitude.toDouble(), airport.longitude.toDouble())
            if (i == 0) {
                origin = coord
                mMap.addMarker(
                    MarkerOptions()
                        .position(coord)
                        .title("${airport.name} (${airport.code})")
                        .draggable(false)
                )
            }

            if (i == lastIndex) {
                destination = coord
                mMap.addMarker(
                    MarkerOptions()
                        .position(coord)
                        .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        .title("${airport.name} (${airport.code})")
                        .draggable(false)
                )
            }

            lines = lines.add(coord)
        }
        mMap.addPolyline(
            lines.width(8f).color(Color.RED)
        )
        val bounds = LatLngBounds.Builder().include(origin!!).include(destination!!).build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.10).toInt()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
        return view
    }

    override fun onDestroyView() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}

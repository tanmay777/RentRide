package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRentVehicle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel

import com.example.tanmay.rentbaazvehicleadministration.R

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_all_vehicle.*

import kotlinx.android.synthetic.main.fragment_on_rent_vehicle.view.*
import java.util.*

class OnRentVehicleFragment : Fragment() {

    lateinit var fragmentView: View
    val rootRef = FirebaseFirestore.getInstance()
    private lateinit var mRunnable:Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_on_rent_vehicle, container, false)
        return fragmentView
    }
    
    override fun onStart() {
        super.onStart()
        loadOnRentVehicle(true) //This true/false is to distinguish whether we have to load data from swipe down to refresh or normal load when fragment starts
        fragmentView.recycler_view_on_rent.layoutManager = LinearLayoutManager(fragmentView.context)
        fragmentView.no_vehicle_on_rent_text.visibility=View.GONE
        swipe_container.setOnRefreshListener {
                loadOnRentVehicle(false)
        }
    }

    private fun loadOnRentVehicle(flag:Boolean) {
        if(flag)
            fragmentView.progressBar.visibility=View.VISIBLE
        var onRentVehicleList: ArrayList<VehicleModel> = ArrayList()
        rootRef.collection("vehicle")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val onRentVehicle: VehicleModel =it.toObject(VehicleModel::class.java)
                        onRentVehicle.id=it.id
                        var flag=false
                        for (bookingModel in onRentVehicle.booking) {
                            if ((bookingModel.pickup_details.compareTo(Date()) > 0) &&
                                    (bookingModel.drop_details.compareTo(Date()) < 0)) {
                                flag = true
                            }
                        }
                        if(flag) {
                            onRentVehicleList.add(onRentVehicle)
                        }
                    }
                    if(onRentVehicleList.isEmpty()){
                        fragmentView.no_vehicle_on_rent_text.visibility=View.VISIBLE
                    }
                    fragmentView.recycler_view_on_rent.adapter = OnRentVehicleAdapter(onRentVehicleList, fragmentView.context)
                    if(flag)
                        fragmentView.progressBar.visibility=View.GONE
                    else
                        swipe_container.isRefreshing = false
                }

    }
}


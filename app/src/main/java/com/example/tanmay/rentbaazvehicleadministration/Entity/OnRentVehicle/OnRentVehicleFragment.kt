package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRentVehicle


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.OnRentVehicle.OnRentVehicleAdapter

import com.example.tanmay.rentbaazvehicleadministration.R

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.fragment_on_rent_vehicle.view.*
import kotlinx.android.synthetic.main.vehicle_detail_card.view.*
import java.util.*

class OnRentVehicleFragment : Fragment() {

    lateinit var fragmentView: View
    var onRentVehicleList: ArrayList<VehicleModel> = ArrayList()
    val rootRef = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_on_rent_vehicle, container, false)
        return fragmentView
    }
    
    override fun onStart() {
        super.onStart()
        loadOnRentVehilcle()
        fragmentView.recycler_view_on_rent.layoutManager = LinearLayoutManager(fragmentView.context)
    }

    private fun loadOnRentVehilcle() {
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
                    fragmentView.recycler_view_on_rent.adapter = OnRentVehicleAdapter(onRentVehicleList, fragmentView.context)
                }
    }
}


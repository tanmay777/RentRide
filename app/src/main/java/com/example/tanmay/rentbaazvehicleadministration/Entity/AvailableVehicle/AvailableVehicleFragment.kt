package com.example.tanmay.rentbaazvehicleadministration.Entity.AvailableVehicle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel

import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_all_vehicle.*
import kotlinx.android.synthetic.main.fragment_available_vehicle.view.*
import java.util.*
import kotlin.collections.ArrayList

class AvailableFragment : Fragment() {

    lateinit var fragmentView: View
    private lateinit var mRunnable:Runnable
    val rootRef = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_available_vehicle, container, false)
        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        loadAvailableVehilcle(true) //This true/false is to distinguish whether we have to load data from swipe down to refresh or normal load when fragment starts
        fragmentView.recycler_view_available_vehicle.layoutManager = LinearLayoutManager(fragmentView.context)
        fragmentView.no_vehicle_available_text.visibility=View.GONE
        swipe_container.setOnRefreshListener {
                loadAvailableVehilcle(false)

        }
    }

    private fun loadAvailableVehilcle(flag:Boolean) {
        if(flag)
            fragmentView.progressBar.visibility=View.VISIBLE
        var availableVehicleList: ArrayList<VehicleModel> = ArrayList()
        rootRef.collection("vehicle")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val availableVehicle: VehicleModel =it.toObject(VehicleModel::class.java)
                        availableVehicle.id=it.id
                        var flag=true
                        for (bookingModel in availableVehicle.booking) {
                            if ((bookingModel.pickup_details.compareTo(Date()) < 0) &&
                                    (bookingModel.drop_details.compareTo(Date()) > 0)) {
                                flag = false
                            }
                        }
                        if(flag) {
                            availableVehicleList.add(availableVehicle)
                        }
                    }
                    if(availableVehicleList.isEmpty())
                    {
                        fragmentView.no_vehicle_available_text.visibility=View.VISIBLE
                    }
                    fragmentView.recycler_view_available_vehicle.adapter = AvailableVehicleAdapter(availableVehicleList, fragmentView.context)
                    if(flag)
                        fragmentView.progressBar.visibility=View.GONE
                    else
                        swipe_container.isRefreshing = false
                }

    }
}

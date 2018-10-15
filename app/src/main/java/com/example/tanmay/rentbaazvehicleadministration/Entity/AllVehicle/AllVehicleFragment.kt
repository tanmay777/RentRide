package com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle

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

import kotlinx.android.synthetic.main.fragment_all_vehicle.view.*
import java.util.*

class AllVehicleFragment : Fragment() {

    lateinit var fragmentView: View
    val rootRef = FirebaseFirestore.getInstance()
    private lateinit var mRunnable:Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_all_vehicle, container, false)
        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        loadAllVehilcle(true) //This true/false is to distinguish whether we have to load data from swipe down to refresh or normal load when fragment starts
        fragmentView.recycler_view_all_vehicle.layoutManager = LinearLayoutManager(fragmentView.context)
        swipe_container.setOnRefreshListener {
                loadAllVehilcle(false)
        }
    }

    private fun loadAllVehilcle(flag:Boolean) {
        if(flag)
            fragmentView.progressBar.visibility=View.VISIBLE
        var allVehicleList: ArrayList<VehicleModel> = ArrayList()
        rootRef.collection("vehicle")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val allVehicle: VehicleModel = it.toObject(VehicleModel::class.java)
                        allVehicle.id = it.id
                        allVehicleList.add(allVehicle)
                    }
                    fragmentView.recycler_view_all_vehicle.adapter = AllVehicleAdapter(allVehicleList, fragmentView.context)
                    if(flag)
                        fragmentView.progressBar.visibility=View.GONE
                    else
                        swipe_container.isRefreshing = false
                }

    }
}

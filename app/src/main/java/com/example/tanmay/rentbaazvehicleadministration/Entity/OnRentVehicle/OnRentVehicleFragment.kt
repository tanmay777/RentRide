package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRentVehicle


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle.AllVehicleFragment
import com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle.AllVehicleModel

import com.example.tanmay.rentbaazvehicleadministration.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_vehicle.view.*
import kotlinx.android.synthetic.main.fragment_on_rent_vehicle.view.*
import kotlinx.android.synthetic.main.vehicle_detail_card.view.*

class OnRentVehicleFragment : Fragment() {

    lateinit var fragmentView: View
    private var onRentVehicleFirestoreRecyclerAdapter: OnRentVehicleFirestoreRecyclerAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_on_rent_vehicle, container, false)
        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        fragmentView.recycler_view_on_rent.layoutManager= LinearLayoutManager(fragmentView.context)
        //create the root reference of your Firestore database
        val rootRef= FirebaseFirestore.getInstance()
        val query=rootRef!!.collection("rented_vehicle") .orderBy("vehicle_name", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<OnRentVehicleModel>().setQuery(query, OnRentVehicleModel::class.java).build()
        onRentVehicleFirestoreRecyclerAdapter=OnRentVehicleFirestoreRecyclerAdapter(options)
        fragmentView.recycler_view_on_rent.adapter=onRentVehicleFirestoreRecyclerAdapter

        if (onRentVehicleFirestoreRecyclerAdapter!= null) {
            onRentVehicleFirestoreRecyclerAdapter!!.startListening()
        }
    }

    inner class VehicleViewHolder internal constructor(private val view:View): RecyclerView.ViewHolder(view){
        internal fun setVehicleInfo(onRentVehicleModel: OnRentVehicleModel){
            view.bike_name.text=onRentVehicleModel.vehicle_name
            view.bike_number.text=onRentVehicleModel.vehicle_number
            Glide.with(fragmentView).load(onRentVehicleModel.vehicle_image_url).into(view.bike_img)
            view.bike_vendor_organization.text=onRentVehicleModel.vendor_organization
            view.cost_weekday.text=onRentVehicleModel.weekday_cost
            view.cost_weekend.text=onRentVehicleModel.weekend_cost
        }
    }

    inner class OnRentVehicleFirestoreRecyclerAdapter internal constructor(options:FirestoreRecyclerOptions<OnRentVehicleModel>): FirestoreRecyclerAdapter<OnRentVehicleModel, VehicleViewHolder>(options){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.vehicle_detail_card,parent,false)
            return VehicleViewHolder(view)
        }

        override fun onBindViewHolder(vehicleViewHolder: VehicleViewHolder, position: Int, onRentVehicleModel: OnRentVehicleModel) {
            vehicleViewHolder.setVehicleInfo(onRentVehicleModel)
        }

        override fun onDataChanged() {
            fragmentView.recycler_view_on_rent.layoutManager?.scrollToPosition(itemCount - 1)
            //TODO: Add a refresh sign too
        }

    }

    override fun onStop() {
        super.onStop()

        if (onRentVehicleFirestoreRecyclerAdapter!= null) {
            onRentVehicleFirestoreRecyclerAdapter!!.stopListening()
        }
    }
}

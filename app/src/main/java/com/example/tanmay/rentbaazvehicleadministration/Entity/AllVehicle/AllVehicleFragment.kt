package com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.tanmay.rentbaazvehicleadministration.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.fragment_all_vehicle.*
import kotlinx.android.synthetic.main.fragment_all_vehicle.view.*
import kotlinx.android.synthetic.main.vehicle_detail_card.view.*

class AllVehicleFragment : Fragment() {

    lateinit var fragmentView: View
    private var allVehicleFirestoreRecyclerAdapter:AllVehicleFirestoreRecyclerAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_all_vehicle, container, false)
        return fragmentView
    }
/*
    class VehicleViewHolder(view:View):RecyclerView.ViewHolder(view) {
    }

    */
    override fun onStart() {
        super.onStart()
        fragmentView.recycler_view_all_vehicle.layoutManager=LinearLayoutManager(fragmentView.context)
        //create the root reference of your Firestore database
        val rootRef=FirebaseFirestore.getInstance()
        val query=rootRef!!.collection("all_vehicles") .orderBy("vehicle_name",Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<AllVehicleModel>().setQuery(query,AllVehicleModel::class.java).build()
        allVehicleFirestoreRecyclerAdapter=AllVehicleFirestoreRecyclerAdapter(options)
        fragmentView.recycler_view_all_vehicle.adapter=allVehicleFirestoreRecyclerAdapter

    if (allVehicleFirestoreRecyclerAdapter != null) {
        allVehicleFirestoreRecyclerAdapter!!.startListening()
    }

    }

    inner class VehicleViewHolder internal constructor(private val view:View):RecyclerView.ViewHolder(view){
        internal fun setVehicleInfo(vehicleModel: AllVehicleModel){
            view.bike_name.text=vehicleModel.vehicle_name
            view.bike_number.text=vehicleModel.vehicle_number
            Glide.with(fragmentView).load(vehicleModel.vehicle_image_url).into(view.bike_img)
            view.bike_vendor_organization.text=vehicleModel.vendor_organization
            view.cost_weekday.text=vehicleModel.weekday_cost
            view.cost_weekend.text=vehicleModel.weekend_cost
        }
    }

    inner class AllVehicleFirestoreRecyclerAdapter internal constructor(options:FirestoreRecyclerOptions<AllVehicleModel>):FirestoreRecyclerAdapter<AllVehicleModel,VehicleViewHolder>(options){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.vehicle_detail_card,parent,false)
            return VehicleViewHolder(view)
        }

        override fun onBindViewHolder(vehicleViewHolder: VehicleViewHolder, position: Int, vehicleModel: AllVehicleModel) {
            vehicleViewHolder.setVehicleInfo(vehicleModel)
        }

        override fun onDataChanged() {
            fragmentView.recycler_view_all_vehicle.layoutManager?.scrollToPosition(itemCount - 1)
            //TODO: Add a refresh sign too
        }

    }

    override fun onStop() {
        super.onStop()

        if (allVehicleFirestoreRecyclerAdapter != null) {
            allVehicleFirestoreRecyclerAdapter!!.stopListening()
        }
    }

}


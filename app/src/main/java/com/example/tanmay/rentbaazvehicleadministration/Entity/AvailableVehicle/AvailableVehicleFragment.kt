package com.example.tanmay.rentbaazvehicleadministration.Entity.AvailableVehicle


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle.AllVehicleFragment
import com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle.AllVehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.Rent.RentActivity

import com.example.tanmay.rentbaazvehicleadministration.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_vehicle.view.*
import kotlinx.android.synthetic.main.fragment_available_vehicle.view.*
import kotlinx.android.synthetic.main.vehicle_detail_card.view.*

class AvailableFragment : Fragment() {

    lateinit var fragmentView: View
    private var availableVehicleFirestoreRecyclerAdapter: AvailableVehicleFirestoreRecyclerAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_available_vehicle, container, false)
        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        fragmentView.recycler_view_available_vehicle.layoutManager= LinearLayoutManager(fragmentView.context)
        //create the root reference of your Firestore database
        val rootRef= FirebaseFirestore.getInstance()
        val query=rootRef.collection("available_vehicle") .orderBy("vehicle_name", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<AvailableVehicleModel>().setQuery(query, AvailableVehicleModel::class.java).build()
        availableVehicleFirestoreRecyclerAdapter=AvailableVehicleFirestoreRecyclerAdapter(options)
        fragmentView.recycler_view_available_vehicle.adapter=availableVehicleFirestoreRecyclerAdapter

        if (availableVehicleFirestoreRecyclerAdapter!= null) {
            availableVehicleFirestoreRecyclerAdapter!!.startListening()
        }
    }

    inner class VehicleViewHolder internal constructor(private val view:View): RecyclerView.ViewHolder(view){
        internal fun setVehicleInfo(availableVehicleModel: AvailableVehicleModel,itemId:String?){
            view.bike_name.text=availableVehicleModel.vehicle_name
            view.bike_number.text=availableVehicleModel.vehicle_number
            Glide.with(fragmentView).load(availableVehicleModel.vehicle_image_url).into(view.bike_img)
            view.bike_vendor_organization.text=availableVehicleModel.vendor_organization
            view.cost_weekday.text=availableVehicleModel.weekday_cost
            view.cost_weekend.text=availableVehicleModel.weekend_cost
            view.setOnClickListener{
                val intent=Intent(fragmentView.context,RentActivity::class.java)
                intent.putExtra("item_id",itemId)
                startActivity(intent)
            }
        }
    }

    inner class AvailableVehicleFirestoreRecyclerAdapter internal constructor(options:FirestoreRecyclerOptions<AvailableVehicleModel>): FirestoreRecyclerAdapter<AvailableVehicleModel, VehicleViewHolder>(options){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.vehicle_detail_card,parent,false)
            return VehicleViewHolder(view)
        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun onBindViewHolder(vehicleViewHolder: VehicleViewHolder, position: Int, availableVehicleModel: AvailableVehicleModel) {
            vehicleViewHolder.setVehicleInfo(availableVehicleModel,availableVehicleFirestoreRecyclerAdapter?.snapshots?.getSnapshot(position)?.id)
        }

        override fun onDataChanged() {
            fragmentView.recycler_view_available_vehicle.layoutManager?.scrollToPosition(itemCount - 1)
            //TODO: Add a refresh sign too
        }

    }

    override fun onStop() {
        super.onStop()

        if (availableVehicleFirestoreRecyclerAdapter!= null) {
            availableVehicleFirestoreRecyclerAdapter!!.stopListening()
        }
    }

}

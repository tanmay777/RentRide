package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.tanmay.rentbaazvehicleadministration.Entity.AllVehicle.AllVehicleFragment
import com.example.tanmay.rentbaazvehicleadministration.Entity.AvailableVehicle.AvailableFragment
import com.example.tanmay.rentbaazvehicleadministration.Entity.OnRentVehicle.OnRentVehicleFragment

class HomeTabPagerAdapter(fm:FragmentManager,private var tabCount:Int):FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when(position){
            0->return AllVehicleFragment()
            1->return AvailableFragment()
            2->return OnRentVehicleFragment()
            else->return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
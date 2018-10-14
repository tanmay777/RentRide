package com.example.tanmay.rentbaazvehicleadministration.Entity.AddVehicles

import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel

data class AddVehicleAvailableModel(val vendor:String="",
                                    val vehicle_name:String="",
                                    val vehicle_number:String="",
                                    val vendor_organization:String="",
                                    val vehicle_image_url:String="",
                                    val weekday_cost:String="",
                                    val weekend_cost:String="",
                                    val prebooking: List<bookingModel> = emptyList())

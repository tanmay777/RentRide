package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

data class VehicleModel(var vendor: String = "",
                        var vehicle_name: String = "",
                        var vehicle_number: String = "",
                        var vendor_organization: String = "",
                        var vehicle_image_url: String = "",
                        var weekday_cost: String = "",
                        var weekend_cost: String = "",
                        var booking: MutableList<bookingModel> = mutableListOf<bookingModel>(),
                        var id: String = "")


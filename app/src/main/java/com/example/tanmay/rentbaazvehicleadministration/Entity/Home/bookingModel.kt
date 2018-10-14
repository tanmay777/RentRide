package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import java.util.Date
import java.sql.Timestamp

data class bookingModel(val phone_num:String="",val drop_details:Date= Date(0), val pickup_details:Date=Date(0))
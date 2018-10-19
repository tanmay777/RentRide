package com.example.tanmay.rentbaazvehicleadministration.Entity.Home

import java.util.Date
import java.sql.Timestamp

data class bookingModel(val phone_num:String="", var pickup_details:Date=Date(0),var drop_details:Date= Date(0),val bill:String="")
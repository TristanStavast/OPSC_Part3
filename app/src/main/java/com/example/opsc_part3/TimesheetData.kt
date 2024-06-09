package com.example.opsc_part3

data class TimesheetData (
    var username : String? = null,
    var tsName : String? = null,
    var tsCategory: String? = null,
    var date : String? = null,
    var sTime : String? = null,
    var eTime : String? = null,
    var totalTime : String? = null,
    var description : String? = null,
    var image : String? = null
) {
}
package com.example.opsc_part3

class CheckIfExists {

    companion object {

        fun isUsernameExists(username: String, userList: List<Users>): Boolean {
            return userList.any { it.username == username }
        }

        fun isTimesheetExists(tsName: String, timesheetList: List<TimesheetData>): Boolean {
            return timesheetList.any { it.tsName == tsName }
        }

        fun isCategoryExists(categoryName: String, categoryList: List<CategoryData>): Boolean {
            return categoryList.any { it.CategoryName == categoryName }
        }

    }

}
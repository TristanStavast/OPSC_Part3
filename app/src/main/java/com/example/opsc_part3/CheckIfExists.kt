package com.example.opsc_part3

class CheckIfExists {

    companion object {

        fun isUsernameExists(username: String?, userList: List<Users>): Boolean {
            return userList.any { it.username == username }
        }

        fun isTimesheetExists(username: String?, tsName: String?, timesheetList: List<TimesheetData>): Boolean {
            return timesheetList.any { it.username == username && it.tsName == tsName }
        }

        fun isCategoryExists(username: String?, categoryName: String?, categoryList: List<CategoryData>): Boolean {
            return categoryList.any { it.username == username && it.CategoryName == categoryName }
        }

    }

}
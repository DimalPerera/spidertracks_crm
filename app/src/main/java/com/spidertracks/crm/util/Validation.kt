package com.spidertracks.crm.util

import android.text.TextUtils
import android.util.Patterns

class Validation {

    /**
     * These functions take an input value as a parameter and return a Boolean indicating whether
     * the input is valid or not based on the validation logic inside the function.
     */

    companion object {
        /*private fun checkIfEmailIsValid(email: String): String? {

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                activityMainBinding.loginEmail.error = "Invalid Email Address"
            }
            else{
                activityMainBinding.loginEmail.error = null
            }
        }
    }*/

        fun isNameValid(name: String): Boolean {
            val nameRegex = "[A-Za-z\\s]+"
            return name.matches(nameRegex.toRegex()) && !TextUtils.isEmpty(name)
        }

        fun isJoinDateValid(date: String): Boolean {
            return !TextUtils.isEmpty(date)
        }

        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPhoneNumberValid(phoneNumber: String): Boolean {
            return Patterns.PHONE.matcher(phoneNumber).matches()
        }

        fun getCustomerStatus(phoneNumber: String): Boolean {
            return Patterns.PHONE.matcher(phoneNumber).matches()
        }
    }
}
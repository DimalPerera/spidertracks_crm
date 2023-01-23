package com.spidertracks.crm.util

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarController {
    companion object {

        fun showSnackBar(contextView: View, message: String){
            Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}
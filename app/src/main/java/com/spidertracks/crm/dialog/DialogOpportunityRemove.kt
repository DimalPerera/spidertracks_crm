package com.spidertracks.crm.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.spidertracks.crm.model.opportunity.Opportunity

class DialogOpportunityRemove(opportunity: Opportunity) : DialogFragment() {

    private var opportunity: Opportunity = opportunity
    private var callback: DialogCallback? = null

    interface DialogCallback {
        fun onOpportunityDeleteListener(opportunity: Opportunity)
        //fun onNegativeClicked()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you really want to delete this opportunity?")
                .setPositiveButton(
                   "Yes"
                ) { dialog, id ->
                    callback?.onOpportunityDeleteListener(opportunity)
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    //callback?.onNegativeClicked()
                    dialog.dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setCallback(callback: DialogCallback) {
        this.callback = callback
    }
}

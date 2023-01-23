package com.spidertracks.crm.model.opportunity


import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

/**
 * A data class representing a Opportunity object, which include properties such as customerId, id,
 * name, and status. It implements the Parcelable interface for efficient data transport.
 */

@Parcelize
data class Opportunity(
    @JsonProperty("customerId")
    var customerId: Int?, // 2
    @JsonProperty("id")
    var id: Int?, // 4
    @JsonProperty("name")
    var name: String?, // Subscription to food package
    @JsonProperty("status")
    var status: String? // new
) : Parcelable
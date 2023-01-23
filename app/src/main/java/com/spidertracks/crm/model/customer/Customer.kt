package com.spidertracks.crm.model.customer


import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

/**
 * A data class representing a Customer object, which include properties such as
 * id, createdDate, email, name, phoneNumber, and status.
 * It implements the Parcelable interface for efficient data transport.
 */
@Parcelize
data class Customer(
    @JsonProperty("id")
    var id: Int?, // 1
    @JsonProperty("createdDate")
    var createdDate: String?, // 2022-10-07T11:18:00
    @JsonProperty("email")
    var email: String?, // hscherme5a@aol.com
    @JsonProperty("name")
    var name: String?, // Myca Blanchflower
    @JsonProperty("phoneNumber")
    var phoneNumber: String?, // (555) 857-1351
    @JsonProperty("status")
    var status: String? // active
) : Parcelable
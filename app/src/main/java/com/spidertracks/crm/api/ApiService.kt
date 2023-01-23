package com.spidertracks.crm.api

import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.model.customer.CustomerList
import com.spidertracks.crm.model.opportunity.Opportunity
import com.spidertracks.crm.model.opportunity.OpportunityList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ApiService {
    @GET("customers")
    fun getCustomers(): Single<CustomerList>?

    @POST("customers")
    fun createCustomers(@Body customer: Customer): Single<Customer>?

    @DELETE("customers/{id}")
    fun deleteCustomers(@Path("id") id: String): Completable

    @PUT("customers/{id}")
    fun updateCustomers(@Path("id") id: String, @Body customer: Customer): Single<Customer>?


    @GET("customers/{customer_id}/opportunities/")
    fun getOpportunities(@Path("customer_id") customerId: String): Single<OpportunityList>?

    @POST("customers/{customer_id}/opportunities/")
    fun createOpportunity(
        @Path("customer_id") customerId: String,
        @Body opportunity: Opportunity
    ): Single<Opportunity>?

    @DELETE("customers/{customer_id}/opportunities/{opId}")
    fun deleteOpportunity(
        @Path("customer_id") customerId: String,
        @Path("opId") opId: String
    ): Completable

    @PUT("customers/{customer_id}/opportunities/{opId}")
    fun updateOpportunity(
        @Path("customer_id") id: String,
        @Path("opId") opId: String,
        @Body opportunity: Opportunity
    ): Single<Opportunity>?

}
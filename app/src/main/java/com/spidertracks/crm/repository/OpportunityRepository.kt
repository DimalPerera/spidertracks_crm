package com.spidertracks.crm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spidertracks.crm.api.ApiService
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.model.opportunity.Opportunity
import com.spidertracks.crm.model.opportunity.OpportunityList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OpportunityRepository(private var apiService: ApiService) {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var pageSize = 2
    private var currentPage = 1

    private var disposable: CompositeDisposable? = null
    //private var paginationDisposable: Disposable? = null
    //private val apiService: ApiService = ApiClient.getRetrofitClient()!!.create(ApiService::class.java)

    init {

    }

    fun getOpportunityList(customer: Customer): Single<OpportunityList>?{
        return apiService.getOpportunities(customer.id.toString())
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    }

    fun createOpportunity(customerId: String, opportunity: Opportunity): Single<Opportunity>? {
        return apiService.createOpportunity(customerId, opportunity)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteOpportunity(opportunity: Opportunity): Completable? {
        return apiService.deleteOpportunity(opportunity.customerId.toString(), opportunity.id.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun updateOpportunity(opportunity: Opportunity): Single<Opportunity>? {
        return apiService.updateOpportunity(opportunity.customerId.toString(), opportunity.id.toString(), opportunity)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}



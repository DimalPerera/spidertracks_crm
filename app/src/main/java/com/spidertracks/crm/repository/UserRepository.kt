package com.spidertracks.crm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spidertracks.crm.api.ApiService
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.model.customer.CustomerList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * A UserRepository class that communicates with a web service through an ApiService instance,
 * which is passed as a parameter in the constructor. It has properties such as _isLoading,
 * pageSize, currentPage, disposable and a few functions such as getCustomersFromApi,
 * deleteCustomers, createCustomer and updateCustomer which are used to fetch, delete, create
 * and update customers respectively. These functions are using subscribeOn and observeOn
 * to schedule the network requests on different threads.
 */

class UserRepository(private var apiService: ApiService) {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var pageSize = 2
    private var currentPage = 1

    private var disposable: CompositeDisposable? = null
    //private var paginationDisposable: Disposable? = null
    //private val apiService: ApiService = ApiClient.getRetrofitClient()!!.create(ApiService::class.java)

    init {

    }

    fun getCustomersFromApi(): Single<CustomerList>?{
        return apiService.getCustomers()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    }

    fun deleteCustomers(customerId: String): Completable? {
        return apiService.deleteCustomers(customerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createCustomer(customer: Customer): Single<Customer>? {
        return apiService.createCustomers(customer)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun updateCustomer(customer: Customer): Single<Customer>? {
        return apiService.updateCustomers(customer.id.toString(), customer)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}



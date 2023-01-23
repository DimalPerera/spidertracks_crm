package com.spidertracks.crm.viewmodel

import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.*
import com.spidertracks.crm.R
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.repository.UserRepository


class CustomerViewModel(private val repository: UserRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Customer>>()
    val items: LiveData<List<Customer>> = _items

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _removeMessage = MutableLiveData<String>()
    val removeMessage: LiveData<String> = _removeMessage

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    private val _filteredItems = MutableLiveData<List<Customer>>()
    val filteredItems: LiveData<List<Customer>> = _filteredItems


    init {
        //fetchCustomerList()
    }

    fun fetchCustomerList() {
        _progressBar.value = true

        repository.getCustomersFromApi()
            ?.subscribe({ items ->
                _progressBar.value = false
                _items.value = items
            }, {
                _progressBar.value = false
                _message.value = it.message
            })
    }


    fun createCustomer(customer: Customer) {
        _progressBar.value = true
        repository.createCustomer(customer)
            ?.subscribe(
                {
                    _progressBar.value = false
                    val currentItems = _items.value ?: emptyList()
                    val newItems = currentItems + it
                    _items.value = newItems
                    _message.value = "Customer created successfully!"
                },
                {
                    _progressBar.value = false
                    _message.value = it.message
                }
            )
    }

    fun updateCustomer(customer: Customer) {
        _progressBar.value = true
        repository.updateCustomer(customer)
            ?.subscribe(
                {
                    _progressBar.value = false
                    val currentItems = _items.value ?: return@subscribe
                    val updatedItems = currentItems.map {
                        if (it.id == customer.id) {
                            customer
                        } else {
                            it
                        }
                    }
                    _items.value = updatedItems
                    _message.value = "Customer updated successfully!"
                },
                {
                    _progressBar.value = false
                    _message.value = it.message
                }
            )
    }

    fun removeCustomer(customer: Customer) {
        _progressBar.value = true
        repository.deleteCustomers(customer.id.toString())
            ?.subscribe({
                _progressBar.value = false
                val currentItems = _items.value ?: emptyList()
                val newItems = currentItems.filter { it != customer }
                _items.value = newItems
                _removeMessage.value = "Customer removed successfully!"

            }, {
                _progressBar.value = false
                _removeMessage.value = it.message
            })
    }

    fun filterItems(condition: (Customer) -> Boolean) {
        val filteredItems = _items.value?.filter(condition)
        _filteredItems.value = filteredItems!!
    }


}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
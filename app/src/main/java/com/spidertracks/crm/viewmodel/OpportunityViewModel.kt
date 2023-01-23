package com.spidertracks.crm.viewmodel

import androidx.lifecycle.*
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.model.opportunity.Opportunity
import com.spidertracks.crm.repository.OpportunityRepository


class OpportunityViewModel(private val repository: OpportunityRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Opportunity>>()
    val items: LiveData<List<Opportunity>> = _items

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _removeMessage = MutableLiveData<String>()
    val removeMessage: LiveData<String> = _removeMessage

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    fun getOpportunities(customer: Customer) {
        _progressBar.value = true
        repository.getOpportunityList(customer)
            ?.subscribe({ items ->
                _progressBar.value = false
                _items.value = items
            }, {
                _progressBar.value = false
                _message.value = it.message
            })
    }

    fun createOpportunity(opportunity: Opportunity) {
        _progressBar.value = true
        repository.createOpportunity(opportunity.customerId.toString(), opportunity)
            ?.subscribe(
                {

                    _progressBar.value = false
                    val currentItems = _items.value ?: emptyList()
                    val newItems = currentItems + it
                    _items.value = newItems

                    _message.value = "Opportunity created successfully!"
                },
                {
                    _progressBar.value = false
                    _message.value = it.message
                }
            )


    }

    fun updateOpportunity(opportunity: Opportunity) {
        _progressBar.value = true
        repository.updateOpportunity(opportunity)
            ?.subscribe(
                {
                    _progressBar.value = false
                    val currentItems = _items.value
                    val updatedItems = currentItems?.map {
                        if (it.id == opportunity.id) {
                            opportunity
                        } else {
                            it
                        }
                    }
                    _items.value = updatedItems!!

                    _message.value = "Opportunity updated successfully!"
                },
                {
                    _progressBar.value = false
                    _message.value = it.message
                }
            )
    }

    fun removeOpportunity(opportunity: Opportunity) {
        _progressBar.value = true
        repository.deleteOpportunity(opportunity)
            ?.subscribe({
                _progressBar.value = false
                val currentItems = _items.value ?: emptyList()
                val newItems = currentItems.filter { it != opportunity }
                _items.value = newItems
                _removeMessage.value = "Opportunity deleted successfully!"
            }, {
                _progressBar.value = false
                _removeMessage.value = it.message
            })
    }

}


class OpportunityViewModelFactory(private val repository: OpportunityRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OpportunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OpportunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
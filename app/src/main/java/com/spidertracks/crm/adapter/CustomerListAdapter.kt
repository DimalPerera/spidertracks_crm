package com.spidertracks.crm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.databinding.ListItemCustomerBinding

class CustomerListAdapter() :
    RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    private var stationList: List<Customer> = emptyList()

    private var callback: CustomerAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCustomerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Customer = stationList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = stationList.size


    /**
    ViewHolder class for handling UI elements and data binding for each item in the RecyclerView.
    Takes a private val of type ListItemCustomerBinding as its constructor parameter.
    Has a bind function that takes a Customer object as a parameter and sets click listeners for
    the row, long click, and delete button.
     */
    inner class ViewHolder(private val binding: ListItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer) {
            binding.customer = customer

            binding.row.setOnClickListener {
                // Handle item click event
                callback?.onItemClicked(customer)
            }

            binding.row.setOnLongClickListener() {
                // Handle item click event
                callback?.onItemLongClicked(customer)
                true
            }

            binding.imgDelete.setOnClickListener {
                // Handle item click event
                callback?.onItemDeleteClicked(customer)

            }

        }
    }
    fun updateItems(users: List<Customer>) {
        stationList = users
        notifyDataSetChanged()
    }

    fun setCallback(callback: CustomerAdapterCallback) {
        this.callback = callback
    }

    interface CustomerAdapterCallback {
        fun onItemClicked(customer: Customer)
        fun onItemLongClicked(customer: Customer)
        fun onItemDeleteClicked(customer: Customer)
    }

}

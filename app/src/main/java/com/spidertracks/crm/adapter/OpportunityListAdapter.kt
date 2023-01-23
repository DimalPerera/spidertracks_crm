package com.spidertracks.crm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spidertracks.crm.databinding.ListItemOpportunityBinding
import com.spidertracks.crm.model.opportunity.Opportunity

class OpportunityListAdapter() :
    RecyclerView.Adapter<OpportunityListAdapter.ViewHolder>() {

    private var stationList: List<Opportunity> = emptyList()

    private var callback: OpportunityAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemOpportunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: Opportunity = stationList[position]

        holder.bind(data)

    }

    override fun getItemCount(): Int = stationList.size


    /**
    The ViewHolder class is an inner class of RecyclerView.ViewHolder that holds the UI elements
    for each item in the RecyclerView.
    It takes a private val of type [ListItemOpportunityBinding] as its constructor parameter which
    is used to bind the data to the UI elements.
    The bind function takes an [Opportunity] object as its parameter and sets the opportunity
    object to the binding object. It also sets click listeners for the row, long click and
    delete button. On clicking the row, onItemClicked function of the callback is called.
    On long clicking the row, onItemLongClicked function of the callback is called and on
    clicking delete button, onItemDeleteClicked function of the callback is called.
     */
    inner class ViewHolder(private val binding: ListItemOpportunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Opportunity) {
            binding.opportunity = customer

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

    fun updateItems(opportunities: List<Opportunity>) {
        stationList = opportunities
        notifyDataSetChanged()
    }

    fun setCallback(callback: OpportunityAdapterCallback) {
        this.callback = callback
    }


    interface OpportunityAdapterCallback {
        fun onItemClicked(opportunity: Opportunity)
        fun onItemLongClicked(opportunity: Opportunity)
        fun onItemDeleteClicked(opportunity: Opportunity)
    }
}

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

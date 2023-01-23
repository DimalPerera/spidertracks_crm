package com.spidertracks.crm.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spidertracks.crm.R
import com.spidertracks.crm.adapter.OpportunityListAdapter
import com.spidertracks.crm.api.ApiClient
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.databinding.FragmentOpportunityListBinding
import com.spidertracks.crm.dialog.DialogOpportunityRemove
import com.spidertracks.crm.model.opportunity.Opportunity
import com.spidertracks.crm.repository.OpportunityRepository
import com.spidertracks.crm.viewmodel.OpportunityViewModel
import com.spidertracks.crm.viewmodel.OpportunityViewModelFactory


class OpportunityListFragment : Fragment(), OpportunityListAdapter.OpportunityAdapterCallback, DialogOpportunityRemove.DialogCallback  {

    private lateinit var binding: FragmentOpportunityListBinding

    private val repository: OpportunityRepository by lazy {
        OpportunityRepository(ApiClient.service)
    }
    private val viewModel: OpportunityViewModel by activityViewModels { OpportunityViewModelFactory(repository) }

    private var selectedCustomer: Customer? = null
    private lateinit var adapter: OpportunityListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCustomer = it.getParcelable("customer")
            if(selectedCustomer != null) {
                viewModel.getOpportunities(selectedCustomer!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opportunity_list, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = OpportunityListAdapter()
        adapter.setCallback(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled above hide the FAB
                if (dy > 10 && binding.fabButton.isShown) {
                    binding.fabButton.hide()
                }

                // if the recycler view is scrolled above show the FAB
                if (dy < -10 && !binding.fabButton.isShown) {
                    binding.fabButton.show()
                }

                // of the recycler view is at the first item always show the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    binding.fabButton.show()
                }
            }
        })

        binding.fabButton.setOnClickListener {

            val opportunityFragment = OpportunityFragment()
            val args = Bundle()
            args.putParcelable("customer", selectedCustomer)
            opportunityFragment.arguments = args
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, opportunityFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)

        })

        /*viewModel.removeMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })*/

        viewModel.progressBar.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        return binding.root
    }

    override fun onItemClicked(opportunity: Opportunity) {

    }

    override fun onItemLongClicked(opportunity: Opportunity) {
        val opportunityFragment = OpportunityFragment()
        val args = Bundle()
        args.putParcelable("customer", selectedCustomer)
        args.putParcelable("opportunity", opportunity)
        opportunityFragment.arguments = args
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, opportunityFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onItemDeleteClicked(opportunity: Opportunity) {
        val dialog = DialogOpportunityRemove(opportunity)
        dialog.setCallback(this)
        dialog.show( parentFragmentManager, "dialog")
    }

    override fun onOpportunityDeleteListener(opportunity: Opportunity) {
        Log.e("opportunity", opportunity.id.toString() + " "+opportunity.customerId.toString())
        viewModel.removeOpportunity(opportunity)
    }

}
package com.spidertracks.crm.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spidertracks.crm.R
import com.spidertracks.crm.adapter.CustomerListAdapter
import com.spidertracks.crm.api.ApiClient
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.databinding.FragmentCustomerListBinding
import com.spidertracks.crm.dialog.DialogCustomerRemove
import com.spidertracks.crm.repository.UserRepository
import com.spidertracks.crm.viewmodel.CustomerViewModel
import com.spidertracks.crm.viewmodel.ViewModelFactory
import com.spidertracks.crm.viewmodel.observeOnce

class CustomerListFragment : Fragment(), CustomerListAdapter.CustomerAdapterCallback,
    DialogCustomerRemove.DialogCallback {

    private lateinit var binding: FragmentCustomerListBinding
    private val repository: UserRepository by lazy {
        UserRepository(ApiClient.service)
    }
    private val viewModel: CustomerViewModel by activityViewModels() { ViewModelFactory(repository) }

    private lateinit var adapter: CustomerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list, container, false)


        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        configFilterOptions()

        adapter = CustomerListAdapter()
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

            val secondFragment = CustomerFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, secondFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)
        })

        viewModel.filteredItems.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)
        })

        /*viewModel.removeMessage.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })*/

        viewModel.progressBar.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        return binding.root
    }

    override fun onItemClicked(customer: Customer) {
        val secondFragment = OpportunityListFragment()
        val args = Bundle()
        args.putParcelable("customer", customer)
        secondFragment.arguments = args
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, secondFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onItemLongClicked(customer: Customer) {
        val secondFragment = CustomerFragment()
        val args = Bundle()
        args.putParcelable("customer", customer)
        secondFragment.arguments = args
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, secondFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onItemDeleteClicked(customer: Customer) {
        val dialog = DialogCustomerRemove(customer)
        dialog.setCallback(this)
        dialog.show(parentFragmentManager, "dialog")
    }

    override fun onCustomerDeleteListener(customer: Customer) {
        viewModel.removeCustomer(customer)
    }

    private fun configFilterOptions() {
        val items =
            arrayOf("Select Filter Option","All Customers", "Active Customers", "Non-Active Customers", "Lead Customers")
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, items) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (position) {
                    1 -> {
                        viewModel.fetchCustomerList()
                    }
                    2 -> {
                        viewModel.filterItems { it.status == "active" }
                    }
                    3 -> {
                        viewModel.filterItems { it.status == "non-active" }
                    }
                    4 -> {
                        viewModel.filterItems { it.status == "lead" }
                    }
                }

            }
        }


    }


}
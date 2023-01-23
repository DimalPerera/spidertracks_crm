package com.spidertracks.crm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.spidertracks.crm.R
import com.spidertracks.crm.api.ApiClient
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.databinding.FragmentOpportunityBinding
import com.spidertracks.crm.model.opportunity.Opportunity
import com.spidertracks.crm.repository.OpportunityRepository
import com.spidertracks.crm.util.Validation
import com.spidertracks.crm.viewmodel.OpportunityViewModel
import com.spidertracks.crm.viewmodel.OpportunityViewModelFactory

class OpportunityFragment : Fragment() {

    private lateinit var binding: FragmentOpportunityBinding

    private val repository: OpportunityRepository by lazy {
        OpportunityRepository(ApiClient.service)
    }

    private val viewModel: OpportunityViewModel by activityViewModels {
        OpportunityViewModelFactory(
            repository
        )
    }

    private var selectedCustomer: Customer? = null
    private var selectedOpportunity: Opportunity? = null

    private val formValidator = OpportunityFormValidator()
    private val opportunityFactory = OpportunityFactory()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opportunity, container, false)

        arguments?.let {
            selectedCustomer = it.getParcelable("customer")
            selectedOpportunity = it.getParcelable("opportunity")
        }

        if (selectedOpportunity != null) {
            bindOpportunity(binding, selectedOpportunity)

        } else {
            binding.btnSubmit.setOnClickListener {
                if (formValidator.validate(binding)) {
                    val opportunity = opportunityFactory.create(binding, selectedCustomer)
                    viewModel.createOpportunity(opportunity)
                }
            }
        }

        /*viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })*/

        viewModel.progressBar.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })


        return binding.root
    }


    private fun bindOpportunity(
        binding: FragmentOpportunityBinding,
        selectedOpportunity: Opportunity?
    ) {
        selectedOpportunity?.let {

            binding.etName.setText(it.name)

            when (it.status) {
                "new" -> {
                    binding.toggleButton.check(R.id.btnToggleNew)
                }
                "won" -> {
                    binding.toggleButton.check(R.id.btnToggleWon)
                }
                "lost" -> {
                    binding.toggleButton.check(R.id.btnToggleLost)
                }
            }

            binding.btnSubmit.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE

            binding.btnUpdate.setOnClickListener {

                if (formValidator.validate(binding)) {
                    val opportunity = opportunityFactory.create(binding, selectedCustomer)
                    opportunity.id = selectedOpportunity.id
                    viewModel.updateOpportunity(opportunity)
                }

            }
        }
    }

}

class OpportunityFormValidator {
    fun validate(binding: FragmentOpportunityBinding): Boolean {
        if (!Validation.isNameValid(binding.etName.text.toString())) {
            binding.etName.error = "Invalid opportunity"
            return false
        }
        return true
    }
}

class OpportunityFactory {
    fun create(binding: FragmentOpportunityBinding, customer: Customer?): Opportunity {
        // create customer object using form fields
        return Opportunity(
            customer?.id,
            0,
            binding.etName.text.toString(),
            getCustomerStatus(binding)
        )
    }

    private fun getCustomerStatus(binding: FragmentOpportunityBinding): String {
        when (binding.toggleButton.checkedButtonId) {
            binding.btnToggleWon.id -> {
                return "won"
            }
            binding.btnToggleLost.id -> {
                return "lost"
            }
        }
        return "new"
    }
}
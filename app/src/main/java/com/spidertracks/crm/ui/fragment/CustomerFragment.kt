package com.spidertracks.crm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.spidertracks.crm.R
import com.spidertracks.crm.api.ApiClient
import com.spidertracks.crm.model.customer.Customer
import com.spidertracks.crm.databinding.FragmentCustomerBinding
import com.spidertracks.crm.repository.UserRepository
import com.spidertracks.crm.util.DatePickerFragment
import com.spidertracks.crm.util.DateTimeController
import com.spidertracks.crm.util.Validation
import com.spidertracks.crm.viewmodel.CustomerViewModel
import com.spidertracks.crm.viewmodel.ViewModelFactory
import com.spidertracks.crm.viewmodel.observeOnce
import java.util.*


class CustomerFragment : Fragment(), DatePickerFragment.DatePickerCallback {

    private lateinit var binding: FragmentCustomerBinding
    private val repository: UserRepository by lazy {
        UserRepository(ApiClient.service)
    }
    private val viewModel: CustomerViewModel by activityViewModels() { ViewModelFactory(repository) }

    private val formValidator = FormValidator()
    private val customerFactory = CustomerFactory()

    private var selectedCustomer: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer, container, false)

        arguments?.let {
            selectedCustomer = it.getParcelable("customer")
            bindCustomerData(selectedCustomer)
        }

        binding.imgCalendar.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.setOnResultsListener(this)
            newFragment.show(parentFragmentManager, "DATE_PICKER")
        }

        binding.btnSubmit.setOnClickListener {
            if (formValidator.validate(binding)) {
                val customer = customerFactory.create(binding)
                viewModel.createCustomer(customer)
            }
        }

        /*viewModel.message.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })*/

        viewModel.progressBar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })



        return binding.root
    }

    override fun onDateSelected(date: Calendar) {
        binding.etJoinDate.setText(DateTimeController.convertCalenderToIsoFormat(date))
    }

    private fun bindCustomerData(selectedCustomer: Customer?) {
        selectedCustomer?.let {

            binding.etName.setText(it.name)
            binding.etJoinDate.setText(it.createdDate)
            binding.etEmail.setText(it.email)
            binding.etPhoneNo.setText(it.phoneNumber)

            when (it.status) {
                "active" -> {
                    binding.toggleButton.check(R.id.btnToggleActive)
                }
                "non-active" -> {
                    binding.toggleButton.check(R.id.btnToggleNonActive)
                }
                "lead" -> {
                    binding.toggleButton.check(R.id.btnToggleLead)
                }
            }

            binding.btnSubmit.visibility = View.GONE
            binding.btnUpdate.visibility = View.VISIBLE

            binding.btnUpdate.setOnClickListener {

                if (formValidator.validate(binding)) {
                    val customer = customerFactory.create(binding)
                    customer.id = selectedCustomer.id
                    viewModel.updateCustomer(customer)
                }
            }
        }
    }

}

/**
 * A FormValidator class that contains a function 'validate' which takes a binding object of
 * FragmentCustomerBinding as parameter. The function checks the validation of form fields
 * such as name, join date, email and phone number, and set the error message if any of the fields
 * are not valid. It returns true if all the fields are valid, otherwise returns false.
 */

class FormValidator {
    fun validate(binding: FragmentCustomerBinding): Boolean {
        // validate form fields and return true or false

        if (!Validation.isNameValid(binding.etName.text.toString())) {
            binding.etName.error = "Invalid name"
            return false
        }

        if (!Validation.isJoinDateValid(binding.etJoinDate.text.toString())) {
            binding.etJoinDate.error = "Invalid join date"
            return false
        }

        if (!Validation.isEmailValid(binding.etEmail.text.toString())) {
            binding.etEmail.error = "Invalid e-mail address"
            return false
        }

        if (!Validation.isPhoneNumberValid(binding.etPhoneNo.text.toString())) {
            binding.etPhoneNo.error = "Invalid phone number"
            return false
        }

        return true
    }

}

/**
 * A CustomerFactory class that contains a function 'create' which takes a binding object of
 * FragmentCustomerBinding as parameter. The function uses the form fields from the binding
 * object to create a Customer object and returns the created customer object. it also has a
 * private helper function 'getCustomerStatus' which takes the same binding object as parameter,
 * and returns the status of the customer by checking the id of the checked button of a toggle button.
 */
class CustomerFactory {
    fun create(binding: FragmentCustomerBinding): Customer {
        // create customer object using form fields

        return Customer(
            0,
            binding.etJoinDate.text.toString(),
            binding.etEmail.text.toString(),
            binding.etName.text.toString(),
            binding.etPhoneNo.text.toString(),
            getCustomerStatus(binding)
        )
    }

    private fun getCustomerStatus(binding: FragmentCustomerBinding): String {
        when (binding.toggleButton.checkedButtonId) {
            binding.btnToggleNonActive.id -> {
                return "non-active"
            }
            binding.btnToggleLead.id -> {
                return "lead"
            }
        }

        return "active"
    }
}
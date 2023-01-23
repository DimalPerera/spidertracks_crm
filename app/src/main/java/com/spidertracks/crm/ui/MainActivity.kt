package com.spidertracks.crm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.spidertracks.crm.R
import com.spidertracks.crm.api.ApiClient
import com.spidertracks.crm.databinding.ActivityMainBinding
import com.spidertracks.crm.repository.UserRepository
import com.spidertracks.crm.ui.fragment.CustomerListFragment
import com.spidertracks.crm.viewmodel.CustomerViewModel
import com.spidertracks.crm.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository: UserRepository by lazy {
        UserRepository(ApiClient.service)
    }
    private val viewModel: CustomerViewModel by viewModels { ViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        viewModel.fetchCustomerList()

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CustomerListFragment())
                .commit()
        }*/

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CustomerListFragment>(R.id.container)
            }
        }
    }
}
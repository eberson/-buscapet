package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.adapter.AdvertisementAdapter
import br.com.etecmatao.buscapet.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var vm: HomeViewModel
    private lateinit var adapter: AdvertisementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this).get(HomeViewModel::class.java)
        adapter = AdvertisementAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        vm.loadCurrentUser()
        vm.loadAdvertisements()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.user.observe(viewLifecycleOwner, Observer {
            it?.let { user ->
                txtFullName.text = "${user.firstName} ${user.lastName}"
                txtEmail.text = user.email
            }
        })

        vm.advertisements.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addItems(it)
            }
        })

        val adapter = this.adapter

        advertisementsItems.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }

        btnNewAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_home_to_advertisement)
        }
    }
}
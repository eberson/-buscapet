package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.adapter.AdvertisementAdapter
import br.com.etecmatao.buscapet.viewModel.PostsViewModel
import kotlinx.android.synthetic.main.fragment_my_posts.*

class MyPostsFragment : Fragment() {
    private lateinit var vm: PostsViewModel
    private lateinit var adapter: AdvertisementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(PostsViewModel::class.java)
        adapter = AdvertisementAdapter(requireContext()){
            val action = MyPostsFragmentDirections.actionMyPostsToPost(it.id)
            findNavController().navigate(action)
        }


        adItemsView.layoutManager = LinearLayoutManager(requireContext())
        adItemsView.adapter = adapter

        registerObservers()
    }

    private fun registerObservers(){
        vm.advertisements.observe(viewLifecycleOwner, Observer {
            it?.let { items ->
                adapter.addItems(items)
            }
        })
    }
}
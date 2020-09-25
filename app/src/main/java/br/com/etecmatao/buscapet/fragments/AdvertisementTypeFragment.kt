package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.model.AdType
import br.com.etecmatao.buscapet.viewModel.NewPostViewModel
import kotlinx.android.synthetic.main.fragment_advertisement_type.*

class AdvertisementTypeFragment : Fragment() {
    private lateinit var vm: NewPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_advertisement_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(NewPostViewModel::class.java)
        vm.loadCurrentUser()

        imgNewAd.setOnClickListener { selectAdType(AdType.PET_ADVERTISEMENT) }
        imgNewDonation.setOnClickListener { selectAdType(AdType.PET_DONATION) }
        imgNewPetLost.setOnClickListener { selectAdType(AdType.PET_LOST) }
    }

    private fun selectAdType(type: AdType){
        vm.postType.postValue(type)
        findNavController().navigate(R.id.action_add_type_to_gallery)
    }
}
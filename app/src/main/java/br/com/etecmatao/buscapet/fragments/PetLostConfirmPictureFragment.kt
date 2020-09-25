package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.etecmatao.buscapet.R
import kotlinx.android.synthetic.main.pet_lost_confirm_picture_fragment.*

class PetLostConfirmPictureFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pet_lost_confirm_picture_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConfirmContinue.setOnClickListener {
            findNavController().navigate(R.id.action_confirm_image_to_post)
        }

        btnConfirmBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
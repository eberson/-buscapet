package br.com.etecmatao.buscapet.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.viewModel.NewPostViewModel
import kotlinx.android.synthetic.main.pet_lost_confirm_picture_fragment.*

class PetLostConfirmPictureFragment : Fragment() {

    private lateinit var vm: NewPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pet_lost_confirm_picture_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(NewPostViewModel::class.java)
        vm.image.observe(viewLifecycleOwner, Observer {
            it?.let { encodedContent ->
                val decodedString = Base64.decode(encodedContent, Base64.DEFAULT)
                val image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                imgPetPicture.setImageBitmap(image)
            }
        })

        btnConfirmContinue.setOnClickListener {
            findNavController().navigate(R.id.action_confirm_image_to_post)
        }

        btnConfirmBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
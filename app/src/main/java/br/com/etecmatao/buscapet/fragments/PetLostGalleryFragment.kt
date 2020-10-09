package br.com.etecmatao.buscapet.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.adapter.GalleryImageAdapter
import br.com.etecmatao.buscapet.dto.Image
import br.com.etecmatao.buscapet.viewModel.GalleryViewModel
import br.com.etecmatao.buscapet.viewModel.NewPostViewModel
import kotlinx.android.synthetic.main.pet_lost_gallery_fragment.*

class PetLostGalleryFragment: Fragment() {

    private lateinit var vm: GalleryViewModel
    private lateinit var newPostVM: NewPostViewModel

    private lateinit var galleryAdapter: GalleryImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pet_lost_gallery_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
        newPostVM = ViewModelProvider(requireActivity()).get(NewPostViewModel::class.java)

        newPostVM.image.observe(viewLifecycleOwner, Observer {
            it?.let { image -> Log.i("Gallery", image) }
        })

        vm.images.observe(viewLifecycleOwner, Observer {
            it?.let { images ->
                galleryAdapter.setImages(images);
            }
        })

        galleryAdapter = GalleryImageAdapter(){
            val contentResolver = requireActivity().application.contentResolver

            val bytes = contentResolver.openInputStream(it.contentURI)!!.readBytes()

            newPostVM.updateImage(Base64.encodeToString(bytes, Base64.DEFAULT))
        }


        galleryItems.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter = galleryAdapter
        }

        btnGalleryContinue.setOnClickListener {
            findNavController().navigate(R.id.action_gallery_to_confirm_image)
        }

        openMediaStore()

        loadImages()
    }

    private fun loadImages(){
//        galleryList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
//        galleryList.add(Image("https://i.ibb.co/gM5NNJX/butterfly.jpg", "Butterfly"))
//        galleryList.add(Image("https://i.ibb.co/10fFGkZ/car-race.jpg", "Car Racing"))
//        galleryList.add(Image("https://i.ibb.co/ygqHsHV/coffee-milk.jpg", "Coffee with Milk"))
//        galleryList.add(Image("https://i.ibb.co/7XqwsLw/fox.jpg", "Fox"))
//        galleryList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
//        galleryList.add(Image("https://i.ibb.co/wc9rSgw/desserts.jpg", "Desserts Table"))
//        galleryList.add(Image("https://i.ibb.co/wdrdpKC/kitten.jpg", "Kitten"))
//        galleryList.add(Image("https://i.ibb.co/dBCHzXQ/paris.jpg", "Paris Eiffel"))
//        galleryList.add(Image("https://i.ibb.co/JKB0KPk/pizza.jpg", "Pizza Time"))
//        galleryList.add(Image("https://i.ibb.co/VYYPZGk/salmon.jpg", "Salmon "))
//        galleryList.add(Image("https://i.ibb.co/JvWpzYC/sunset.jpg", "Sunset in Beach"))
//
//        galleryAdapter.notifyDataSetChanged()
    }

    private fun openMediaStore() {
        if (haveStoragePermission()) {
            vm.loadImages()
        } else {
            requestPermission()
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 1001)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    vm.loadImages()
                }
                return
            }
        }
    }

    companion object{
        // gallery column count
        private const val SPAN_COUNT = 3
    }
}
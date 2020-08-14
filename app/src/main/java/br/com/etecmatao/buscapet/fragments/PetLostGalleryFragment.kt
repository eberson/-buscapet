package br.com.etecmatao.buscapet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import br.com.etecmatao.buscapet.R
import br.com.etecmatao.buscapet.adapter.GalleryImageAdapter
import br.com.etecmatao.buscapet.dto.Image
import kotlinx.android.synthetic.main.pet_lost_gallery_fragment.*

class PetLostGalleryFragment: Fragment() {

    private val galleryList = mutableListOf<Image>()
    lateinit var galleryAdapter: GalleryImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.pet_lost_gallery_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        galleryAdapter = GalleryImageAdapter(galleryList)
        galleryAdapter.listener = object : GalleryImageAdapter.GalleryImageClickListener {
            override fun onClick(position: Int) {
                TODO("Not yet implemented")
            }
        }

        galleryItems.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter = galleryAdapter
        }

        loadImages()
    }

    private fun loadImages(){
        galleryList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        galleryList.add(Image("https://i.ibb.co/gM5NNJX/butterfly.jpg", "Butterfly"))
        galleryList.add(Image("https://i.ibb.co/10fFGkZ/car-race.jpg", "Car Racing"))
        galleryList.add(Image("https://i.ibb.co/ygqHsHV/coffee-milk.jpg", "Coffee with Milk"))
        galleryList.add(Image("https://i.ibb.co/7XqwsLw/fox.jpg", "Fox"))
        galleryList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        galleryList.add(Image("https://i.ibb.co/wc9rSgw/desserts.jpg", "Desserts Table"))
        galleryList.add(Image("https://i.ibb.co/wdrdpKC/kitten.jpg", "Kitten"))
        galleryList.add(Image("https://i.ibb.co/dBCHzXQ/paris.jpg", "Paris Eiffel"))
        galleryList.add(Image("https://i.ibb.co/JKB0KPk/pizza.jpg", "Pizza Time"))
        galleryList.add(Image("https://i.ibb.co/VYYPZGk/salmon.jpg", "Salmon "))
        galleryList.add(Image("https://i.ibb.co/JvWpzYC/sunset.jpg", "Sunset in Beach"))

        galleryAdapter.notifyDataSetChanged()
    }

    companion object{
        // gallery column count
        private const val SPAN_COUNT = 3
    }
}
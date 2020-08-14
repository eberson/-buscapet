package br.com.etecmatao.buscapet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.etecmatao.buscapet.fragments.PetLostConfirmPictureFragment
import br.com.etecmatao.buscapet.fragments.PetLostGalleryFragment
import br.com.etecmatao.buscapet.fragments.PetLostSaveFragment

class PetLostRegister : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PetLostPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_lost_register)

        adapter = PetLostPageAdapter(this)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        adapter.fragmentByPosition(viewPager.currentItem).onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    private inner class PetLostPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){
        private val fragments : MutableList<Fragment> = mutableListOf(
            PetLostGalleryFragment(),
            PetLostConfirmPictureFragment(),
            PetLostSaveFragment()
        )

        fun fragmentByPosition(position: Int): Fragment = fragments[position]
        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}

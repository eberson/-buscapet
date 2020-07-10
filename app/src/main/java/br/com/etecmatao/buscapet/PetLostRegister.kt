package br.com.etecmatao.buscapet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.etecmatao.buscapet.fragments.PetLostConfirmPictureFragment
import br.com.etecmatao.buscapet.fragments.PetLostSaveFragment

class PetLostRegister : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_lost_register)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = PetLostPageAdapter(this)
    }

    private inner class PetLostPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            if (position == 2){
                return PetLostSaveFragment()
            }

            return PetLostConfirmPictureFragment()
        }

    }
}

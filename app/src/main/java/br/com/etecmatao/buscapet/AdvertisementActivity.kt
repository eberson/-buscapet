package br.com.etecmatao.buscapet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import br.com.etecmatao.buscapet.model.AdType
import br.com.etecmatao.buscapet.model.Advertisement
import br.com.etecmatao.buscapet.model.Chat
import br.com.etecmatao.buscapet.model.User
import br.com.etecmatao.buscapet.viewModel.AdvertisementViewModel
import kotlinx.android.synthetic.main.activity_advertisement.*

class AdvertisementActivity : AppCompatActivity() {
    lateinit var vm: AdvertisementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisement)

        vm = ViewModelProvider(this).get(AdvertisementViewModel::class.java)
    }

    fun publish(v:View){
        val adType = when(spAdType.selectedItem.toString()){
            "Anúncio" -> AdType.PET_ADVERTISEMENT
            "Doação" -> AdType.PET_DONATION
            "Animal Perdido" -> AdType.PET_LOST
            else -> AdType.UNKNOWN
        }

        val advertisement = Advertisement(
            title = txtTitle.text.toString(),
            description = txtDescription.text.toString(),
            type = adType,
            picture = mutableListOf()
        )

        vm.onSuccess = Runnable { this.finish() }
        vm.addAdvertisement(advertisement)
    }


}
package br.com.etecmatao.buscapet.model

import br.com.etecmatao.buscapet.R

enum class AdType {
    PET_LOST(R.drawable.ic_pet_lost),
    PET_DONATION(R.drawable.ic_pet_donation),
    PET_ADVERTISEMENT(R.drawable.ic_cao),
    UNKNOWN(R.drawable.ic_cao)
    ;

    var resourceImage: Int

    constructor(resourceImage: Int){
        this.resourceImage = resourceImage
    }
}
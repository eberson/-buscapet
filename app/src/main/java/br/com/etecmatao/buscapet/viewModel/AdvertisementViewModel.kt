package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.Advertisement
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdvertisementViewModel(application: Application): AndroidViewModel(application){
    val advertisements: MutableLiveData<List<Advertisement>> = MutableLiveData()

    var onSuccess: Runnable? = null

    init {
        val database = FirebaseDatabase.getInstance()

        database.getReference("advertisements").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Advertisement>()

                snapshot.children.forEach{
                    items.add(it.getValue(Advertisement::class.java)!!)
                }

                advertisements.value = items
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ADVERTISEMENT", error.message, error.toException())
            }
        })
    }

    fun addAdvertisement(advertisement: Advertisement) = viewModelScope.launch(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()

        database.reference
            .child("advertisements")
            .child(advertisement.id)
            .setValue(advertisement)
            .addOnSuccessListener { onSuccess?.run() }
    }

}
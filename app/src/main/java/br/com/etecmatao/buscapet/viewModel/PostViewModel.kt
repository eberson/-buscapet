package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.Advertisement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    val post: MutableLiveData<Advertisement> = MutableLiveData()
    val mayResolve: MutableLiveData<Boolean> = MutableLiveData(false)

    fun updateResolveStatus() = viewModelScope.launch(Dispatchers.IO) {
        val authenticatedUser = FirebaseAuth.getInstance().currentUser

        authenticatedUser?.let { usr ->
            post.value?.let {
                mayResolve.postValue(it.user?.email == usr.email)
            }
        }
    }

    fun loadPost(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()

        database.getReference("advertisements")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val ad = it.getValue(Advertisement::class.java)

                        ad?.let { currentAd ->
                            if (currentAd.id == id){
                                post.postValue(currentAd)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("POST_VM", error.message, error.toException())
                }

            })
    }

    fun markAsResolved(advertisement: Advertisement, onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        advertisement.done = true

        FirebaseDatabase.getInstance().reference
            .child("advertisements")
            .child(advertisement.id)
            .setValue(advertisement)
            .addOnCompleteListener {
                onSuccess()
            }

    }
}
package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.AdType
import br.com.etecmatao.buscapet.model.Advertisement
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPostViewModel(application: Application): AndroidViewModel(application) {
    val postType: MutableLiveData<AdType> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()
    val image: MutableLiveData<String> = MutableLiveData()

    fun updateImage(content: String) = viewModelScope.launch(Dispatchers.IO) {
        image.postValue(content)
    }

    fun loadCurrentUser() = viewModelScope.launch(Dispatchers.IO) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { loggedUser ->
            val usersReference = FirebaseDatabase.getInstance().getReference("users")

            usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.children.any()){
                        return
                    }

                    val storedUser = snapshot.children.map { e -> e.getValue(User::class.java) }.first {
                        it?.email ?: "" == loggedUser.email
                    }

                    user.value = storedUser
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HOME_VM", error.message, error.toException())
                }
            })
        }
    }

    fun publish(title: String, description: String, onSuccess: (advertisement: Advertisement) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val pictures = mutableListOf<String>()

        image.value?.let { pictures.add(it) }

        val advertisement = Advertisement(
            title = title,
            description = description,
            user = user.value,
            type = postType.value!!,
            picture = pictures
        )

        FirebaseDatabase.getInstance().reference
            .child("advertisements")
            .child(advertisement.id)
            .setValue(advertisement)
            .addOnCompleteListener { result ->
                if (result.isSuccessful){
                    onSuccess(advertisement)
                    return@addOnCompleteListener
                }

                result.exception?.let {
                    Log.e("POSTVM", it.message, it)
                }
            }
    }
}
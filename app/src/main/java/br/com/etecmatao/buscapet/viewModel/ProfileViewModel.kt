package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    val mayChangeProfile: MutableLiveData<Boolean> = MutableLiveData(false)
    var user: MutableLiveData<User> = MutableLiveData()

    fun startUpdatingProfile() = viewModelScope.launch(Dispatchers.IO) {
        mayChangeProfile.postValue(true)
    }

    fun endUpdatingProfile() = viewModelScope.launch(Dispatchers.IO) {
        mayChangeProfile.postValue(false)
    }

    fun saveProfile(user: User, onSuccess: (user: User) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
         var database = FirebaseDatabase.getInstance().reference

        database.child("users")
            .child(user.id)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    onSuccess(user)
                    return@addOnCompleteListener
                }

                it.exception?.let {e ->
                    Log.e("PROFILE-VM", e.message, e)
                }
            }
    }

    fun updatePassword(oldPassword: String, newPassword: String, onSuccess: () -> Unit, onInvalidPassword: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val auth = FirebaseAuth.getInstance()

        auth.currentUser?.let { user ->
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

            user.reauthenticate(credential).addOnCompleteListener { rea ->
                if (rea.isSuccessful){
                    user.updatePassword(newPassword).addOnCompleteListener {
                        if (it.isSuccessful){
                            onSuccess()
                        } else {
                            it.exception?.let {e ->
                                Log.e("PROFILE-VM", e.message, e)
                            }
                        }
                    }

                    return@addOnCompleteListener
                }

                onInvalidPassword()
            }
        }
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
}
package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.Advertisement
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsViewModel(application: Application): AndroidViewModel(application) {
    private val shouldFilter: MutableLiveData<Boolean> = MutableLiveData(true)

    val user: MutableLiveData<User> = MutableLiveData()
    val advertisements: MutableLiveData<List<Advertisement>> = MutableLiveData()
    val myAdvertisements: MutableLiveData<List<Advertisement>> = MutableLiveData()

    fun filterMyAdvertisements() = viewModelScope.launch(Dispatchers.IO) {
        if (!shouldFilter.value!!){
            return@launch
        }

        user.value?.let { usr ->
            advertisements.value?.let { items ->
                val filteredItems = items.filter { it.user?.id == usr.id}
                myAdvertisements.postValue(filteredItems)
            }
        }
    }

    fun loadAdvertisements() = viewModelScope.launch(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()

        database.getReference("advertisements").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shouldFilter.postValue(true)

                val items = mutableListOf<Advertisement>()
                val filteredItems = mutableListOf<Advertisement>()

                snapshot.children.forEach{
                    val item = it.getValue(Advertisement::class.java)!!

                    if (item.done){
                        return@forEach
                    }

                    user.value?.let { usr ->
                        shouldFilter.postValue(false)
                        if (usr.id == item.user?.id){
                            filteredItems.add(item)
                        }
                    }

                    items.add(item)
                }

                myAdvertisements.postValue(filteredItems)
                advertisements.value = items
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HOME_VM", error.message, error.toException())
            }
        })
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
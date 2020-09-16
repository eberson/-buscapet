package br.com.etecmatao.buscapet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.etecmatao.buscapet.model.Chat
import br.com.etecmatao.buscapet.model.Message
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    val user: MutableLiveData<User> = MutableLiveData()
    val messages: MutableLiveData<List<Message>> = MutableLiveData()

    fun loadMessages(chatID: String) = viewModelScope.launch(Dispatchers.IO) {
        val reference = FirebaseDatabase.getInstance().reference

        reference.child("messages").child(chatID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items: MutableList<Message> = mutableListOf()

                snapshot.children.forEach {
                    val message = it.getValue(Message::class.java)

                    message?.let { m ->
                        items.add(m)
                    }
                }

                messages.postValue(items)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CHAT-VM", error.message, error.toException())
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

    fun sendMessage(chatID: String, message: Message) = viewModelScope.launch(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()

        database.reference.child("messages").child(chatID).child(message.id).setValue(message)
    }


    fun initChat(chat: Chat) = viewModelScope.launch(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()

        database.reference.child("chats").child(chat.id).setValue(chat)
    }
}
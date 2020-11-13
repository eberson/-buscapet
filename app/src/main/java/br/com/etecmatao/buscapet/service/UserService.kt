package br.com.etecmatao.buscapet.service

import android.util.Log
import br.com.etecmatao.buscapet.model.Credential
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.database.FirebaseDatabase

class UserService {

    fun save(user: User, credential: Credential?, onSuccess: () -> Unit){
        val database = FirebaseDatabase.getInstance().reference

        val task = database.child("users").child(user.id).setValue(user)

        task.addOnSuccessListener {
            Log.d("USER", "user ${user.firstName} was successfully saved")

            if (credential == null){
                onSuccess()
                return@addOnSuccessListener
            }

            credential.signUp(onSuccess)

        }.addOnFailureListener {
            Log.e("USER", "error saving user ${user.firstName}", it)
        }
    }

    fun authenticate(email: String, password: String) {

    }

    companion object{
        val instance = UserService()
    }
}
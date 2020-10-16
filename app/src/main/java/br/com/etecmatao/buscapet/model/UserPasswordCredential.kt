package br.com.etecmatao.buscapet.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

data class UserPasswordCredential(
    var email: String,
    var password: String
) : Credential {
    override fun signUp(onComplete: () -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Log.d("USER_AUTH", "user for $email successfully created")
            onComplete()
        }.addOnFailureListener {
            Log.e("USER_AUTH", "failed creating $email user", it)
        }
    }

    override fun signIn() {

    }
}
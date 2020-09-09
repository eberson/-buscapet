package br.com.etecmatao.buscapet

import android.app.Application
import android.content.Context
import android.content.Intent
import br.com.etecmatao.buscapet.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MyApplication: Application() {
    fun isLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()

        return auth.currentUser != null
    }

    fun goToLogin(context: Context){
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}
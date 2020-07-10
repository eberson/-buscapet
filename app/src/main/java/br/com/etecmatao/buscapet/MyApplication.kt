package br.com.etecmatao.buscapet

import android.app.Application
import android.content.Context
import android.content.Intent
import br.com.etecmatao.buscapet.model.User

class MyApplication: Application() {
    var user: User? = null

    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun goToLogin(context: Context){
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}
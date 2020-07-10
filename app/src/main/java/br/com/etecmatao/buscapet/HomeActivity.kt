package br.com.etecmatao.buscapet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onResume() {
        super.onResume()

        val app = application as MyApplication

        if (!app.isLoggedIn()){
            app.goToLogin(this)
        }
    }

    fun startPublishing(v: View){
        val intent = Intent(this, PetLostRegister::class.java)
        startActivity(intent)
    }
}

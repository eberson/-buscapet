package br.com.etecmatao.buscapet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()
    }

    override fun onResume() {
        super.onResume()

        val app = application as MyApplication

        if (!app.isLoggedIn()){
            app.goToLogin(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                FirebaseAuth.getInstance().signOut()

                val app = application as MyApplication
                app.goToLogin(this)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpViews(){
        var controller = Navigation.findNavController(this, R.id.navigation_host)

        navigation.setupWithNavController(controller)
        setupActionBarWithNavController(controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navigation_host).navigateUp()
    }
}
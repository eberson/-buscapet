package br.com.etecmatao.buscapet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.etecmatao.buscapet.adapter.AdvertisementAdapter
import br.com.etecmatao.buscapet.fragments.MyPostsFragmentDirections
import br.com.etecmatao.buscapet.viewModel.AdvertisementViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        val adapter = AdvertisementAdapter(this){
//            val action = MyPostsFragmentDirections.actionMyPostsToPost(it.id)
//            findNavController().navigate(action)
//        }
//        val vm = ViewModelProvider(this).get(AdvertisementViewModel::class.java)
//
//        adItems.apply {
//            this.adapter = adapter
//            this.layoutManager = LinearLayoutManager(this@HomeActivity)
//        }
//
//        vm.advertisements.observe(this, Observer { items ->
//            items?.let {  adapter.addItems(it) }
//        })
    }

    override fun onResume() {
        super.onResume()

        val app = application as MyApplication

        if (!app.isLoggedIn()){
            app.goToLogin(this)
        }
    }

    fun startPublishing(v: View){
        val intent = Intent(this, AdvertisementActivity::class.java)
        startActivity(intent)
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
}

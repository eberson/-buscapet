package br.com.etecmatao.buscapet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.etecmatao.buscapet.repository.UserRepository
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun openRegister(v: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(v: View) {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        val user = UserRepository.instance.login(email, password)

        if (user == null) {
            Toast.makeText(
                this,
                getString(R.string.msg_invalid_credential),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        (application as MyApplication).user = user

        Toast.makeText(
            this,
            getString(R.string.msg_login_welcome, user.firstName),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}

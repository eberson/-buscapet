package br.com.etecmatao.buscapet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.etecmatao.buscapet.model.User
import br.com.etecmatao.buscapet.model.UserPasswordCredential
import br.com.etecmatao.buscapet.service.UserService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txtEmail
import kotlinx.android.synthetic.main.activity_login.txtPassword
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var googleClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        google_signin_button.setOnClickListener {
            googleLogin(it)
        }
    }

    fun openRegister(v: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun googleLogin(v: View) {
        val intent = googleClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun login(v: View) {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(
                this,
                getString(R.string.msg_login_welcome, it.user?.email),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                getString(R.string.msg_invalid_credential),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)!!

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val auth = FirebaseAuth.getInstance()

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful) {
                val user = auth.currentUser

                Toast.makeText(
                    this,
                    getString(R.string.msg_login_welcome, user?.displayName),
                    Toast.LENGTH_SHORT
                ).show()

                it.result?.user?.let { usr ->
                    val newUser = User(
                        firstName = usr.displayName!!,
                        lastName = "",
                        email = usr.email!!,
                        picture = ""
                    )

                    UserService.instance.save(newUser, null){
                        finish()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.msg_invalid_credential),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    companion object{
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 1010
        const val TAG = "LOGIN"
    }
}

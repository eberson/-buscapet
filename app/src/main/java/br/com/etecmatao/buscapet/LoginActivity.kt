package br.com.etecmatao.buscapet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import br.com.etecmatao.buscapet.biometrics.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val cipherTextWrapper
        get() = cryptographyManager.getCipherTextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHER_TEXT_WRAPPER
        )


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

        val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            use_biometrics.visibility = View.VISIBLE
            use_biometrics.setOnClickListener {
                if (cipherTextWrapper != null) {
                    showBiometricPromptForDecryption()
                }
            }
        } else {
            use_biometrics.visibility = View.INVISIBLE
        }
    }

    private fun showBiometricPromptForDecryption() {
        cipherTextWrapper?.let { textWrapper ->
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    this,
                    ::decryptServerTokenFromStorage
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        cipherTextWrapper?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext = cryptographyManager.decryptData(textWrapper.ciphertext, it)
                SampleAppUser.fakeToken = plaintext
            }
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

                finish()
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

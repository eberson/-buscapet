package br.com.etecmatao.buscapet

import android.app.Application
import br.com.etecmatao.buscapet.model.User

class MyApplication(): Application() {
    lateinit var user: User
}
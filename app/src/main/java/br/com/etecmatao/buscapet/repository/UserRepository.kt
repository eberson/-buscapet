package br.com.etecmatao.buscapet.repository

import br.com.etecmatao.buscapet.model.User
import java.lang.Exception

class UserRepository {
    private var users: MutableList<User> = mutableListOf()

    fun save(user: User){
        user.id = (users.size + 1).toLong()
        users.add(user)
    }

    fun login(email: String, password: String): User?{
        return try {
            users.first { it.email == email && it.password == password }
        } catch (e: Exception){
            null
        }
    }

    companion object{
        val instance = UserRepository()
    }
}
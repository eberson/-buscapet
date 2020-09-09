package br.com.etecmatao.buscapet.model

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    var firstName: String,
    var lastName: String,
    var email: String,
    var picture: String
){
    constructor(): this(firstName = "", lastName = "", email = "", picture = "")
}
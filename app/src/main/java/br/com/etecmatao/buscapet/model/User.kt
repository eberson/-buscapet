package br.com.etecmatao.buscapet.model

data class User(
    var id: Long?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String
)
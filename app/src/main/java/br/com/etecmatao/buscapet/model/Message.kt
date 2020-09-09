package br.com.etecmatao.buscapet.model

import java.util.*

data class Message(
    var id: String = UUID.randomUUID().toString(),
    var user: User,
    var date: Date = Date(),
    var text: String
)
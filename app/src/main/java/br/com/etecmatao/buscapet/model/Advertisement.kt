package br.com.etecmatao.buscapet.model

import java.util.*

data class Advertisement(
    var id: String = UUID.randomUUID().toString(),
    var user: User? = null,
    var title: String,
    var date: Date? = Date(),
    var description: String,
    var picture: List<String>,
    var type: AdType,
    var done: Boolean = false,
    var chat: String = UUID.randomUUID().toString()
){
    constructor(): this(title = "", description = "", picture = listOf<String>(), type = AdType.UNKNOWN)
}
package br.com.etecmatao.buscapet.model

interface Credential{
    fun signUp(onComplete: () -> Unit)
    fun signIn()
}
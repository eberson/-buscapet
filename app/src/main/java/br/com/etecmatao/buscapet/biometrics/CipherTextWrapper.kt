package br.com.etecmatao.buscapet.biometrics

data class CipherTextWrapper(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
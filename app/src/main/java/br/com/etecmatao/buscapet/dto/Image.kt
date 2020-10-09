package br.com.etecmatao.buscapet.dto

import android.net.Uri

data class Image (
    val contentURI: Uri,
    val id: String,
    val displayName: String
)
package com.nyan.data.util

data class HttpRequest(val path: String, val method: HttpMethod)

enum class HttpMethod { GET, POST, DELETE, PUT }
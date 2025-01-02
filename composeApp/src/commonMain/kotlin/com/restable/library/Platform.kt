package com.restable.library

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
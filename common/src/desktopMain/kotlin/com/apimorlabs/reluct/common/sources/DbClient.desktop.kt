package com.apimorlabs.reluct.common.sources

internal actual class DbClient {
    actual fun platformName(): String = "Java ${System.getProperty("java.version")}"
}
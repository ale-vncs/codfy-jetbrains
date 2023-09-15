package com.ale.vncs.codfy.utils

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe

object SpotifyTokens {
    private val appName = Constants.APP_NAME
    private var credentialAttributesAccessKey = createCredentialAttributes("ACCESS_KEY")
    private var credentialAttributesRefreshToken = createCredentialAttributes("REFRESH_TOKEN")

    data class Tokens(val accessToken: String?, val refreshToken: String?)

    fun getTokens(): Tokens {
        val accessKey = PasswordSafe.instance.getPassword(credentialAttributesAccessKey)
        val refreshToken = PasswordSafe.instance.getPassword(credentialAttributesRefreshToken)
        return Tokens(accessKey, refreshToken)
    }

    fun setTokens(accessToken: String, refreshToken: String) {
        val credentialsAccessKey = Credentials(appName, accessToken)
        val credentialsRefreshToken = Credentials(appName, refreshToken)
        PasswordSafe.instance.set(credentialAttributesAccessKey, credentialsAccessKey)
        PasswordSafe.instance.set(credentialAttributesRefreshToken, credentialsRefreshToken)
    }

    fun removeTokens() {
        PasswordSafe.instance.set(credentialAttributesAccessKey, null)
        PasswordSafe.instance.set(credentialAttributesRefreshToken, null)
    }

    private fun createCredentialAttributes(key: String): CredentialAttributes {
        return CredentialAttributes(
            generateServiceName(appName, key)
        )
    }
}


package com.karthik.pro.engr.github.api.android_interview_mastery.data.auth

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.karthik.pro.engr.github.api.android_interview_mastery.data.security.AesGcmEncryptor
import com.karthik.pro.engr.github.api.android_interview_mastery.data.security.EncryptedPayload
import com.karthik.pro.engr.github.api.android_interview_mastery.data.security.MissingEncryptionKeyException
import com.karthik.pro.engr.github.api.android_interview_mastery.data.security.AndroidKeystoreKeyManager
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStore
import com.karthik.pro.engr.github.api.android_interview_mastery.domain.model.auth.TokenPair
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.AEADBadTagException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.secureTokenDataStore by preferencesDataStore(
    name = "secure_token_store"
)

@Serializable
private data class TokenPairPayload(
    val accessToken: String,
    val refreshToken: String
)

@Singleton
class SecureTokenStoreImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val encryptor: AesGcmEncryptor,
    private val keyManager: AndroidKeystoreKeyManager
) : TokenStore {

    private val dataStore = context.secureTokenDataStore

    private val json = Json

    @Volatile
    private var tokenPair: TokenPair? = null

    @Volatile
    private var initialized = false

    companion object {
        private val ENCRYPTED_TOKENS =
            stringPreferencesKey("encrypted_token_pair")

        private const val FORMAT_VERSION = "1"
    }

    /**
     * Call and await this before starting authenticated requests.
     */
    suspend fun initialize() {
        check(!initialized) {
            "TokenStore has already been initialized"
        }

        val preferences = dataStore.data.first()
        val stored = preferences[ENCRYPTED_TOKENS]

        if (stored == null) {
            tokenPair = null
            initialized = true
            return
        }

        try {
            val payload = decodePayload(stored)
            val plaintext = encryptor.decrypt(payload)
            val dto = json.decodeFromString<TokenPairPayload>(
                plaintext.decodeToString()
            )

            tokenPair = TokenPair(
                accessToken = dto.accessToken,
                refreshToken = dto.refreshToken
            )
            initialized = true
        } catch (
            ex: MissingEncryptionKeyException
        ) {
            recoverUnusableCredentials()
        } catch (
            ex: AEADBadTagException
        ) {
            recoverUnusableCredentials()
        } catch (
            ex: SerializationException
        ) {
            recoverUnusableCredentials()
        } catch (
            ex: IllegalArgumentException
        ) {
            recoverUnusableCredentials()
        }
    }

    @Synchronized
    override fun getAccessToken(): String? {
        check(initialized) {
            "TokenStore must be initialized before use"
        }
        return tokenPair?.accessToken
    }

    @Synchronized
    override fun getRefreshToken(): String? {
        check(initialized) {
            "TokenStore must be initialized before use"
        }
        return tokenPair?.refreshToken
    }

    /**
     * This synchronous method blocks until persistence succeeds.
     * Never call it from the main thread.
     */
    @Synchronized
    override fun saveTokens(tokenPair: TokenPair) {
        check(initialized) {
            "TokenStore must be initialized before use"
        }

        require(
            tokenPair.accessToken.isNotBlank() &&
                tokenPair.refreshToken.isNotBlank()
        ) {
            "Tokens must not be blank"
        }

        val dto = TokenPairPayload(
            accessToken = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken
        )

        val plaintext = json.encodeToString(dto)
            .toByteArray(Charsets.UTF_8)

        val encrypted = encryptor.encrypt(plaintext)
        val encoded = encodePayload(encrypted)

        // Persist first. Publish the new memory snapshot only on success.
        runBlocking(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[ENCRYPTED_TOKENS] = encoded
            }
        }

        this.tokenPair = tokenPair
    }

    @Synchronized
    override fun clearTokens() {
        check(initialized) {
            "TokenStore must be initialized before use"
        }

        runBlocking(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(ENCRYPTED_TOKENS)
            }
        }

        tokenPair = null
    }


    private suspend fun recoverUnusableCredentials() {
        // Remove the encrypted token payload from DataStore.
        dataStore.edit { preferences ->
            preferences.remove(ENCRYPTED_TOKENS)
        }

        // The stored credentials are no longer usable.
        tokenPair = null

        // Initialization is complete only after the removal succeeds.
        initialized = true
    }


    private fun encodePayload(payload: EncryptedPayload): String {
        val nonce = Base64.encodeToString(
            payload.nonce,
            Base64.NO_WRAP
        )
        val ciphertext = Base64.encodeToString(
            payload.ciphertext,
            Base64.NO_WRAP
        )

        return "$FORMAT_VERSION:$nonce:$ciphertext"
    }

    private fun decodePayload(value: String): EncryptedPayload {
        val parts = value.split(":", limit = 3)

        if (parts.size != 3 || parts[0] != FORMAT_VERSION) {
            throw IllegalArgumentException(
                "Unsupported encrypted token format"
            )
        }

        return EncryptedPayload(
            nonce = Base64.decode(parts[1], Base64.NO_WRAP),
            ciphertext = Base64.decode(parts[2], Base64.NO_WRAP)
        )
    }
}

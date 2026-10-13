
package com.karthik.pro.engr.github.api.android_interview_mastery.data.security

import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AesGcmEncryptor @Inject constructor(
    private val keyManager: AndroidKeystoreKeyManager
) {

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_SIZE_BITS = 128

        private val ASSOCIATED_DATA =
            "android-interview-mastery:token-payload:v1"
                .toByteArray(Charsets.UTF_8)
    }

    fun encrypt(plaintext: ByteArray): EncryptedPayload {
        require(plaintext.isNotEmpty()) {
            "Plaintext must not be empty"
        }

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            keyManager.getOrCreateKey()
        )

        val nonce = requireNotNull(cipher.iv) {
            "Encryption provider did not generate a nonce"
        }

        val ciphertext = cipher.run {
            updateAAD(ASSOCIATED_DATA)
            doFinal(plaintext)
        }

        return EncryptedPayload(
            nonce = nonce,
            ciphertext = ciphertext
        )
    }

    fun decrypt(payload: EncryptedPayload): ByteArray {
        if (payload.nonce.size != 12) {
            throw GeneralSecurityException(
                "Invalid AES-GCM nonce length"
            )
        }

        if (payload.ciphertext.size < 16) {
            throw GeneralSecurityException(
                "Encrypted payload is too short"
            )
        }

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            keyManager.getOrCreateKey(),
            GCMParameterSpec(
                GCM_TAG_SIZE_BITS,
                payload.nonce
            )
        )

        cipher.updateAAD(ASSOCIATED_DATA)

        // Authentication-tag verification happens inside doFinal().
        // If authentication fails, decryption throws an exception.
        return cipher.doFinal(payload.ciphertext)
    }
}

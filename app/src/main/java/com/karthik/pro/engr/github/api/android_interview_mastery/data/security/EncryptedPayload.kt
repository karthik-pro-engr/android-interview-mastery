
package com.karthik.pro.engr.github.api.android_interview_mastery.data.security

data class EncryptedPayload(
    val nonce: ByteArray,
    val ciphertext: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedPayload

        if (!nonce.contentEquals(other.nonce)) return false
        if (!ciphertext.contentEquals(other.ciphertext)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nonce.contentHashCode()
        result = 31 * result + ciphertext.contentHashCode()
        return result
    }
}

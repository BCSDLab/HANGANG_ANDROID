package `in`.hangang.core.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object HashGeneratorUtil {
    fun generateMD5(message: String): String? {
        return hashString(message, "MD5")
    }

    fun generateSHA256(message: String): String? {
        return hashString(message, "SHA-256")
    }

    private fun hashString(message: String, algorithm: String): String? {
        try {
            val digest = MessageDigest.getInstance(algorithm)
            digest.update(message.toByteArray())
            val hashedBytes = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (hashedByte in hashedBytes) {
                val h = StringBuilder(Integer.toHexString(0xFF and hashedByte.toInt()))
                while (h.length < 2) h.insert(0, "0")
                hexString.append(h)
            }
            return hexString.toString()
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }
        return ""
    }
}
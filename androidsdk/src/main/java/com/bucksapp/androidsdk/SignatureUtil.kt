package com.bucksapp.androidsdk

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object SignatureUtil {
    private fun signatureDigest(sig: Signature): String? {
        val signature = sig.toByteArray()
        return try {
            val md = MessageDigest.getInstance("SHA1")
            val digest = md.digest(signature)
            val hexBuilder = StringBuilder();
            for(b in digest){
                hexBuilder.append(String.format("%02x:", b));
            }
            val hexSignature = hexBuilder.toString().toUpperCase();
            hexSignature.dropLast(1)
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }


    private fun signatureDigest(sigList: Array<Signature>): List<String?> {
        val signaturesList: MutableList<String?> = ArrayList()
        for (signature in sigList) {
            if (signature != null) {
                signaturesList.add(signatureDigest(signature))
            }
        }
        return signaturesList
    }

    fun getSignatures(pm: PackageManager, packageName: String): List<String?>? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo =
                    pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                if (packageInfo == null
                    || packageInfo.signingInfo == null
                ) {
                    return null
                }
                if (packageInfo.signingInfo.hasMultipleSigners()) {
                    signatureDigest(packageInfo.signingInfo.apkContentsSigners)
                } else {
                    signatureDigest(packageInfo.signingInfo.signingCertificateHistory)
                }
            } else {
                @SuppressLint("PackageManagerGetSignatures") val packageInfo =
                    pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                if (packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.size == 0 || packageInfo.signatures[0] == null) {
                    null
                } else signatureDigest(packageInfo.signatures)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}
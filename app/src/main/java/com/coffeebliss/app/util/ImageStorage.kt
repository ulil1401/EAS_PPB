package com.coffeebliss.app.util

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageStorage {

    suspend fun saveProfilePhoto(context: Context, memberId: Long, sourceUri: Uri): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val dir = File(context.filesDir, "profiles").apply { mkdirs() }
                val file = File(dir, "member_$memberId.jpg")
                context.contentResolver.openInputStream(sourceUri)?.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                } ?: return@withContext Result.failure(IllegalStateException("Gagal membaca foto"))

                Result.success(file.absolutePath)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun deletePhoto(path: String?) {
        if (path.isNullOrBlank()) return
        File(path).takeIf { it.exists() }?.delete()
    }
}

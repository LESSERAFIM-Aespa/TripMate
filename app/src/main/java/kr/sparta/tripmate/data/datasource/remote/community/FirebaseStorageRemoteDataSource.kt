package kr.sparta.tripmate.data.datasource.remote.community

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 작성자: 서정한
 * 내용: Firbase Storage에 Image를 저장
 * */
class FirebaseStorageRemoteDataSource {
    private fun getReference(value: String) = Firebase.storage.reference.child(value)

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 글작성 페이지에서
     * 이미지를 FireStore에 업로드한다.
     * */
    suspend fun uploadImage(
        imgName: String,
        image: Bitmap,
    ): String {
        val imageRef = getReference("$imgName.jpg")
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        val result = suspendCoroutine<String> { continuation ->
            uploadTask.addOnFailureListener {
                Log.e("TripMates", "upload Image Error: ${it.toString()}")
            }.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    // 발행
                    continuation.resume(url.toString())
                }
            }
        }
        return result
    }
}
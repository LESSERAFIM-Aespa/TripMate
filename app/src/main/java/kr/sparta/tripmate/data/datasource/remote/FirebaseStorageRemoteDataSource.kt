package kr.sparta.tripmate.data.datasource.remote

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.ByteArrayOutputStream

/**
 * 작성자: 서정한
 * 내용: Firbase Storage에 Image를 저장
 * */
class FirebaseStorageRemoteDataSource {
    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 글작성 페이지에서
     * 이미지를 FireStore에 업로드한다.
     * */
    fun uploadImage(
        imgName: String,
        image: Bitmap,
        publishUri: PublishSubject<String>,
    ) {
        val storage = Firebase.storage

        val imageRef = storage.reference.child("$imgName.jpg")
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Log.e("TripMates", "upload Image Error: ${it.toString()}")
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { url ->
                // 발행
                publishUri.onNext(url.toString())
            }
        }
    }
}
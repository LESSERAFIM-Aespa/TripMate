package kr.sparta.tripmate.domain.repository

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * 작성자: 서정한
 * 내용: FirebaseStorage Repository
 * */
interface FirebaseStorageRepository {
    fun uploadImage(
        imgName: String,
        image: Bitmap,
        publishUri: PublishSubject<String>,
    )
}
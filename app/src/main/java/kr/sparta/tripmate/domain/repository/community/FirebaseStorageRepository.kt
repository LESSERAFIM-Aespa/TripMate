package kr.sparta.tripmate.domain.repository.community

import android.graphics.Bitmap

/**
 * 작성자: 서정한
 * 내용: FirebaseStorage Repository
 * */
interface FirebaseStorageRepository {
   suspend fun uploadImage(
        imgName: String,
        image: Bitmap,
    ):String
}
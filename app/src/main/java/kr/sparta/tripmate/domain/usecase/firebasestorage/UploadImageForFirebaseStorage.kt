package kr.sparta.tripmate.domain.usecase.firebasestorage

import android.graphics.Bitmap
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository
import javax.inject.Inject

class UploadImageForFirebaseStorage @Inject constructor(private val repository: FirebaseStorageRepository) {
    suspend operator fun invoke(
        imgName: String,
        image: Bitmap,
    ): String = repository.uploadImage(
        imgName = imgName,
        image = image,
    )
}
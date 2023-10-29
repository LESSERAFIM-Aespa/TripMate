package kr.sparta.tripmate.domain.usecase.firebasestorage

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject
import kr.sparta.tripmate.domain.repository.FirebaseStorageRepository

class UploadImageForFirebaseStorage(private val repository: FirebaseStorageRepository) {
    operator fun invoke(
        imgName: String,
        image: Bitmap,
        publishSubject: PublishSubject<String>
    ) = repository.uploadImage(
        imgName = imgName,
        image = image,
        publishUri = publishSubject,
    )
}
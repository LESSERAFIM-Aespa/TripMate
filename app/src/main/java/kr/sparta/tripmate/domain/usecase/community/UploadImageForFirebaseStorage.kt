package kr.sparta.tripmate.domain.usecase.community

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository

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
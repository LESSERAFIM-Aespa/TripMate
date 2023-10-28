package kr.sparta.tripmate.domain.repository.community

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject

interface FirebaseStorageRepository {
    fun uploadImage(
        imgName: String,
        image: Bitmap,
        publishUri: PublishSubject<String>,
    )
}
package kr.sparta.tripmate.data.repository

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject
import kr.sparta.tripmate.data.datasource.remote.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.domain.repository.FirebaseStorageRepository

class FirebaseStorageRepositoryImpl(private val remoteDataSource: FirebaseStorageRemoteDataSource) :
    FirebaseStorageRepository {
    override fun uploadImage(
        imgName: String,
        image: Bitmap,
        publishUri: PublishSubject<String>
    ) =
        remoteDataSource.uploadImage(
            imgName,
            image,
            publishUri
        )
}
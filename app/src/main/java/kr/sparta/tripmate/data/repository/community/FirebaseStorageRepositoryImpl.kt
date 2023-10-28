package kr.sparta.tripmate.data.repository.community

import android.graphics.Bitmap
import io.reactivex.rxjava3.subjects.PublishSubject
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository

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
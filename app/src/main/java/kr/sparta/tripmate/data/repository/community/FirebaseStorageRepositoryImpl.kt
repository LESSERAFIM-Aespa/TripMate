package kr.sparta.tripmate.data.repository.community

import android.graphics.Bitmap
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository

/**
 * 작성자: 서정한
 * 내용: FirebaseStorage Repository
 * */
class FirebaseStorageRepositoryImpl(private val remoteDataSource: FirebaseStorageRemoteDataSource) :
    FirebaseStorageRepository {
    override suspend fun uploadImage(
        imgName: String,
        image: Bitmap,
    ): String =
        remoteDataSource.uploadImage(
            imgName,
            image,
        )
}
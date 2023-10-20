package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class FirebaseScrapRepositoryImpl(private val remoteSource: FirebaseDBRemoteDataSource) : FirebaseScrapRepository {
    override fun getScrapedData(uid: String, liveData: MutableLiveData<List<ScrapEntity>>) = remoteSource.getScrapedData(uid, liveData)
}
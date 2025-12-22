package kr.sparta.tripmate.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity
import kr.sparta.tripmate.domain.repository.BlogRepository
import kr.sparta.tripmate.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(private val repository: BlogRepository) {
    suspend operator fun invoke(query: String, sort: String = "sim"): Flow<ImageServerDataEntity> =
        repository.getImage(query, sort)

}
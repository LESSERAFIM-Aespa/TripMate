package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity
import kr.sparta.tripmate.domain.repository.ImageRepository

class GetImageUseCase (private val repository: ImageRepository) {
    suspend operator fun invoke(query: String, sort: String = "sim"): ImageServerDataEntity =
        repository.getImage(query, sort)

}
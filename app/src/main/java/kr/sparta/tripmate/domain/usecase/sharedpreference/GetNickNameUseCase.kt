package kr.sparta.tripmate.domain.usecase.sharedpreference

import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import javax.inject.Inject

class GetNickNameUseCase @Inject constructor(
    private val sharedPreferenceReopository: SharedPreferenceReopository
) {
    operator fun invoke() : String = sharedPreferenceReopository.getNickName()
}
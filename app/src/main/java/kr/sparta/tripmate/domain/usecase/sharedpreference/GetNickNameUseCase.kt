package kr.sparta.tripmate.domain.usecase.sharedpreference

import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository

class GetNickNameUseCase(
    private val sharedPreferenceReopository: SharedPreferenceReopository
) {
    operator fun invoke() : String = sharedPreferenceReopository.getNickName()
}
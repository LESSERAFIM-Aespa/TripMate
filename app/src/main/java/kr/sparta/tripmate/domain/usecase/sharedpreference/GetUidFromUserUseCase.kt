package kr.sparta.tripmate.domain.usecase.sharedpreference

import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository

class GetUidFromUserUseCase(
    private val sharedPreferenceReopository: SharedPreferenceReopository,
) {
    operator fun invoke(): String = sharedPreferenceReopository.getUidFromUser()
}
package kr.sparta.tripmate.domain.usecase.sharedpreference

import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository

class SaveProfileUseCase(
    private val sharedPreferenceReopository: SharedPreferenceReopository,
) {
    operator fun invoke(profile: String) {
        sharedPreferenceReopository.saveProfile(profile)
    }
}
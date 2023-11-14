package kr.sparta.tripmate.domain.usecase.sharedpreference

import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository

class SaveUidFromUserUseCase(
    private val sharedPreferenceReopository: SharedPreferenceReopository,
) {
    operator fun invoke(uidFromUser: String) {
        sharedPreferenceReopository.saveUidFromUser(uidFromUser)
    }
}
package kr.sparta.tripmate.data.repository.sharedpreference

import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository

class SharedPreferenceReopositoryImpl(
    private val sharedPreferencesDataSource: SharedPreferencesLocalDataSource,
) : SharedPreferenceReopository {
    override fun saveUid(uid: String) {
        sharedPreferencesDataSource.saveUid(uid)
    }

    override fun getUid(): String {
        return sharedPreferencesDataSource.getUid()
    }

    override fun saveProfile(profile: String) {
        sharedPreferencesDataSource.saveProfile(profile)
    }

    override fun getProfile(): String {
        return sharedPreferencesDataSource.getProfile()
    }

    override fun saveNickName(nickName: String) {
        sharedPreferencesDataSource.saveNickName(nickName)
    }

    override fun getNickName(): String {
        return sharedPreferencesDataSource.getNickName()
    }

    override fun saveUidFromUser(uidFromUser: String) {
        sharedPreferencesDataSource.saveUidFromUser(uidFromUser)
    }

    override fun getUidFromUser(): String {
        return sharedPreferencesDataSource.getUidFromUser()
    }

    override fun removeKey() {
        sharedPreferencesDataSource.removeKey()
    }

}
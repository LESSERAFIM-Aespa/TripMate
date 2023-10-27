package kr.sparta.tripmate.ui.viewmodel.community.write

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommunityWriteData
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage

class CommunityWriteViewModel(
    private val updateCommunityWriteData: UpdateCommunityWriteData,
    private val uploadImageForFirebaseStorage: UploadImageForFirebaseStorage,
    private val getCommunityKeyUseCase: GetCommunityKeyUseCase,
) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 이벤트처리를위한 PublishSubject
    val publishSubject: PublishSubject<CommunityModelEntity> = PublishSubject.create()

    /**
     * 작성자: 서정한
     * 내용: 현재 로딩상태를 변경하는데 사용
     * */
    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    /**
     * 작성자: 서정한
     * 내용: RDB에 저장할 Unique Key 획득
     * */
    fun getCommunityKey(): String = getCommunityKeyUseCase.invoke()

    /**
     * 작성자: 서정한
     * 내용: 글이 있으면 업데이트. 없으면 생성.
     * */
    @SuppressLint("CheckResult")
    fun updateCommunityWrite(imgName: String, image: Bitmap?, item: CommunityModelEntity) {
        // Observable 생성
        val getImageUrlSubject: PublishSubject<String> = PublishSubject.create()

        // 이미지 없는경우
        if(image == null){
            // 글 업로드
            updateCommunityWriteData.invoke(item)

            // 디테일페이지에 업데이트할 아이템 발행
            publishSubject.onNext(item)
            return@updateCommunityWrite
        }

        // 이미지 Storage업로드 요청
        uploadImageForFirebaseStorage.invoke(
            imgName = imgName,
            image = image,
            publishSubject = getImageUrlSubject,
        )

        // 이미지 Url 결과값 받기
        getImageUrlSubject.subscribeBy(
            onNext = {
                val newItem = item.copy(
                    addedImage = it
                )

                // 글 업로드
                updateCommunityWriteData.invoke(newItem)

                // 디테일페이지에 업데이트할 아이템 발행
                publishSubject.onNext(newItem)

                // 아이템 발행 중지
                getImageUrlSubject.onComplete()
            },
            onError = {
                it.printStackTrace()
            },
            onComplete = {
                Log.d("TripMates", "Complete WriteViewModel")
            }
        )
    }
}

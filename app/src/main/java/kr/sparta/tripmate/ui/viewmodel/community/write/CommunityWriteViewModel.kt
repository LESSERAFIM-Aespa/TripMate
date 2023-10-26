package kr.sparta.tripmate.ui.viewmodel.community.write

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    /**
     * 작성자: 서정한
     * 내용: 현재 로딩상태를 변경하는데 사용
     * */
    fun setLoadingState(isLoading:Boolean) {
        _isLoading.value = isLoading
    }

    /**
     * 작성자: 서정한
     * 내용: RDB에 저장할 Unique Key 획득
     * */
    fun getCommunityKey() : String = getCommunityKeyUseCase.invoke()

    /**
     * 작성자: 서정한
     * 내용: 글이 있으면 업데이트. 없으면 생성.
     * */
    @SuppressLint("CheckResult")
    fun updateCommunityWrite(imgName: String, image: Bitmap, item: CommunityModelEntity) {
        if (imgName.isNotEmpty() && image != null) {
            val subject: PublishSubject<String> = PublishSubject.create()
            uploadImageForFirebaseStorage.invoke(
                imgName = imgName,
                image = image,
                publishSubject = subject,
            )
            // 이미지 Url 결과값 받기
            subject.subscribe(
                { value ->
                    updateCommunityWriteData.invoke(
                        item.copy(
                            addedImage = value
                        )
                    )
                    setLoadingState(false)
                },
                { error ->
                    Log.e("TripMates", "error by CommunityWriteViewModel: ${error.toString()}")
                },
                {
                    Log.d("TripMates", "Complete Rx_kotlin")
                    subject.onComplete()
                },
            )
            return
        } else {
            // 이미지 링크없이 전송
            updateCommunityWriteData.invoke(item)
            setLoadingState(false)
        }
    }
}

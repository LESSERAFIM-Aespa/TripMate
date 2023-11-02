package kr.sparta.tripmate.ui.viewmodel.community.write

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.AddBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.util.method.isWindowTouchable

class CommunityWriteViewModel(
    private val addBoardUseCase: AddBoardUseCase,
    private val uploadImageForFirebaseStorage: UploadImageForFirebaseStorage,
    private val getCommunityKeyUseCase: GetCommunityKeyUseCase,
) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 이벤트처리를위한 PublishSubject
    val publishSubject: PublishSubject<CommunityEntity> = PublishSubject.create()

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
    fun updateCommunityWrite(imgName: String, image: Bitmap?, item: CommunityEntity, context: Context) {

        CoroutineScope(Dispatchers.Main).launch {
            // 이미지 없는경우
            if (image == null) {
                // 글 업로드
                addBoardUseCase.invoke(item)

                // 로딩 중지
                setLoadingState(false)

                // 업로드 완료후 업데이트된 model DetailPage에 전달
                val intent =
                    CommunityDetailActivity.newIntentForEntity(context, item)
                (context as Activity).setResult(RESULT_OK, intent)

                // 화면터치 해제
                isWindowTouchable(context, false)
                (context as Activity).finish()
                return@launch
            }

            // 이미지 Storage업로드 요청
            val url = uploadImageForFirebaseStorage.invoke(
                imgName = imgName,
                image = image,
            )

            val newItem = item.copy(
                image = url
            )

            // 글 업로드
            addBoardUseCase.invoke(newItem)

            // 로딩 중지
            setLoadingState(false)

            // 업로드 완료후 업데이트된 model DetailPage에 전달
            val intent =
                CommunityDetailActivity.newIntentForEntity(context, item)
            (context as Activity).setResult(RESULT_OK, intent)

            // 화면터치 해제
            isWindowTouchable(context, false)
            (context as Activity).finish()
        }
    }
}

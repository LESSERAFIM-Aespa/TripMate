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
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.util.method.isWindowTouchable

class CommunityWriteViewModel(
    private val addBoardUseCase: AddBoardUseCase,
    private val uploadImageForFirebaseStorage: UploadImageForFirebaseStorage,
    private val getCommunityKeyUseCase: GetCommunityKeyUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase
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
    private fun updateItem(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context,
        useCase: (CommunityEntity) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (image == null) {
                useCase.invoke(item)
                setLoadingState(false)

                isWindowTouchable(context, false)
                (context as Activity).finish()
                return@launch
            }


            val url = uploadImageForFirebaseStorage.invoke(
                imgName = imgName,
                image = image
            )
            val newItem = item.copy(image = url)

            useCase.invoke(newItem)

            setLoadingState(false)

            if (useCase == updateBoardUseCase::invoke) {
                val intent = CommunityDetailActivity.newIntentForEntity(context, newItem)
                (context as Activity).setResult(RESULT_OK, intent)
            }

            isWindowTouchable(context, false)
            (context as Activity).finish()
        }
    }

    fun addCommunityWrite(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context
    ) {
        updateItem(imgName, image, item, context, addBoardUseCase::invoke)
    }

    fun updateCommunityWrite(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context
    ) {
        updateItem(imgName, image, item, context, updateBoardUseCase::invoke)
    }
}

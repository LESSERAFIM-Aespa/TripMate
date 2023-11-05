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

    private val _isAddLoading = MutableLiveData<Boolean>()
    val isAddLoading: LiveData<Boolean> get() = _isAddLoading
    private val _isEditLoading = MutableLiveData<Boolean>()
    val isEditLoading : LiveData<Boolean> get() = _isEditLoading

    // 이벤트처리를위한 PublishSubject
    val publishSubject: PublishSubject<CommunityEntity> = PublishSubject.create()

    /**
     * 작성자: 서정한
     * 내용: 현재 로딩상태를 변경하는데 사용
     * */
    fun setAddLoadingState(isAddLoading: Boolean) {
        _isAddLoading.value = isAddLoading
    }
    fun setEditLoadingState(isEditLoading:Boolean){
        _isEditLoading.value = isEditLoading
    }

    /**
     * 작성자: 서정한
     * 내용: RDB에 저장할 Unique Key 획득
     * */
    fun getCommunityKey(): String = getCommunityKeyUseCase.invoke()

    /**
     * 작성자 : 박성수
     * 재활용 함수 (수정 후 업데이트 및 게시글 추가 할 때 공용으로 사용합니다.)
     * 수정&추가 데이터를 RDB에 저장합니다.
     */
    private fun updateCommunityWrite(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context,
        useCase: (CommunityEntity) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (image == null) {
                useCase.invoke(item)
                if(useCase == addBoardUseCase::invoke){
                    setAddLoadingState(false)
                } else setEditLoadingState(false)

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
            if(useCase == addBoardUseCase::invoke){
                setAddLoadingState(false)
            }else setEditLoadingState(false)

            if (useCase == updateBoardUseCase::invoke) {
                val intent = CommunityDetailActivity.newIntentForEntity(context, newItem)
                (context as Activity).setResult(RESULT_OK, intent)
            }

            isWindowTouchable(context, false)
            (context as Activity).finish()
        }
    }
    /**
     * 작성자: 서정한
     * 내용: 게시글을 추가 했을때 업데이트 합니다.
     * */
    fun addCommunityWrite(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context
    ) {
        updateCommunityWrite(imgName, image, item, context, addBoardUseCase::invoke)
    }

    /**작성자 : 박성수
     * 내용 : 게시글을 수정 했을 때 업데이트 합니다.
     */

    fun updateCommunityWrite(
        imgName: String,
        image: Bitmap?,
        item: CommunityEntity,
        context: Context
    ) {
        updateCommunityWrite(imgName, image, item, context, updateBoardUseCase::invoke)
    }
}

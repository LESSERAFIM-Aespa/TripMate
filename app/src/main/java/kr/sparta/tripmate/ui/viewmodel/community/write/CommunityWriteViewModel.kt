package kr.sparta.tripmate.ui.viewmodel.community.write

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.usecase.community.UploadImageForFirebaseStorage
import kr.sparta.tripmate.domain.usecase.community.board.AddBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.board.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemUseCase

class CommunityWriteViewModel(
    private val uploadImageForFirebaseStorage: UploadImageForFirebaseStorage,
    private val updateBoardItemUseCase: UpdateBoardItemUseCase,
    private val getCommunityKeyUseCase: GetCommunityKeyUseCase,
    private val addBoardItemUseCase: AddBoardItemUseCase,
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
     * 내용: 게시글 생성.
     * */
    suspend fun addCommunityWrite(imgName: String, image: Bitmap?, item: CommunityEntity) {
        viewModelScope.launch {
            // Observable 생성
            val getImageUrlSubject: PublishSubject<String> = PublishSubject.create()

            // 이미지 없는경우
            if (image == null) {
                // 글 업로드
                addBoardItemUseCase.invoke(item)

                // 디테일페이지에 업데이트할 아이템 발행
                publishSubject.onNext(item)
                return@launch
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
                    viewModelScope.launch {
                        val newItem = item.copy(
                            image = it
                        )

                        // 글 업로드
                        addBoardItemUseCase.invoke(newItem)

                        // 디테일페이지에 업데이트할 아이템 발행
                        publishSubject.onNext(newItem)

                        // 아이템 발행 중지
                        getImageUrlSubject.onComplete()
                    }
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

    /**
     * 작성자: 서정한
     * 내용: 게시글 수정
     * */
    @SuppressLint("CheckResult")
    suspend fun updateCommunityWrite(imgName: String, image: Bitmap?, item: CommunityEntity) {
        viewModelScope.launch {
            // Observable 생성
            val getImageUrlSubject: PublishSubject<String> = PublishSubject.create()

            // 이미지 없는경우
            if (image == null) {
                // 글 업로드
                updateBoardItemUseCase.invoke(item)

                // 디테일페이지에 업데이트할 아이템 발행
                publishSubject.onNext(item)
                return@launch
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
                    viewModelScope.launch {
                        val newItem = item.copy(
                            image = it
                        )

                        // 글 업로드
                        updateBoardItemUseCase.invoke(newItem)

                        // 디테일페이지에 업데이트할 아이템 발행
                        publishSubject.onNext(newItem)

                        // 아이템 발행 중지
                        getImageUrlSubject.onComplete()
                    }
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
}

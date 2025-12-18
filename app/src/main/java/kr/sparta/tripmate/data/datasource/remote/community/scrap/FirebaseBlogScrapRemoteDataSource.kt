package kr.sparta.tripmate.data.datasource.remote.community.scrap

import android.os.Build
import android.text.Html
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.remote.model.search.SearchBlogModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity

/**
 * 작성자: 서정한
 * 내용: 모든 블로그 스크랩데이터 가져오기
 * */
class FirebaseBlogScrapRemoteDataSource {
    private final val REFERENCE_BLOG_SCRAP = "BlogScrap"
    private fun getReference(uid: String) =
        Firebase.database.getReference(REFERENCE_BLOG_SCRAP).child(uid)

    /**
     * 작성자: 서정한
     * 내용:내가 스크랩한 블로그 목록 불러오기
     * */
    fun getAllBlogScrapsFlow(uid: String): Flow<List<SearchBlogModel?>> {
        val ref = getReference(uid)
        return ref.snapshots.map { snapshot ->
            snapshot.children.mapNotNull {
                it.getValue<SearchBlogModel>()
            }
        }
    }

    fun updateBlogScrap(uid: String, model: SearchBlogModel) {
        val ref = getReference(uid)
        ref.get().addOnSuccessListener { snapshot ->
            // 스크랩한 블로그목록 가져오기
            val scrapBlogs = snapshot.children.map {
                it.getValue(SearchBlogModel::class.java)
            }.toMutableList()

            // link가 일치하는 블로그 불러오기
            val blogItem = scrapBlogs.find { it?.link == model.link }

            // 블로그 목록 업데이트
            if (blogItem == null) {
                addBlogScrap(
                    uid = uid,
                    scrapBlogs = scrapBlogs,
                    model = model,
                )
            } else {
                removeBlogScrap(
                    uid = uid,
                    scrapBlogs = scrapBlogs,
                    model = model,
                )
            }
        }

    }

    /**
     * 작성자: 서정한
     * 내용: 해당 key의 블로그 스크랩데이터 가져오기
     * Firebase RDB에 저장한다.
     * */
    private fun addBlogScrap(
        uid: String,
        scrapBlogs: MutableList<SearchBlogModel?>,
        model: SearchBlogModel
    ) {
        /**
         * 작성자: 서정한
         * 내용: 스크랩데이터를 네이버에서 받아올때 html태그가 String에 섞여있음.
         * 검색한 데이터의 String값만 뽑아내기위한 메서드
         * */
        fun stripHtml(html: String): String {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return Html.fromHtml(html).toString()
            }
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        }

        // 제목 html 태그 제거
        val title = model.title?.let { stripHtml(it) }
        // 내용 html 태그 제거
        val description = model.description?.let { stripHtml(it) }

        val ref = getReference(uid)
        scrapBlogs.add(
            model.copy(
                title = title,
                description = description,
            )
        )

        ref.setValue(scrapBlogs)
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 블로그 스크랩 삭제
     * */
    private fun removeBlogScrap(
        uid: String,
        scrapBlogs: MutableList<SearchBlogModel?>,
        model: SearchBlogModel
    ) {
        val ref = getReference(uid)

        val removeItem = scrapBlogs.find { it?.link == model.link } ?: return
        scrapBlogs.remove(removeItem)

        ref.setValue(scrapBlogs)
    }
}

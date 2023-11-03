package kr.sparta.tripmate.domain.model.search

/**
 * 작성자: 서정한
 * 내용: 블로그 검색결과 서버데이터 Model
 * */
data class SearchBlogServerDataEntity(
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: List<SearchBlogEntity>?
)
package kr.sparta.tripmate.ui.community

/**
 * 작성자: 서정한
 * 내용: 커뮤니티 게시판에서 자기글을 수정하는경우와 글작성하는경우의
 * 분기를 받아 처리하기위핸 sealed class
 * */
sealed class ContentType {
    class Write : ContentType() {
        companion object {
            const val name = "write"
        }
    }

    class Edit: ContentType() {
        companion object {
            const val name = "edit"
        }
    }
}

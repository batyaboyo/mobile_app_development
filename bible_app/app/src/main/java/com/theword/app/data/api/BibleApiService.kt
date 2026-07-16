package com.theword.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface BibleApiService {

    @GET("available_translations.json")
    suspend fun getTranslations(): TranslationsResponse

    @GET("available_commentaries.json")
    suspend fun getCommentaries(): CommentariesResponse

    @GET("{translationId}/books.json")
    suspend fun getBooks(@Path("translationId") translationId: String): BooksResponse

    @GET("{translationId}/complete.json")
    suspend fun getCompleteTranslation(@Path("translationId") translationId: String): CompleteTranslationResponse

    @GET("{translationId}/{bookId}/{chapter}.json")
    suspend fun getChapter(
        @Path("translationId") translationId: String,
        @Path("bookId") bookId: String,
        @Path("chapter") chapter: Int
    ): ChapterResponse

    @GET("c/{commentaryId}/{bookId}/{chapter}.json")
    suspend fun getCommentaryChapter(
        @Path("commentaryId") commentaryId: String,
        @Path("bookId") bookId: String,
        @Path("chapter") chapter: Int
    ): CommentaryChapterResponse
}

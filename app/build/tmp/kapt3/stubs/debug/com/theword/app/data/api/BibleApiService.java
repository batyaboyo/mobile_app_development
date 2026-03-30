package com.theword.app.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J,\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\t\u001a\u00020\u00052\b\b\u0001\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ,\u0010\u0010\u001a\u00020\u00112\b\b\u0001\u0010\u0012\u001a\u00020\u00052\b\b\u0001\u0010\t\u001a\u00020\u00052\b\b\u0001\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0015\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0017"}, d2 = {"Lcom/theword/app/data/api/BibleApiService;", "", "getBooks", "Lcom/theword/app/data/api/BooksResponse;", "translationId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChapter", "Lcom/theword/app/data/api/ChapterResponse;", "bookId", "chapter", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentaries", "Lcom/theword/app/data/api/CommentariesResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentaryChapter", "Lcom/theword/app/data/api/CommentaryChapterResponse;", "commentaryId", "getCompleteTranslation", "Lcom/theword/app/data/api/CompleteTranslationResponse;", "getTranslations", "Lcom/theword/app/data/api/TranslationsResponse;", "app_debug"})
public abstract interface BibleApiService {
    
    @retrofit2.http.GET(value = "available_translations.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTranslations(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.TranslationsResponse> $completion);
    
    @retrofit2.http.GET(value = "available_commentaries.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCommentaries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.CommentariesResponse> $completion);
    
    @retrofit2.http.GET(value = "{translationId}/books.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBooks(@retrofit2.http.Path(value = "translationId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.BooksResponse> $completion);
    
    @retrofit2.http.GET(value = "{translationId}/complete.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCompleteTranslation(@retrofit2.http.Path(value = "translationId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.CompleteTranslationResponse> $completion);
    
    @retrofit2.http.GET(value = "{translationId}/{bookId}/{chapter}.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getChapter(@retrofit2.http.Path(value = "translationId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @retrofit2.http.Path(value = "bookId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @retrofit2.http.Path(value = "chapter")
    int chapter, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.ChapterResponse> $completion);
    
    @retrofit2.http.GET(value = "c/{commentaryId}/{bookId}/{chapter}.json")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCommentaryChapter(@retrofit2.http.Path(value = "commentaryId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String commentaryId, @retrofit2.http.Path(value = "bookId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, @retrofit2.http.Path(value = "chapter")
    int chapter, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.api.CommentaryChapterResponse> $completion);
}
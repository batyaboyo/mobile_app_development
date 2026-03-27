package com.theword.app.data.api;

@com.squareup.moshi.JsonClass(generateAdapter = false)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0003\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0003\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0003\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J-\u0010\u0012\u001a\u00020\u00002\n\b\u0003\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0003\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/theword/app/data/api/ChapterResponse;", "", "chapter", "Lcom/theword/app/data/api/ChapterDto;", "book", "Lcom/theword/app/data/api/BookRefDto;", "translation", "Lcom/theword/app/data/api/TranslationRefDto;", "(Lcom/theword/app/data/api/ChapterDto;Lcom/theword/app/data/api/BookRefDto;Lcom/theword/app/data/api/TranslationRefDto;)V", "getBook", "()Lcom/theword/app/data/api/BookRefDto;", "getChapter", "()Lcom/theword/app/data/api/ChapterDto;", "getTranslation", "()Lcom/theword/app/data/api/TranslationRefDto;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class ChapterResponse {
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.data.api.ChapterDto chapter = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.data.api.BookRefDto book = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.data.api.TranslationRefDto translation = null;
    
    public ChapterResponse(@com.squareup.moshi.Json(name = "chapter")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.ChapterDto chapter, @com.squareup.moshi.Json(name = "book")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.BookRefDto book, @com.squareup.moshi.Json(name = "translation")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.TranslationRefDto translation) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.ChapterDto getChapter() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.BookRefDto getBook() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.TranslationRefDto getTranslation() {
        return null;
    }
    
    public ChapterResponse() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.ChapterDto component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.BookRefDto component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.api.TranslationRefDto component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.api.ChapterResponse copy(@com.squareup.moshi.Json(name = "chapter")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.ChapterDto chapter, @com.squareup.moshi.Json(name = "book")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.BookRefDto book, @com.squareup.moshi.Json(name = "translation")
    @org.jetbrains.annotations.Nullable()
    com.theword.app.data.api.TranslationRefDto translation) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}
package com.theword.app.data.api;

@com.squareup.moshi.JsonClass(generateAdapter = false)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B3\u0012\b\b\u0003\u0010\u0002\u001a\u00020\u0003\u0012\u0010\b\u0003\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u0010\b\u0003\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\tJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\u0011\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\u0011\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005H\u00c6\u0003J7\u0010\u0012\u001a\u00020\u00002\b\b\u0003\u0010\u0002\u001a\u00020\u00032\u0010\b\u0003\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00052\u0010\b\u0003\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0019\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0019"}, d2 = {"Lcom/theword/app/data/api/ChapterDto;", "", "number", "", "content", "", "Lcom/theword/app/data/api/ContentItemDto;", "footnotes", "Lcom/theword/app/data/api/FootnoteDto;", "(ILjava/util/List;Ljava/util/List;)V", "getContent", "()Ljava/util/List;", "getFootnotes", "getNumber", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class ChapterDto {
    private final int number = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.theword.app.data.api.ContentItemDto> content = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.theword.app.data.api.FootnoteDto> footnotes = null;
    
    public ChapterDto(@com.squareup.moshi.Json(name = "number")
    int number, @com.squareup.moshi.Json(name = "content")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.ContentItemDto> content, @com.squareup.moshi.Json(name = "footnotes")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.FootnoteDto> footnotes) {
        super();
    }
    
    public final int getNumber() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.theword.app.data.api.ContentItemDto> getContent() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.theword.app.data.api.FootnoteDto> getFootnotes() {
        return null;
    }
    
    public ChapterDto() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.theword.app.data.api.ContentItemDto> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.theword.app.data.api.FootnoteDto> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.api.ChapterDto copy(@com.squareup.moshi.Json(name = "number")
    int number, @com.squareup.moshi.Json(name = "content")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.ContentItemDto> content, @com.squareup.moshi.Json(name = "footnotes")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.FootnoteDto> footnotes) {
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
package com.theword.app.data.api;

@com.squareup.moshi.JsonClass(generateAdapter = false)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0003\u0010\u0002\u001a\u00020\u0003\u0012\u0010\b\u0003\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\u0011\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\bH\u00c6\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0003\u0010\u0002\u001a\u00020\u00032\u0010\b\u0003\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00052\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\bH\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0019"}, d2 = {"Lcom/theword/app/data/api/CommentaryChapterDto;", "", "number", "", "content", "", "Lcom/theword/app/data/api/ContentItemDto;", "introduction", "", "(ILjava/util/List;Ljava/lang/String;)V", "getContent", "()Ljava/util/List;", "getIntroduction", "()Ljava/lang/String;", "getNumber", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class CommentaryChapterDto {
    private final int number = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.theword.app.data.api.ContentItemDto> content = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String introduction = null;
    
    public CommentaryChapterDto(@com.squareup.moshi.Json(name = "number")
    int number, @com.squareup.moshi.Json(name = "content")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.ContentItemDto> content, @com.squareup.moshi.Json(name = "introduction")
    @org.jetbrains.annotations.Nullable()
    java.lang.String introduction) {
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
    public final java.lang.String getIntroduction() {
        return null;
    }
    
    public CommentaryChapterDto() {
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
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.api.CommentaryChapterDto copy(@com.squareup.moshi.Json(name = "number")
    int number, @com.squareup.moshi.Json(name = "content")
    @org.jetbrains.annotations.Nullable()
    java.util.List<com.theword.app.data.api.ContentItemDto> content, @com.squareup.moshi.Json(name = "introduction")
    @org.jetbrains.annotations.Nullable()
    java.lang.String introduction) {
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
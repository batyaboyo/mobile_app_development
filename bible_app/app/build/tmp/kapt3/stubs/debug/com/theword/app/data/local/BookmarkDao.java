package com.theword.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\f2\u0006\u0010\u0010\u001a\u00020\tH\'J\u0016\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J \u0010\u0012\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\tH\u00a7@\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/theword/app/data/local/BookmarkDao;", "", "deleteBookmark", "", "bookmark", "Lcom/theword/app/data/local/BookmarkEntity;", "(Lcom/theword/app/data/local/BookmarkEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteByReference", "reference", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllBookmarks", "Lkotlinx/coroutines/flow/Flow;", "", "getBookmark", "getBookmarksByCollection", "collection", "insertBookmark", "updateCollection", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface BookmarkDao {
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.data.local.BookmarkEntity>> getAllBookmarks();
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks WHERE collection = :collection ORDER BY bookmarkedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.data.local.BookmarkEntity>> getBookmarksByCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String collection);
    
    @androidx.room.Query(value = "SELECT * FROM bookmarks WHERE reference = :reference LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.local.BookmarkEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBookmark(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.BookmarkEntity bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteBookmark(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.BookmarkEntity bookmark, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM bookmarks WHERE reference = :reference")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteByReference(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE bookmarks SET collection = :collection WHERE reference = :reference")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.Nullable()
    java.lang.String collection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
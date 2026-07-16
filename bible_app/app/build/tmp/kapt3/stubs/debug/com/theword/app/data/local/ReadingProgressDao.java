package com.theword.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u001c\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0007\u001a\u00020\bH\'J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0003H\'J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/theword/app/data/local/ReadingProgressDao;", "", "getAllProgress", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/theword/app/data/local/ReadingProgressEntity;", "getProgressForBook", "bookId", "", "getTotalChaptersRead", "", "markChapterRead", "", "progress", "(Lcom/theword/app/data/local/ReadingProgressEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ReadingProgressDao {
    
    @androidx.room.Query(value = "SELECT * FROM reading_progress")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.data.local.ReadingProgressEntity>> getAllProgress();
    
    @androidx.room.Query(value = "SELECT * FROM reading_progress WHERE bookId = :bookId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.data.local.ReadingProgressEntity>> getProgressForBook(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM reading_progress")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getTotalChaptersRead();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markChapterRead(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.ReadingProgressEntity progress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
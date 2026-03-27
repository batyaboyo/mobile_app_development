package com.theword.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcom/theword/app/data/local/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "bookmarkDao", "Lcom/theword/app/data/local/BookmarkDao;", "highlightDao", "Lcom/theword/app/data/local/HighlightDao;", "quizResultDao", "Lcom/theword/app/data/local/QuizResultDao;", "readingProgressDao", "Lcom/theword/app/data/local/ReadingProgressDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.theword.app.data.local.BookmarkEntity.class, com.theword.app.data.local.HighlightEntity.class, com.theword.app.data.local.ReadingProgressEntity.class, com.theword.app.data.local.QuizResultEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.theword.app.data.local.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.theword.app.data.local.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.theword.app.data.local.BookmarkDao bookmarkDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.theword.app.data.local.HighlightDao highlightDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.theword.app.data.local.ReadingProgressDao readingProgressDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.theword.app.data.local.QuizResultDao quizResultDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/theword/app/data/local/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/theword/app/data/local/AppDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.theword.app.data.local.AppDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}
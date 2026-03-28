package com.theword.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0012\u0010\u0017\u001a\u00020\u000b2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u0002J\u0012\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\f0\u001aJ\u0012\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\f0\u001aJ\u0012\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\f0\u001aJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010!\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J,\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00100\f2\u0006\u0010!\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010\'J\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00120\fH\u0086@\u00a2\u0006\u0002\u0010)J(\u0010*\u001a\u0004\u0018\u00010+2\u0006\u0010,\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010\'J\u000e\u0010-\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0002J\u0018\u0010.\u001a\u0004\u0018\u00010\u001d2\u0006\u0010/\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J\u0018\u00100\u001a\u0004\u0018\u0001012\u0006\u00102\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J\f\u00103\u001a\b\u0012\u0004\u0012\u00020&0\u001aJ\u0014\u00104\u001a\b\u0012\u0004\u0012\u00020\u00160\fH\u0086@\u00a2\u0006\u0002\u0010)J0\u00105\u001a\u0004\u0018\u00010\u000b2\u0006\u0010!\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&2\u0006\u00106\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u00107J\u0016\u00108\u001a\u0002092\u0006\u0010/\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J\u001e\u0010:\u001a\u00020;2\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010<J \u0010=\u001a\u00020;2\u0006\u0010/\u001a\u00020\u000b2\b\u0010>\u001a\u0004\u0018\u00010\u000bH\u0086@\u00a2\u0006\u0002\u0010?J,\u0010@\u001a\b\u0012\u0004\u0012\u00020A0\f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00012\u0012\u0010B\u001a\u000e\u0012\u0004\u0012\u00020&\u0012\u0004\u0012\u00020D0CH\u0002J\u0016\u0010E\u001a\u00020;2\u0006\u0010/\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J(\u0010F\u001a\u00020;2\u0006\u0010/\u001a\u00020\u000b2\u0006\u0010G\u001a\u00020\u000b2\b\u0010H\u001a\u0004\u0018\u00010\u000bH\u0086@\u00a2\u0006\u0002\u0010IJ6\u0010J\u001a\u00020;2\u0006\u00102\u001a\u00020\u000b2\u0006\u0010K\u001a\u00020\u000b2\u0006\u0010L\u001a\u00020\u000b2\u0006\u0010M\u001a\u00020&2\u0006\u0010N\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010OJ\u001e\u0010P\u001a\u0002092\u0006\u0010/\u001a\u00020\u000b2\u0006\u0010Q\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010?R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\f0\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0011\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R \u0010\u0015\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006R"}, d2 = {"Lcom/theword/app/data/repository/BibleRepository;", "", "api", "Lcom/theword/app/data/api/BibleApiService;", "db", "Lcom/theword/app/data/local/AppDatabase;", "prefs", "Lcom/theword/app/data/local/PreferencesManager;", "(Lcom/theword/app/data/api/BibleApiService;Lcom/theword/app/data/local/AppDatabase;Lcom/theword/app/data/local/PreferencesManager;)V", "booksCache", "Ljava/util/concurrent/ConcurrentHashMap;", "", "", "Lcom/theword/app/domain/model/BibleBook;", "chapterCache", "", "Lcom/theword/app/domain/model/ChapterContent;", "commentariesCache", "Lcom/theword/app/domain/model/Commentary;", "getPrefs", "()Lcom/theword/app/data/local/PreferencesManager;", "translationsCache", "Lcom/theword/app/domain/model/Translation;", "extractTextFromContent", "content", "getAllBookmarks", "Lkotlinx/coroutines/flow/Flow;", "Lcom/theword/app/domain/model/Bookmark;", "getAllHighlights", "Lcom/theword/app/domain/model/Highlight;", "getAllProgress", "Lcom/theword/app/data/local/ReadingProgressEntity;", "getBooks", "translationId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChapter", "bookId", "chapter", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentaries", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentaryChapter", "Lcom/theword/app/domain/model/CommentaryContent;", "commentaryId", "getFallbackBooks", "getHighlight", "reference", "getQuizResult", "Lcom/theword/app/data/local/QuizResultEntity;", "dateKey", "getTotalChaptersRead", "getTranslations", "getVerseText", "verseNumber", "(Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isBookmarked", "", "markChapterRead", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveBookmarkToCollection", "collection", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseVerseParts", "Lcom/theword/app/domain/model/VersePart;", "footnotes", "", "Lcom/theword/app/data/api/FootnoteDto;", "removeBookmark", "saveHighlight", "color", "note", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveQuizResult", "questionsJson", "answersJson", "score", "total", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toggleBookmark", "text", "app_debug"})
public final class BibleRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.data.api.BibleApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.data.local.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.data.local.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<com.theword.app.domain.model.Translation>> translationsCache = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<com.theword.app.domain.model.BibleBook>> booksCache = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.List<com.theword.app.domain.model.ChapterContent>> chapterCache = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<com.theword.app.domain.model.Commentary>> commentariesCache = null;
    
    public BibleRepository(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.api.BibleApiService api, @org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.AppDatabase db, @org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.PreferencesManager prefs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.local.PreferencesManager getPrefs() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTranslations(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.theword.app.domain.model.Translation>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCommentaries(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.theword.app.domain.model.Commentary>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBooks(@org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.theword.app.domain.model.BibleBook>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getChapter(@org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int chapter, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<? extends com.theword.app.domain.model.ChapterContent>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getVerseText(@org.jetbrains.annotations.NotNull()
    java.lang.String translationId, @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int chapter, int verseNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCommentaryChapter(@org.jetbrains.annotations.NotNull()
    java.lang.String commentaryId, @org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int chapter, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.domain.model.CommentaryContent> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.domain.model.Bookmark>> getAllBookmarks() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isBookmarked(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object toggleBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removeBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object moveBookmarkToCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.Nullable()
    java.lang.String collection, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.domain.model.Highlight>> getAllHighlights() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getHighlight(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.domain.model.Highlight> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveHighlight(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String color, @org.jetbrains.annotations.Nullable()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.data.local.ReadingProgressEntity>> getAllProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getTotalChaptersRead() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markChapterRead(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int chapter, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getQuizResult(@org.jetbrains.annotations.NotNull()
    java.lang.String dateKey, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.local.QuizResultEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveQuizResult(@org.jetbrains.annotations.NotNull()
    java.lang.String dateKey, @org.jetbrains.annotations.NotNull()
    java.lang.String questionsJson, @org.jetbrains.annotations.NotNull()
    java.lang.String answersJson, int score, int total, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    private final java.lang.String extractTextFromContent(java.lang.Object content) {
        return null;
    }
    
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    private final java.util.List<com.theword.app.domain.model.VersePart> parseVerseParts(java.lang.Object content, java.util.Map<java.lang.Integer, com.theword.app.data.api.FootnoteDto> footnotes) {
        return null;
    }
    
    private final java.util.List<com.theword.app.domain.model.BibleBook> getFallbackBooks() {
        return null;
    }
}
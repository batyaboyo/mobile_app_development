package com.theword.app.ui.bible;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\'\b\u0086\b\u0018\u00002\u00020\u0001B\u00ab\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0005\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u0011\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\n0\u0011\u0012\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00150\u0014\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u0019J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\u0015\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00150\u0014H\u00c6\u0003J\t\u0010/\u001a\u00020\u0017H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u000f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u00102\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\t\u00103\u001a\u00020\nH\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u00105\u001a\u00020\rH\u00c6\u0003J\u000f\u00106\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0005H\u00c6\u0003J\u000f\u00107\u001a\b\u0012\u0004\u0012\u00020\n0\u0011H\u00c6\u0003J\u000f\u00108\u001a\b\u0012\u0004\u0012\u00020\n0\u0011H\u00c6\u0003J\u00af\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00052\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u00112\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\n0\u00112\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00150\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010:\u001a\u00020\u00172\b\u0010;\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010<\u001a\u00020\rH\u00d6\u0001J\t\u0010=\u001a\u00020\nH\u00d6\u0001R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0013\u0010\u0018\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010 R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\n0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001bR\u001d\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00150\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010%R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001d\u00a8\u0006>"}, d2 = {"Lcom/theword/app/ui/bible/BibleUiState;", "", "navState", "Lcom/theword/app/ui/bible/BibleNavState;", "books", "", "Lcom/theword/app/domain/model/BibleBook;", "translations", "Lcom/theword/app/domain/model/Translation;", "currentVersion", "", "selectedBook", "selectedChapter", "", "chapterContent", "Lcom/theword/app/domain/model/ChapterContent;", "bookmarkedRefs", "", "favoriteRefs", "highlightsMap", "", "Lcom/theword/app/domain/model/Highlight;", "isLoading", "", "error", "(Lcom/theword/app/ui/bible/BibleNavState;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Lcom/theword/app/domain/model/BibleBook;ILjava/util/List;Ljava/util/Set;Ljava/util/Set;Ljava/util/Map;ZLjava/lang/String;)V", "getBookmarkedRefs", "()Ljava/util/Set;", "getBooks", "()Ljava/util/List;", "getChapterContent", "getCurrentVersion", "()Ljava/lang/String;", "getError", "getFavoriteRefs", "getHighlightsMap", "()Ljava/util/Map;", "()Z", "getNavState", "()Lcom/theword/app/ui/bible/BibleNavState;", "getSelectedBook", "()Lcom/theword/app/domain/model/BibleBook;", "getSelectedChapter", "()I", "getTranslations", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class BibleUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.ui.bible.BibleNavState navState = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.theword.app.domain.model.BibleBook> books = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.theword.app.domain.model.Translation> translations = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentVersion = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.domain.model.BibleBook selectedBook = null;
    private final int selectedChapter = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.theword.app.domain.model.ChapterContent> chapterContent = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> bookmarkedRefs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> favoriteRefs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.theword.app.domain.model.Highlight> highlightsMap = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public BibleUiState(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.bible.BibleNavState navState, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.BibleBook> books, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.Translation> translations, @org.jetbrains.annotations.NotNull()
    java.lang.String currentVersion, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.BibleBook selectedBook, int selectedChapter, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.theword.app.domain.model.ChapterContent> chapterContent, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> bookmarkedRefs, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favoriteRefs, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.theword.app.domain.model.Highlight> highlightsMap, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.ui.bible.BibleNavState getNavState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.BibleBook> getBooks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.Translation> getTranslations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentVersion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.BibleBook getSelectedBook() {
        return null;
    }
    
    public final int getSelectedChapter() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.ChapterContent> getChapterContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getBookmarkedRefs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getFavoriteRefs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.theword.app.domain.model.Highlight> getHighlightsMap() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public BibleUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.ui.bible.BibleNavState component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.theword.app.domain.model.Highlight> component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.BibleBook> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.Translation> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.BibleBook component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.ChapterContent> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.ui.bible.BibleUiState copy(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.bible.BibleNavState navState, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.BibleBook> books, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.Translation> translations, @org.jetbrains.annotations.NotNull()
    java.lang.String currentVersion, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.BibleBook selectedBook, int selectedChapter, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.theword.app.domain.model.ChapterContent> chapterContent, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> bookmarkedRefs, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favoriteRefs, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.theword.app.domain.model.Highlight> highlightsMap, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
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
package com.theword.app.ui.bible;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u001e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000fJ\b\u0010\u0015\u001a\u00020\rH\u0002J\u0006\u0010\u0016\u001a\u00020\rJ\u0006\u0010\u0017\u001a\u00020\rJ\b\u0010\u0018\u001a\u00020\rH\u0002J\b\u0010\u0019\u001a\u00020\rH\u0002J\u0006\u0010\u001a\u001a\u00020\rJ \u0010\u001b\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u000fJ\u000e\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\r2\u0006\u0010\"\u001a\u00020#J\u0016\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#J\u001e\u0010&\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000fJ\u0016\u0010\'\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000fJ\u0016\u0010(\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006*"}, d2 = {"Lcom/theword/app/ui/bible/BibleViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/theword/app/data/repository/BibleRepository;", "(Lcom/theword/app/data/repository/BibleRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/theword/app/ui/bible/BibleUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "changeVersion", "", "versionId", "", "copyVerse", "context", "Landroid/content/Context;", "reference", "text", "loadInitialData", "navigateBack", "nextChapter", "observeBookmarks", "observeHighlights", "previousChapter", "saveHighlight", "color", "note", "selectBook", "book", "Lcom/theword/app/domain/model/BibleBook;", "selectChapter", "chapter", "", "selectChapterDeepLink", "bookId", "shareVerse", "toggleBookmark", "toggleFavorite", "Companion", "app_debug"})
public final class BibleViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.data.repository.BibleRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.theword.app.ui.bible.BibleUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.theword.app.ui.bible.BibleUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.ViewModelProvider.Factory Factory = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.theword.app.ui.bible.BibleViewModel.Companion Companion = null;
    
    public BibleViewModel(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.repository.BibleRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.theword.app.ui.bible.BibleUiState> getUiState() {
        return null;
    }
    
    private final void loadInitialData() {
    }
    
    private final void observeBookmarks() {
    }
    
    private final void observeHighlights() {
    }
    
    public final void selectBook(@org.jetbrains.annotations.NotNull()
    com.theword.app.domain.model.BibleBook book) {
    }
    
    public final void selectChapter(int chapter) {
    }
    
    public final void navigateBack() {
    }
    
    public final void selectChapterDeepLink(@org.jetbrains.annotations.NotNull()
    java.lang.String bookId, int chapter) {
    }
    
    public final void previousChapter() {
    }
    
    public final void nextChapter() {
    }
    
    public final void changeVersion(@org.jetbrains.annotations.NotNull()
    java.lang.String versionId) {
    }
    
    public final void toggleBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void copyVerse(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void shareVerse(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void saveHighlight(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String color, @org.jetbrains.annotations.Nullable()
    java.lang.String note) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/theword/app/ui/bible/BibleViewModel$Companion;", "", "()V", "Factory", "Landroidx/lifecycle/ViewModelProvider$Factory;", "getFactory", "()Landroidx/lifecycle/ViewModelProvider$Factory;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.ViewModelProvider.Factory getFactory() {
            return null;
        }
    }
}
package com.theword.app.ui.bookmarks;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017J\u0018\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u001a\u001a\u0004\u0018\u00010\u0017J\u000e\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0017J\u0010\u0010\u001c\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u0017J\u001e\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001f"}, d2 = {"Lcom/theword/app/ui/bookmarks/BookmarksViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/theword/app/data/repository/BibleRepository;", "(Lcom/theword/app/data/repository/BibleRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/theword/app/ui/bookmarks/BookmarksUiState;", "filteredBookmarks", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/theword/app/domain/model/Bookmark;", "getFilteredBookmarks", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "copyVerse", "", "context", "Landroid/content/Context;", "reference", "", "text", "moveToCollection", "collection", "removeBookmark", "selectCollection", "shareVerse", "Companion", "app_debug"})
public final class BookmarksViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.theword.app.data.repository.BibleRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.theword.app.ui.bookmarks.BookmarksUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.theword.app.ui.bookmarks.BookmarksUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.domain.model.Bookmark>> filteredBookmarks = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.ViewModelProvider.Factory Factory = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.theword.app.ui.bookmarks.BookmarksViewModel.Companion Companion = null;
    
    public BookmarksViewModel(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.repository.BibleRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.theword.app.ui.bookmarks.BookmarksUiState> getUiState() {
        return null;
    }
    
    public final void selectCollection(@org.jetbrains.annotations.Nullable()
    java.lang.String collection) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.theword.app.domain.model.Bookmark>> getFilteredBookmarks() {
        return null;
    }
    
    public final void removeBookmark(@org.jetbrains.annotations.NotNull()
    java.lang.String reference) {
    }
    
    public final void moveToCollection(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.Nullable()
    java.lang.String collection) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/theword/app/ui/bookmarks/BookmarksViewModel$Companion;", "", "()V", "Factory", "Landroidx/lifecycle/ViewModelProvider$Factory;", "getFactory", "()Landroidx/lifecycle/ViewModelProvider$Factory;", "app_debug"})
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
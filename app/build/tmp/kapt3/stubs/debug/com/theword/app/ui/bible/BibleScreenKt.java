package com.theword.app.ui.bible;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a$\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a@\u0010\u000b\u001a\u00020\u00012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a\u0018\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001aJ\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u001a\u0010\u0017\u001a\u0016\u0012\u0004\u0012\u00020\u0013\u0012\u0006\u0012\u0004\u0018\u00010\u0013\u0012\u0004\u0012\u00020\u00010\u0018H\u0007\u001a\u0018\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001aH\u0010\u001c\u001a\u00020\u00012\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\r2\u0006\u0010\u001f\u001a\u00020\u00132\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010!\u001a\u00020\u000f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u0017\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0013H\u0007\u00a2\u0006\u0002\u0010&\u001a\u0016\u0010\'\u001a\u00020(*\u00020$H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b)\u0010*\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006+"}, d2 = {"BibleScreen", "", "viewModel", "Lcom/theword/app/ui/bible/BibleViewModel;", "onBack", "Lkotlin/Function0;", "BookItem", "book", "Lcom/theword/app/domain/model/BibleBook;", "onSelect", "Lkotlin/Function1;", "BooksList", "books", "", "isLoading", "", "ChapterGrid", "HighlightDialog", "reference", "", "currentColor", "currentNote", "onDismiss", "onSave", "Lkotlin/Function2;", "VerseDisplay", "uiState", "Lcom/theword/app/ui/bible/BibleUiState;", "VersionSelector", "translations", "Lcom/theword/app/domain/model/Translation;", "currentVersion", "onVersionChange", "isSyncing", "onSync", "getHighlightColor", "Landroidx/compose/ui/graphics/Color;", "color", "(Ljava/lang/String;)J", "luminance", "", "luminance-8_81llA", "(J)F", "app_debug"})
public final class BibleScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void BibleScreen(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.bible.BibleViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VersionSelector(java.util.List<com.theword.app.domain.model.Translation> translations, java.lang.String currentVersion, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onVersionChange, boolean isSyncing, kotlin.jvm.functions.Function0<kotlin.Unit> onSync) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BooksList(java.util.List<com.theword.app.domain.model.BibleBook> books, boolean isLoading, kotlin.jvm.functions.Function0<kotlin.Unit> onBack, kotlin.jvm.functions.Function1<? super com.theword.app.domain.model.BibleBook, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BookItem(com.theword.app.domain.model.BibleBook book, kotlin.jvm.functions.Function1<? super com.theword.app.domain.model.BibleBook, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChapterGrid(com.theword.app.domain.model.BibleBook book, com.theword.app.ui.bible.BibleViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VerseDisplay(com.theword.app.ui.bible.BibleUiState uiState, com.theword.app.ui.bible.BibleViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void HighlightDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String reference, @org.jetbrains.annotations.NotNull()
    java.lang.String currentColor, @org.jetbrains.annotations.NotNull()
    java.lang.String currentNote, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSave) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final long getHighlightColor(@org.jetbrains.annotations.Nullable()
    java.lang.String color) {
        return 0L;
    }
}
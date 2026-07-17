package com.theword.app.ui.bookmarks;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a^\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0014\u0010\u000b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0006\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a*\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00062\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u00a8\u0006\u0012"}, d2 = {"BookmarkCard", "", "bookmark", "Lcom/theword/app/domain/model/Bookmark;", "collections", "", "", "onCopy", "Lkotlin/Function0;", "onShare", "onRemove", "onMoveToCollection", "Lkotlin/Function1;", "BookmarksScreen", "viewModel", "Lcom/theword/app/ui/bookmarks/BookmarksViewModel;", "initialCollection", "onNavigateToBible", "app_debug"})
public final class BookmarksScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void BookmarksScreen(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.bookmarks.BookmarksViewModel viewModel, @org.jetbrains.annotations.Nullable()
    java.lang.String initialCollection, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToBible) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void BookmarkCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.domain.model.Bookmark bookmark, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> collections, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCopy, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onShare, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onMoveToCollection) {
    }
}
package com.theword.app.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u001a\u0010\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\tH\u0007\u001a,\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u001a\u001e\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00a8\u0006\u0013"}, d2 = {"DailyDevotionCard", "", "devotion", "Lcom/theword/app/data/embedded/Devotion;", "DailyPrayerCard", "prayer", "Lcom/theword/app/domain/model/Prayer;", "DailyStoryCard", "story", "Lcom/theword/app/domain/model/BibleStory;", "DailyVerseCard", "uiState", "Lcom/theword/app/ui/home/HomeUiState;", "onNavigateToBible", "Lkotlin/Function0;", "onRetry", "HomeScreen", "viewModel", "Lcom/theword/app/ui/home/HomeViewModel;", "app_debug"})
public final class HomeScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.home.HomeViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToBible) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DailyVerseCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.home.HomeUiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToBible, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRetry) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DailyStoryCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.domain.model.BibleStory story) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DailyPrayerCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.domain.model.Prayer prayer) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DailyDevotionCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.embedded.Devotion devotion) {
    }
}
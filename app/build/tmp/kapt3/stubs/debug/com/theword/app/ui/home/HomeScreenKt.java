package com.theword.app.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007\u001a,\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\rH\u0007\u001a\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0002H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\rH\u0007\"\u0017\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004\u00a8\u0006\u0014"}, d2 = {"features", "", "Lcom/theword/app/ui/home/Feature;", "getFeatures", "()Ljava/util/List;", "DailyDevotionCard", "", "devotion", "Lcom/theword/app/data/embedded/Devotion;", "DailyVerseCard", "uiState", "Lcom/theword/app/ui/home/HomeUiState;", "onNavigateToBible", "Lkotlin/Function0;", "onRetry", "FeatureCard", "feature", "HomeScreen", "viewModel", "Lcom/theword/app/ui/home/HomeViewModel;", "app_debug"})
public final class HomeScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.theword.app.ui.home.Feature> features = null;
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.theword.app.ui.home.Feature> getFeatures() {
        return null;
    }
    
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
    public static final void FeatureCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.ui.home.Feature feature) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DailyDevotionCard(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.embedded.Devotion devotion) {
    }
}
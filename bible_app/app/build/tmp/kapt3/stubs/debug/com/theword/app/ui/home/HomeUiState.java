package com.theword.app.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001a\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bc\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\u0002\u0010\u0010J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\rH\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u000fH\u00c6\u0003Jg\u0010&\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u00c6\u0001J\u0013\u0010\'\u001a\u00020\r2\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010)\u001a\u00020*H\u00d6\u0001J\t\u0010+\u001a\u00020\u000fH\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u001d\u00a8\u0006,"}, d2 = {"Lcom/theword/app/ui/home/HomeUiState;", "", "dailyVerse", "Lcom/theword/app/ui/home/DailyVerse;", "dailyDevotion", "Lcom/theword/app/data/embedded/Devotion;", "dailyStory", "Lcom/theword/app/domain/model/BibleStory;", "dailyPrayer", "Lcom/theword/app/domain/model/Prayer;", "dailyPsalm", "dailyProverb", "isLoading", "", "error", "", "(Lcom/theword/app/ui/home/DailyVerse;Lcom/theword/app/data/embedded/Devotion;Lcom/theword/app/domain/model/BibleStory;Lcom/theword/app/domain/model/Prayer;Lcom/theword/app/ui/home/DailyVerse;Lcom/theword/app/ui/home/DailyVerse;ZLjava/lang/String;)V", "getDailyDevotion", "()Lcom/theword/app/data/embedded/Devotion;", "getDailyPrayer", "()Lcom/theword/app/domain/model/Prayer;", "getDailyProverb", "()Lcom/theword/app/ui/home/DailyVerse;", "getDailyPsalm", "getDailyStory", "()Lcom/theword/app/domain/model/BibleStory;", "getDailyVerse", "getError", "()Ljava/lang/String;", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class HomeUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.ui.home.DailyVerse dailyVerse = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.data.embedded.Devotion dailyDevotion = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.domain.model.BibleStory dailyStory = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.domain.model.Prayer dailyPrayer = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.ui.home.DailyVerse dailyPsalm = null;
    @org.jetbrains.annotations.Nullable()
    private final com.theword.app.ui.home.DailyVerse dailyProverb = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public HomeUiState(@org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyVerse, @org.jetbrains.annotations.Nullable()
    com.theword.app.data.embedded.Devotion dailyDevotion, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.BibleStory dailyStory, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.Prayer dailyPrayer, @org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyPsalm, @org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyProverb, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse getDailyVerse() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.embedded.Devotion getDailyDevotion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.BibleStory getDailyStory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.Prayer getDailyPrayer() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse getDailyPsalm() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse getDailyProverb() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public HomeUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.data.embedded.Devotion component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.BibleStory component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.domain.model.Prayer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.theword.app.ui.home.DailyVerse component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.ui.home.HomeUiState copy(@org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyVerse, @org.jetbrains.annotations.Nullable()
    com.theword.app.data.embedded.Devotion dailyDevotion, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.BibleStory dailyStory, @org.jetbrains.annotations.Nullable()
    com.theword.app.domain.model.Prayer dailyPrayer, @org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyPsalm, @org.jetbrains.annotations.Nullable()
    com.theword.app.ui.home.DailyVerse dailyProverb, boolean isLoading, @org.jetbrains.annotations.Nullable()
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
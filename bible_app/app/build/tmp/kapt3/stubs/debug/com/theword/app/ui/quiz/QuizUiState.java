package com.theword.app.ui.quiz;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\"\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001Bw\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\n\u0012\b\b\u0002\u0010\r\u001a\u00020\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u00a2\u0006\u0002\u0010\u0011J\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00c6\u0003J\u0011\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\bH\u00c6\u0003J\t\u0010\"\u001a\u00020\nH\u00c6\u0003J\t\u0010#\u001a\u00020\nH\u00c6\u0003J\t\u0010$\u001a\u00020\nH\u00c6\u0003J\t\u0010%\u001a\u00020\bH\u00c6\u0003J\t\u0010&\u001a\u00020\bH\u00c6\u0003J\t\u0010\'\u001a\u00020\bH\u00c6\u0003J{\u0010(\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\bH\u00c6\u0001J\u0013\u0010)\u001a\u00020\n2\b\u0010*\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010+\u001a\u00020\bH\u00d6\u0001J\t\u0010,\u001a\u00020-H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0013R\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0017R\u0011\u0010\u0010\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017\u00a8\u0006."}, d2 = {"Lcom/theword/app/ui/quiz/QuizUiState;", "", "questions", "", "Lcom/theword/app/domain/model/QuizQuestion;", "answers", "Lcom/theword/app/domain/model/QuizAnswer;", "currentIndex", "", "isComplete", "", "showReview", "alreadyTaken", "previousScore", "previousTotal", "streak", "totalPoints", "(Ljava/util/List;Ljava/util/List;IZZZIIII)V", "getAlreadyTaken", "()Z", "getAnswers", "()Ljava/util/List;", "getCurrentIndex", "()I", "getPreviousScore", "getPreviousTotal", "getQuestions", "getShowReview", "getStreak", "getTotalPoints", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
public final class QuizUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.theword.app.domain.model.QuizQuestion> questions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.theword.app.domain.model.QuizAnswer> answers = null;
    private final int currentIndex = 0;
    private final boolean isComplete = false;
    private final boolean showReview = false;
    private final boolean alreadyTaken = false;
    private final int previousScore = 0;
    private final int previousTotal = 0;
    private final int streak = 0;
    private final int totalPoints = 0;
    
    public QuizUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.QuizQuestion> questions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.QuizAnswer> answers, int currentIndex, boolean isComplete, boolean showReview, boolean alreadyTaken, int previousScore, int previousTotal, int streak, int totalPoints) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.QuizQuestion> getQuestions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.QuizAnswer> getAnswers() {
        return null;
    }
    
    public final int getCurrentIndex() {
        return 0;
    }
    
    public final boolean isComplete() {
        return false;
    }
    
    public final boolean getShowReview() {
        return false;
    }
    
    public final boolean getAlreadyTaken() {
        return false;
    }
    
    public final int getPreviousScore() {
        return 0;
    }
    
    public final int getPreviousTotal() {
        return 0;
    }
    
    public final int getStreak() {
        return 0;
    }
    
    public final int getTotalPoints() {
        return 0;
    }
    
    public QuizUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.QuizQuestion> component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.theword.app.domain.model.QuizAnswer> component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.ui.quiz.QuizUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.QuizQuestion> questions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.theword.app.domain.model.QuizAnswer> answers, int currentIndex, boolean isComplete, boolean showReview, boolean alreadyTaken, int previousScore, int previousTotal, int streak, int totalPoints) {
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
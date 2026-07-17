package com.theword.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/theword/app/data/local/QuizResultDao;", "", "getAllResults", "", "Lcom/theword/app/data/local/QuizResultEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getResult", "dateKey", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTotalQuizzesTaken", "", "insertResult", "", "result", "(Lcom/theword/app/data/local/QuizResultEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface QuizResultDao {
    
    @androidx.room.Query(value = "SELECT * FROM quiz_results WHERE dateKey = :dateKey LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getResult(@org.jetbrains.annotations.NotNull()
    java.lang.String dateKey, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.theword.app.data.local.QuizResultEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertResult(@org.jetbrains.annotations.NotNull()
    com.theword.app.data.local.QuizResultEntity result, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM quiz_results")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTotalQuizzesTaken(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM quiz_results ORDER BY dateKey DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllResults(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.theword.app.data.local.QuizResultEntity>> $completion);
}
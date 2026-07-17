package com.theword.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\n@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000e@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0012@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0019"}, d2 = {"Lcom/theword/app/TheWordApplication;", "Landroid/app/Application;", "()V", "applicationScope", "Lkotlinx/coroutines/CoroutineScope;", "<set-?>", "Lcom/theword/app/data/embedded/BundledBibleProvider;", "bundledBibleProvider", "getBundledBibleProvider", "()Lcom/theword/app/data/embedded/BundledBibleProvider;", "Lcom/theword/app/data/local/AppDatabase;", "database", "getDatabase", "()Lcom/theword/app/data/local/AppDatabase;", "Lcom/theword/app/data/local/PreferencesManager;", "preferencesManager", "getPreferencesManager", "()Lcom/theword/app/data/local/PreferencesManager;", "Lcom/theword/app/data/repository/BibleRepository;", "repository", "getRepository", "()Lcom/theword/app/data/repository/BibleRepository;", "onCreate", "", "Companion", "app_debug"})
public final class TheWordApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope applicationScope = null;
    private com.theword.app.data.local.AppDatabase database;
    private com.theword.app.data.local.PreferencesManager preferencesManager;
    private com.theword.app.data.repository.BibleRepository repository;
    private com.theword.app.data.embedded.BundledBibleProvider bundledBibleProvider;
    private static com.theword.app.TheWordApplication instance;
    @org.jetbrains.annotations.NotNull()
    public static final com.theword.app.TheWordApplication.Companion Companion = null;
    
    public TheWordApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.local.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.local.PreferencesManager getPreferencesManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.repository.BibleRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.theword.app.data.embedded.BundledBibleProvider getBundledBibleProvider() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/theword/app/TheWordApplication$Companion;", "", "()V", "<set-?>", "Lcom/theword/app/TheWordApplication;", "instance", "getInstance", "()Lcom/theword/app/TheWordApplication;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.theword.app.TheWordApplication getInstance() {
            return null;
        }
    }
}
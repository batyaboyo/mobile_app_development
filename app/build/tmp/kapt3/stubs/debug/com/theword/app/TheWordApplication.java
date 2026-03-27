package com.theword.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0013"}, d2 = {"Lcom/theword/app/TheWordApplication;", "Landroid/app/Application;", "()V", "<set-?>", "Lcom/theword/app/data/local/AppDatabase;", "database", "getDatabase", "()Lcom/theword/app/data/local/AppDatabase;", "Lcom/theword/app/data/local/PreferencesManager;", "preferencesManager", "getPreferencesManager", "()Lcom/theword/app/data/local/PreferencesManager;", "Lcom/theword/app/data/repository/BibleRepository;", "repository", "getRepository", "()Lcom/theword/app/data/repository/BibleRepository;", "onCreate", "", "Companion", "app_debug"})
public final class TheWordApplication extends android.app.Application {
    private com.theword.app.data.local.AppDatabase database;
    private com.theword.app.data.local.PreferencesManager preferencesManager;
    private com.theword.app.data.repository.BibleRepository repository;
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
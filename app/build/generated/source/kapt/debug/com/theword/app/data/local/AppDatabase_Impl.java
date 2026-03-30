package com.theword.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BookmarkDao _bookmarkDao;

  private volatile HighlightDao _highlightDao;

  private volatile ReadingProgressDao _readingProgressDao;

  private volatile QuizResultDao _quizResultDao;

  private volatile BibleCacheDao _bibleCacheDao;

  private volatile JournalDao _journalDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`reference` TEXT NOT NULL, `text` TEXT NOT NULL, `collection` TEXT, `bookmarkedAt` INTEGER NOT NULL, PRIMARY KEY(`reference`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `highlights` (`reference` TEXT NOT NULL, `color` TEXT NOT NULL, `note` TEXT, PRIMARY KEY(`reference`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reading_progress` (`id` TEXT NOT NULL, `bookId` TEXT NOT NULL, `chapter` INTEGER NOT NULL, `readAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_results` (`dateKey` TEXT NOT NULL, `questionsJson` TEXT NOT NULL, `answersJson` TEXT NOT NULL, `score` INTEGER NOT NULL, `total` INTEGER NOT NULL, PRIMARY KEY(`dateKey`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bible_translations_cache` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `shortName` TEXT NOT NULL, `language` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, `isDownloaded` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bible_books_cache` (`id` TEXT NOT NULL, `translationId` TEXT NOT NULL, `bookId` TEXT NOT NULL, `name` TEXT NOT NULL, `totalChapters` INTEGER NOT NULL, `order` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bible_chapters_cache` (`id` TEXT NOT NULL, `translationId` TEXT NOT NULL, `bookId` TEXT NOT NULL, `chapter` INTEGER NOT NULL, `contentJson` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `journal_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cb9117c1c268f85442dca4b2ca529ebe')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `bookmarks`");
        db.execSQL("DROP TABLE IF EXISTS `highlights`");
        db.execSQL("DROP TABLE IF EXISTS `reading_progress`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_results`");
        db.execSQL("DROP TABLE IF EXISTS `bible_translations_cache`");
        db.execSQL("DROP TABLE IF EXISTS `bible_books_cache`");
        db.execSQL("DROP TABLE IF EXISTS `bible_chapters_cache`");
        db.execSQL("DROP TABLE IF EXISTS `journal_entries`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBookmarks = new HashMap<String, TableInfo.Column>(4);
        _columnsBookmarks.put("reference", new TableInfo.Column("reference", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("collection", new TableInfo.Column("collection", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("bookmarkedAt", new TableInfo.Column("bookmarkedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookmarks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBookmarks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBookmarks = new TableInfo("bookmarks", _columnsBookmarks, _foreignKeysBookmarks, _indicesBookmarks);
        final TableInfo _existingBookmarks = TableInfo.read(db, "bookmarks");
        if (!_infoBookmarks.equals(_existingBookmarks)) {
          return new RoomOpenHelper.ValidationResult(false, "bookmarks(com.theword.app.data.local.BookmarkEntity).\n"
                  + " Expected:\n" + _infoBookmarks + "\n"
                  + " Found:\n" + _existingBookmarks);
        }
        final HashMap<String, TableInfo.Column> _columnsHighlights = new HashMap<String, TableInfo.Column>(3);
        _columnsHighlights.put("reference", new TableInfo.Column("reference", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHighlights = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHighlights = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHighlights = new TableInfo("highlights", _columnsHighlights, _foreignKeysHighlights, _indicesHighlights);
        final TableInfo _existingHighlights = TableInfo.read(db, "highlights");
        if (!_infoHighlights.equals(_existingHighlights)) {
          return new RoomOpenHelper.ValidationResult(false, "highlights(com.theword.app.data.local.HighlightEntity).\n"
                  + " Expected:\n" + _infoHighlights + "\n"
                  + " Found:\n" + _existingHighlights);
        }
        final HashMap<String, TableInfo.Column> _columnsReadingProgress = new HashMap<String, TableInfo.Column>(4);
        _columnsReadingProgress.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingProgress.put("bookId", new TableInfo.Column("bookId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingProgress.put("chapter", new TableInfo.Column("chapter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingProgress.put("readAt", new TableInfo.Column("readAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReadingProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReadingProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReadingProgress = new TableInfo("reading_progress", _columnsReadingProgress, _foreignKeysReadingProgress, _indicesReadingProgress);
        final TableInfo _existingReadingProgress = TableInfo.read(db, "reading_progress");
        if (!_infoReadingProgress.equals(_existingReadingProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "reading_progress(com.theword.app.data.local.ReadingProgressEntity).\n"
                  + " Expected:\n" + _infoReadingProgress + "\n"
                  + " Found:\n" + _existingReadingProgress);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizResults = new HashMap<String, TableInfo.Column>(5);
        _columnsQuizResults.put("dateKey", new TableInfo.Column("dateKey", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("questionsJson", new TableInfo.Column("questionsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("answersJson", new TableInfo.Column("answersJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("total", new TableInfo.Column("total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizResults = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuizResults = new TableInfo("quiz_results", _columnsQuizResults, _foreignKeysQuizResults, _indicesQuizResults);
        final TableInfo _existingQuizResults = TableInfo.read(db, "quiz_results");
        if (!_infoQuizResults.equals(_existingQuizResults)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_results(com.theword.app.data.local.QuizResultEntity).\n"
                  + " Expected:\n" + _infoQuizResults + "\n"
                  + " Found:\n" + _existingQuizResults);
        }
        final HashMap<String, TableInfo.Column> _columnsBibleTranslationsCache = new HashMap<String, TableInfo.Column>(6);
        _columnsBibleTranslationsCache.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleTranslationsCache.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleTranslationsCache.put("shortName", new TableInfo.Column("shortName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleTranslationsCache.put("language", new TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleTranslationsCache.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleTranslationsCache.put("isDownloaded", new TableInfo.Column("isDownloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBibleTranslationsCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBibleTranslationsCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBibleTranslationsCache = new TableInfo("bible_translations_cache", _columnsBibleTranslationsCache, _foreignKeysBibleTranslationsCache, _indicesBibleTranslationsCache);
        final TableInfo _existingBibleTranslationsCache = TableInfo.read(db, "bible_translations_cache");
        if (!_infoBibleTranslationsCache.equals(_existingBibleTranslationsCache)) {
          return new RoomOpenHelper.ValidationResult(false, "bible_translations_cache(com.theword.app.data.local.TranslationCacheEntity).\n"
                  + " Expected:\n" + _infoBibleTranslationsCache + "\n"
                  + " Found:\n" + _existingBibleTranslationsCache);
        }
        final HashMap<String, TableInfo.Column> _columnsBibleBooksCache = new HashMap<String, TableInfo.Column>(7);
        _columnsBibleBooksCache.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("translationId", new TableInfo.Column("translationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("bookId", new TableInfo.Column("bookId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("totalChapters", new TableInfo.Column("totalChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleBooksCache.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBibleBooksCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBibleBooksCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBibleBooksCache = new TableInfo("bible_books_cache", _columnsBibleBooksCache, _foreignKeysBibleBooksCache, _indicesBibleBooksCache);
        final TableInfo _existingBibleBooksCache = TableInfo.read(db, "bible_books_cache");
        if (!_infoBibleBooksCache.equals(_existingBibleBooksCache)) {
          return new RoomOpenHelper.ValidationResult(false, "bible_books_cache(com.theword.app.data.local.BookCacheEntity).\n"
                  + " Expected:\n" + _infoBibleBooksCache + "\n"
                  + " Found:\n" + _existingBibleBooksCache);
        }
        final HashMap<String, TableInfo.Column> _columnsBibleChaptersCache = new HashMap<String, TableInfo.Column>(6);
        _columnsBibleChaptersCache.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleChaptersCache.put("translationId", new TableInfo.Column("translationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleChaptersCache.put("bookId", new TableInfo.Column("bookId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleChaptersCache.put("chapter", new TableInfo.Column("chapter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleChaptersCache.put("contentJson", new TableInfo.Column("contentJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBibleChaptersCache.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBibleChaptersCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBibleChaptersCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBibleChaptersCache = new TableInfo("bible_chapters_cache", _columnsBibleChaptersCache, _foreignKeysBibleChaptersCache, _indicesBibleChaptersCache);
        final TableInfo _existingBibleChaptersCache = TableInfo.read(db, "bible_chapters_cache");
        if (!_infoBibleChaptersCache.equals(_existingBibleChaptersCache)) {
          return new RoomOpenHelper.ValidationResult(false, "bible_chapters_cache(com.theword.app.data.local.ChapterCacheEntity).\n"
                  + " Expected:\n" + _infoBibleChaptersCache + "\n"
                  + " Found:\n" + _existingBibleChaptersCache);
        }
        final HashMap<String, TableInfo.Column> _columnsJournalEntries = new HashMap<String, TableInfo.Column>(4);
        _columnsJournalEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJournalEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesJournalEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoJournalEntries = new TableInfo("journal_entries", _columnsJournalEntries, _foreignKeysJournalEntries, _indicesJournalEntries);
        final TableInfo _existingJournalEntries = TableInfo.read(db, "journal_entries");
        if (!_infoJournalEntries.equals(_existingJournalEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "journal_entries(com.theword.app.data.local.JournalEntryEntity).\n"
                  + " Expected:\n" + _infoJournalEntries + "\n"
                  + " Found:\n" + _existingJournalEntries);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cb9117c1c268f85442dca4b2ca529ebe", "bd2601cf1dfbffd0f0c029e23f0c2bf5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "bookmarks","highlights","reading_progress","quiz_results","bible_translations_cache","bible_books_cache","bible_chapters_cache","journal_entries");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `bookmarks`");
      _db.execSQL("DELETE FROM `highlights`");
      _db.execSQL("DELETE FROM `reading_progress`");
      _db.execSQL("DELETE FROM `quiz_results`");
      _db.execSQL("DELETE FROM `bible_translations_cache`");
      _db.execSQL("DELETE FROM `bible_books_cache`");
      _db.execSQL("DELETE FROM `bible_chapters_cache`");
      _db.execSQL("DELETE FROM `journal_entries`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BookmarkDao.class, BookmarkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HighlightDao.class, HighlightDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReadingProgressDao.class, ReadingProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizResultDao.class, QuizResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BibleCacheDao.class, BibleCacheDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(JournalDao.class, JournalDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BookmarkDao bookmarkDao() {
    if (_bookmarkDao != null) {
      return _bookmarkDao;
    } else {
      synchronized(this) {
        if(_bookmarkDao == null) {
          _bookmarkDao = new BookmarkDao_Impl(this);
        }
        return _bookmarkDao;
      }
    }
  }

  @Override
  public HighlightDao highlightDao() {
    if (_highlightDao != null) {
      return _highlightDao;
    } else {
      synchronized(this) {
        if(_highlightDao == null) {
          _highlightDao = new HighlightDao_Impl(this);
        }
        return _highlightDao;
      }
    }
  }

  @Override
  public ReadingProgressDao readingProgressDao() {
    if (_readingProgressDao != null) {
      return _readingProgressDao;
    } else {
      synchronized(this) {
        if(_readingProgressDao == null) {
          _readingProgressDao = new ReadingProgressDao_Impl(this);
        }
        return _readingProgressDao;
      }
    }
  }

  @Override
  public QuizResultDao quizResultDao() {
    if (_quizResultDao != null) {
      return _quizResultDao;
    } else {
      synchronized(this) {
        if(_quizResultDao == null) {
          _quizResultDao = new QuizResultDao_Impl(this);
        }
        return _quizResultDao;
      }
    }
  }

  @Override
  public BibleCacheDao bibleCacheDao() {
    if (_bibleCacheDao != null) {
      return _bibleCacheDao;
    } else {
      synchronized(this) {
        if(_bibleCacheDao == null) {
          _bibleCacheDao = new BibleCacheDao_Impl(this);
        }
        return _bibleCacheDao;
      }
    }
  }

  @Override
  public JournalDao journalDao() {
    if (_journalDao != null) {
      return _journalDao;
    } else {
      synchronized(this) {
        if(_journalDao == null) {
          _journalDao = new JournalDao_Impl(this);
        }
        return _journalDao;
      }
    }
  }
}

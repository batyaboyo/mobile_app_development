package com.ugtours.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ugtours.data.local.entities.RecentlyViewedEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RecentlyViewedDao_Impl implements RecentlyViewedDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecentlyViewedEntity> __insertionAdapterOfRecentlyViewedEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearRecentlyViewed;

  private final SharedSQLiteStatement __preparedStmtOfTrimOldEntries;

  public RecentlyViewedDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecentlyViewedEntity = new EntityInsertionAdapter<RecentlyViewedEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recently_viewed` (`id`,`attractionId`,`viewedAt`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecentlyViewedEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAttractionId());
        statement.bindLong(3, entity.getViewedAt());
      }
    };
    this.__preparedStmtOfClearRecentlyViewed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recently_viewed";
        return _query;
      }
    };
    this.__preparedStmtOfTrimOldEntries = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM recently_viewed \n"
                + "        WHERE id NOT IN (\n"
                + "            SELECT id FROM recently_viewed \n"
                + "            ORDER BY viewedAt DESC \n"
                + "            LIMIT 20\n"
                + "        )\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object addRecentlyViewed(final RecentlyViewedEntity recentlyViewed,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecentlyViewedEntity.insert(recentlyViewed);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearRecentlyViewed(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearRecentlyViewed.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearRecentlyViewed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object trimOldEntries(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfTrimOldEntries.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfTrimOldEntries.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RecentlyViewedEntity>> getRecentlyViewed() {
    final String _sql = "SELECT * FROM recently_viewed ORDER BY viewedAt DESC LIMIT 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recently_viewed"}, new Callable<List<RecentlyViewedEntity>>() {
      @Override
      @NonNull
      public List<RecentlyViewedEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfViewedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "viewedAt");
          final List<RecentlyViewedEntity> _result = new ArrayList<RecentlyViewedEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecentlyViewedEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpAttractionId;
            _tmpAttractionId = _cursor.getInt(_cursorIndexOfAttractionId);
            final long _tmpViewedAt;
            _tmpViewedAt = _cursor.getLong(_cursorIndexOfViewedAt);
            _item = new RecentlyViewedEntity(_tmpId,_tmpAttractionId,_tmpViewedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentlyViewedIds(final Continuation<? super List<Integer>> $completion) {
    final String _sql = "SELECT attractionId FROM recently_viewed ORDER BY viewedAt DESC LIMIT 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Integer>>() {
      @Override
      @NonNull
      public List<Integer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Integer _item;
            _item = _cursor.getInt(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getRecentlyViewedCount() {
    final String _sql = "SELECT COUNT(*) FROM recently_viewed";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recently_viewed"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentlyViewedCountSync(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM recently_viewed";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

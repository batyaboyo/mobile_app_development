package com.b7b.sobriety.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.b7b.sobriety.data.model.CheckIn;
import java.lang.Class;
import java.lang.Exception;
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
public final class CheckInDao_Impl implements CheckInDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CheckIn> __insertionAdapterOfCheckIn;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CheckInDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCheckIn = new EntityInsertionAdapter<CheckIn>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `check_ins` (`date`,`status`,`mood`,`urge`,`note`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CheckIn entity) {
        statement.bindString(1, entity.getDate());
        statement.bindString(2, entity.getStatus());
        if (entity.getMood() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMood());
        }
        if (entity.getUrge() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUrge());
        }
        if (entity.getNote() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNote());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM check_ins";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final CheckIn checkIn, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCheckIn.insert(checkIn);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CheckIn>> getAllFlow() {
    final String _sql = "SELECT * FROM check_ins ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"check_ins"}, new Callable<List<CheckIn>>() {
      @Override
      @NonNull
      public List<CheckIn> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfUrge = CursorUtil.getColumnIndexOrThrow(_cursor, "urge");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<CheckIn> _result = new ArrayList<CheckIn>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CheckIn _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getString(_cursorIndexOfMood);
            }
            final String _tmpUrge;
            if (_cursor.isNull(_cursorIndexOfUrge)) {
              _tmpUrge = null;
            } else {
              _tmpUrge = _cursor.getString(_cursorIndexOfUrge);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new CheckIn(_tmpDate,_tmpStatus,_tmpMood,_tmpUrge,_tmpNote);
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
  public Object getAll(final Continuation<? super List<CheckIn>> $completion) {
    final String _sql = "SELECT * FROM check_ins ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CheckIn>>() {
      @Override
      @NonNull
      public List<CheckIn> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfUrge = CursorUtil.getColumnIndexOrThrow(_cursor, "urge");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<CheckIn> _result = new ArrayList<CheckIn>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CheckIn _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getString(_cursorIndexOfMood);
            }
            final String _tmpUrge;
            if (_cursor.isNull(_cursorIndexOfUrge)) {
              _tmpUrge = null;
            } else {
              _tmpUrge = _cursor.getString(_cursorIndexOfUrge);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new CheckIn(_tmpDate,_tmpStatus,_tmpMood,_tmpUrge,_tmpNote);
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
  public Object getByDate(final String date, final Continuation<? super CheckIn> $completion) {
    final String _sql = "SELECT * FROM check_ins WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CheckIn>() {
      @Override
      @Nullable
      public CheckIn call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfUrge = CursorUtil.getColumnIndexOrThrow(_cursor, "urge");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final CheckIn _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getString(_cursorIndexOfMood);
            }
            final String _tmpUrge;
            if (_cursor.isNull(_cursorIndexOfUrge)) {
              _tmpUrge = null;
            } else {
              _tmpUrge = _cursor.getString(_cursorIndexOfUrge);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _result = new CheckIn(_tmpDate,_tmpStatus,_tmpMood,_tmpUrge,_tmpNote);
          } else {
            _result = null;
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
  public Object getRange(final String startDate, final String endDate,
      final Continuation<? super List<CheckIn>> $completion) {
    final String _sql = "SELECT * FROM check_ins WHERE date BETWEEN ? AND ? ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CheckIn>>() {
      @Override
      @NonNull
      public List<CheckIn> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfUrge = CursorUtil.getColumnIndexOrThrow(_cursor, "urge");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<CheckIn> _result = new ArrayList<CheckIn>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CheckIn _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getString(_cursorIndexOfMood);
            }
            final String _tmpUrge;
            if (_cursor.isNull(_cursorIndexOfUrge)) {
              _tmpUrge = null;
            } else {
              _tmpUrge = _cursor.getString(_cursorIndexOfUrge);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new CheckIn(_tmpDate,_tmpStatus,_tmpMood,_tmpUrge,_tmpNote);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

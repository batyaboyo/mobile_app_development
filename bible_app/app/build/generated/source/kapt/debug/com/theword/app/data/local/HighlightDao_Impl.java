package com.theword.app.data.local;

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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HighlightDao_Impl implements HighlightDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HighlightEntity> __insertionAdapterOfHighlightEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteHighlight;

  public HighlightDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHighlightEntity = new EntityInsertionAdapter<HighlightEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `highlights` (`reference`,`color`,`note`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HighlightEntity entity) {
        if (entity.getReference() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getReference());
        }
        if (entity.getColor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getColor());
        }
        if (entity.getNote() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNote());
        }
      }
    };
    this.__preparedStmtOfDeleteHighlight = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM highlights WHERE reference = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertHighlight(final HighlightEntity highlight,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHighlightEntity.insert(highlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteHighlight(final String reference, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteHighlight.acquire();
        int _argIndex = 1;
        if (reference == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, reference);
        }
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
          __preparedStmtOfDeleteHighlight.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<HighlightEntity>> getAllHighlights() {
    final String _sql = "SELECT * FROM highlights";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"highlights"}, new Callable<List<HighlightEntity>>() {
      @Override
      @NonNull
      public List<HighlightEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfReference = CursorUtil.getColumnIndexOrThrow(_cursor, "reference");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<HighlightEntity> _result = new ArrayList<HighlightEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HighlightEntity _item;
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new HighlightEntity(_tmpReference,_tmpColor,_tmpNote);
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
  public Object getHighlight(final String reference,
      final Continuation<? super HighlightEntity> arg1) {
    final String _sql = "SELECT * FROM highlights WHERE reference = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (reference == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, reference);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HighlightEntity>() {
      @Override
      @Nullable
      public HighlightEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfReference = CursorUtil.getColumnIndexOrThrow(_cursor, "reference");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final HighlightEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _result = new HighlightEntity(_tmpReference,_tmpColor,_tmpNote);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

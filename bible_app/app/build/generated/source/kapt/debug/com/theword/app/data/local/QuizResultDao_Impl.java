package com.theword.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class QuizResultDao_Impl implements QuizResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuizResultEntity> __insertionAdapterOfQuizResultEntity;

  public QuizResultDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuizResultEntity = new EntityInsertionAdapter<QuizResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quiz_results` (`dateKey`,`questionsJson`,`answersJson`,`score`,`total`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizResultEntity entity) {
        if (entity.getDateKey() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDateKey());
        }
        if (entity.getQuestionsJson() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getQuestionsJson());
        }
        if (entity.getAnswersJson() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAnswersJson());
        }
        statement.bindLong(4, entity.getScore());
        statement.bindLong(5, entity.getTotal());
      }
    };
  }

  @Override
  public Object insertResult(final QuizResultEntity result, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuizResultEntity.insert(result);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object getResult(final String dateKey, final Continuation<? super QuizResultEntity> arg1) {
    final String _sql = "SELECT * FROM quiz_results WHERE dateKey = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dateKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, dateKey);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<QuizResultEntity>() {
      @Override
      @Nullable
      public QuizResultEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfQuestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "questionsJson");
          final int _cursorIndexOfAnswersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "answersJson");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final QuizResultEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final String _tmpQuestionsJson;
            if (_cursor.isNull(_cursorIndexOfQuestionsJson)) {
              _tmpQuestionsJson = null;
            } else {
              _tmpQuestionsJson = _cursor.getString(_cursorIndexOfQuestionsJson);
            }
            final String _tmpAnswersJson;
            if (_cursor.isNull(_cursorIndexOfAnswersJson)) {
              _tmpAnswersJson = null;
            } else {
              _tmpAnswersJson = _cursor.getString(_cursorIndexOfAnswersJson);
            }
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _result = new QuizResultEntity(_tmpDateKey,_tmpQuestionsJson,_tmpAnswersJson,_tmpScore,_tmpTotal);
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

  @Override
  public Object getTotalQuizzesTaken(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM quiz_results";
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
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object getAllResults(final Continuation<? super List<QuizResultEntity>> arg0) {
    final String _sql = "SELECT * FROM quiz_results ORDER BY dateKey DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuizResultEntity>>() {
      @Override
      @NonNull
      public List<QuizResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfQuestionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "questionsJson");
          final int _cursorIndexOfAnswersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "answersJson");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final List<QuizResultEntity> _result = new ArrayList<QuizResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizResultEntity _item;
            final String _tmpDateKey;
            if (_cursor.isNull(_cursorIndexOfDateKey)) {
              _tmpDateKey = null;
            } else {
              _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            }
            final String _tmpQuestionsJson;
            if (_cursor.isNull(_cursorIndexOfQuestionsJson)) {
              _tmpQuestionsJson = null;
            } else {
              _tmpQuestionsJson = _cursor.getString(_cursorIndexOfQuestionsJson);
            }
            final String _tmpAnswersJson;
            if (_cursor.isNull(_cursorIndexOfAnswersJson)) {
              _tmpAnswersJson = null;
            } else {
              _tmpAnswersJson = _cursor.getString(_cursorIndexOfAnswersJson);
            }
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new QuizResultEntity(_tmpDateKey,_tmpQuestionsJson,_tmpAnswersJson,_tmpScore,_tmpTotal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

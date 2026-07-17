package com.ugtours.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ugtours.data.local.entities.BookingEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class BookingsDao_Impl implements BookingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookingEntity> __insertionAdapterOfBookingEntity;

  private final EntityDeletionOrUpdateAdapter<BookingEntity> __deletionAdapterOfBookingEntity;

  private final EntityDeletionOrUpdateAdapter<BookingEntity> __updateAdapterOfBookingEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookingStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllUserBookings;

  public BookingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookingEntity = new EntityInsertionAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bookings` (`id`,`userId`,`attractionId`,`attractionName`,`accommodationName`,`accommodationType`,`checkInDate`,`checkOutDate`,`numberOfGuests`,`numberOfNights`,`pricePerNightUSD`,`totalPriceUSD`,`totalPriceUGX`,`status`,`contactEmail`,`contactPhone`,`specialRequests`,`bookingDate`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getAttractionId());
        statement.bindString(4, entity.getAttractionName());
        statement.bindString(5, entity.getAccommodationName());
        statement.bindString(6, entity.getAccommodationType());
        statement.bindString(7, entity.getCheckInDate());
        statement.bindString(8, entity.getCheckOutDate());
        statement.bindLong(9, entity.getNumberOfGuests());
        statement.bindLong(10, entity.getNumberOfNights());
        statement.bindDouble(11, entity.getPricePerNightUSD());
        statement.bindDouble(12, entity.getTotalPriceUSD());
        statement.bindDouble(13, entity.getTotalPriceUGX());
        statement.bindString(14, entity.getStatus());
        statement.bindString(15, entity.getContactEmail());
        statement.bindString(16, entity.getContactPhone());
        statement.bindString(17, entity.getSpecialRequests());
        statement.bindLong(18, entity.getBookingDate());
        statement.bindLong(19, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfBookingEntity = new EntityDeletionOrUpdateAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bookings` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBookingEntity = new EntityDeletionOrUpdateAdapter<BookingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bookings` SET `id` = ?,`userId` = ?,`attractionId` = ?,`attractionName` = ?,`accommodationName` = ?,`accommodationType` = ?,`checkInDate` = ?,`checkOutDate` = ?,`numberOfGuests` = ?,`numberOfNights` = ?,`pricePerNightUSD` = ?,`totalPriceUSD` = ?,`totalPriceUGX` = ?,`status` = ?,`contactEmail` = ?,`contactPhone` = ?,`specialRequests` = ?,`bookingDate` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getAttractionId());
        statement.bindString(4, entity.getAttractionName());
        statement.bindString(5, entity.getAccommodationName());
        statement.bindString(6, entity.getAccommodationType());
        statement.bindString(7, entity.getCheckInDate());
        statement.bindString(8, entity.getCheckOutDate());
        statement.bindLong(9, entity.getNumberOfGuests());
        statement.bindLong(10, entity.getNumberOfNights());
        statement.bindDouble(11, entity.getPricePerNightUSD());
        statement.bindDouble(12, entity.getTotalPriceUSD());
        statement.bindDouble(13, entity.getTotalPriceUGX());
        statement.bindString(14, entity.getStatus());
        statement.bindString(15, entity.getContactEmail());
        statement.bindString(16, entity.getContactPhone());
        statement.bindString(17, entity.getSpecialRequests());
        statement.bindLong(18, entity.getBookingDate());
        statement.bindLong(19, entity.getUpdatedAt());
        statement.bindLong(20, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateBookingStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE bookings SET status = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllUserBookings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bookings WHERE userId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBooking(final BookingEntity booking,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBookingEntity.insertAndReturnId(booking);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBooking(final BookingEntity booking,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBookingEntity.handle(booking);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBooking(final BookingEntity booking,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBookingEntity.handle(booking);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBookingStatus(final long bookingId, final String status, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookingStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, bookingId);
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
          __preparedStmtOfUpdateBookingStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllUserBookings(final long userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllUserBookings.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
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
          __preparedStmtOfDeleteAllUserBookings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BookingEntity>> getUserBookings(final long userId) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? ORDER BY bookingDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfAttractionName = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionName");
          final int _cursorIndexOfAccommodationName = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationName");
          final int _cursorIndexOfAccommodationType = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationType");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfNumberOfGuests = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfGuests");
          final int _cursorIndexOfNumberOfNights = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfNights");
          final int _cursorIndexOfPricePerNightUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerNightUSD");
          final int _cursorIndexOfTotalPriceUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUSD");
          final int _cursorIndexOfTotalPriceUGX = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUGX");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactEmail");
          final int _cursorIndexOfContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contactPhone");
          final int _cursorIndexOfSpecialRequests = CursorUtil.getColumnIndexOrThrow(_cursor, "specialRequests");
          final int _cursorIndexOfBookingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingDate");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpAttractionId;
            _tmpAttractionId = _cursor.getString(_cursorIndexOfAttractionId);
            final String _tmpAttractionName;
            _tmpAttractionName = _cursor.getString(_cursorIndexOfAttractionName);
            final String _tmpAccommodationName;
            _tmpAccommodationName = _cursor.getString(_cursorIndexOfAccommodationName);
            final String _tmpAccommodationType;
            _tmpAccommodationType = _cursor.getString(_cursorIndexOfAccommodationType);
            final String _tmpCheckInDate;
            _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            final String _tmpCheckOutDate;
            _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            final int _tmpNumberOfGuests;
            _tmpNumberOfGuests = _cursor.getInt(_cursorIndexOfNumberOfGuests);
            final int _tmpNumberOfNights;
            _tmpNumberOfNights = _cursor.getInt(_cursorIndexOfNumberOfNights);
            final double _tmpPricePerNightUSD;
            _tmpPricePerNightUSD = _cursor.getDouble(_cursorIndexOfPricePerNightUSD);
            final double _tmpTotalPriceUSD;
            _tmpTotalPriceUSD = _cursor.getDouble(_cursorIndexOfTotalPriceUSD);
            final double _tmpTotalPriceUGX;
            _tmpTotalPriceUGX = _cursor.getDouble(_cursorIndexOfTotalPriceUGX);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpContactEmail;
            _tmpContactEmail = _cursor.getString(_cursorIndexOfContactEmail);
            final String _tmpContactPhone;
            _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            final String _tmpSpecialRequests;
            _tmpSpecialRequests = _cursor.getString(_cursorIndexOfSpecialRequests);
            final long _tmpBookingDate;
            _tmpBookingDate = _cursor.getLong(_cursorIndexOfBookingDate);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BookingEntity(_tmpId,_tmpUserId,_tmpAttractionId,_tmpAttractionName,_tmpAccommodationName,_tmpAccommodationType,_tmpCheckInDate,_tmpCheckOutDate,_tmpNumberOfGuests,_tmpNumberOfNights,_tmpPricePerNightUSD,_tmpTotalPriceUSD,_tmpTotalPriceUGX,_tmpStatus,_tmpContactEmail,_tmpContactPhone,_tmpSpecialRequests,_tmpBookingDate,_tmpUpdatedAt);
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
  public Object getBookingById(final long bookingId,
      final Continuation<? super BookingEntity> $completion) {
    final String _sql = "SELECT * FROM bookings WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookingId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BookingEntity>() {
      @Override
      @Nullable
      public BookingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfAttractionName = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionName");
          final int _cursorIndexOfAccommodationName = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationName");
          final int _cursorIndexOfAccommodationType = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationType");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfNumberOfGuests = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfGuests");
          final int _cursorIndexOfNumberOfNights = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfNights");
          final int _cursorIndexOfPricePerNightUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerNightUSD");
          final int _cursorIndexOfTotalPriceUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUSD");
          final int _cursorIndexOfTotalPriceUGX = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUGX");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactEmail");
          final int _cursorIndexOfContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contactPhone");
          final int _cursorIndexOfSpecialRequests = CursorUtil.getColumnIndexOrThrow(_cursor, "specialRequests");
          final int _cursorIndexOfBookingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingDate");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final BookingEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpAttractionId;
            _tmpAttractionId = _cursor.getString(_cursorIndexOfAttractionId);
            final String _tmpAttractionName;
            _tmpAttractionName = _cursor.getString(_cursorIndexOfAttractionName);
            final String _tmpAccommodationName;
            _tmpAccommodationName = _cursor.getString(_cursorIndexOfAccommodationName);
            final String _tmpAccommodationType;
            _tmpAccommodationType = _cursor.getString(_cursorIndexOfAccommodationType);
            final String _tmpCheckInDate;
            _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            final String _tmpCheckOutDate;
            _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            final int _tmpNumberOfGuests;
            _tmpNumberOfGuests = _cursor.getInt(_cursorIndexOfNumberOfGuests);
            final int _tmpNumberOfNights;
            _tmpNumberOfNights = _cursor.getInt(_cursorIndexOfNumberOfNights);
            final double _tmpPricePerNightUSD;
            _tmpPricePerNightUSD = _cursor.getDouble(_cursorIndexOfPricePerNightUSD);
            final double _tmpTotalPriceUSD;
            _tmpTotalPriceUSD = _cursor.getDouble(_cursorIndexOfTotalPriceUSD);
            final double _tmpTotalPriceUGX;
            _tmpTotalPriceUGX = _cursor.getDouble(_cursorIndexOfTotalPriceUGX);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpContactEmail;
            _tmpContactEmail = _cursor.getString(_cursorIndexOfContactEmail);
            final String _tmpContactPhone;
            _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            final String _tmpSpecialRequests;
            _tmpSpecialRequests = _cursor.getString(_cursorIndexOfSpecialRequests);
            final long _tmpBookingDate;
            _tmpBookingDate = _cursor.getLong(_cursorIndexOfBookingDate);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new BookingEntity(_tmpId,_tmpUserId,_tmpAttractionId,_tmpAttractionName,_tmpAccommodationName,_tmpAccommodationType,_tmpCheckInDate,_tmpCheckOutDate,_tmpNumberOfGuests,_tmpNumberOfNights,_tmpPricePerNightUSD,_tmpTotalPriceUSD,_tmpTotalPriceUGX,_tmpStatus,_tmpContactEmail,_tmpContactPhone,_tmpSpecialRequests,_tmpBookingDate,_tmpUpdatedAt);
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
  public Flow<List<BookingEntity>> getUserBookingsByStatus(final long userId, final String status) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? AND status = ? ORDER BY bookingDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfAttractionName = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionName");
          final int _cursorIndexOfAccommodationName = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationName");
          final int _cursorIndexOfAccommodationType = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationType");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfNumberOfGuests = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfGuests");
          final int _cursorIndexOfNumberOfNights = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfNights");
          final int _cursorIndexOfPricePerNightUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerNightUSD");
          final int _cursorIndexOfTotalPriceUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUSD");
          final int _cursorIndexOfTotalPriceUGX = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUGX");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactEmail");
          final int _cursorIndexOfContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contactPhone");
          final int _cursorIndexOfSpecialRequests = CursorUtil.getColumnIndexOrThrow(_cursor, "specialRequests");
          final int _cursorIndexOfBookingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingDate");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpAttractionId;
            _tmpAttractionId = _cursor.getString(_cursorIndexOfAttractionId);
            final String _tmpAttractionName;
            _tmpAttractionName = _cursor.getString(_cursorIndexOfAttractionName);
            final String _tmpAccommodationName;
            _tmpAccommodationName = _cursor.getString(_cursorIndexOfAccommodationName);
            final String _tmpAccommodationType;
            _tmpAccommodationType = _cursor.getString(_cursorIndexOfAccommodationType);
            final String _tmpCheckInDate;
            _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            final String _tmpCheckOutDate;
            _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            final int _tmpNumberOfGuests;
            _tmpNumberOfGuests = _cursor.getInt(_cursorIndexOfNumberOfGuests);
            final int _tmpNumberOfNights;
            _tmpNumberOfNights = _cursor.getInt(_cursorIndexOfNumberOfNights);
            final double _tmpPricePerNightUSD;
            _tmpPricePerNightUSD = _cursor.getDouble(_cursorIndexOfPricePerNightUSD);
            final double _tmpTotalPriceUSD;
            _tmpTotalPriceUSD = _cursor.getDouble(_cursorIndexOfTotalPriceUSD);
            final double _tmpTotalPriceUGX;
            _tmpTotalPriceUGX = _cursor.getDouble(_cursorIndexOfTotalPriceUGX);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpContactEmail;
            _tmpContactEmail = _cursor.getString(_cursorIndexOfContactEmail);
            final String _tmpContactPhone;
            _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            final String _tmpSpecialRequests;
            _tmpSpecialRequests = _cursor.getString(_cursorIndexOfSpecialRequests);
            final long _tmpBookingDate;
            _tmpBookingDate = _cursor.getLong(_cursorIndexOfBookingDate);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BookingEntity(_tmpId,_tmpUserId,_tmpAttractionId,_tmpAttractionName,_tmpAccommodationName,_tmpAccommodationType,_tmpCheckInDate,_tmpCheckOutDate,_tmpNumberOfGuests,_tmpNumberOfNights,_tmpPricePerNightUSD,_tmpTotalPriceUSD,_tmpTotalPriceUGX,_tmpStatus,_tmpContactEmail,_tmpContactPhone,_tmpSpecialRequests,_tmpBookingDate,_tmpUpdatedAt);
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
  public Flow<List<BookingEntity>> getUpcomingBookings(final long userId) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? AND status = 'CONFIRMED' ORDER BY checkInDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfAttractionName = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionName");
          final int _cursorIndexOfAccommodationName = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationName");
          final int _cursorIndexOfAccommodationType = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationType");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfNumberOfGuests = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfGuests");
          final int _cursorIndexOfNumberOfNights = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfNights");
          final int _cursorIndexOfPricePerNightUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerNightUSD");
          final int _cursorIndexOfTotalPriceUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUSD");
          final int _cursorIndexOfTotalPriceUGX = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUGX");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactEmail");
          final int _cursorIndexOfContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contactPhone");
          final int _cursorIndexOfSpecialRequests = CursorUtil.getColumnIndexOrThrow(_cursor, "specialRequests");
          final int _cursorIndexOfBookingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingDate");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpAttractionId;
            _tmpAttractionId = _cursor.getString(_cursorIndexOfAttractionId);
            final String _tmpAttractionName;
            _tmpAttractionName = _cursor.getString(_cursorIndexOfAttractionName);
            final String _tmpAccommodationName;
            _tmpAccommodationName = _cursor.getString(_cursorIndexOfAccommodationName);
            final String _tmpAccommodationType;
            _tmpAccommodationType = _cursor.getString(_cursorIndexOfAccommodationType);
            final String _tmpCheckInDate;
            _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            final String _tmpCheckOutDate;
            _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            final int _tmpNumberOfGuests;
            _tmpNumberOfGuests = _cursor.getInt(_cursorIndexOfNumberOfGuests);
            final int _tmpNumberOfNights;
            _tmpNumberOfNights = _cursor.getInt(_cursorIndexOfNumberOfNights);
            final double _tmpPricePerNightUSD;
            _tmpPricePerNightUSD = _cursor.getDouble(_cursorIndexOfPricePerNightUSD);
            final double _tmpTotalPriceUSD;
            _tmpTotalPriceUSD = _cursor.getDouble(_cursorIndexOfTotalPriceUSD);
            final double _tmpTotalPriceUGX;
            _tmpTotalPriceUGX = _cursor.getDouble(_cursorIndexOfTotalPriceUGX);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpContactEmail;
            _tmpContactEmail = _cursor.getString(_cursorIndexOfContactEmail);
            final String _tmpContactPhone;
            _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            final String _tmpSpecialRequests;
            _tmpSpecialRequests = _cursor.getString(_cursorIndexOfSpecialRequests);
            final long _tmpBookingDate;
            _tmpBookingDate = _cursor.getLong(_cursorIndexOfBookingDate);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BookingEntity(_tmpId,_tmpUserId,_tmpAttractionId,_tmpAttractionName,_tmpAccommodationName,_tmpAccommodationType,_tmpCheckInDate,_tmpCheckOutDate,_tmpNumberOfGuests,_tmpNumberOfNights,_tmpPricePerNightUSD,_tmpTotalPriceUSD,_tmpTotalPriceUGX,_tmpStatus,_tmpContactEmail,_tmpContactPhone,_tmpSpecialRequests,_tmpBookingDate,_tmpUpdatedAt);
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
  public Object getActiveBookingsCount(final long userId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM bookings WHERE userId = ? AND status = 'CONFIRMED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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

  @Override
  public Object getTotalSpentUSD(final long userId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(totalPriceUSD) FROM bookings WHERE userId = ? AND status = 'CONFIRMED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
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
    }, $completion);
  }

  @Override
  public Flow<List<BookingEntity>> getAttractionBookings(final long userId,
      final String attractionId) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? AND attractionId = ? ORDER BY bookingDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindString(_argIndex, attractionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<BookingEntity>>() {
      @Override
      @NonNull
      public List<BookingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfAttractionName = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionName");
          final int _cursorIndexOfAccommodationName = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationName");
          final int _cursorIndexOfAccommodationType = CursorUtil.getColumnIndexOrThrow(_cursor, "accommodationType");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfNumberOfGuests = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfGuests");
          final int _cursorIndexOfNumberOfNights = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfNights");
          final int _cursorIndexOfPricePerNightUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "pricePerNightUSD");
          final int _cursorIndexOfTotalPriceUSD = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUSD");
          final int _cursorIndexOfTotalPriceUGX = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPriceUGX");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contactEmail");
          final int _cursorIndexOfContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contactPhone");
          final int _cursorIndexOfSpecialRequests = CursorUtil.getColumnIndexOrThrow(_cursor, "specialRequests");
          final int _cursorIndexOfBookingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingDate");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<BookingEntity> _result = new ArrayList<BookingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpAttractionId;
            _tmpAttractionId = _cursor.getString(_cursorIndexOfAttractionId);
            final String _tmpAttractionName;
            _tmpAttractionName = _cursor.getString(_cursorIndexOfAttractionName);
            final String _tmpAccommodationName;
            _tmpAccommodationName = _cursor.getString(_cursorIndexOfAccommodationName);
            final String _tmpAccommodationType;
            _tmpAccommodationType = _cursor.getString(_cursorIndexOfAccommodationType);
            final String _tmpCheckInDate;
            _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            final String _tmpCheckOutDate;
            _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            final int _tmpNumberOfGuests;
            _tmpNumberOfGuests = _cursor.getInt(_cursorIndexOfNumberOfGuests);
            final int _tmpNumberOfNights;
            _tmpNumberOfNights = _cursor.getInt(_cursorIndexOfNumberOfNights);
            final double _tmpPricePerNightUSD;
            _tmpPricePerNightUSD = _cursor.getDouble(_cursorIndexOfPricePerNightUSD);
            final double _tmpTotalPriceUSD;
            _tmpTotalPriceUSD = _cursor.getDouble(_cursorIndexOfTotalPriceUSD);
            final double _tmpTotalPriceUGX;
            _tmpTotalPriceUGX = _cursor.getDouble(_cursorIndexOfTotalPriceUGX);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpContactEmail;
            _tmpContactEmail = _cursor.getString(_cursorIndexOfContactEmail);
            final String _tmpContactPhone;
            _tmpContactPhone = _cursor.getString(_cursorIndexOfContactPhone);
            final String _tmpSpecialRequests;
            _tmpSpecialRequests = _cursor.getString(_cursorIndexOfSpecialRequests);
            final long _tmpBookingDate;
            _tmpBookingDate = _cursor.getLong(_cursorIndexOfBookingDate);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BookingEntity(_tmpId,_tmpUserId,_tmpAttractionId,_tmpAttractionName,_tmpAccommodationName,_tmpAccommodationType,_tmpCheckInDate,_tmpCheckOutDate,_tmpNumberOfGuests,_tmpNumberOfNights,_tmpPricePerNightUSD,_tmpTotalPriceUSD,_tmpTotalPriceUGX,_tmpStatus,_tmpContactEmail,_tmpContactPhone,_tmpSpecialRequests,_tmpBookingDate,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

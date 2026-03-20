package com.wiyadama.expensetracker.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wiyadama.expensetracker.data.entity.RentalProperty;
import java.lang.Class;
import java.lang.Exception;
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
public final class RentalPropertyDao_Impl implements RentalPropertyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RentalProperty> __insertionAdapterOfRentalProperty;

  private final EntityDeletionOrUpdateAdapter<RentalProperty> __deletionAdapterOfRentalProperty;

  private final EntityDeletionOrUpdateAdapter<RentalProperty> __updateAdapterOfRentalProperty;

  public RentalPropertyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRentalProperty = new EntityInsertionAdapter<RentalProperty>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rental_properties` (`id`,`name`,`type`,`currentTenant`,`monthlyRent`,`lastPaidDate`,`advancePayment`,`notes`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalProperty entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getType());
        if (entity.getCurrentTenant() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCurrentTenant());
        }
        statement.bindLong(5, entity.getMonthlyRent());
        if (entity.getLastPaidDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastPaidDate());
        }
        statement.bindLong(7, entity.getAdvancePayment());
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfRentalProperty = new EntityDeletionOrUpdateAdapter<RentalProperty>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rental_properties` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalProperty entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRentalProperty = new EntityDeletionOrUpdateAdapter<RentalProperty>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rental_properties` SET `id` = ?,`name` = ?,`type` = ?,`currentTenant` = ?,`monthlyRent` = ?,`lastPaidDate` = ?,`advancePayment` = ?,`notes` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalProperty entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getType());
        if (entity.getCurrentTenant() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCurrentTenant());
        }
        statement.bindLong(5, entity.getMonthlyRent());
        if (entity.getLastPaidDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastPaidDate());
        }
        statement.bindLong(7, entity.getAdvancePayment());
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertProperty(final RentalProperty property,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRentalProperty.insertAndReturnId(property);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProperty(final RentalProperty property,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRentalProperty.handle(property);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProperty(final RentalProperty property,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRentalProperty.handle(property);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RentalProperty>> getAllProperties() {
    final String _sql = "SELECT * FROM rental_properties ORDER BY type, name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rental_properties"}, new Callable<List<RentalProperty>>() {
      @Override
      @NonNull
      public List<RentalProperty> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentTenant = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTenant");
          final int _cursorIndexOfMonthlyRent = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyRent");
          final int _cursorIndexOfLastPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPaidDate");
          final int _cursorIndexOfAdvancePayment = CursorUtil.getColumnIndexOrThrow(_cursor, "advancePayment");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentalProperty> _result = new ArrayList<RentalProperty>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalProperty _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCurrentTenant;
            if (_cursor.isNull(_cursorIndexOfCurrentTenant)) {
              _tmpCurrentTenant = null;
            } else {
              _tmpCurrentTenant = _cursor.getString(_cursorIndexOfCurrentTenant);
            }
            final int _tmpMonthlyRent;
            _tmpMonthlyRent = _cursor.getInt(_cursorIndexOfMonthlyRent);
            final Long _tmpLastPaidDate;
            if (_cursor.isNull(_cursorIndexOfLastPaidDate)) {
              _tmpLastPaidDate = null;
            } else {
              _tmpLastPaidDate = _cursor.getLong(_cursorIndexOfLastPaidDate);
            }
            final int _tmpAdvancePayment;
            _tmpAdvancePayment = _cursor.getInt(_cursorIndexOfAdvancePayment);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentalProperty(_tmpId,_tmpName,_tmpType,_tmpCurrentTenant,_tmpMonthlyRent,_tmpLastPaidDate,_tmpAdvancePayment,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<RentalProperty>> getPropertiesByType(final String type) {
    final String _sql = "SELECT * FROM rental_properties WHERE type = ? ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rental_properties"}, new Callable<List<RentalProperty>>() {
      @Override
      @NonNull
      public List<RentalProperty> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentTenant = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTenant");
          final int _cursorIndexOfMonthlyRent = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyRent");
          final int _cursorIndexOfLastPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPaidDate");
          final int _cursorIndexOfAdvancePayment = CursorUtil.getColumnIndexOrThrow(_cursor, "advancePayment");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentalProperty> _result = new ArrayList<RentalProperty>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalProperty _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCurrentTenant;
            if (_cursor.isNull(_cursorIndexOfCurrentTenant)) {
              _tmpCurrentTenant = null;
            } else {
              _tmpCurrentTenant = _cursor.getString(_cursorIndexOfCurrentTenant);
            }
            final int _tmpMonthlyRent;
            _tmpMonthlyRent = _cursor.getInt(_cursorIndexOfMonthlyRent);
            final Long _tmpLastPaidDate;
            if (_cursor.isNull(_cursorIndexOfLastPaidDate)) {
              _tmpLastPaidDate = null;
            } else {
              _tmpLastPaidDate = _cursor.getLong(_cursorIndexOfLastPaidDate);
            }
            final int _tmpAdvancePayment;
            _tmpAdvancePayment = _cursor.getInt(_cursorIndexOfAdvancePayment);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentalProperty(_tmpId,_tmpName,_tmpType,_tmpCurrentTenant,_tmpMonthlyRent,_tmpLastPaidDate,_tmpAdvancePayment,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getPropertyById(final long id,
      final Continuation<? super RentalProperty> $completion) {
    final String _sql = "SELECT * FROM rental_properties WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RentalProperty>() {
      @Override
      @Nullable
      public RentalProperty call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCurrentTenant = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTenant");
          final int _cursorIndexOfMonthlyRent = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyRent");
          final int _cursorIndexOfLastPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPaidDate");
          final int _cursorIndexOfAdvancePayment = CursorUtil.getColumnIndexOrThrow(_cursor, "advancePayment");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final RentalProperty _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCurrentTenant;
            if (_cursor.isNull(_cursorIndexOfCurrentTenant)) {
              _tmpCurrentTenant = null;
            } else {
              _tmpCurrentTenant = _cursor.getString(_cursorIndexOfCurrentTenant);
            }
            final int _tmpMonthlyRent;
            _tmpMonthlyRent = _cursor.getInt(_cursorIndexOfMonthlyRent);
            final Long _tmpLastPaidDate;
            if (_cursor.isNull(_cursorIndexOfLastPaidDate)) {
              _tmpLastPaidDate = null;
            } else {
              _tmpLastPaidDate = _cursor.getLong(_cursorIndexOfLastPaidDate);
            }
            final int _tmpAdvancePayment;
            _tmpAdvancePayment = _cursor.getInt(_cursorIndexOfAdvancePayment);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RentalProperty(_tmpId,_tmpName,_tmpType,_tmpCurrentTenant,_tmpMonthlyRent,_tmpLastPaidDate,_tmpAdvancePayment,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

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
import com.wiyadama.expensetracker.data.entity.Income;
import java.lang.Class;
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
public final class IncomeDao_Impl implements IncomeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Income> __insertionAdapterOfIncome;

  private final EntityDeletionOrUpdateAdapter<Income> __deletionAdapterOfIncome;

  private final EntityDeletionOrUpdateAdapter<Income> __updateAdapterOfIncome;

  public IncomeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfIncome = new EntityInsertionAdapter<Income>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `incomes` (`id`,`dateTime`,`amountCents`,`currency`,`categoryType`,`propertyId`,`notes`,`paymentMethod`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Income entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDateTime());
        statement.bindLong(3, entity.getAmountCents());
        statement.bindString(4, entity.getCurrency());
        statement.bindString(5, entity.getCategoryType());
        if (entity.getPropertyId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPropertyId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindString(8, entity.getPaymentMethod());
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfIncome = new EntityDeletionOrUpdateAdapter<Income>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `incomes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Income entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfIncome = new EntityDeletionOrUpdateAdapter<Income>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `incomes` SET `id` = ?,`dateTime` = ?,`amountCents` = ?,`currency` = ?,`categoryType` = ?,`propertyId` = ?,`notes` = ?,`paymentMethod` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Income entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDateTime());
        statement.bindLong(3, entity.getAmountCents());
        statement.bindString(4, entity.getCurrency());
        statement.bindString(5, entity.getCategoryType());
        if (entity.getPropertyId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPropertyId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindString(8, entity.getPaymentMethod());
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertIncome(final Income income, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfIncome.insertAndReturnId(income);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteIncome(final Income income, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfIncome.handle(income);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateIncome(final Income income, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfIncome.handle(income);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Income>> getAllIncomes() {
    final String _sql = "SELECT * FROM incomes ORDER BY dateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"incomes"}, new Callable<List<Income>>() {
      @Override
      @NonNull
      public List<Income> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfAmountCents = CursorUtil.getColumnIndexOrThrow(_cursor, "amountCents");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Income> _result = new ArrayList<Income>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Income _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            final int _tmpAmountCents;
            _tmpAmountCents = _cursor.getInt(_cursorIndexOfAmountCents);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpPropertyId;
            if (_cursor.isNull(_cursorIndexOfPropertyId)) {
              _tmpPropertyId = null;
            } else {
              _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Income(_tmpId,_tmpDateTime,_tmpAmountCents,_tmpCurrency,_tmpCategoryType,_tmpPropertyId,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Income>> getIncomesByCategory(final String categoryType) {
    final String _sql = "SELECT * FROM incomes WHERE categoryType = ? ORDER BY dateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, categoryType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"incomes"}, new Callable<List<Income>>() {
      @Override
      @NonNull
      public List<Income> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfAmountCents = CursorUtil.getColumnIndexOrThrow(_cursor, "amountCents");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Income> _result = new ArrayList<Income>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Income _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            final int _tmpAmountCents;
            _tmpAmountCents = _cursor.getInt(_cursorIndexOfAmountCents);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpPropertyId;
            if (_cursor.isNull(_cursorIndexOfPropertyId)) {
              _tmpPropertyId = null;
            } else {
              _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Income(_tmpId,_tmpDateTime,_tmpAmountCents,_tmpCurrency,_tmpCategoryType,_tmpPropertyId,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Income>> getIncomesByProperty(final long propertyId) {
    final String _sql = "SELECT * FROM incomes WHERE propertyId = ? ORDER BY dateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, propertyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"incomes"}, new Callable<List<Income>>() {
      @Override
      @NonNull
      public List<Income> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfAmountCents = CursorUtil.getColumnIndexOrThrow(_cursor, "amountCents");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Income> _result = new ArrayList<Income>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Income _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            final int _tmpAmountCents;
            _tmpAmountCents = _cursor.getInt(_cursorIndexOfAmountCents);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpPropertyId;
            if (_cursor.isNull(_cursorIndexOfPropertyId)) {
              _tmpPropertyId = null;
            } else {
              _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Income(_tmpId,_tmpDateTime,_tmpAmountCents,_tmpCurrency,_tmpCategoryType,_tmpPropertyId,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getIncomeById(final long id, final Continuation<? super Income> $completion) {
    final String _sql = "SELECT * FROM incomes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Income>() {
      @Override
      @Nullable
      public Income call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfAmountCents = CursorUtil.getColumnIndexOrThrow(_cursor, "amountCents");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Income _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            final int _tmpAmountCents;
            _tmpAmountCents = _cursor.getInt(_cursorIndexOfAmountCents);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpPropertyId;
            if (_cursor.isNull(_cursorIndexOfPropertyId)) {
              _tmpPropertyId = null;
            } else {
              _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Income(_tmpId,_tmpDateTime,_tmpAmountCents,_tmpCurrency,_tmpCategoryType,_tmpPropertyId,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<Integer> getTotalByCategory(final String categoryType) {
    final String _sql = "SELECT SUM(amountCents) FROM incomes WHERE categoryType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, categoryType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"incomes"}, new Callable<Integer>() {
      @Override
      @Nullable
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

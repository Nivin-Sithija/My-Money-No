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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus;
import com.wiyadama.expensetracker.data.entity.RentTransaction;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
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
public final class RentTransactionDao_Impl implements RentTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RentTransaction> __insertionAdapterOfRentTransaction;

  private final EntityDeletionOrUpdateAdapter<RentTransaction> __deletionAdapterOfRentTransaction;

  private final EntityDeletionOrUpdateAdapter<RentTransaction> __updateAdapterOfRentTransaction;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByProperty;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePaymentStatus;

  public RentTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRentTransaction = new EntityInsertionAdapter<RentTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rent_transactions` (`id`,`propertyId`,`dueDate`,`expectedAmount`,`paidAmount`,`status`,`paidDate`,`notes`,`paymentMethod`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentTransaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPropertyId());
        statement.bindLong(3, entity.getDueDate());
        statement.bindLong(4, entity.getExpectedAmount());
        statement.bindLong(5, entity.getPaidAmount());
        statement.bindString(6, __RentPaymentStatus_enumToString(entity.getStatus()));
        if (entity.getPaidDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidDate());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPaymentMethod());
        }
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfRentTransaction = new EntityDeletionOrUpdateAdapter<RentTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rent_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentTransaction entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRentTransaction = new EntityDeletionOrUpdateAdapter<RentTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rent_transactions` SET `id` = ?,`propertyId` = ?,`dueDate` = ?,`expectedAmount` = ?,`paidAmount` = ?,`status` = ?,`paidDate` = ?,`notes` = ?,`paymentMethod` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentTransaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPropertyId());
        statement.bindLong(3, entity.getDueDate());
        statement.bindLong(4, entity.getExpectedAmount());
        statement.bindLong(5, entity.getPaidAmount());
        statement.bindString(6, __RentPaymentStatus_enumToString(entity.getStatus()));
        if (entity.getPaidDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidDate());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotes());
        }
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPaymentMethod());
        }
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteByProperty = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rent_transactions WHERE propertyId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePaymentStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE rent_transactions SET status = ?, paidAmount = ?, paidDate = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RentTransaction transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRentTransaction.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<RentTransaction> transactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRentTransaction.insert(transactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RentTransaction transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRentTransaction.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RentTransaction transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRentTransaction.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByProperty(final long propertyId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByProperty.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, propertyId);
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
          __preparedStmtOfDeleteByProperty.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePaymentStatus(final long id, final RentPaymentStatus status,
      final int paidAmount, final Long paidDate, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePaymentStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, __RentPaymentStatus_enumToString(status));
        _argIndex = 2;
        _stmt.bindLong(_argIndex, paidAmount);
        _argIndex = 3;
        if (paidDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, paidDate);
        }
        _argIndex = 4;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdatePaymentStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RentTransaction>> getAllTransactions() {
    final String _sql = "SELECT * FROM rent_transactions ORDER BY dueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent_transactions"}, new Callable<List<RentTransaction>>() {
      @Override
      @NonNull
      public List<RentTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentTransaction> _result = new ArrayList<RentTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentTransaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<RentTransaction>> getTransactionsByProperty(final long propertyId) {
    final String _sql = "SELECT * FROM rent_transactions WHERE propertyId = ? ORDER BY dueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, propertyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent_transactions"}, new Callable<List<RentTransaction>>() {
      @Override
      @NonNull
      public List<RentTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentTransaction> _result = new ArrayList<RentTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentTransaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getTransactionById(final long id,
      final Continuation<? super RentTransaction> $completion) {
    final String _sql = "SELECT * FROM rent_transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RentTransaction>() {
      @Override
      @Nullable
      public RentTransaction call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final RentTransaction _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<RentTransaction>> getTransactionsByStatus(final RentPaymentStatus status) {
    final String _sql = "SELECT * FROM rent_transactions WHERE status = ? ORDER BY dueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __RentPaymentStatus_enumToString(status));
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent_transactions"}, new Callable<List<RentTransaction>>() {
      @Override
      @NonNull
      public List<RentTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentTransaction> _result = new ArrayList<RentTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentTransaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<RentTransaction>> getTransactionsByPropertyAndStatus(final long propertyId,
      final RentPaymentStatus status) {
    final String _sql = "SELECT * FROM rent_transactions WHERE propertyId = ? AND status = ? ORDER BY dueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, propertyId);
    _argIndex = 2;
    _statement.bindString(_argIndex, __RentPaymentStatus_enumToString(status));
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent_transactions"}, new Callable<List<RentTransaction>>() {
      @Override
      @NonNull
      public List<RentTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentTransaction> _result = new ArrayList<RentTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentTransaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<RentTransaction>> getTransactionsByDateRange(final long startDate,
      final long endDate) {
    final String _sql = "SELECT * FROM rent_transactions WHERE dueDate BETWEEN ? AND ? ORDER BY dueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent_transactions"}, new Callable<List<RentTransaction>>() {
      @Override
      @NonNull
      public List<RentTransaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPropertyId = CursorUtil.getColumnIndexOrThrow(_cursor, "propertyId");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfExpectedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaidDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paidDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RentTransaction> _result = new ArrayList<RentTransaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentTransaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPropertyId;
            _tmpPropertyId = _cursor.getLong(_cursorIndexOfPropertyId);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final int _tmpExpectedAmount;
            _tmpExpectedAmount = _cursor.getInt(_cursorIndexOfExpectedAmount);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final RentPaymentStatus _tmpStatus;
            _tmpStatus = __RentPaymentStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpPaidDate;
            if (_cursor.isNull(_cursorIndexOfPaidDate)) {
              _tmpPaidDate = null;
            } else {
              _tmpPaidDate = _cursor.getLong(_cursorIndexOfPaidDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RentTransaction(_tmpId,_tmpPropertyId,_tmpDueDate,_tmpExpectedAmount,_tmpPaidAmount,_tmpStatus,_tmpPaidDate,_tmpNotes,_tmpPaymentMethod,_tmpCreatedAt,_tmpUpdatedAt);
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

  private String __RentPaymentStatus_enumToString(@NonNull final RentPaymentStatus _value) {
    switch (_value) {
      case UNPAID: return "UNPAID";
      case PARTIAL: return "PARTIAL";
      case PAID: return "PAID";
      case OVERDUE: return "OVERDUE";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private RentPaymentStatus __RentPaymentStatus_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "UNPAID": return RentPaymentStatus.UNPAID;
      case "PARTIAL": return RentPaymentStatus.PARTIAL;
      case "PAID": return RentPaymentStatus.PAID;
      case "OVERDUE": return RentPaymentStatus.OVERDUE;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}

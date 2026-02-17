package com.wiyadama.expensetracker.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wiyadama.expensetracker.data.entity.BackupMeta;
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
public final class BackupMetaDao_Impl implements BackupMetaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BackupMeta> __insertionAdapterOfBackupMeta;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldBackups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBackupMeta;

  public BackupMetaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBackupMeta = new EntityInsertionAdapter<BackupMeta>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `backup_meta` (`id`,`createdAt`,`type`,`path`,`checksum`,`size`,`appVersion`,`schemaVersion`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BackupMeta entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCreatedAt());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getPath());
        statement.bindString(5, entity.getChecksum());
        statement.bindLong(6, entity.getSize());
        statement.bindString(7, entity.getAppVersion());
        statement.bindLong(8, entity.getSchemaVersion());
      }
    };
    this.__preparedStmtOfDeleteOldBackups = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backup_meta WHERE createdAt < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBackupMeta = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backup_meta WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBackupMeta(final BackupMeta backupMeta,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBackupMeta.insertAndReturnId(backupMeta);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldBackups(final long beforeTimestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldBackups.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, beforeTimestamp);
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
          __preparedStmtOfDeleteOldBackups.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBackupMeta(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBackupMeta.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteBackupMeta.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BackupMeta>> getAllBackups() {
    final String _sql = "SELECT * FROM backup_meta ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"backup_meta"}, new Callable<List<BackupMeta>>() {
      @Override
      @NonNull
      public List<BackupMeta> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
          final int _cursorIndexOfSchemaVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "schemaVersion");
          final List<BackupMeta> _result = new ArrayList<BackupMeta>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BackupMeta _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final String _tmpChecksum;
            _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
            final long _tmpSize;
            _tmpSize = _cursor.getLong(_cursorIndexOfSize);
            final String _tmpAppVersion;
            _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
            final int _tmpSchemaVersion;
            _tmpSchemaVersion = _cursor.getInt(_cursorIndexOfSchemaVersion);
            _item = new BackupMeta(_tmpId,_tmpCreatedAt,_tmpType,_tmpPath,_tmpChecksum,_tmpSize,_tmpAppVersion,_tmpSchemaVersion);
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
  public Flow<List<BackupMeta>> getBackupsByType(final String type, final int limit) {
    final String _sql = "SELECT * FROM backup_meta WHERE type = ? ORDER BY createdAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"backup_meta"}, new Callable<List<BackupMeta>>() {
      @Override
      @NonNull
      public List<BackupMeta> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "appVersion");
          final int _cursorIndexOfSchemaVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "schemaVersion");
          final List<BackupMeta> _result = new ArrayList<BackupMeta>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BackupMeta _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpPath;
            _tmpPath = _cursor.getString(_cursorIndexOfPath);
            final String _tmpChecksum;
            _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
            final long _tmpSize;
            _tmpSize = _cursor.getLong(_cursorIndexOfSize);
            final String _tmpAppVersion;
            _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
            final int _tmpSchemaVersion;
            _tmpSchemaVersion = _cursor.getInt(_cursorIndexOfSchemaVersion);
            _item = new BackupMeta(_tmpId,_tmpCreatedAt,_tmpType,_tmpPath,_tmpChecksum,_tmpSize,_tmpAppVersion,_tmpSchemaVersion);
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

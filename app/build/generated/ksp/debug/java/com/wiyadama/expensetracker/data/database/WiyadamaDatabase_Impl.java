package com.wiyadama.expensetracker.data.database;

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
import com.wiyadama.expensetracker.data.dao.BackupMetaDao;
import com.wiyadama.expensetracker.data.dao.BackupMetaDao_Impl;
import com.wiyadama.expensetracker.data.dao.CategoryDao;
import com.wiyadama.expensetracker.data.dao.CategoryDao_Impl;
import com.wiyadama.expensetracker.data.dao.IncomeDao;
import com.wiyadama.expensetracker.data.dao.IncomeDao_Impl;
import com.wiyadama.expensetracker.data.dao.MemberDao;
import com.wiyadama.expensetracker.data.dao.MemberDao_Impl;
import com.wiyadama.expensetracker.data.dao.RentalPropertyDao;
import com.wiyadama.expensetracker.data.dao.RentalPropertyDao_Impl;
import com.wiyadama.expensetracker.data.dao.ShopDao;
import com.wiyadama.expensetracker.data.dao.ShopDao_Impl;
import com.wiyadama.expensetracker.data.dao.TransactionDao;
import com.wiyadama.expensetracker.data.dao.TransactionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WiyadamaDatabase_Impl extends WiyadamaDatabase {
  private volatile MemberDao _memberDao;

  private volatile CategoryDao _categoryDao;

  private volatile ShopDao _shopDao;

  private volatile TransactionDao _transactionDao;

  private volatile BackupMetaDao _backupMetaDao;

  private volatile IncomeDao _incomeDao;

  private volatile RentalPropertyDao _rentalPropertyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `phone` TEXT, `email` TEXT, `color` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_members_name` ON `members` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `parentId` INTEGER, `isSystem` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, `requiresMember` INTEGER NOT NULL, `color` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`parentId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_parentId_sortOrder` ON `categories` (`parentId`, `sortOrder`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_name` ON `categories` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `shops` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `address` TEXT, `lastUsedAt` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_shops_name` ON `shops` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dateTime` INTEGER NOT NULL, `amountCents` INTEGER NOT NULL, `currency` TEXT NOT NULL, `categoryId` INTEGER NOT NULL, `memberId` INTEGER, `shopId` INTEGER, `merchantName` TEXT, `paymentMethod` TEXT NOT NULL, `notes` TEXT, `deletedAt` INTEGER, `revision` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_dateTime` ON `transactions` (`dateTime`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_categoryId` ON `transactions` (`categoryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_memberId` ON `transactions` (`memberId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_shopId` ON `transactions` (`shopId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_deletedAt` ON `transactions` (`deletedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `backup_meta` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `createdAt` INTEGER NOT NULL, `type` TEXT NOT NULL, `path` TEXT NOT NULL, `checksum` TEXT NOT NULL, `size` INTEGER NOT NULL, `appVersion` TEXT NOT NULL, `schemaVersion` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `incomes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dateTime` INTEGER NOT NULL, `amountCents` INTEGER NOT NULL, `currency` TEXT NOT NULL, `categoryType` TEXT NOT NULL, `propertyId` INTEGER, `notes` TEXT, `paymentMethod` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rental_properties` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `currentTenant` TEXT, `monthlyRent` INTEGER NOT NULL, `lastPaidDate` INTEGER, `advancePayment` INTEGER NOT NULL, `notes` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4637a53bc4974a2f31d7110c1304315b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `members`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `shops`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `backup_meta`");
        db.execSQL("DROP TABLE IF EXISTS `incomes`");
        db.execSQL("DROP TABLE IF EXISTS `rental_properties`");
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
        db.execSQL("PRAGMA foreign_keys = ON");
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
        final HashMap<String, TableInfo.Column> _columnsMembers = new HashMap<String, TableInfo.Column>(7);
        _columnsMembers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("color", new TableInfo.Column("color", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMembers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMembers = new HashSet<TableInfo.Index>(1);
        _indicesMembers.add(new TableInfo.Index("index_members_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoMembers = new TableInfo("members", _columnsMembers, _foreignKeysMembers, _indicesMembers);
        final TableInfo _existingMembers = TableInfo.read(db, "members");
        if (!_infoMembers.equals(_existingMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "members(com.wiyadama.expensetracker.data.entity.Member).\n"
                  + " Expected:\n" + _infoMembers + "\n"
                  + " Found:\n" + _existingMembers);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(9);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("parentId", new TableInfo.Column("parentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("isSystem", new TableInfo.Column("isSystem", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("requiresMember", new TableInfo.Column("requiresMember", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCategories.add(new TableInfo.ForeignKey("categories", "RESTRICT", "NO ACTION", Arrays.asList("parentId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(2);
        _indicesCategories.add(new TableInfo.Index("index_categories_parentId_sortOrder", false, Arrays.asList("parentId", "sortOrder"), Arrays.asList("ASC", "ASC")));
        _indicesCategories.add(new TableInfo.Index("index_categories_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.wiyadama.expensetracker.data.entity.Category).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsShops = new HashMap<String, TableInfo.Column>(6);
        _columnsShops.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("lastUsedAt", new TableInfo.Column("lastUsedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShops = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesShops = new HashSet<TableInfo.Index>(1);
        _indicesShops.add(new TableInfo.Index("index_shops_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoShops = new TableInfo("shops", _columnsShops, _foreignKeysShops, _indicesShops);
        final TableInfo _existingShops = TableInfo.read(db, "shops");
        if (!_infoShops.equals(_existingShops)) {
          return new RoomOpenHelper.ValidationResult(false, "shops(com.wiyadama.expensetracker.data.entity.Shop).\n"
                  + " Expected:\n" + _infoShops + "\n"
                  + " Found:\n" + _existingShops);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(14);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("dateTime", new TableInfo.Column("dateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amountCents", new TableInfo.Column("amountCents", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("memberId", new TableInfo.Column("memberId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("shopId", new TableInfo.Column("shopId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("merchantName", new TableInfo.Column("merchantName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("paymentMethod", new TableInfo.Column("paymentMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("deletedAt", new TableInfo.Column("deletedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("revision", new TableInfo.Column("revision", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("categories", "RESTRICT", "NO ACTION", Arrays.asList("categoryId"), Arrays.asList("id")));
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("members", "SET NULL", "NO ACTION", Arrays.asList("memberId"), Arrays.asList("id")));
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("shops", "SET NULL", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(5);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_dateTime", false, Arrays.asList("dateTime"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_categoryId", false, Arrays.asList("categoryId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_memberId", false, Arrays.asList("memberId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_deletedAt", false, Arrays.asList("deletedAt"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.wiyadama.expensetracker.data.entity.Transaction).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsBackupMeta = new HashMap<String, TableInfo.Column>(8);
        _columnsBackupMeta.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("path", new TableInfo.Column("path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("checksum", new TableInfo.Column("checksum", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("size", new TableInfo.Column("size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("appVersion", new TableInfo.Column("appVersion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupMeta.put("schemaVersion", new TableInfo.Column("schemaVersion", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBackupMeta = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBackupMeta = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBackupMeta = new TableInfo("backup_meta", _columnsBackupMeta, _foreignKeysBackupMeta, _indicesBackupMeta);
        final TableInfo _existingBackupMeta = TableInfo.read(db, "backup_meta");
        if (!_infoBackupMeta.equals(_existingBackupMeta)) {
          return new RoomOpenHelper.ValidationResult(false, "backup_meta(com.wiyadama.expensetracker.data.entity.BackupMeta).\n"
                  + " Expected:\n" + _infoBackupMeta + "\n"
                  + " Found:\n" + _existingBackupMeta);
        }
        final HashMap<String, TableInfo.Column> _columnsIncomes = new HashMap<String, TableInfo.Column>(10);
        _columnsIncomes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("dateTime", new TableInfo.Column("dateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("amountCents", new TableInfo.Column("amountCents", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("categoryType", new TableInfo.Column("categoryType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("propertyId", new TableInfo.Column("propertyId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("paymentMethod", new TableInfo.Column("paymentMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIncomes.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIncomes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIncomes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIncomes = new TableInfo("incomes", _columnsIncomes, _foreignKeysIncomes, _indicesIncomes);
        final TableInfo _existingIncomes = TableInfo.read(db, "incomes");
        if (!_infoIncomes.equals(_existingIncomes)) {
          return new RoomOpenHelper.ValidationResult(false, "incomes(com.wiyadama.expensetracker.data.entity.Income).\n"
                  + " Expected:\n" + _infoIncomes + "\n"
                  + " Found:\n" + _existingIncomes);
        }
        final HashMap<String, TableInfo.Column> _columnsRentalProperties = new HashMap<String, TableInfo.Column>(10);
        _columnsRentalProperties.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("currentTenant", new TableInfo.Column("currentTenant", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("monthlyRent", new TableInfo.Column("monthlyRent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("lastPaidDate", new TableInfo.Column("lastPaidDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("advancePayment", new TableInfo.Column("advancePayment", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalProperties.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRentalProperties = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRentalProperties = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRentalProperties = new TableInfo("rental_properties", _columnsRentalProperties, _foreignKeysRentalProperties, _indicesRentalProperties);
        final TableInfo _existingRentalProperties = TableInfo.read(db, "rental_properties");
        if (!_infoRentalProperties.equals(_existingRentalProperties)) {
          return new RoomOpenHelper.ValidationResult(false, "rental_properties(com.wiyadama.expensetracker.data.entity.RentalProperty).\n"
                  + " Expected:\n" + _infoRentalProperties + "\n"
                  + " Found:\n" + _existingRentalProperties);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4637a53bc4974a2f31d7110c1304315b", "8b13811bfc91beada10da3b10365b75e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "members","categories","shops","transactions","backup_meta","incomes","rental_properties");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `members`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `shops`");
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `backup_meta`");
      _db.execSQL("DELETE FROM `incomes`");
      _db.execSQL("DELETE FROM `rental_properties`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
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
    _typeConvertersMap.put(MemberDao.class, MemberDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ShopDao.class, ShopDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BackupMetaDao.class, BackupMetaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(IncomeDao.class, IncomeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RentalPropertyDao.class, RentalPropertyDao_Impl.getRequiredConverters());
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
  public MemberDao memberDao() {
    if (_memberDao != null) {
      return _memberDao;
    } else {
      synchronized(this) {
        if(_memberDao == null) {
          _memberDao = new MemberDao_Impl(this);
        }
        return _memberDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public ShopDao shopDao() {
    if (_shopDao != null) {
      return _shopDao;
    } else {
      synchronized(this) {
        if(_shopDao == null) {
          _shopDao = new ShopDao_Impl(this);
        }
        return _shopDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public BackupMetaDao backupMetaDao() {
    if (_backupMetaDao != null) {
      return _backupMetaDao;
    } else {
      synchronized(this) {
        if(_backupMetaDao == null) {
          _backupMetaDao = new BackupMetaDao_Impl(this);
        }
        return _backupMetaDao;
      }
    }
  }

  @Override
  public IncomeDao incomeDao() {
    if (_incomeDao != null) {
      return _incomeDao;
    } else {
      synchronized(this) {
        if(_incomeDao == null) {
          _incomeDao = new IncomeDao_Impl(this);
        }
        return _incomeDao;
      }
    }
  }

  @Override
  public RentalPropertyDao rentalPropertyDao() {
    if (_rentalPropertyDao != null) {
      return _rentalPropertyDao;
    } else {
      synchronized(this) {
        if(_rentalPropertyDao == null) {
          _rentalPropertyDao = new RentalPropertyDao_Impl(this);
        }
        return _rentalPropertyDao;
      }
    }
  }
}

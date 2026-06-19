package com.coffeebliss.app.data.database;

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
import com.coffeebliss.app.data.dao.MemberDao;
import com.coffeebliss.app.data.dao.MemberDao_Impl;
import com.coffeebliss.app.data.dao.RedemptionDao;
import com.coffeebliss.app.data.dao.RedemptionDao_Impl;
import com.coffeebliss.app.data.dao.TransactionDao;
import com.coffeebliss.app.data.dao.TransactionDao_Impl;
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
public final class CoffeeBlissDatabase_Impl extends CoffeeBlissDatabase {
  private volatile MemberDao _memberDao;

  private volatile TransactionDao _transactionDao;

  private volatile RedemptionDao _redemptionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `phone` TEXT NOT NULL, `memberNumber` TEXT NOT NULL, `status` TEXT NOT NULL, `totalPoints` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `memberId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `pointsEarned` INTEGER NOT NULL, FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_memberId` ON `transactions` (`memberId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `redemptions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `memberId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `rewardName` TEXT NOT NULL, `pointsUsed` INTEGER NOT NULL, FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_redemptions_memberId` ON `redemptions` (`memberId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '22ee837b164a1fbe9f192de340086576')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `members`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `redemptions`");
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
        _columnsMembers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("memberNumber", new TableInfo.Column("memberNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("totalPoints", new TableInfo.Column("totalPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMembers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMembers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMembers = new TableInfo("members", _columnsMembers, _foreignKeysMembers, _indicesMembers);
        final TableInfo _existingMembers = TableInfo.read(db, "members");
        if (!_infoMembers.equals(_existingMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "members(com.coffeebliss.app.data.model.Member).\n"
                  + " Expected:\n" + _infoMembers + "\n"
                  + " Found:\n" + _existingMembers);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(5);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("memberId", new TableInfo.Column("memberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("pointsEarned", new TableInfo.Column("pointsEarned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("members", "CASCADE", "NO ACTION", Arrays.asList("memberId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(1);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_memberId", false, Arrays.asList("memberId"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.coffeebliss.app.data.model.Transaction).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsRedemptions = new HashMap<String, TableInfo.Column>(5);
        _columnsRedemptions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRedemptions.put("memberId", new TableInfo.Column("memberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRedemptions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRedemptions.put("rewardName", new TableInfo.Column("rewardName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRedemptions.put("pointsUsed", new TableInfo.Column("pointsUsed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRedemptions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRedemptions.add(new TableInfo.ForeignKey("members", "CASCADE", "NO ACTION", Arrays.asList("memberId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRedemptions = new HashSet<TableInfo.Index>(1);
        _indicesRedemptions.add(new TableInfo.Index("index_redemptions_memberId", false, Arrays.asList("memberId"), Arrays.asList("ASC")));
        final TableInfo _infoRedemptions = new TableInfo("redemptions", _columnsRedemptions, _foreignKeysRedemptions, _indicesRedemptions);
        final TableInfo _existingRedemptions = TableInfo.read(db, "redemptions");
        if (!_infoRedemptions.equals(_existingRedemptions)) {
          return new RoomOpenHelper.ValidationResult(false, "redemptions(com.coffeebliss.app.data.model.Redemption).\n"
                  + " Expected:\n" + _infoRedemptions + "\n"
                  + " Found:\n" + _existingRedemptions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "22ee837b164a1fbe9f192de340086576", "86bf2023f9e3d7c0628372ecf1a5c184");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "members","transactions","redemptions");
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
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `redemptions`");
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
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RedemptionDao.class, RedemptionDao_Impl.getRequiredConverters());
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
  public RedemptionDao redemptionDao() {
    if (_redemptionDao != null) {
      return _redemptionDao;
    } else {
      synchronized(this) {
        if(_redemptionDao == null) {
          _redemptionDao = new RedemptionDao_Impl(this);
        }
        return _redemptionDao;
      }
    }
  }
}

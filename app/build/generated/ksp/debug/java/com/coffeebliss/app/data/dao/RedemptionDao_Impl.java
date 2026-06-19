package com.coffeebliss.app.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.coffeebliss.app.data.model.Redemption;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RedemptionDao_Impl implements RedemptionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Redemption> __insertionAdapterOfRedemption;

  public RedemptionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRedemption = new EntityInsertionAdapter<Redemption>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `redemptions` (`id`,`memberId`,`date`,`rewardName`,`pointsUsed`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Redemption entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemberId());
        statement.bindLong(3, entity.getDate());
        statement.bindString(4, entity.getRewardName());
        statement.bindLong(5, entity.getPointsUsed());
      }
    };
  }

  @Override
  public Object insert(final Redemption redemption, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRedemption.insertAndReturnId(redemption);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Redemption>> getRedemptionsByMember(final long memberId) {
    final String _sql = "SELECT * FROM redemptions WHERE memberId = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memberId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"redemptions"}, new Callable<List<Redemption>>() {
      @Override
      @NonNull
      public List<Redemption> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMemberId = CursorUtil.getColumnIndexOrThrow(_cursor, "memberId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfRewardName = CursorUtil.getColumnIndexOrThrow(_cursor, "rewardName");
          final int _cursorIndexOfPointsUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "pointsUsed");
          final List<Redemption> _result = new ArrayList<Redemption>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Redemption _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMemberId;
            _tmpMemberId = _cursor.getLong(_cursorIndexOfMemberId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpRewardName;
            _tmpRewardName = _cursor.getString(_cursorIndexOfRewardName);
            final int _tmpPointsUsed;
            _tmpPointsUsed = _cursor.getInt(_cursorIndexOfPointsUsed);
            _item = new Redemption(_tmpId,_tmpMemberId,_tmpDate,_tmpRewardName,_tmpPointsUsed);
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

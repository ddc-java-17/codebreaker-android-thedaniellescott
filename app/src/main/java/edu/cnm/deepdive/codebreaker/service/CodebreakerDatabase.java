package edu.cnm.deepdive.codebreaker.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.codebreaker.model.dao.GameResultDao;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase.Converters;
import java.time.Duration;
import java.time.Instant;

@Database(entities = {GameResult.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CodebreakerDatabase extends RoomDatabase {

  public static final String NAME = "codebreaker_results";

  CodebreakerDatabase() {
    // Avoid generation of Javadoc HTML.
  }

  public abstract GameResultDao getGameResultDao();

  public static class Converters {

    @TypeConverter
    @Nullable
    public static Long toLong(@Nullable Instant value) {
      return (value != null) ? value.toEpochMilli() : null;
    }

    @TypeConverter
    @Nullable
    public static Instant toInstant(@Nullable Long value) {
      return (value != null) ? Instant.ofEpochMilli(value) : null;
    }

    @TypeConverter
    @Nullable
    public static Long toLong(@Nullable Duration value) {
      return (value != null) ? value.toMillis() : null;
    }

    @TypeConverter
    @Nullable
    public static Duration toDuration(@Nullable Long value) {
      return (value != null) ? Duration.ofMillis(value) : null;
    }

  }

  public static class Callback extends RoomDatabase.Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      // Include code to pre-populate.
    }

    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      super.onOpen(db);
      // Include code for automatic, infrequent update.
    }
  }

}

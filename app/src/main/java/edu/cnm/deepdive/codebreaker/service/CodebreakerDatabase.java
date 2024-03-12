package edu.cnm.deepdive.codebreaker.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GameResultDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.GameResult;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase.Converters;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Database(entities = {User.class, Game.class, Guess.class},
    views = GameResult.class,
    version = 1
)
@TypeConverters({Converters.class})
public abstract class CodebreakerDatabase extends RoomDatabase {

  private static final String NAME = "codebreaker_results";

  CodebreakerDatabase() {
    // Avoid generation of Javadoc HTML.
  }

  public static String getName(){
    return NAME;
  }

  public abstract GameResultDao getGameResultDao();

  public abstract UserDao getUserDao();

  public abstract GameDao getGameDao();

  public abstract GuessDao getGuessDao();

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

    @TypeConverter
    @Nullable
    public static Long toLong(@Nullable Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    @Nullable
    public static Date toDate(@Nullable Long value) {
      return (value != null) ? new Date(value) : null;
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

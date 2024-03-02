package edu.cnm.deepdive.codebreaker.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GameResultDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase;
import javax.inject.Singleton;

@InstallIn(SingletonComponent.class)
@Module
public class CodebreakerDatabaseModule {

  CodebreakerDatabaseModule() {
    // Avoid Javadoc HTML generation.
  }

  @Provides
  @Singleton
  public CodebreakerDatabase provideDatabase(@ApplicationContext Context context) {
    return Room.databaseBuilder(context, CodebreakerDatabase.class, CodebreakerDatabase.getName())
        .addCallback(new CodebreakerDatabase.Callback())
        .build();
  }

  @Provides
  @Singleton
  public GameResultDao provideResultsDao(CodebreakerDatabase database) {
    return database.getGameResultDao();
  }

  @Provides
  @Singleton
  public UserDao provideUserDao(CodebreakerDatabase database) {
    return database.getUserDao();
  }

  @Provides
  @Singleton
  public GameDao provideGameDao(CodebreakerDatabase database) {
    return database.getGameDao();
  }

  @Provides
  @Singleton
  public GuessDao provideGuessDao(CodebreakerDatabase database) {
    return database.getGuessDao();
  }

}

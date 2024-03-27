package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.model.Ranking;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CodebreakerServiceProxy {

  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  @GET("games/{id}")
  Single<Game> getGame(
      @Path("id") String id,
      @Header("Authorization") String bearerToken);

  @POST("games/{id}/guesses")
  Single<Guess> submitGuess(
      @Path("id") String gameId,
      @Body Guess guess,
      @Header("Authorization") String bearerToken);

  @GET("rankings")
  Single<List<Ranking>> getRankings(
      @Query("pool-size") int poolSize,
      @Query("code-length") int codeLength,
      @Query("games-threshold") int gamesThreshold,
      @Header("Authorization") String bearerToken);
}

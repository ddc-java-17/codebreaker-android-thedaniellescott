package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CodebreakerServiceProxy {

  @POST("games")
  Single<Game> startGame(@Body Game game);

  @GET("games/{id}")
  Single<Game> getGame(@Path("id") String id);

  @POST("games/{id}/guesses")
  Single<Guess> submitGuess(@Path("id") String gameId, @Body Guess guess);

}

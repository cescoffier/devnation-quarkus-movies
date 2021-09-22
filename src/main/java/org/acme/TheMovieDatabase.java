package org.acme;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RegisterRestClient(configKey = "the-movie-database")
public interface TheMovieDatabase {

    @GET
    @Path("/search/movie")
    @Retry(maxRetries = 4, delay = 1, delayUnit = ChronoUnit.SECONDS)
    @Timeout(1000)
    TmdbResponse search(@QueryParam("api_key") String key, @QueryParam("query") String title);


    class TmdbResponse {
        public List<MovieResponse> results;
    }

    class MovieResponse {
        public String id;
        public String title;
        @JsonProperty("poster_path")
        public String poster;
    }
}

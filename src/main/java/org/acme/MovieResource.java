package org.acme;

import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@Path("/movies")
public class MovieResource {

    @GET
    public List<Movie> getAll() {
        return Movie.listAll(Sort.descending("rating"));
    }

    @POST
    @Transactional
    public Movie addOne(@Valid Movie movie) {
        enrich(movie);
        movie.persist();
        return movie;
    }

    @RestClient TheMovieDatabase service;
    @ConfigProperty(name = "the-movie-database.image-root") String root;
    @ConfigProperty(name = "the-movie-database.api-key") String key;

    private void enrich(Movie movie) {
        Optional<TheMovieDatabase.MovieResponse> response = service.search(key, movie.title).results
                .stream().filter(mr -> {
                    return mr.title.equalsIgnoreCase(movie.title);
                })
                .findAny();

        response.ifPresent(mr -> {
            movie.cover = root + mr.poster;
            movie.title = mr.title;
        });
    }


}

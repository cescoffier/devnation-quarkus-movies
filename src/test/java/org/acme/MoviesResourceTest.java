package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;


@QuarkusTest
public class MoviesResourceTest {

    @Transactional
    @BeforeEach
    public void clear() {
        Movie.deleteAll();
    }

    @Test
    public void testMovies() {

        get("/movies").then().statusCode(200).body("size()", is(0));

        Movie movie1 = new Movie();
        movie1.title = "rambo";
        movie1.rating = 3;

        Movie movie2 = new Movie();
        movie2.title = "the pianist";
        movie2.rating = 2;

        given().body(movie1).header("content-type", "application/json")
                .when().post("/movies")
                .then().statusCode(200).body("title", containsStringIgnoringCase("Rambo"));

        given().body(movie2).header("content-type", "application/json")
                .when().post("/movies")
                .then().statusCode(200).body("rating", is(movie2.rating));

        given().body(movie2).header("content-type", "application/json")
                .when().post("/movies")
                .then().statusCode(500);


        get("/movies").then().statusCode(200).body("size()", is(2));
    }

    @Test
    public void testInvalidRating() {
        Movie movie3 = new Movie();
        movie3.title = "invalid";
        movie3.rating = 6;

        given().body(movie3).header("content-type", "application/json")
                .when().post("/movies")
                .then().statusCode(400);
    }
}

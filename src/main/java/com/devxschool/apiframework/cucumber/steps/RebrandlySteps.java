package com.devxschool.apiframework.cucumber.steps;

import com.devxschool.apiframework.cucumber.api.pojos.RebrandlyLinkResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RebrandlySteps {
    private Response response;

    //this regex means: either matches all in double quotes or nothing
    @When("^the base URL \"([^\"]*)\"$")
    public void theBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    @When("^all links are requested$")
    public void all_links_are_requested() {
        //starting point to build request
        RequestSpecification requestSpec = RestAssured.given();

        requestSpec.headers("apiKey", "b784b04b122144ffab7d39dfde062c71");
        requestSpec.accept(ContentType.JSON);

        response = requestSpec.get("/v1/links");
    }

    @Then("^a status code (\\d+) is returned$")
    public void a_status_code_is_returned(int statusCode) {
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(statusCode));
    }

    @When("all links are requested with the following query params")
    public void all_links_are_requested_with_the_following_query_params(List<Map<String, String>> queryParams) {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.headers("apiKey", "b784b04b122144ffab7d39dfde062c71");
        requestSpec.accept(ContentType.JSON);

        response = requestSpec
                .queryParams("limit", queryParams.get(0).get("limit"))
                .get("/v1/links");
    }

    @Then("only 1 link is returned")
    public void only_link_is_returned() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

/**
 * The objectMapper doesn't have a build in function to convert Json array to List of objects.
 * We can convert a Json array into the Java array of objects. In this case, we're converting the body which
 * is a Json array, we're converting it to the array of RebrandlyLinkResponse[].
 * And then we convert regular array to List using Arrays.asList(...) method.
 *
 * If we want to convert the Json Obj into the Java obj, we dont need to put [] after Rebr.Link.Resp.
 * But if we want to convert the Json array into the Java array, we need to put [], bc that's how you define
 * the array.
 *
 * In the next line we simply convert Json array into Java array. And then we convert this array into the List
 * of objects.
 */
        List<RebrandlyLinkResponse> linksList =
                Arrays.asList(objectMapper.readValue(response.body().asString(), RebrandlyLinkResponse[].class));
        MatcherAssert.assertThat(linksList.size(), Matchers.is(1));

    }


}

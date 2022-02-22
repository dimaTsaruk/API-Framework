package com.devxschool.apiframework.cucumber.steps;

import com.devxschool.apiframework.cucumber.api.pojos.RebrandlyLink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
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
    private String linkId;



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
                .queryParams(queryParams.get(0))
                .get("/v1/links");
    }

    @Then("only 1 link is returned")
    public void only_link_is_returned() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

/**
 * The objectMapper doesn't have a build in function to convert Json array to List of objects.
 * We can convert a Json array into the Java array of objects. In this case, we're converting the body which
 * is a Json array, we're converting it to the array of RebrandlyLink[].
 * And then we convert regular array to List using Arrays.asList(...) method.
 *
 * If we want to convert the Json Obj into the Java obj, we dont need to put [] after Rebr.Link.Resp.
 * But if we want to convert the Json array into the Java array, we need to put [], bc that's how you define
 * the array.
 *
 * In the next line we simply convert Json array into Java array. And then we convert this array into the List
 * of objects.
 */
        List<RebrandlyLink> linksList =
                Arrays.asList(objectMapper.readValue(response.body().asString(), RebrandlyLink[].class));
        MatcherAssert.assertThat(linksList.size(), Matchers.is(1));

    }

    @And("verify that {int} links has been returned with the following domainId {string}")
    public void verifyThatLinksHasBeenReturnedWithTheFollowingDomainId(int numberOfLinks, String domainId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<RebrandlyLink> linksList
                = Arrays.asList(objectMapper.readValue(response.body().asString(), RebrandlyLink[].class));

        MatcherAssert.assertThat(linksList.size(), Matchers.is(numberOfLinks));

        for (RebrandlyLink rebrandlyLinkResponse : linksList) {
            MatcherAssert.assertThat(rebrandlyLinkResponse.getDomainId(), Matchers.is(domainId));
        }
    }

    @When("the following link is created")
    public void the_following_link_is_created(List<Map<String, String>> linkRequest) {
        RebrandlyLink rebrandlyLink = new RebrandlyLink();
        rebrandlyLink.setDestination(linkRequest.get(0).get("destination"));

        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.headers("apiKey", "b784b04b122144ffab7d39dfde062c71");
        requestSpec.contentType(ContentType.JSON);
        requestSpec.accept(ContentType.JSON);
        requestSpec.body(rebrandlyLink);

        response = requestSpec.post("/v1/links");

        linkId = response.getBody().jsonPath().getString("id");
    }

    @Then("the following link has been created")
    public void the_following_link_has_been_created(List<Map<String, String>> linkResponse) throws JsonProcessingException {

        //deserialize our response to the pojo
        ObjectMapper objectMapper = new ObjectMapper();
        RebrandlyLink rebrandlyLinkResponse
                = objectMapper.readValue(response.body().asString(), RebrandlyLink.class);

        linkId = rebrandlyLinkResponse.getId();

        MatcherAssert.assertThat(rebrandlyLinkResponse.getDestination(), Matchers.is(linkResponse.get(0).get("destination")));

    }


    @When("the link details has been requested")
    public void requestLinkDetails() {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.headers("apiKey", "b784b04b122144ffab7d39dfde062c71");
        requestSpec.accept(ContentType.JSON);

        response = requestSpec
                .pathParam("linkId", linkId)
                .get("/v1/links/{linkId}");


    }
}


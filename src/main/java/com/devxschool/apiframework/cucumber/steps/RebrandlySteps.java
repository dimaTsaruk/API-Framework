package com.devxschool.apiframework.cucumber.steps;

import com.devxschool.apiframework.cucumber.api.pojos.RebrandlyLink;
import com.devxschool.apiframework.cucumber.steps.common.CommonData;
import com.devxschool.apiframework.cucumber.utilities.ObjectConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.Map;

public class RebrandlySteps {
    private CommonData commonData;
    private String linkId;

    public RebrandlySteps(CommonData commonData) {
        this.commonData = commonData;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://api.rebrandly.com/";

    }

    @When("^all links are requested$")
    public void all_links_are_requested() {
        RequestSpecification requestSpec = setUpHeaders();
        commonData.response = requestSpec.get("/v1/links");
    }

//    @Then("^a status code (\\d+) is returned$")
//    public void a_status_code_is_returned(int statusCode) {
//        MatcherAssert.assertThat(commonData.response.getStatusCode(), Matchers.is(statusCode));
//    }

    @When("all links are requested with the following query params")
    public void all_links_are_requested_with_the_following_query_params(List<Map<String, String>> queryParams) {
        RequestSpecification requestSpec = setUpHeaders();

        commonData.response = requestSpec
                .queryParams(queryParams.get(0))
                .get("/v1/links");
    }

    @Then("only 1 link is returned")
    public void only_link_is_returned() throws JsonProcessingException {
        //ObjectMapper objectMapper = new ObjectMapper();

        // RebrandlyLink[] responseArray = objectMapper.readValue(response.body().asString(), RebrandlyLink[].class);

        List<RebrandlyLink> linksList = ObjectConverter.convertJsonArrayToListOfObjects(commonData.response.body().asString(),
                RebrandlyLink[].class);

//        List<RebrandlyLink> linksList =
//                Arrays.asList(objectMapper.readValue(response.body().asString(), RebrandlyLink[].class));
        MatcherAssert.assertThat(linksList.size(), Matchers.is(1));

    }

    @And("verify that {int} links has been returned with the following domainId {string}")
    public void verifyThatLinksHasBeenReturnedWithTheFollowingDomainId(int numberOfLinks, String domainId) throws JsonProcessingException {
        // ObjectMapper objectMapper = new ObjectMapper();
        List<RebrandlyLink> linksList = ObjectConverter.convertJsonArrayToListOfObjects(commonData.response.body().asString(),
                RebrandlyLink[].class);
        // = Arrays.asList(objectMapper.readValue(response.body().asString(), RebrandlyLink[].class));

        MatcherAssert.assertThat(linksList.size(), Matchers.is(numberOfLinks));

        for (RebrandlyLink rebrandlyLinkResponse : linksList) {
            MatcherAssert.assertThat(rebrandlyLinkResponse.getDomainId(), Matchers.is(domainId));
        }
    }

    @When("the following link is created")
    public void the_following_link_is_created(List<Map<String, String>> linkRequest) {
        RebrandlyLink rebrandlyLink = new RebrandlyLink();
        rebrandlyLink.setDestination(linkRequest.get(0).get("destination"));

        RequestSpecification requestSpec = setUpHeaders();
        requestSpec.body(rebrandlyLink);

        commonData.response = requestSpec.post("/v1/links");

        linkId = commonData.response.getBody().jsonPath().getString("id");
    }

    @Then("the following link has been returned")
    public void the_following_link_has_been_returned(List<Map<String, String>> linkResponse) throws JsonProcessingException {

        //deserialize our response to the pojo
        ObjectMapper objectMapper = new ObjectMapper();
        RebrandlyLink rebrandlyLinkResponse
                = ObjectConverter.convertJsonObjectToJavaObject(commonData.response.body().asString(), RebrandlyLink.class);
        // = objectMapper.readValue(response.body().asString(), RebrandlyLink.class);

        linkId = rebrandlyLinkResponse.getId();

        MatcherAssert.assertThat(rebrandlyLinkResponse.getDestination(), Matchers.is(linkResponse.get(0).get("destination")));

    }

    @When("the link details has been requested")
    public void requestLinkDetails() {
        RequestSpecification requestSpec = setUpHeaders();
        commonData.response = requestSpec
                .pathParam("linkId", linkId)
                .get("/v1/links/{linkId}");
    }

    @When("the link with id {string} is updated with the following data")
    public void the_link_with_id_is_updated_with_the_following_data(String id, List<Map<String, String>> linkRequestBody) {

        RequestSpecification requestSpec = setUpHeaders();

        RebrandlyLink rebrandlyLinkBody = new RebrandlyLink();
        rebrandlyLinkBody.setDestination(linkRequestBody.get(0).get("destination"));

        requestSpec.body(rebrandlyLinkBody);

        commonData.response = requestSpec
                .pathParam("linkId", id)
                .post("/v1/links/{linkId}");
    }

    @When("the link has been deleted")
    public void deleteLink() {
        RequestSpecification requestSpec = setUpHeaders();

        commonData.response = requestSpec
                .pathParam("linkId", linkId)
                .delete("v1/links/{linkId}");


    }

    private RequestSpecification setUpHeaders() {
        RequestSpecification requestSpec = RestAssured.given();

        requestSpec.headers("apiKey", "b784b04b122144ffab7d39dfde062c71");
        requestSpec.contentType(ContentType.JSON);
        requestSpec.accept(ContentType.JSON);

        return requestSpec;
    }

    @After
    public void tearDown() {
        if (linkId != null) {
            deleteLink();
        }
    }
}


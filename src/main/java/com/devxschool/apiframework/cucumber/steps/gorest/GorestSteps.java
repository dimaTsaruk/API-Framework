package com.devxschool.apiframework.cucumber.steps.gorest;

import com.devxschool.apiframework.cucumber.api.pojos.RebrandlyLink;
import com.devxschool.apiframework.cucumber.api.pojos.User;
import com.devxschool.apiframework.cucumber.utilities.ObjectConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
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

public class GorestSteps {
    private Response response;
    private String linkId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public/v2/";
    }

    @When("all users are requested")
    public void all_users_are_requested() {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.accept(ContentType.JSON);
        response = requestSpec.get("/users");
    }

    @Then("^a status code (\\d+) is returned$")
    public void a_status_code_is_returned(int statusCode) {
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(statusCode));
    }

    @Then("{int} users are returned")
    public void users_are_returned(Integer amountOfUsers) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<User> linksList =
                Arrays.asList(objectMapper.readValue(response.body().asString(), User[].class));
        MatcherAssert.assertThat(linksList.size(), Matchers.is(20));

    }

    //check doesnt work
    @When("the following user is created")
    public void the_following_user_is_created(List<Map<String, String>> userRequest) {
        RequestSpecification requestSpec = RestAssured.given();
        User user = new User();
        user.setName(userRequest.get(0).get("name"));
        user.setEmail(userRequest.get(0).get("email"));
        user.setId(userRequest.get(0).get("id"));
        user.setStatus(userRequest.get(0).get("status"));

        requestSpec.headers("Authorization", "Bearer 970abad0e6f59248cde5d963618b42872a28eb99f18d020e8fe0382971142217");
        requestSpec.contentType(ContentType.JSON);
        requestSpec.accept(ContentType.JSON);
        requestSpec.body(userRequest);

        response = requestSpec.post("/users");

    }

    //check doesnt work
    @Then("the following user has been returned")
    public void the_following_user_has_been_returned(List<Map<String, String>> userResponse) throws JsonProcessingException {
        User actualUser = ObjectConverter.convertJsonObjectToJavaObject(response.body().asString(), User.class);

        MatcherAssert.assertThat(actualUser.getName(), Matchers.is(userResponse.get(0).get("name")));
        MatcherAssert.assertThat(actualUser.getEmail(), Matchers.is(userResponse.get(0).get("email")));
        MatcherAssert.assertThat(actualUser.getGender(), Matchers.is(userResponse.get(0).get("status")));
        MatcherAssert.assertThat(actualUser.getStatus(), Matchers.is(userResponse.get(0).get("gender")));

    }

    private RequestSpecification setUpHeaders() {
        RequestSpecification requestSpec = RestAssured.given();

        requestSpec.headers("Authorization", "Bearer 970abad0e6f59248cde5d963618b42872a28eb99f18d020e8fe0382971142217");
        requestSpec.contentType(ContentType.JSON);
        requestSpec.accept(ContentType.JSON);

        return requestSpec;
    }


}












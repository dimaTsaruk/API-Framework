package com.devxschool.apiframework.cucumber.steps;

import com.devxschool.apiframework.cucumber.api.pojos.Post;
import com.devxschool.apiframework.cucumber.steps.common.CommonData;
import com.devxschool.apiframework.cucumber.utilities.ObjectConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.AfterClass;

import java.util.List;

public class GorestPostSteps {

    private CommonData commonData;
    private Post post;


    public GorestPostSteps(CommonData commonData) {
        this.commonData = commonData;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/public/v2/";
    }

    /**
     * @When("^I Enter My Regular Expenses$")
     * public void I_Enter_My_Regular_Expenses(DataTable dataTable) throws Throwable {
     *   List<Expense> expenseList = dataTable.asList(Expense.class);
     *
     *   for (Expense expense : expenseList) {
     *     System.out.println(expense);
     *   }
     *
     *   // Here, asList() creates a List of Expense objects.
     * }
     *
     */






    @When("the following post has been created")
    public void the_following_post_has_been_created(io.cucumber.datatable.DataTable dataTable) {
        //here we convert DataTable to pojo class object
        List<Post> posts = dataTable.asList(Post.class);

        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.headers("Authorization", "Bearer 970abad0e6f59248cde5d963618b42872a28eb99f18d020e8fe0382971142217");
        requestSpec.contentType(ContentType.JSON);
        requestSpec.accept(ContentType.JSON);
        requestSpec.body(posts.get(0));

        commonData.response = requestSpec.post("/posts");
    }





    @Then("the following post has been returned")
    public void the_following_post_has_been_returned(io.cucumber.datatable.DataTable dataTable) throws JsonProcessingException {
        List<Post> posts = dataTable.asList(Post.class);
        post =
                ObjectConverter.convertJsonObjectToJavaObject(commonData.response.getBody().asString(), Post.class);
        MatcherAssert.assertThat(post.getTitle(), Matchers.is(posts.get(0).getTitle()));
    }

    @When("the post details has been requested")
    public void the_post_details_has_been_requested() {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.headers("Authorization", "Bearer 970abad0e6f59248cde5d963618b42872a28eb99f18d020e8fe0382971142217");
        requestSpec.contentType(ContentType.JSON);

        commonData.response = requestSpec
                .pathParam("postId", post.getId())
                .get("/posts/{postId}");
    }

    @After
    public void tearDown() {
        if (post.getId() != 0) {
            RequestSpecification requestSpec = RestAssured.given();
            requestSpec.headers("Authorization", "Bearer 970abad0e6f59248cde5d963618b42872a28eb99f18d020e8fe0382971142217");

            requestSpec
                    .pathParam("postId", post.getId())
                    .delete("/posts/{postId}");
        }
    }

}

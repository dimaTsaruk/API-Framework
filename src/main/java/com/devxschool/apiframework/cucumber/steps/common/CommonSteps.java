package com.devxschool.apiframework.cucumber.steps.common;

import io.cucumber.java.en.Then;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

public class CommonSteps {
    private CommonData commonData;

    public CommonSteps(CommonData commonData) {
        this.commonData = commonData;
    }

    @Then("^a status code (\\d+) is returned$")
    public void a_status_code_is_returned(int statusCode) {
        MatcherAssert.assertThat(commonData.response.getStatusCode(), Matchers.is(statusCode));
    }
}

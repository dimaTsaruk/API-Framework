package com.devxschool.apiframework.cucumber.testrunners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/resources/features/rebrandly.feature"
        },
        glue = {
                "com/devxschool/apiframework/cucumber/steps"
        },
        tags = "@Rebrandly",
        dryRun = false
)

public class RebrandlyTest {
}

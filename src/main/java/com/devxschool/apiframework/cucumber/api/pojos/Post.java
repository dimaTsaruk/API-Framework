package com.devxschool.apiframework.cucumber.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    private int id;
    @JsonProperty("user_id")
    private int userId;
    private String title;
    private String body;

}

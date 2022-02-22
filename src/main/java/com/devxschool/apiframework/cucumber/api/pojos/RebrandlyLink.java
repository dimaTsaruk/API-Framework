package com.devxschool.apiframework.cucumber.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
//Jackson provide annotation to ignore all unknown properties
@JsonIgnoreProperties(ignoreUnknown = true)

public class RebrandlyLink {
    private String id;
    private String title;
    private String destination;
    private int clicks;
    private boolean isPublished;
    private String domainId;
    private String status;
}

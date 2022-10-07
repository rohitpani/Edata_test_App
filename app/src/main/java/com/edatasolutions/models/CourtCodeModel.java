package com.edatasolutions.models;

public class CourtCodeModel {

    public String getCourt_code_value() {
        return court_code_value;
    }

    public void setCourt_code_value(String court_code_value) {
        this.court_code_value = court_code_value;
    }

    private String court_code_value;


    public void setCourt_description(String court_description) {
        this.court_description = court_description;
    }

    private String court_description;

    public String getCourt_description() {
        return court_description;
    }



    public CourtCodeModel(String court_description,String court_code_value)
    {
        this.court_description = court_description;
        this.court_code_value = court_code_value;

    }

    public CourtCodeModel()
    {
    }

}

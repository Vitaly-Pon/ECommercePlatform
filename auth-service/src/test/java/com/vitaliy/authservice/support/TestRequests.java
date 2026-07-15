package com.vitaliy.authservice.support;

public class TestRequests {

    public static final String REGISTER_JSON = """
            {
              "email":"%s",
              "password":"%s"
            }
            """.formatted(
            TestData.EMAIL,
            TestData.PASSWORD
    );


    public static final String LOGIN_JSON = """
            {
              "email":"%s",
              "password":"%s"
            }
            """.formatted(
            TestData.EMAIL,
            TestData.PASSWORD
    );


    public static final String INVALID_EMAIL_JSON = """
            {
              "email":"NOEMAIL",
              "password":"%s"
            }
            """.formatted(
            TestData.PASSWORD
    );


    public static final String EMPTY_PASSWORD_JSON = """
            {
              "email":"%s",
              "password":""
            }
            """.formatted(
            TestData.EMAIL
    );


    public static final String WRONG_PASSWORD_JSON = """
            {
              "email":"%s",
              "password":"wrongPassword"
            }
            """.formatted(
            TestData.EMAIL
    );


    public static final String EMPTY_EMAIL_JSON = """
            {
              "email":"",
              "password":"%s"
            }
            """.formatted(
            TestData.PASSWORD
    );


    private TestRequests() {
    }

}

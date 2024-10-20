package com.example.util;

import java.util.logging.Logger;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;

/**
 * Class that handles GET, POST, and DELETE requests.
 */
public class BackendConnection {

    private BackendConnection() {

    }

    private static GetRequest createGetRequest(String url, String bearerToken) {
        if (bearerToken != null) {
            return Unirest.get(url).header("Authorization", "Bearer " + bearerToken);
        } else {
            return Unirest.get(url);
        }
    }

    private static RequestBodyEntity createPostRequest(String url, String jsonBody, String bearerToken) {
        if (bearerToken != null) {
            return Unirest.post(url).body(jsonBody)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + bearerToken);
        } else {
            return Unirest.post(url).body(jsonBody)
                .header("Content-Type", "application/json");
        }
    }

    /**
     * Invoke a get request and get the response as a JsonNode.
     */
    public static JsonNode getJsonNodeRequest(String url) {
        return getJsonNodeRequest(url, null);
    }

    public static JsonNode getJsonNodeRequest(String url, String bearerToken) {
        try {
            GetRequest getRequest = createGetRequest(url, bearerToken);
            HttpResponse<JsonNode> httpResponse = getRequest != null ? getRequest.asJson() : null;
            return httpResponse != null && httpResponse.isSuccess() ? httpResponse.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Invoke a get request and get the response as a String.
     */
    public static String getStringRequest(String url) {
        return getStringRequest(url, null);
    }

    public static String getStringRequest(String url, String bearerToken) {
        try {
            GetRequest getRequest = createGetRequest(url, bearerToken);
            HttpResponse<String> httpResponse = getRequest != null ? getRequest.asString() : null;
            return httpResponse != null && httpResponse.isSuccess() ? httpResponse.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode postJsonNodeRequest(String url, String jsonBody) throws Exception {
        return postJsonNodeRequest(url, jsonBody, null);
    }

    public static JsonNode postJsonNodeRequest(String url, String jsonBody, String bearerToken) throws Exception {
        if (url == null || jsonBody == null) {
            return null;
        }
        RequestBodyEntity requestWithBody = createPostRequest(url, jsonBody, bearerToken);
        HttpResponse<JsonNode> httpResponse = requestWithBody != null ? requestWithBody.asJson() : null;
        if (httpResponse == null) {
            Logger.getGlobal().info("HttpResponse for %s was a failure.".formatted(url));
        } else if (!httpResponse.isSuccess()) {
            Logger.getGlobal().info("HttpResponse for %s was a failure. Status: %s".formatted(url, httpResponse.getStatus()));
        }
        return httpResponse != null && httpResponse.isSuccess() ? httpResponse.getBody() : null;
    }

    public static String postStringRequest(String url, String jsonBody) throws Exception {
        return postStringRequest(url, jsonBody, null);
    }

    public static String postStringRequest(String url, String jsonBody, String bearerToken) throws Exception {
        if (url == null || jsonBody == null) {
            return null;
        }
        RequestBodyEntity requestWithBody = createPostRequest(url, jsonBody, bearerToken);
        HttpResponse<String> httpResponse = requestWithBody != null ? requestWithBody.asString() : null;
        return httpResponse != null && httpResponse.isSuccess() ? httpResponse.getBody() : null;
    }

    /**
     * Invoke a delete request.
     */
    public static boolean deleteRequest(String url, String bearerToken) {
        try {
            HttpRequestWithBody requestWithBody = Unirest.delete(url).header("Authorization", "Bearer " + bearerToken);
            HttpResponse<JsonNode> httpResponse = requestWithBody != null ? requestWithBody.asJson() : null;
            return httpResponse != null && httpResponse.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }
}

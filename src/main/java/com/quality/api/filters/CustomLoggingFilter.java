package com.quality.api.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                          FilterableResponseSpecification responseSpec,
                          FilterContext ctx) {
        
        long start = System.currentTimeMillis();
        Response response = ctx.next(requestSpec, responseSpec);
        long end = System.currentTimeMillis();

        if (logger.isInfoEnabled()) {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("\n=========================== API CALL ===========================\n");
            logMessage.append(String.format("Request: [%s] %s%n", requestSpec.getMethod(), requestSpec.getURI()));
            
            if (requestSpec.getBody() != null) {
                logMessage.append("Request Body:\n").append(requestSpec.getBody().toString()).append("\n");
            }
            
            logMessage.append("----------------------------------------------------------------\n");
            logMessage.append(String.format("Response: Status %d (took %d ms)%n", response.getStatusCode(), (end - start)));
            
            String responseBody = response.getBody() != null ? response.getBody().asString() : "";
            if (!responseBody.isEmpty()) {
                try {
                    if (responseBody.length() > 5000) {
                        logMessage.append("Response Body (truncated):\n").append(responseBody.substring(0, 5000)).append("...\n");
                    } else {
                        logMessage.append("Response Body:\n").append(response.getBody().asPrettyString()).append("\n");
                    }
                } catch (Exception e) {
                    logMessage.append("Response Body:\n").append(responseBody).append("\n");
                }
            }
            logMessage.append("================================================================");

            logger.info(logMessage.toString());
        }
        
        return response;
    }
}

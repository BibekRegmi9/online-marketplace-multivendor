package com.bibek.generics.controller;

import com.bibek.generics.enums.ResponseStatus;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class BaseController {


    /**
     * ObjectMapper instance
     */
    public ObjectMapper objectMapper = new ObjectMapper();

    /**
     * API Success ResponseStatus
     */
    protected final ResponseStatus API_SUCCESS_STATUS = ResponseStatus.SUCCESS;

    /**
     * API Error ResponseStatus
     */
    protected final ResponseStatus API_ERROR_STATUS = ResponseStatus.FAIL;

    /**
     * Function that sends successful API Response
     *
     * @param message
     * @param data
     * @return
     */
    protected GlobalApiResponse successResponse(String message, Object data) {
        GlobalApiResponse globalApiResponse = new GlobalApiResponse();
        globalApiResponse.setStatus(API_SUCCESS_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(data);
        return globalApiResponse;
    }

    /**
     * Function that sends error API Response
     *
     * @param message
     * @param errors
     * @return
     */
    protected GlobalApiResponse errorResponse(String message, Object errors) {
        GlobalApiResponse globalApiResponse = new GlobalApiResponse();
        globalApiResponse.setStatus(API_ERROR_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(errors);
        return globalApiResponse;
    }
}

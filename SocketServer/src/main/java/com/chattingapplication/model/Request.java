package com.chattingapplication.model;


import com.chattingapplication.service.ClientHandleService;

public class Request {
    private String requestFunction;
    private String requestParam;
    public Request(String requestFunction, String requestParam, ClientHandleService clientHandleService) {
        this.requestFunction = requestFunction;
        this.requestParam = requestParam;
    }
    public String getRequestFunction() {
        return requestFunction;
    }
    public void setRequestFunction(String requestFunction) {
        this.requestFunction = requestFunction;
    }
    public String getRequestParam() {
        return requestParam;
    }
    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
}

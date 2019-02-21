package com.aiden.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * /* @author aiden
 * /* @creat  2019/2/21 14:10
 * /* @Description
 **/
public abstract class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public void init(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }


    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}

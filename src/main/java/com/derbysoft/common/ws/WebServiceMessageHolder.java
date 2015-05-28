package com.derbysoft.common.ws;

public interface WebServiceMessageHolder {
    public void holdRequest(String request);

    public void holdResponse(String response);

    public String retrieveRequest();

    public String retrieveResponse();

    public void reset();

}

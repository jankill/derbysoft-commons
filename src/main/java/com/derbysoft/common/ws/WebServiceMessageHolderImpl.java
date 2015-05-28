package com.derbysoft.common.ws;

public class WebServiceMessageHolderImpl implements WebServiceMessageHolder {

    private static final ThreadLocal<String> TL_REQUEST = new ThreadLocal<String>();
    private static final ThreadLocal<String> TL_RESPONSE = new ThreadLocal<String>();

    @Override
    public void holdRequest(String request) {
        TL_REQUEST.set(request);
    }

    @Override
    public void holdResponse(String response) {
        TL_RESPONSE.set(response);
    }

    @Override
    public String retrieveRequest() {
        return TL_REQUEST.get();
    }

    @Override
    public String retrieveResponse() {
        return TL_RESPONSE.get();
    }

    @Override
    public void reset() {
        TL_REQUEST.remove();
        TL_RESPONSE.remove();
    }
}

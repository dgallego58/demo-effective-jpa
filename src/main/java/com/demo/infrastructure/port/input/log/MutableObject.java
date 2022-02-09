package com.demo.infrastructure.port.input.log;


public class MutableObject {

    private Object beforeAttr;
    private Object afterAtt;
    private Object reqHeaders;
    private Object responseHeaders;

    public Object getResponseHeaders() {
        return responseHeaders;
    }

    public MutableObject setResponseHeaders(Object responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
    }

    public Object getReqHeaders() {
        return reqHeaders;
    }

    public MutableObject setReqHeaders(Object reqHeaders) {
        this.reqHeaders = reqHeaders;
        return this;
    }

    public Object getBeforeAttr() {
        return beforeAttr;
    }

    public MutableObject setBeforeAttr(Object beforeAttr) {
        this.beforeAttr = beforeAttr;
        return this;
    }

    public Object getAfterAtt() {
        return afterAtt;
    }

    public MutableObject setAfterAtt(Object afterAtt) {
        this.afterAtt = afterAtt;
        return this;
    }
}

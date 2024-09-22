package com.agony.demo.utils;

/**
 * @author itchen
 * @date 2024/9/21 22:05
 * @description 统一返回结果
 */
public class Result {

    boolean res;
    String payload;
    String vuln;

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getVuln() {
        return vuln;
    }

    public void setVuln(String vuln) {
        this.vuln = vuln;
    }

    public Result(boolean res,  String vuln,String payload) {
        this.res = res;
        this.payload = payload;
        this.vuln = vuln;
    }

}

package com.agony.demo.common;

import com.agony.demo.utils.Result;

/**
 * @author itchen
 * @date 2024/9/21 22:03
 * @description payload接口
 */
public interface  BasePayload {
    Result checkVUL(String url) throws Exception;
    Result exeVUL(String url,String cmd) throws Exception;
    Result getShell(String url) throws Exception;
}

package com.agony.demo.utils;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;

/**
 * @author itchen
 * @date 2024/9/21 23:44
 * @description 模块
 */
public class Module {
    public String getModule(String url) {
        ArrayList<String> list = new ArrayList<String>() {{
            add("manage");
            add("admin");
            add("api");
        }};
        String mod = "index";
        for (int i = 0; i < list.size(); i++) {
            try {
                int code = HttpRequest.get(url + "/?s=/" + list.get(i)).code();
                if (code == 200) {
                    mod = list.get(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mod;
    }
}

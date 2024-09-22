package com.agony.demo.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author itchen
 * @date 2024/9/21 21:53
 * @description 用来获取EXP列表
 */
public class Exp_list {

    public static  List<String> get_expList(){

        return new ArrayList<String>() {{
            add("ThinkPHP 2 RCE");
            add("ThinkPHP 5.0 RCE");
            add("ThinkPHP 5.0.10 RCE");
            add("ThinkPHP 5.0.22/5.1.29 RCE");
            add("ThinkPHP 5.0.23 RCE");
            add("ThinkPHP 5.0.24-5.1.30 RCE");
            add("ThinkPHP 5 文件包含漏洞");
            add("ThinkPHP 5 show-id RCE");
            add("ThinkPHP 5 method filter RCE");
            add("ThinkPHP 5 session 文件包含漏洞");
            add("ThinkPHP 5 SQL注入漏洞 && 敏感信息泄露");
            add("ThinkPHP 5.x 数据库信息泄露");
            add("ThinkPHP 5.x 日志泄露");
            add("ThinkPHP 3.x RCE");
            add("ThinkPHP 3.x 日志泄露");
            add("ThinkPHP 3.x Log RCE");
            add("ThinkPHP 6.x 日志泄露");
            add("ThinkPHP 6 文件包含漏洞");
            add("ThinkPHP 6 session文件写入");
            add("ThinkPHP catch 命令执行漏洞");
            add("ThinkPHP check-code sql注入漏洞");
            add("ThinkPHP multi sql注入 && 信息泄露漏洞");
            add("ThinkPHP orderid sql注入");
            add("ThinkPHP update sql注入");
            add("ThinkPHP recent_xff sql注入");
        }};
    }
}

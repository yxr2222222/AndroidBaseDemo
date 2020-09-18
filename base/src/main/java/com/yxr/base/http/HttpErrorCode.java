package com.yxr.base.http;

/**
 * @author ciba
 * @description 接口访问异常错误码
 * @date 2020/09/17
 */
public interface HttpErrorCode {
     int CODE_CONNECT_EXCEPTION = -5000;
     int CODE_NULL_RESPONSE = -5001;
     int CODE_NULL_BODY = -5002;
     int CODE_UNKNOW = -1;

     String MESSAGE_UNKNOW = "未知错误";
     String MESSAGE_CONNECT_EXCEPTION = "网络异常";
     String MESSAGE_CONNECT_TIME_OUT = "网络超时";
     String MESSAGE_CONNECT_NO = "没有网络连接";
     String MESSAGE_NULL_RESPONSE = "服务器没有响应";
     String MESSAGE_NULL_BODY = "获取数据失败";
}

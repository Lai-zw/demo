package com.laizhw.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alimt.model.v20181012.TranslateECommerceRequest;
import com.aliyuncs.alimt.model.v20181012.TranslateECommerceResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.net.URLEncoder;

public class AliyunTranslateUtil {
    public static String aliyunTranslate(String s) {
        String accessKeyId = "LTAI5tQNbjGGhKYEUwqDoHed";// 使用您的阿里云访问密钥 AccessKeyId
        String accessKeySecret = "4wo1aqcaiVPHLFJYIEdsFq1z3w1WJI";// 使用您的阿里云访问密钥
        // 创建DefaultAcsClient实例并初始化
        try {
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",// 地域ID
                    accessKeyId,// 阿里云账号的AccessKey ID
                    accessKeySecret);// 阿里云账号Access Key Secret
            IAcsClient client = new DefaultAcsClient(profile);
            // 创建API请求并设置参数
            TranslateECommerceRequest eCommerceRequest = new TranslateECommerceRequest();
            eCommerceRequest.setScene("title");
            eCommerceRequest.setMethod(MethodType.POST);// 设置请求方式，POST
            eCommerceRequest.setFormatType("text");//翻译文本的格式
            eCommerceRequest.setSourceLanguage("zh");//源语言
            eCommerceRequest.setSourceText(URLEncoder.encode(s, "UTF-8"));//原文
            eCommerceRequest.setTargetLanguage("en");//目标语言
            TranslateECommerceResponse eCommerceResponse = client.getAcsResponse(eCommerceRequest);
            System.out.println(JSONObject.toJSON(eCommerceResponse));
            return eCommerceResponse.getData().getTranslated();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
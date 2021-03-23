package com.comumunity.fei.provider;

import com.alibaba.fastjson.JSON;
import com.comumunity.fei.dto.AccessTokenDTO;
import com.comumunity.fei.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
        /*
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        此步骤导致获取不到user信息
        将access_token通过作为Authorization HTTP header中的参数传输，而不是作为url中的参数明文传输。改之后的，将token放到header里传递过去
        原因：Github更新官方推荐的使用access_token安全访问API的方式，用Authorization HTTP header代替query paramet，旧方式即将被废弃
*/
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser ;
        } catch (IOException e) {
        }
        return null;
    }
}

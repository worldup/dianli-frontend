package com.mdata.thirdparty.dianli.frontend.weather;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeatherClient {

    static String url="http://d1.weather.com.cn/sk_2d/101021000.html?_=";
    String refer="http://www.weather.com.cn/weather1d/101021000.shtml";

    public static void main(String[] args) throws Exception {
        String result=run(url+new Date().getTime());
        List<String > stringList=Splitter.on("=").splitToList(result);
        if(stringList!=null && stringList.size()==2){
            result= stringList.get(1);
                    Gson gson=new Gson();
            Map map=gson.fromJson(result, Map.class);

            System.out.println(map);
        }

    }
    static OkHttpClient client = new OkHttpClient();
    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer","http://www.weather.com.cn/weather1d/101021000.shtml")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

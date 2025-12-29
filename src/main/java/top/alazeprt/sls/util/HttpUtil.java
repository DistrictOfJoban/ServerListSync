package top.alazeprt.sls.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static top.alazeprt.sls.config.SLSConfig.address;

public class HttpUtil {
    private static final int TIMEOUT_MS = 2000;

    public static JsonObject get() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT_MS)
                .setConnectTimeout(TIMEOUT_MS)
                .setSocketTimeout(TIMEOUT_MS)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(address);
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                return new Gson().fromJson("{\"error\":{\"code\":\"" + response.getStatusLine().getStatusCode() + "\"}}", JsonObject.class);
            }
            if (entity != null) {
                return new Gson().fromJson(EntityUtils.toString(entity), JsonObject.class);
            }
        } catch (IOException e) {
            return new Gson().fromJson("{\"error\":{\"message\":\"" + e + "\"}}", JsonObject.class);
        }
        return new Gson().fromJson("{\"error\":{\"message\":\"Unknown error\"}}", JsonObject.class);
    }
}

package pl.mzlnk.emergencyspot.service.network.requests;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

public abstract class BaseHttpRequestParams<T> implements HttpRequestParams<T> {

//    private static final String HOST = "emergency-spot-api.eu-central-1.elasticbeanstalk.com";
//    private static final String PORT = "80";

    private static final String HOST = "192.168.0.21";
    private static final String PORT = "5000";

    @Override
    public String getRequestBody() {
        return null;
    }

    @Override
    public boolean isAuthorized() {
        return false;
    }

    @Override
    public Class<T> receivedDataClass() {
        return null;
    }

    @Override
    public TypeToken<List<T>> receivedDataTypeToken() {
        return null;
    }

    @Override
    public String getUrl() {
        return String.format("http://%s:%s", HOST, PORT);
    }

}

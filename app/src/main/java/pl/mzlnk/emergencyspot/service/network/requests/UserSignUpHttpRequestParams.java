package pl.mzlnk.emergencyspot.service.network.requests;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import pl.mzlnk.emergencyspot.model.user.NewUserDto;

@AllArgsConstructor
public class UserSignUpHttpRequestParams extends BaseHttpRequestParams<Void> {

    private NewUserDto userDto;

    @Override
    public int getRequestMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getRequestBody() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", userDto.getUsername());
        map.put("firstName", userDto.getFirstName());
        map.put("lastName", userDto.getLastName());
        map.put("pesel", userDto.getPesel());
        map.put("password", userDto.getPassword());

        return new JSONObject(map).toString();
    }

    @Override
    public Class<Void> receivedDataClass() {
        return Void.class;
    }

    @Override
    public String getUrl() {
        return super.getUrl() + "/users/signup";
    }

}

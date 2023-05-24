import org.apache.camel.BindToRegistry;
import java.util.*;
import org.apache.camel.Exchange;
import com.google.gson.Gson;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import java.net.URLEncoder;

@Named("loginBean")
@BindToRegistry("loginBean")

public class Login {

    public class Credentials {
        String type;
        String value;
        Boolean temporary;

        public Credentials(String type, String value, Boolean temporary) {
            this.type = type;
            this.value = value;
            this.temporary = temporary;
        }
    }

    public class User {
        String username;
        String enabled;
        Credentials credentials[] = new Credentials[1];

        public User(String username, String type, String password, Boolean temporary) {
            this.username = username;
            this.enabled = "true";
            credentials[0] = new Credentials(type, password, temporary);
        }
    new RuntimeException("Username or password is null");
        exchange.setProperty("username", username);
        exchange.setProperty("password", password);
    }

    public void getUserCredentialsFromBody(Exchange exchange) {
        String requestBody = exchange.getIn().getBody(String.class);
        Gson gson = new Gson();
        Map<String, String> requestBodyMap = gson.fromJson(requestBody, Map.class);
        if (requestBodyMap.get("username") == null || requestBodyMap.get("password") == null) {
            throw new RuntimeException("Username or password is null");
        } else {
            User newUser = new User(requestBodyMap.get("username"), "password", requestBodyMap.get("password"), false);
            String newUserJson = gson.toJson(newUser);
            exchange.setProperty("newUserJson", newUserJson);
        }
    }

    public void getAccessToken(Exchange exchange) {
        String access_token = exchange.getIn().getHeader("access_token").toString();
        String refresh_token = exchange.getIn().getHeader("refresh_token").toString();
        if (access_token == null || refresh_token == null)
            throw new RuntimeException("Username or password is null");
        exchange.setProperty("access_token", access_token);
        exchange.setProperty("refresh_token", refresh_token);
    }

    public void checkIfExpired(Exchange exchange) {
        String responseAfterIntrospect = exchange.getIn().getBody(String.class);
        Gson gson = new Gson();
        Map<String, String> responseMap = gson.fromJson(responseAfterIntrospect, Map.class);
        exchange.setProperty("active", String.valueOf(responseMap.get("active")));
    }


   

    public void getAccessTokenFromBody(Exchange exchange) {
        String requestBody = exchange.getIn().getBody(String.class);
        Gson gson = new Gson();
        Map<String, String> requestBodyMap = gson.fromJson(requestBody, Map.class);
        if (requestBodyMap.get("access_token") == null) {
            exchange.getOut().setBody("{message: {Access token is null}}");
            exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, "500");
            throw new RuntimeException("Access token is null");
        } else {
            exchange.setProperty("accessToken", requestBodyMap.get("access_token"));
        }
    }
}

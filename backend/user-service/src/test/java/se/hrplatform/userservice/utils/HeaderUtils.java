package se.hrplatform.userservice.utils;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.springframework.http.HttpHeaders;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HeaderUtils {

  private static final String SECRET = "test-secret";
  private static final String USER = "test-user";
  private static final String TIMESTAMP = String.valueOf(System.currentTimeMillis());

  public static Headers createHeadersWithRolesForRestAssured(String roles) {
    String payload = USER + "|" + roles + "|" + TIMESTAMP;
    String signature = hmacSha256(payload, SECRET);
    return Headers.headers(
            new Header("X-User", USER),
            new Header("X-Roles", roles),
            new Header("X-Timestamp", TIMESTAMP),
            new Header("X-Signature", signature));
  }

  public static HttpHeaders createHeadersWithRolesForMockMvc(String roles) {
    String payload = USER + "|" + roles + "|" + TIMESTAMP;
    String signature = hmacSha256(payload, SECRET);

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-User", USER);
    headers.set("X-Roles", roles);
    headers.set("X-Timestamp", TIMESTAMP);
    headers.set("X-Signature", signature);

    return headers;
  }

  private static String hmacSha256(String data, String secret) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec key =
              new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      mac.init(key);
      return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    } catch (Exception e) {
      throw new IllegalStateException("HMAC error", e);
    }
  }
}


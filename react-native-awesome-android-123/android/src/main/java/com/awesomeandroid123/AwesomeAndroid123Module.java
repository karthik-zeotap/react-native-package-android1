package com.awesomeandroid123;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.zeotap.collect.Collect;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@ReactModule(name = AwesomeAndroid123Module.NAME)
public class AwesomeAndroid123Module extends ReactContextBaseJavaModule {
  public static final String NAME = "AwesomeAndroid123";

  String TAG = "Zeotap_App: ";
  private static final String ALGORITHM_TYPE_SHA_256 = "SHA-256";
  private static final String ENCODING_TYPE_UTF_8 = "UTF-8";
  Collect collect;

  public AwesomeAndroid123Module(ReactApplicationContext reactContext) {
    super(reactContext);
    collect = Collect.getInstance();
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }


  public static String hex(byte[] bytes) {
    StringBuilder uniqueIDBuilder = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      int bitData = (0xFF & bytes[i]);
      if (bitData <= 0xF) {
        uniqueIDBuilder.append("0");
      }
      uniqueIDBuilder.append(Integer.toHexString(bitData));
    }
    return new String(uniqueIDBuilder);
  }

  public static String digest(String string, String digestAlgorithm, String encoding) {
    try {
      MessageDigest digester = MessageDigest.getInstance(digestAlgorithm);
      digester.update(string.getBytes(encoding));
      return hex(digester.digest());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String sha256L(String stringToEncode) {
    return digest(stringToEncode.toLowerCase(), ALGORITHM_TYPE_SHA_256, ENCODING_TYPE_UTF_8);
  }

  public static String sha256(String stringToEncode) {
    return digest(stringToEncode, ALGORITHM_TYPE_SHA_256, ENCODING_TYPE_UTF_8);
  }

  @ReactMethod
  public void setEventNameProperties(String eventName) {
    Log.d("teest", "called from setEvent inside module");
    Log.d(TAG, "setEventProperties: EventName = " + eventName);
    collect.setEventProperties(eventName);
  }

  @ReactMethod
  public void setEventProperties(String eventName, ReadableMap eventProp) {
    Log.d(TAG, "setEventProperties: EventName = " + eventName + " EventProperty = " + eventProp.toHashMap().toString());
    collect.setEventProperties(eventName, eventProp.toHashMap());
  }

  @ReactMethod
  public void setInstantEventNameProperties(String eventName) {
    Log.d(TAG, "setInstantEventProperties: EventName = " + eventName);
    collect.setInstantEventProperties(eventName);
  }

  @ReactMethod
  public void setInstantEventProperties(String eventName, ReadableMap eventProp) {
    Log.d(TAG, "setInstantEventProperties: EventName = " + eventName + " EventProperty = " + eventProp.toHashMap().toString());
    collect.setInstantEventProperties(eventName, eventProp.toHashMap());
  }

  @ReactMethod
  public void setUserIdentities(ReadableMap userProp) {
    Log.d(TAG, "setUserIdentities: UserIdentities = " + userProp.toHashMap().toString());
    collect.setUserIdentities(userProp.toHashMap());
  }

  @ReactMethod
  public void hashUserIdentitiesAndSet(ReadableMap userProp, boolean useOldConfig) {
    Map<String, Object> identities = userProp.toHashMap();
    Map<String, Object> hashedIdentities = new HashMap<>();
    for (Map.Entry<String, Object> entry : identities.entrySet()) {
      switch (entry.getKey()) {
        case "email":
          hashedIdentities.put("email_sha256_lowercase", sha256L(entry.getValue().toString()));
          break;
        case "loginid":
          hashedIdentities.put("loginid_sha256_lowercase", sha256L(entry.getValue().toString()));
          break;
        case "cellno":
          if (((String) entry.getValue()).contains(" ")) {
            String[] ans = ((String) entry.getValue()).split(" ");
            hashedIdentities.put("cellno_without_country_code_sha256", sha256(ans[1]));
            hashedIdentities.put("cellno_with_country_code_sha256", sha256(ans[0] + ans[1]));
            hashedIdentities.put("cellno_e164_sha256", sha256("+" + ans[0] + ans[1]));
          } else {
            hashedIdentities.put("cellno_without_country_code_sha256", sha256(entry.getValue().toString()));
          }
          break;
        case "cellno_cc":
          if (useOldConfig) {
            hashedIdentities.put("cellno_with_country_code_sha256", sha256(entry.getValue().toString()));
          }
          break;
        default:
          hashedIdentities.put(entry.getKey(), entry.getValue());
      }
    }
    Log.d(TAG, "setUserIdentities: UserIdentities = " + userProp.toHashMap().toString());
    collect.setUserIdentities(hashedIdentities);
  }

  @ReactMethod
  public void setUserProperties(ReadableMap userProp) {
    Log.d(TAG, "setUserProperties: UserProperties = " + userProp.toHashMap().toString());
    collect.setUserProperties(userProp.toHashMap());
  }

  @ReactMethod
  public void setPageProperties(ReadableMap properties) {
    Log.d(TAG, "setPageProperties: PageProperty = " + properties.toHashMap().toString());
    collect.setPageProperties(properties.toHashMap());
  }

  @ReactMethod
  public void unsetUserIdentities() {
    Log.d(TAG, "unsetUserIdentities");
    collect.unsetUserIdentities();
  }

  @ReactMethod
  public void deInitiate() {
    Log.d(TAG, "pauseCollection");
    collect.pauseCollection();
  }

  @ReactMethod
  public void reInitialize() {
    Log.d(TAG, "resumeCollection");
    collect.resumeCollection();
  }

  @ReactMethod
  public void setConsent(ReadableMap consent) {
    Log.d(TAG, "setConsent: ConsentProperty = " + consent.toHashMap().toString());
    collect.setConsent(consent.toHashMap());
  }

  @ReactMethod
  public void addAskForConsentListener(Callback cb) {
    collect.listenToAskForConsent((data) -> cb.invoke());
  }

  @ReactMethod
  public void getZI(Callback cb) {
    String zi = collect.getZI();
    cb.invoke(zi);
  }
  @ReactMethod
  public void resetZI() {
    collect.resetZI();
  }
}

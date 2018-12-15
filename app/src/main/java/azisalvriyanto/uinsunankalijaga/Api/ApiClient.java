package azisalvriyanto.uinsunankalijaga.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClient {
    public static final String BASE_URL = "http://trainstays.com/hmkk/";  // Production

    private static Retrofit retrofit = null;
    private static boolean checkClient(){
        if(retrofit !=null){
            return true;
        } else {
            return false;
        }
    }

    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }

    public static Retrofit getClient() {
        if (checkClient()) {
            return getRetrofit();
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // set your desired log leve
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager();
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                    }
                })
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}

package th.com.bloomcode.paymentservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {

    public static <T> T convertJsonToObject(String json, Class<T> type) {

        ObjectMapper mapper = new ObjectMapper();
        T obj = null;
        try {
            obj = mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

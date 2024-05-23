package th.com.bloomcode.paymentservice.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Adapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(v);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    @Override
    public String marshal(String v) throws Exception {
        return Base64.getEncoder().encodeToString(v.getBytes(StandardCharsets.UTF_8));
    }

}

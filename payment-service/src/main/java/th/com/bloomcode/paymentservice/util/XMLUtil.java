package th.com.bloomcode.paymentservice.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {

    // covert XML to Java class object
    public static <T> T xmlUnmarshall(Class<T> clazz, String xml) throws Exception {
        try {
            // covert text to Java class object
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            @SuppressWarnings("unchecked")
            T header = (T) jaxbUnmarshaller.unmarshal(reader);
            reader.close();

            return header;
        } catch (Throwable t) {
            if (t.getMessage() == null)
                throw new Exception("Unknown XML error");
            throw t;
        }
    }

    // convert Java class object to XML
    public static <T> String xmlMarshall(Class<T> clazz, T o) throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();

            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(o, writer);
            String result = writer.toString();
            writer.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (Throwable t) {
            if (t.getMessage() == null)
                throw new Exception("Unknown XML error");
            throw t;
        }
    }
}

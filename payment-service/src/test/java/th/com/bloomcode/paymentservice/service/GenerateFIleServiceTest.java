//package th.com.bloomcode.paymentservice.service;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class GenerateFileServiceTest {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private GenerateFileService generateFileService;
//
//    @Autowired
//    private FileTransferService fileTransferService;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @DisplayName("Calculate Bank Fee")
//    void calculateSmartFeeTest() {
////        assertEquals(Util.getBigDecimal(8), generateFileService.calculateSmartFee("0010041052", Util.getBigDecimal(100000)));
//    }
//
//    @Test
//    @DisplayName("Check Swift Format")
//    void checkSwiftFormatTest() {
////        assertTrue(generateFileService.checkSwiftFormat("009", ""));
//    }
//
//    @Test
//    @DisplayName("Generate File")
//    void generateFileTest() {
////        generateFileService.generateFile((long) 1000000102);
////        assertTrue(null != generateFile);
//    }
//
//    @Test
//    @DisplayName("Send File")
//    void sendFile() {
//
//        String string = "This is a String.\nWe are going to convert it to InputStream.\n" +
//                "Greetings from JavaCodeGeeks!";
//
//        //use ByteArrayInputStream to get the bytes of the String and convert them to InputStream.
//        InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
//
////        fileTransferService.uploadFile(inputStream,"20201223","kkkk.txt");
//        fileTransferService.downloadFile("aaa","bbb");
////        fileTransferService.uploadFile("/Users/nerves/Documents/GenFile/S050820201900083", "./OUTGOING/asdasd");
//    }
//}
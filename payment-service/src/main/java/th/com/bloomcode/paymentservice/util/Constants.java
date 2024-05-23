package th.com.bloomcode.paymentservice.util;

public class Constants {


    public static final String RUN_STATUS_SUCCESS = "ชำระเงินสำเร็จ";
    public static final String RUN_STATUS_ERROR = "ชำระเงินไม่สำเร็จ";

    public static final String PROPOSAL_STATUS_SUCCESS = "สร้างข้อเสนอสำเร็จ";
    public static final String PROPOSAL_STATUS_ERROR = "สร้างข้อเสนอไม่สำเร็จ";

    public static final String PARAMETER_STATUS_SUCCESS = "บันทึกพารามิเตอร์สำเร็จ";
    public static final String PARAMETER_STATUS_ERROR = "ชำระเงินไม่สำเร็จ";


    public static final String GENERATE_FILE_STATUS_SUCCESS = "สร้างไฟล์ชำระเงินสำเร็จ";
    public static final String GENERATE_FILE_STATUS_ERROR = "สร้างไฟล์ชำระเงินไม่สำเร็จ";
    public static final String GENERATE_FILE_STATUS_WAIT= "รอดำเนินการสร้างไฟล์ชำระเงิน";

    public static final Long PAIN_SEQ_MIN_VALUE = 40001L;
    public static final Long PAIN_SEQ_MAX_VALUE = 49999L;
    public static final Long SWIFT_SEQ_MIN_VALUE = 400001L;
    public static final Long SWIFT_SEQ_MAX_VALUE = 499999L;
    public static final Long SWIFT_RUNNING_SEQ_MIN_VALUE = 4000001L;
    public static final Long SWIFT_RUNNING_SEQ_MAX_VALUE = 4999999L;
    public static final Long GIRO_SEQ_MIN_VALUE = 401L;
    public static final Long GIRO_SEQ_MAX_VALUE = 499L;
    public static final Long INHOUSE_SEQ_MIN_VALUE = 401L;
    public static final Long INHOUSE_SEQ_MAX_VALUE = 499L;
    public static final Long GGIRO_SEQ_MIN_VALUE = 401L;
    public static final Long GGIRO_SEQ_MAX_VALUE = 499L;
    public static final Long INH_SEQ_MIN_VALUE = 401L;
    public static final Long INH_SEQ_MAX_VALUE = 499L;
    public static final Long GM_SEQ_MIN_VALUE = 401L;
    public static final Long GM_SEQ_MAX_VALUE = 499L;
    public static final Long LOG_SEQ_MIN_VALUE = 5L;
    public static final Long LOG_SEQ_MAX_VALUE = 9999999999L;
}

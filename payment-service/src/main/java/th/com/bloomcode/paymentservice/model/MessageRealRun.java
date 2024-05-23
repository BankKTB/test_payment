package th.com.bloomcode.paymentservice.model;

public enum MessageRealRun {

    MessageSuccess(">> เลขที่เอกสาร {0} {1} {2} ได้ เลขที่เอกสารจ่าย  {3} {4} {5}  "),


    MessageErrorStep("|--------------------------------------------------------------------------------------| "),
    MessageErrorStep1(">> เลขที่เอกสาร {0} {1} {2} ไม่สำเร็จ                                                       "),
    MessageErrorStep2(">> รายการข้อผิดพลาดดังนี้                                                                    ");


    private final String messageText;

    MessageRealRun(String messageText) {
        this.messageText = messageText;
    }


    public String getMessageText() {
        return messageText;
    }
}

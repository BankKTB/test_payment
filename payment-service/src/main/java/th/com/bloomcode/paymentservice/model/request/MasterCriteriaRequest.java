//package th.com.bloomcode.paymentservice.model.request;
//
//import lombok.Data;
//
//import java.util.List;
//
//@Data
//public class MasterCriteriaRequest {
//
//    private String companyCode;
//    private String fiscalYear;
//    private String fiArea;
//    private String paymentCenter;
//    private String value;
//    private String formId;
//    private String docType;
//    private String taxId;
//    private String oldCompanyCode;
//    private String bankKey;
//    private String depositAccountCode;
//    private String depositAccountCodeOwner;
//    private String glAccount;
//    private String countryCode;
//    private String vendorGroup;
//    // exclude
//    private boolean exclude;
//    private boolean inter;
//    //vendor
//    private String vendorTaxId;
//    // Authorization
////    private List<Authorize> authorizes;
//    private String authorizationObject;
//    private String authorizationAttribute;
//    private String authorizationActivity;
//    private List<String> authorizationObjects;
//    // Authorization use for ignore global and object
//    private boolean authorizesIgnore;
//    // Document Base type For FM Module program type BT
//    private String docBaseType;
//    // subWithDrawCompanyCode For FM Module program type BT
//    private boolean subWithDrawCompanyCode;
//    private boolean over;
//
//    @Override
//    public String toString() {
//        return "MasterCriteriaRequest{" +
//            "companyCode='" + companyCode + '\'' +
//            ", fiscalYear='" + fiscalYear + '\'' +
//            ", fiArea='" + fiArea + '\'' +
//            ", paymentCenter='" + paymentCenter + '\'' +
//            ", value='" + value + '\'' +
//            ", formId='" + formId + '\'' +
//            ", docType='" + docType + '\'' +
//            ", taxId='" + taxId + '\'' +
//            ", oldCompanyCode='" + oldCompanyCode + '\'' +
//            ", bankKey='" + bankKey + '\'' +
//            ", depositAccountCode='" + depositAccountCode + '\'' +
//            ", depositAccountCodeOwner='" + depositAccountCodeOwner + '\'' +
//            ", glAccount='" + glAccount + '\'' +
//            ", countryCode='" + countryCode + '\'' +
//            ", exclude=" + exclude +
//            ", vendorTaxId='" + vendorTaxId + '\'' +
//            ", authorizes=" + authorizes +
//            ", authorizationObject='" + authorizationObject + '\'' +
//            ", authorizationAttribute='" + authorizationAttribute + '\'' +
//            ", authorizationActivity='" + authorizationActivity + '\'' +
//            ", authorizationObjects=" + authorizationObjects + '\'' +
//            ", docBaseType=" + docBaseType +
//            '}';
//    }
//}

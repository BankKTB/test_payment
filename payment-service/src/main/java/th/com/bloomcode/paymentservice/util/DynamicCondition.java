package th.com.bloomcode.paymentservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.model.common.*;
import th.com.bloomcode.paymentservice.model.config.*;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.request.PaymentBlockListDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.SelectGroupDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.SelectGroupDocumentService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DynamicCondition {

    private static SelectGroupDocumentService selectGroupDocumentService;

    public DynamicCondition(SelectGroupDocumentService selectGroupDocumentService) {
        DynamicCondition.selectGroupDocumentService = selectGroupDocumentService;
    }


    public static void companyCondition(List<CompanyCondition> companyConditions, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != companyConditions && companyConditions.size() > 0) {
            if (companyConditions.size() == 1) {
                for (CompanyCondition companyCondition : companyConditions) {
                    if (!Util.isEmpty(companyCondition.getCompanyFrom())) {
                        sb.append(SqlUtil.whereClauseRangeOne(companyCondition.getCompanyFrom(),
                                companyCondition.getCompanyTo(), "GH.COMPANY_CODE", params, ++index));
                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = companyConditions.size();
                String checkOptionExclude = "";
                for (int i = 0; i < companyConditions.size(); i++) {
                    if (i == 0) {
                        checkOptionExclude = "AND";
                    } else {
                        if (companyConditions.get(i - 1).isOptionExclude()) {
                            checkOptionExclude = "AND";
                        } else {
                            checkOptionExclude = "OR";
                        }

                    }
                    if (!Util.isEmpty(companyConditions.get(i).getCompanyFrom())) {
                        sb.append(SqlUtil.newWhereClauseRangeOr(companyConditions.get(i).getCompanyFrom(),
                                companyConditions.get(i).getCompanyTo(), "GH.COMPANY_CODE", params, ++index, sequence, checkOptionExclude));
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
//                sb.append(")");
            }
        }
    }

    public static void companyConditionTuning(List<CompanyCondition> companyConditions, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != companyConditions && companyConditions.size() > 0) {
            if (companyConditions.size() == 1) {
                for (CompanyCondition companyCondition : companyConditions) {
                    if (!Util.isEmpty(companyCondition.getCompanyFrom())) {
                        sb.append(SqlUtil.whereClauseRangeOneTuning(companyCondition.getCompanyFrom(),
                                companyCondition.getCompanyTo(), "GH.COMPANY_CODE", params, ++index));
                    }
                }
            } else {
                int sequence = 0;
                int lastSequence = companyConditions.size();
                String checkOptionExclude = "";
                for (int i = 0; i < companyConditions.size(); i++) {
                    if (i == 0) {
                        checkOptionExclude = "AND";
                    } else {
                        if (companyConditions.get(i - 1).isOptionExclude()) {
                            checkOptionExclude = "AND";
                        } else {
                            checkOptionExclude = "OR";
                        }

                    }
                    if (!Util.isEmpty(companyConditions.get(i).getCompanyFrom())) {
                        sb.append(SqlUtil.newWhereClauseRangeOrTuning(companyConditions.get(i).getCompanyFrom(),
                                companyConditions.get(i).getCompanyTo(), "GH.COMPANY_CODE", params, ++index, sequence, checkOptionExclude));
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
//                sb.append(")");
            }
        }
    }

//    public static void vendorCondition(List<VendorConfig> vendors, StringBuilder sb, List<Object> params) {
//        int index = 0;
//        if (null != vendors && vendors.size() > 0) {
//            boolean isValue = vendors.stream()
//                    .anyMatch(v -> !"".equalsIgnoreCase(v.getVendorTaxIdFrom()));
//            if (isValue) {
//                if (vendors.size() == 1) {
//                    for (VendorConfig vendor : vendors) {
//                        if (!Util.isEmpty(vendor.getVendorTaxIdFrom())) {
//                            if (vendor.isOptionExclude()) {
//                                sb.append(SqlUtil.whereClauseNotRangeOne(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
//                                        "VD.VALUE", params, ++index));
//                            } else {
//                                sb.append(SqlUtil.whereClauseRangeOne(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
//                                        "VD.VALUE", params, ++index));
//                            }
//                        }
//                    }
//                } else {
//                    int sequence = 0;
//                    int lastSequence = vendors.size();
//                    for (VendorConfig vendor : vendors) {
//                        if (!Util.isEmpty(vendor.getVendorTaxIdFrom())) {
//                            if (vendor.isOptionExclude()) {
//                                sb.append(SqlUtil.whereClauseNotRangeOr(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
//                                        "VD.VALUE", params, ++index, sequence));
//
//                            } else {
//                                sb.append(SqlUtil.whereClauseRangeOr(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
//                                        "VD.VALUE", params, ++index, sequence));
//                            }
//                            sequence++;
//                        } else {
//                            sequence++;
//                        }
//                        if (lastSequence == sequence) {
//                            sb.append(")");
//                        }
//                    }
////                    sb.append(")");
//                }
//            }
//        }
//    }

    public static void newVendorCondition(List<VendorConfig> vendorConfigs, StringBuilder sb, List<Object> params, String column) {
        int index = 0;
        if (null != vendorConfigs && vendorConfigs.size() > 0) {
            boolean isValue = vendorConfigs.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getVendorTaxIdFrom()) && null != v.getVendorTaxIdFrom());
            log.info(" isValue ", isValue);
            if (isValue) {
                if (vendorConfigs.size() == 1) {
                    for (VendorConfig vendor : vendorConfigs) {
                        if (!Util.isEmpty(vendor.getVendorTaxIdFrom())) {
                            if (vendor.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
                                        column, params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
                                        column, params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = vendorConfigs.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < vendorConfigs.size(); i++) {
//                    for (BaseRange baseRange : baseRanges) {
                        if (!Util.isEmpty(vendorConfigs.get(i).getVendorTaxIdFrom())) {
                            if (i == 0) {
                                checkOptionExclude = "AND";
                            } else {
                                if (vendorConfigs.get(i - 1).isOptionExclude()) {
                                    checkOptionExclude = "AND";
                                } else {
                                    checkOptionExclude = "OR";
                                }

                            }
                            log.info(" column {} ", column);
                            log.info(" checkOptionExclude {} ", checkOptionExclude);
                            log.info(" baseRanges {} ", vendorConfigs.get(i));

                            if (vendorConfigs.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeOr(vendorConfigs.get(i).getVendorTaxIdFrom(), vendorConfigs.get(i).getVendorTaxIdTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeOr(vendorConfigs.get(i).getVendorTaxIdFrom(), vendorConfigs.get(i).getVendorTaxIdTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            }
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void newVendorConditionTuning(List<VendorConfig> vendorConfigs, StringBuilder sb, List<Object> params, String column) {
        int index = 0;
        if (null != vendorConfigs && vendorConfigs.size() > 0) {
            boolean isValue = vendorConfigs.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getVendorTaxIdFrom()) && null != v.getVendorTaxIdFrom());
            if (isValue) {
                if (vendorConfigs.size() == 1) {
                    for (VendorConfig vendor : vendorConfigs) {
                        if (!Util.isEmpty(vendor.getVendorTaxIdFrom())) {
                            if (vendor.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOneTuning(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
                                        column, params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOneTuning(vendor.getVendorTaxIdFrom(), vendor.getVendorTaxIdTo(),
                                        column, params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = vendorConfigs.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < vendorConfigs.size(); i++) {
//                    for (BaseRange baseRange : baseRanges) {
                        if (!Util.isEmpty(vendorConfigs.get(i).getVendorTaxIdFrom())) {
                            if (i == 0) {
                                checkOptionExclude = "AND";
                            } else {
                                if (vendorConfigs.get(i).isOptionExclude()) {
                                    checkOptionExclude = "AND";
                                } else {
                                    checkOptionExclude = "OR";
                                }

                            }

                            if (vendorConfigs.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeOrTuning(vendorConfigs.get(i).getVendorTaxIdFrom(), vendorConfigs.get(i).getVendorTaxIdTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeOrTuning(vendorConfigs.get(i).getVendorTaxIdFrom(), vendorConfigs.get(i).getVendorTaxIdTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            }
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }

//    public static void independentCondition(List<Independent> independents, StringBuilder sb, Map<String, Object> params) {
//        int index = 0;
//        if (null != independents && independents.size() > 0) {
//            for (Independent independent : independents) {
//                if (null != independent.getCondition() && independent.getCondition().size() > 0) {
//                    boolean isValue = independent.getCondition().stream()
//                            .anyMatch(c -> !"".equalsIgnoreCase(c.getConditionFieldFrom()));
//                    if (isValue) {
//                        if (independent.getCondition().size() == 1) {
//                            for (Condition condition : independent.getCondition()) {
//                                if (!Util.isEmpty(condition.getConditionFieldFrom())) {
//                                    if (independent.isOptionExclude()) {
//                                        sb.append(SqlUtil.whereClauseNotRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
//                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
//                                    } else {
//                                        sb.append(SqlUtil.whereClauseRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
//                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
//                                    }
//                                }
//                            }
//                        } else {
//                            for (Condition condition : independent.getCondition()) {
//                                if (!Util.isEmpty(condition.getConditionFieldFrom())) {
//                                    if (independent.isOptionExclude()) {
//                                        sb.append(SqlUtil.whereClauseNotRangeOr(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
//                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
//                                    } else {
//                                        sb.append(SqlUtil.whereClauseRangeOr(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
//                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
//                                    }
//                                }
//                            }
//                        }
//                        sb.append(")");
//                    }
//                }
//            }
//        }
//    }


    public static void independentCondition(List<Independent> independents, StringBuilder sb, List<Object> params) {
        if (null != independents && independents.size() > 0) {
            for (Independent independent : independents) {
                int index = 0;
                boolean isValue = independent.getCondition().stream()
                        .anyMatch(v -> !"".equalsIgnoreCase(v.getConditionFieldFrom()));
                if (isValue) {
                    if (independent.getCondition().size() == 1) {
                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(condition.getConditionFieldFrom())) {
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")) {

                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(condition.getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(condition.getConditionFieldTo());

                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                }
                            }

                        }
                    } else {
                        int sequence = 0;
                        int lastSequence = independent.getCondition().size();
                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(condition.getConditionFieldFrom())) {
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")) {

                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(condition.getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(condition.getConditionFieldTo());
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    }
                                    sequence++;
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOr(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOr(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    }
                                    sequence++;
                                }

                            } else {
                                sequence++;
                            }
                            if (lastSequence == sequence && !Util.isEmpty(condition.getConditionFieldFrom())) {
                                sb.append(")");
                            }
                        }
//                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void companyConditionOM(List<CompanyCodeCondition> companyConditions, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != companyConditions && companyConditions.size() > 0) {
            boolean isValue = companyConditions.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getCompanyCodeFrom()));
            if (isValue) {
                if (companyConditions.size() == 1) {
                    for (CompanyCodeCondition companyCondition : companyConditions) {
                        if (!Util.isEmpty(companyCondition.getCompanyCodeFrom())) {
                            if (companyCondition.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(companyCondition.getCompanyCodeFrom(), companyCondition.getCompanyCodeTo(),
                                        "H.COMPANY_CODE", params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(companyCondition.getCompanyCodeFrom(), companyCondition.getCompanyCodeTo(),
                                        "H.COMPANY_CODE", params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = companyConditions.size();
                    for (CompanyCodeCondition companyCondition : companyConditions) {
                        if (!Util.isEmpty(companyCondition.getCompanyCodeFrom())) {
                            if (companyCondition.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOr(companyCondition.getCompanyCodeFrom(), companyCondition.getCompanyCodeTo(),
                                        "H.COMPANY_CODE", params, ++index, sequence));

                            } else {
                                sb.append(SqlUtil.whereClauseRangeOr(companyCondition.getCompanyCodeFrom(), companyCondition.getCompanyCodeTo(),
                                        "H.COMPANY_CODE", params, ++index, sequence));
                            }
                            sequence++;
                        } else {
                            sequence++;
                        }
                        if (lastSequence == sequence) {
                            sb.append(")");
                        }
                    }
                }
            }
        }
    }


    public static void docTypeConditionOM(List<DocType> docTypes, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != docTypes && docTypes.size() > 0) {
            boolean isValue = docTypes.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getDocTypeFrom()));
            if (isValue) {
                if (docTypes.size() == 1) {
                    for (DocType docType : docTypes) {
                        if (!Util.isEmpty(docType.getDocTypeFrom())) {
                            if (docType.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(docType.getDocTypeFrom(), docType.getDocTypeTo(),
                                        "H.DOCUMENT_TYPE", params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(docType.getDocTypeFrom(), docType.getDocTypeTo(),
                                        "H.DOCUMENT_TYPE", params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = docTypes.size();
                    for (DocType docType : docTypes) {
                        if (!Util.isEmpty(docType.getDocTypeFrom())) {
                            if (docType.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOr(docType.getDocTypeFrom(), docType.getDocTypeTo(),
                                        "H.DOCUMENT_TYPE", params, ++index, sequence));

                            } else {
                                sb.append(SqlUtil.whereClauseRangeOr(docType.getDocTypeFrom(), docType.getDocTypeTo(),
                                        "H.DOCUMENT_TYPE", params, ++index, sequence));
                            }
                            sequence++;
                        } else {
                            sequence++;
                        }
                        if (lastSequence == sequence) {
                            sb.append(")");
                        }
                    }
                }
            }
        }
    }


    public static void paymentMethodConditionOM(List<PaymentMethod> paymentMethods, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != paymentMethods && paymentMethods.size() > 0) {
            boolean isValue = paymentMethods.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getPaymentMethodFrom()));
            if (isValue) {
                if (paymentMethods.size() == 1) {
                    for (PaymentMethod paymentMethod : paymentMethods) {
                        if (!Util.isEmpty(paymentMethod.getPaymentMethodFrom())) {
                            if (paymentMethod.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "L.PAYMENT_METHOD", params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "L.PAYMENT_METHOD", params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = paymentMethods.size();
                    for (PaymentMethod paymentMethod : paymentMethods) {
                        if (!Util.isEmpty(paymentMethod.getPaymentMethodFrom())) {
                            if (paymentMethod.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOr(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "L.PAYMENT_METHOD", params, ++index, sequence));

                            } else {
                                sb.append(SqlUtil.whereClauseRangeOr(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "L.PAYMENT_METHOD", params, ++index, sequence));
                            }
                            sequence++;
                        } else {
                            sequence++;
                        }
                        if (lastSequence == sequence) {
                            sb.append(")");
                        }
                    }
                }
            }
        }
    }


    public static void findChildCondition(List<PaymentBlockListDocumentRequest> documentRequestList, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != documentRequestList && documentRequestList.size() > 0) {
            if (documentRequestList.size() == 1) {
                for (PaymentBlockListDocumentRequest documentRequest : documentRequestList) {
                    sb.append(SqlUtil.whereClauseChildRangeOne(documentRequest.getCompanyCode(), documentRequest.getOriginalDocumentNo(),
                            documentRequest.getOriginalFiscalYear(), "H.COMPANY_CODE", "L.INVOICE_DOCUMENT_NO", "L.INVOICE_FISCAL_YEAR", params, ++index));

                }
            } else {
                int sequence = 0;
                int lastSequence = documentRequestList.size();
                for (PaymentBlockListDocumentRequest documentRequest : documentRequestList) {
                    sb.append(SqlUtil.whereClauseChildRangeOr(documentRequest.getCompanyCode(), documentRequest.getOriginalDocumentNo(),
                            documentRequest.getOriginalFiscalYear(), "H.COMPANY_CODE", "L.INVOICE_DOCUMENT_NO", "L.INVOICE_FISCAL_YEAR", params, ++index, sequence));
                    sequence++;

                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void findParentCondition(List<PaymentBlockListDocumentRequest> documentRequestList, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != documentRequestList && documentRequestList.size() > 0) {
            if (documentRequestList.size() == 1) {
                for (PaymentBlockListDocumentRequest documentRequest : documentRequestList) {
                    sb.append(SqlUtil.whereClauseParentRangeOne(documentRequest.getCompanyCode(), documentRequest.getOriginalDocumentNo(),
                            documentRequest.getOriginalFiscalYear(), "H.COMPANY_CODE", "H.ORIGINAL_DOCUMENT_NO", "H.ORIGINAL_FISCAL_YEAR", params, ++index));

                }
            } else {
                int sequence = 0;
                int lastSequence = documentRequestList.size();
                for (PaymentBlockListDocumentRequest documentRequest : documentRequestList) {
                    sb.append(SqlUtil.whereClauseParentRangeOr(documentRequest.getCompanyCode(), documentRequest.getOriginalDocumentNo(),
                            documentRequest.getOriginalFiscalYear(), "H.COMPANY_CODE", "H.ORIGINAL_DOCUMENT_NO", "H.ORIGINAL_FISCAL_YEAR", params, ++index, sequence));
                    sequence++;

                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void baseRangeConditionOM(List<BaseRange> baseRanges, StringBuilder sb, List<Object> params, String column) {
        int index = 0;
        if (null != baseRanges && baseRanges.size() > 0) {
            boolean isValue = baseRanges.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getFrom()) && null != v.getFrom());
            log.info(" isValue ", isValue);
            if (isValue) {
                if (baseRanges.size() == 1) {
                    for (BaseRange baseRange : baseRanges) {
                        if (!Util.isEmpty(baseRange.getFrom())) {
                            if (baseRange.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(baseRange.getFrom(), baseRange.getTo(),
                                        column, params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(baseRange.getFrom(), baseRange.getTo(),
                                        column, params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = baseRanges.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < baseRanges.size(); i++) {
                        if (!Util.isEmpty(baseRanges.get(i).getFrom())) {
                            if (i == 0) {
                                checkOptionExclude = "AND";
                            } else {
                                if (baseRanges.get(i - 1).isOptionExclude()) {
                                    checkOptionExclude = "AND";
                                } else {
                                    if (baseRanges.get(i).isOptionExclude()) {
                                        checkOptionExclude = "AND";
                                    } else {
                                        checkOptionExclude = "OR";
                                    }

                                }

                            }
                            log.info(" column {} ", column);
                            log.info(" checkOptionExclude {} ", checkOptionExclude);
                            log.info(" baseRanges {} ", baseRanges.get(i));

                            if (baseRanges.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeOr(baseRanges.get(i).getFrom(), baseRanges.get(i).getTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeOr(baseRanges.get(i).getFrom(), baseRanges.get(i).getTo(),
                                        column, params, ++index, sequence, checkOptionExclude));
                            }
                        }
                        sequence++;
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }


    public static void newIndependentCondition(List<Independent> independents, StringBuilder sb, List<Object> params) {
        if (null != independents && independents.size() > 0) {
            for (Independent independent : independents) {
                int index = 0;
                boolean isValue = independent.getCondition().stream()
                        .anyMatch(v -> !"".equalsIgnoreCase(v.getConditionFieldFrom()));
                if (isValue) {
                    if (independent.getCondition().size() == 1) {
                        log.info(" independent {}", independent.getCondition());
                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(condition.getConditionFieldFrom())) {
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")|| independent.getDbName().equalsIgnoreCase("DATE_DOC")) {

                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(condition.getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(condition.getConditionFieldTo());

                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOne(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                }
                            }

                        }
                    } else {
                        int sequence = 0;
                        int lastSequence = independent.getCondition().size();
                        String checkOptionExclude = "";
                        for (int i = 0; i < independent.getCondition().size(); i++) {
//                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(independent.getCondition().get(i).getConditionFieldFrom())) {
                                if (i == 0) {
                                    checkOptionExclude = "AND";
                                } else {
                                    if (independent.isOptionExclude()) {
                                        checkOptionExclude = "AND";
                                    } else {
                                        checkOptionExclude = "OR";
                                    }

                                }
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")|| independent.getDbName().equalsIgnoreCase("DATE_DOC")) {

                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(independent.getCondition().get(i).getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(independent.getCondition().get(i).getConditionFieldTo());
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    }
                                    sequence++;
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.newWhereClauseNotRangeOr(independent.getCondition().get(i).getConditionFieldFrom(), independent.getCondition().get(i).getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence, checkOptionExclude));
                                    } else {
                                        sb.append(SqlUtil.newWhereClauseRangeOr(independent.getCondition().get(i).getConditionFieldFrom(), independent.getCondition().get(i).getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence, checkOptionExclude));
                                    }
                                    sequence++;
                                }

                            } else {
                                sequence++;
                            }
                            if (lastSequence == sequence && !Util.isEmpty(independent.getCondition().get(i).getConditionFieldFrom())) {
                                sb.append(")");
                            }
                        }
//                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void newIndependentConditionTuning(List<Independent> independents, StringBuilder sb, List<Object> params) {
        if (null != independents && independents.size() > 0) {
            for (Independent independent : independents) {
                int index = 0;
                boolean isValue = independent.getCondition().stream()
                        .anyMatch(v -> !"".equalsIgnoreCase(v.getConditionFieldFrom()));
                if (isValue) {
                    if (independent.getCondition().size() == 1) {
                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(condition.getConditionFieldFrom())) {
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")|| independent.getDbName().equalsIgnoreCase("DATE_DOC")) {
                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(condition.getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(condition.getConditionFieldTo());

                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOneTime(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeOneTuning(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeOneTuning(condition.getConditionFieldFrom(), condition.getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index));
                                    }
                                }
                            }

                        }
                    } else {
                        int sequence = 0;
                        int lastSequence = independent.getCondition().size();
                        String checkOptionExclude = "";
                        for (int i = 0; i < independent.getCondition().size(); i++) {
//                        for (Condition condition : independent.getCondition()) {
                            if (!Util.isEmpty(independent.getCondition().get(i).getConditionFieldFrom())) {
                                if (i == 0) {
                                    checkOptionExclude = "AND";
                                } else {
                                    if (independent.isOptionExclude()) {
                                        checkOptionExclude = "AND";
                                    } else {
                                        checkOptionExclude = "OR";
                                    }

                                }
                                if (independent.getDbName().equalsIgnoreCase("DATE_ACCT") || independent.getDbName().equalsIgnoreCase("DOCUMENT_CREATED")|| independent.getDbName().equalsIgnoreCase("DATE_DOC")) {

                                    Timestamp conditionFrom = Util.stringToTimestampForIndependent(independent.getCondition().get(i).getConditionFieldFrom());
                                    Timestamp conditionTo = Util.stringToTimestampForIndependent(independent.getCondition().get(i).getConditionFieldTo());
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.whereClauseNotRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    } else {
                                        sb.append(SqlUtil.whereClauseRangeTimeOr(conditionFrom, conditionTo,
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence));
                                    }
                                    sequence++;
                                } else {
                                    if (independent.isOptionExclude()) {
                                        sb.append(SqlUtil.newWhereClauseNotRangeOrTuning(independent.getCondition().get(i).getConditionFieldFrom(), independent.getCondition().get(i).getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence, checkOptionExclude));
                                    } else {
                                        sb.append(SqlUtil.newWhereClauseRangeOrTuning(independent.getCondition().get(i).getConditionFieldFrom(), independent.getCondition().get(i).getConditionFieldTo(),
                                                independent.getTableName() + "." + independent.getDbName(), params, ++index, sequence, checkOptionExclude));
                                    }
                                    sequence++;
                                }

                            } else {
                                sequence++;
                            }
                            if (lastSequence == sequence && !Util.isEmpty(independent.getCondition().get(i).getConditionFieldFrom())) {
                                sb.append(")");
                            }
                        }
//                        sb.append(")");
                    }
                }
            }
        }
    }

    public static void paymentMethodForRun(List<PaymentMethod> paymentMethods, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != paymentMethods && paymentMethods.size() > 0) {
            boolean isValue = paymentMethods.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getPaymentMethodFrom()));
            if (isValue) {
                if (paymentMethods.size() == 1) {
                    for (PaymentMethod paymentMethod : paymentMethods) {
                        if (!Util.isEmpty(paymentMethod.getPaymentMethodFrom())) {
                            if (paymentMethod.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOne(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOne(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = paymentMethods.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < paymentMethods.size(); i++) {
//                    for (BaseRange baseRange : baseRanges) {

                        if (i == 0) {
                            checkOptionExclude = "AND";
                        } else {
                            if (paymentMethods.get(i - 1).isOptionExclude()) {
                                checkOptionExclude = "AND";
                            } else {
                                checkOptionExclude = "OR";
                            }

                        }
                        if (!Util.isEmpty(paymentMethods.get(i).getPaymentMethodFrom())) {
                            if (paymentMethods.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeOr(paymentMethods.get(i).getPaymentMethodFrom(), paymentMethods.get(i).getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index, sequence, checkOptionExclude));

                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeOr(paymentMethods.get(i).getPaymentMethodFrom(), paymentMethods.get(i).getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index, sequence, checkOptionExclude));
                            }
                            sequence++;
                        } else {
                            sequence++;
                        }
                        if (lastSequence == sequence) {
                            sb.append(")");
                        }
                    }
                }
            }
        }
    }

    public static void paymentMethodForRunTuning(List<PaymentMethod> paymentMethods, StringBuilder sb, List<Object> params) {
        int index = 0;
        if (null != paymentMethods && paymentMethods.size() > 0) {
            boolean isValue = paymentMethods.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getPaymentMethodFrom()));
            if (isValue) {
                if (paymentMethods.size() == 1) {
                    for (PaymentMethod paymentMethod : paymentMethods) {
                        if (!Util.isEmpty(paymentMethod.getPaymentMethodFrom())) {
                            if (paymentMethod.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOneTuning(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOneTuning(paymentMethod.getPaymentMethodFrom(), paymentMethod.getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index));
                            }
                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = paymentMethods.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < paymentMethods.size(); i++) {
//                    for (BaseRange baseRange : baseRanges) {

                        if (i == 0) {
                            checkOptionExclude = "AND";
                        } else {
                            if (paymentMethods.get(i - 1).isOptionExclude()) {
                                checkOptionExclude = "AND";
                            } else {
                                checkOptionExclude = "OR";
                            }

                        }
                        if (!Util.isEmpty(paymentMethods.get(i).getPaymentMethodFrom())) {
                            if (paymentMethods.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeOrTuning(paymentMethods.get(i).getPaymentMethodFrom(), paymentMethods.get(i).getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index, sequence, checkOptionExclude));

                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeOrTuning(paymentMethods.get(i).getPaymentMethodFrom(), paymentMethods.get(i).getPaymentMethodTo(),
                                        "GL.PAYMENT_METHOD", params, ++index, sequence, checkOptionExclude));
                            }
                            sequence++;
                        } else {
                            sequence++;
                        }
                        if (lastSequence == sequence) {
                            sb.append(")");
                        }
                    }
                }
            }
        }
    }

    public static void baseDateRangeConditionOM(List<BaseRange> baseRanges, StringBuilder sb, List<Object> params, String column) {
        int index = 0;

        baseRanges.sort((a, b) -> (a.isOptionExclude() == b.isOptionExclude() ? 0 : a.isOptionExclude() ? 1 : -1));
        if (null != baseRanges && baseRanges.size() > 0) {
            boolean isValue = baseRanges.stream()
                    .anyMatch(v -> !"".equalsIgnoreCase(v.getFrom()) && null != v.getFrom());
            log.info(" isValue ", isValue);
            if (isValue) {
                if (baseRanges.size() == 1) {
                    for (BaseRange baseRange : baseRanges) {
                        if (!Util.isEmpty(baseRange.getFrom())) {

                            Timestamp conditionFrom = Util.stringToTimestamp(baseRange.getFrom());
                            Timestamp conditionTo = Util.stringToTimestamp(baseRange.getTo());

                            if (baseRange.isOptionExclude()) {
                                sb.append(SqlUtil.whereClauseNotRangeOneTime(conditionFrom, conditionTo,
                                        column, params, ++index));
                            } else {
                                sb.append(SqlUtil.whereClauseRangeOneTime(conditionFrom, conditionTo,
                                        column, params, ++index));
                            }

                        }
                    }
                } else {
                    int sequence = 0;
                    int lastSequence = baseRanges.size();
                    String checkOptionExclude = "";
                    for (int i = 0; i < baseRanges.size(); i++) {
                        if (!Util.isEmpty(baseRanges.get(i).getFrom())) {
                            if (i == 0) {
                                checkOptionExclude = "AND";
                            } else {
                                if (baseRanges.get(i - 1).isOptionExclude()) {
                                    if (baseRanges.get(i).isOptionExclude()) {
                                        checkOptionExclude = "AND";
                                    } else {
                                        checkOptionExclude = "AND";
                                    }
                                } else {
                                    if (baseRanges.get(i).isOptionExclude()) {
                                        checkOptionExclude = "AND";
                                    } else {
                                        checkOptionExclude = "OR";
                                    }

                                }

                            }

                            Timestamp conditionFrom = Util.stringToTimestamp(baseRanges.get(i).getFrom());
                            Timestamp conditionTo = Util.stringToTimestamp(baseRanges.get(i).getTo());
                            if (baseRanges.get(i).isOptionExclude()) {
                                sb.append(SqlUtil.newWhereClauseNotRangeTimeOr(conditionFrom, conditionTo,
                                        column, params, ++index, sequence, checkOptionExclude));
                            } else {
                                sb.append(SqlUtil.newWhereClauseRangeTimeOr(conditionFrom, conditionTo,
                                        column, params, ++index, sequence, checkOptionExclude));
                            }
                            sequence++;
                        }
                    }
                    if (lastSequence == sequence) {
                        sb.append(")");
                    }
                }
            }
        }
    }
}


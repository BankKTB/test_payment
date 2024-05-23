//package th.com.bloomcode.paymentservice.service;
//
//import th.com.bloomcode.paymentservice.model.config.SelectGroupDocumentConfig;
//import th.com.bloomcode.paymentservice.model.config.SelectGroupDocumentList;
//import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
//import th.com.bloomcode.paymentservice.util.JSONUtil;
//import th.com.bloomcode.paymentservice.util.SqlUtil;
//import th.com.bloomcode.paymentservice.util.Util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Indicator {
//
//    public static void main(String[] args) {
//
//
//        String jsonText = "{\n" +
//                "  \"groupList\": [\n" +
//                "    {\n" +
//                "      \"companyCode\": \"12005\",\n" +
//                "      \"from\": \"3000000000\",\n" +
//                "      \"to\": null,\n" +
//                "      \"optionExclude\": false\n" +
//                "    },\n" +
//                "     {\n" +
//                "      \"companyCode\": null,\n" +
//                "      \"from\": null,\n" +
//                "      \"to\": null,\n" +
//                "      \"optionExclude\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"companyCode\": null,\n" +
//                "      \"from\": null,\n" +
//                "      \"to\": null,\n" +
//                "      \"optionExclude\": false\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
//
//        SelectGroupDocumentList selectGroupDocumentList = JSONUtil.convertJsonToObject(jsonText, SelectGroupDocumentList.class);
//
//        System.out.println(selectGroupDocumentList);
//        StringBuilder sb = new StringBuilder(" AND ((UPPER(GH.ORIGINAL_FISCAL_YEAR) LIKE UPPER('2021') ");
//
//        List<SelectGroupDocumentConfig> selectGroupDocumentConfigList = selectGroupDocumentList.getGroupList();
//        List<Object> params = new ArrayList<>();
//        int index = 0;
////        for (int i = 0; i < selectGroupDocumentConfigList.size(); i++) {
////
////            if (!Util.isEmpty(selectGroupDocumentConfigList.get(i).getFrom())) {
////                params.add("03003");
////                sb.append(" AND ((UPPER(GH.COMPANY_CODE) LIKE UPPER(?) ");
////                sb.append(SqlUtil.whereClauseRangeOne(selectGroupDocumentConfigList.get(i).getFrom(), selectGroupDocumentConfigList.get(i).getTo(),
////                        "GH.ORIGINAL_DOCUMENT_NO", params, ++index));
////                sb.append(" )) ");
////            }
////
////        }
//        sb.append(" )) ");
//
//
//
//
////        System.out.println("  AND (UPPER(GH.ORIGINAL_FISCAL_YEAR) LIKE UPPER('2021')" +
////                "          AND ((UPPER(GH.COMPANY_CODE) LIKE UPPER('12005') AND" +
////                "                UPPER(GH.ORIGINAL_DOCUMENT_NO) BETWEEN UPPER('30000000000') AND UPPER('39000000000'))" +
////                "              OR (UPPER(GH.COMPANY_CODE) LIKE UPPER('03003') AND" +
////                "                  UPPER(GH.ORIGINAL_DOCUMENT_NO) BETWEEN UPPER('3100016700') AND UPPER('3100016995'))))");
//
//    }
//}

package com.example.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.internal.Pair;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;



@Slf4j
public class XmlJsonComparer {

    public static final String XML_AND_JSON_MAPPING_SUCCESS = "XML AND JSON MAPPING SUCCESS";
    public static final String MISSING_VALUES_MSG = "Missing value for the xmlkey: {}  jsonKey: {} ";

    public static final List<String> DATE_FIELDS = Collections.unmodifiableList(Arrays.asList("START" , "FINISH"));

    public static Pair<Boolean, String> compare(JsonNode xmlRoot, JsonNode jsonRoot,
                                                Map<String, String> mappingsHashMap) {

        if (CollectionUtils.isEmpty(mappingsHashMap)) {
            throw new IllegalArgumentException("mappingsHashMap is null or empty");
        }

        Pair<Boolean, String> pairs = null;
        String msg = null;

        for (Map.Entry<String, String> entry : mappingsHashMap.entrySet()) {
            String xmlKey = entry.getKey();
            String jsonKey = entry.getValue();

            pairs = compareNodesByPaths(xmlRoot, xmlKey, jsonRoot, jsonKey);

            if(!pairs.getLeft()){
                break;
            }


        }
        msg = "";//FstpConstants.XML_AND_JSON_MAPPING_SUCCESS;
        log.info(msg);

        return pairs;
    }


    public static boolean isDateField(String xmlKey) {
        return xmlKey.contains("DATE")
                || xmlKey.contains("DATAB")
                || xmlKey.contains("DATBI")
                || xmlKey.contains("ERSDA")
                || DATE_FIELDS.contains(xmlKey);
    }



    private static JsonNode getNodeByPath(JsonNode rootNode, String path) {
        String[] paths = path.split("\\.");
        JsonNode node = rootNode;
        for(String str : paths) {
            System.out.println(str);
            node = node.get(str);
            if(node == null) {
                return node;
            }
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {

            }
        }
        return node;
        /*Iterator<String> fieldNames = rootNode.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            JsonNode childNode = rootNode.get(field);
            if (field.equalsIgnoreCase(path)) {
                return childNode;
            } else if (childNode.isContainerNode()) {
                JsonNode result = getNodeByPath(childNode, path);
                if (result != null) return result;
            }
        }
        return null;*/
    }

    public static Pair<Boolean, String> compareNodesByPaths(JsonNode xmlRoot, String xmlPath, JsonNode jsonRoot, String jsonPath) {
        String[] xmlPaths = xmlPath.split("\\.");
        String[] jsonPaths = jsonPath.split("\\.");

        JsonNode xmlNode = xmlRoot;
        JsonNode jsonNode = jsonRoot;
        String xmlKey = null;
        String jsonKey = null;
        boolean isArray = false;

        a: for (String part : xmlPaths) {
            xmlKey = part;
            if (part.endsWith("[*]")) {
                isArray = true;
                xmlNode = xmlNode.get(part.replace("[*]", ""));
                b: for(String path : jsonPaths) {
                    jsonKey = path;
                    if (path.endsWith("[*]")) {
                       jsonNode = jsonNode.get(path.replace("[*]", ""));
                    }
                    Pair<JsonNode, JsonNode> pairs = compareJsonArrays(xmlNode, part, xmlPath, jsonNode, path, jsonPath);
                    xmlNode = pairs.getLeft();
                    jsonNode = pairs.getRight();
                    xmlKey = xmlPath.split("\\[\\*\\]\\.")[1];
                    jsonKey = jsonPath.split("\\[\\*\\]\\.")[1];

                    break a;
                }

                if (xmlNode == null) break;
            } else {
                xmlNode = xmlNode.get(part);
                if (xmlNode == null) break;
            }
        }

        if(!isArray) {
            for (String part : jsonPaths) {
                jsonKey = part;
                jsonNode = jsonNode.get(part);
                if (jsonNode == null) break;
            }
        }
        if(xmlNode == null || jsonNode == null) {
            return Pair.of(true, "Success");
        }
        boolean isDate = isDateField(xmlKey);
        boolean isNumber = NumberUtils.isParsable(xmlNode.asText());
        boolean mismatch = validateTheEquality(isDate, isNumber, xmlNode.asText(), jsonNode.asText());
        if (!mismatch) {
            String msg = MessageFormat.format("XmlKey:{0} , XmlValue: {1}, json key:{2}, jsonvalue:{3}", xmlKey, xmlNode, jsonKey, jsonNode);
            log.info(msg);
            return Pair.of(mismatch, msg);
        }
        return Pair.of(true, "Success");
        // Compare the final nodes
        //return compareNodes(xmlNode, jsonNode);

    }

    private static Pair<JsonNode, JsonNode> compareJsonArrays(JsonNode xmlNodes, String part, String xmlPath, JsonNode jsonNodes, String path, String jsonPath) {
        boolean value = false;
        JsonNode finalXmlNode = null;
        JsonNode finalJsonNode = null;
        a:for(JsonNode xmlNode : xmlNodes) {
            b: for(JsonNode jsonNode : jsonNodes) {
                finalXmlNode = xmlNode.get(xmlPath.split("\\[\\*\\]\\.")[1]);
                finalJsonNode = jsonNode.get(jsonPath.split("\\[\\*\\]\\.")[1]);
                value = finalXmlNode.equals(finalJsonNode);
                if(value)
                    break a;
            }
        }
        return Pair.of(finalXmlNode, finalJsonNode);
    }


    // Helper method to compare two JsonNode objects
    private static boolean compareNodes(JsonNode node1, JsonNode node2) {
        if (node1 == null || node2 == null) {
            return false;
        }
        // Compare values for primitive types
        if (node1.isValueNode() && node2.isValueNode()) {
            return node1.asText().equals(node2.asText());
        }
        // Handle more complex cases if needed
        return node1.equals(node2);
    }

    public static boolean validateTheEquality(boolean isDate, boolean isNumber, String xmlValue, String jsonValue) {
        boolean result;
        if (!isDate && isNumber) {
            String jsonValueDup = NumberUtils.isParsable(jsonValue) ? jsonValue : "0";
            BigDecimal xmlBigDecimal = new BigDecimal(xmlValue);
            BigDecimal jsonBigDecimal = new BigDecimal(jsonValueDup);
            result = xmlBigDecimal.compareTo(jsonBigDecimal) == 0;
        } else {
            if (isDate) {
                xmlValue = Optional.ofNullable(xmlValue).orElse("").replace("-", "");
                jsonValue = Optional.ofNullable(jsonValue).orElse("").replace("-", "");
            }
            result = xmlValue.equals(jsonValue);
        }
        return result;
    }

    public static Pair<Boolean, String> compareXmlAndJson(String xmlBody, String jsonBody, Map<String, String> mappings)
            throws JsonProcessingException {
        log.info("Starting to compare the xml and the json body:");

        String msg = null;
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode xmlNode = xmlMapper.readTree(xmlBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonBody);
        Pair<Boolean, String> resultPair = XmlJsonComparer.compare(xmlNode, jsonNode, mappings);

        boolean compareResult = resultPair.getLeft();
        String compareResultMsg = compareResult ? "SUCCESS" : "FAILURE";
        msg = MessageFormat.format("{0}, {1} ", compareResultMsg, resultPair.getRight());
        log.info(msg);
        return Pair.of(compareResult, msg);
    }
    public static void main(String[] args) throws JsonProcessingException {
        FstpContants.setUpFixedAssetsMapping();
        FstpContants.setUpDepreciationAreasMapping();
        String xmlString = "<FIXEDASSET_CREATE01>\n" +
                "    <IDOC BEGIN=\"1\">\n" +
                "        <EDI_DC40 SEGMENT=\"1\">\n" +
                "            <TABNAM>EDI_DC40</TABNAM>\n" +
                "            <MANDT>110</MANDT>\n" +
                "            <DOCNUM>0000000000008557</DOCNUM>\n" +
                "            <DOCREL>757</DOCREL>\n" +
                "            <STATUS>30</STATUS>\n" +
                "            <DIRECT>1</DIRECT>\n" +
                "            <OUTMOD>2</OUTMOD>\n" +
                "            <EXPRSS>E</EXPRSS>\n" +
                "            <TEST>T</TEST>\n" +
                "            <IDOCTYP>FIXEDASSET_CREATE01</IDOCTYP>\n" +
                "            <CIMTYP></CIMTYP>\n" +
                "            <MESTYP>FIXEDASSET_CREATE</MESTYP>\n" +
                "            <MESCOD>100</MESCOD>\n" +
                "            <MESFCT>MES</MESFCT>\n" +
                "            <STD>F</STD>\n" +
                "            <STDVRS>ST_VRS</STDVRS>\n" +
                "            <STDMES>FIXEDA</STDMES>\n" +
                "            <SNDPOR>SAPRD1</SNDPOR>\n" +
                "            <SNDPRT>LS</SNDPRT>\n" +
                "            <SNDPFC>SP</SNDPFC>\n" +
                "            <SNDPRN>RD1CLNT110</SNDPRN>\n" +
                "            <SNDSAD>TEST MESSAGE</SNDSAD>\n" +
                "            <SNDLAD>TEST MESSAGE LAD</SNDLAD>\n" +
                "            <RCVPOR>XML_FILE</RCVPOR>\n" +
                "            <RCVPRT>LS</RCVPRT>\n" +
                "            <RCVPFC>RP</RCVPFC>\n" +
                "            <RCVPRN>RD1CLNT110</RCVPRN>\n" +
                "            <RCVSAD>TEST MESSAGE RCV SAD</RCVSAD>\n" +
                "            <RCVLAD>TEST MESSAGE RCV LAD</RCVLAD>\n" +
                "            <CREDAT>20231007</CREDAT>\n" +
                "            <CRETIM>030630</CRETIM>\n" +
                "            <REFINT>100001</REFINT>\n" +
                "            <REFGRP>100002</REFGRP>\n" +
                "            <REFMES>TEST MSG REF</REFMES>\n" +
                "            <ARCKEY>ARC KEY TEST MESSAGE</ARCKEY>\n" +
                "            <SERIAL>20231006223702</SERIAL>\n" +
                "        </EDI_DC40>\n" +
                "        <E1FIXEDASSET_CREATE SEGMENT=\"1\">\n" +
                "            <COMPANYCODE>1000</COMPANYCODE>\n" +
                "            <ASSET>000001100180</ASSET>\n" +
                "            <SUBNUMBER>0074</SUBNUMBER>\n" +
                "            <CREATESUBNUMBER>1</CREATESUBNUMBER>\n" +
                "            <E1BP1022_REFERENCE SEGMENT=\"1\">\n" +
                "                <COMPANYCODE>1000</COMPANYCODE>\n" +
                "                <ASSET>000001100180</ASSET>\n" +
                "                <SUBNUMBER>0074</SUBNUMBER>\n" +
                "            </E1BP1022_REFERENCE>\n" +
                "            <E1BP1022_FEGLG001 SEGMENT=\"1\">\n" +
                "                <ASSETCLASS>00003001</ASSETCLASS>\n" +
                "                <DESCRIPT>E1BP1022_FEGLG001 DESCRPTION 1</DESCRIPT>\n" +
                "                <DESCRIPT2>E1BP1022_FEGLG001 DESCRPTION 2</DESCRIPT2>\n" +
                "                <ACCT_DETRM>10001100</ACCT_DETRM>\n" +
                "                <SERIAL_NO>20231006223702</SERIAL_NO>\n" +
                "                <INVENT_NO>1000011100011</INVENT_NO>\n" +
                "                <QUANTITY>25</QUANTITY>\n" +
                "                <BASE_UOM>152</BASE_UOM>\n" +
                "                <BASE_UOM_ISO>125</BASE_UOM_ISO>\n" +
                "                <HISTORY>1</HISTORY>\n" +
                "                <MAIN_DESCRIPT>MAIN DESCRIPTION</MAIN_DESCRIPT>\n" +
                "            </E1BP1022_FEGLG001>\n" +
                "            <E1BP1022_FEGLG001X SEGMENT=\"1\">\n" +
                "                <ASSETCLASS>Y</ASSETCLASS>\n" +
                "                <DESCRIPT>Y</DESCRIPT>\n" +
                "                <DESCRIPT2>Y</DESCRIPT2>\n" +
                "                <ACCT_DETRM>Y</ACCT_DETRM>\n" +
                "                <SERIAL_NO>Y</SERIAL_NO>\n" +
                "                <INVENT_NO>Y</INVENT_NO>\n" +
                "                <QUANTITY>Y</QUANTITY>\n" +
                "                <BASE_UOM>Y</BASE_UOM>\n" +
                "                <BASE_UOM_ISO>Y</BASE_UOM_ISO>\n" +
                "                <HISTORY>Y</HISTORY>\n" +
                "            </E1BP1022_FEGLG001X>\n" +
                "            <E1BP1022_FEGLG011 SEGMENT=\"1\">\n" +
                "                <DATE>20231008</DATE>\n" +
                "                <NOTE>FEGLG011 note</NOTE>\n" +
                "                <INCLUDE_IN_LIST>1</INCLUDE_IN_LIST>\n" +
                "            </E1BP1022_FEGLG011>\n" +
                "            <E1BP1022_FEGLG011X SEGMENT=\"1\">\n" +
                "                <DATE>Y</DATE>\n" +
                "                <NOTE>Y</NOTE>\n" +
                "                <INCLUDE_IN_LIST>Y</INCLUDE_IN_LIST>\n" +
                "            </E1BP1022_FEGLG011X>\n" +
                "            <E1BP1022_FEGLG002 SEGMENT=\"1\">\n" +
                "                <CAP_DATE>20240907</CAP_DATE>\n" +
                "                <DEACT_DATE>20240907</DEACT_DATE>\n" +
                "                <INITIAL_ACQ>00000000</INITIAL_ACQ>\n" +
                "                <INITIAL_ACQ_YR>0000</INITIAL_ACQ_YR>\n" +
                "                <INITIAL_ACQ_PRD>000</INITIAL_ACQ_PRD>\n" +
                "                <PLRET_DATE>00000000</PLRET_DATE>\n" +
                "                <PO_DATE>20240907</PO_DATE>\n" +
                "                <CAP_KEY>123456</CAP_KEY>\n" +
                "            </E1BP1022_FEGLG002>\n" +
                "            <E1BP1022_FEGLG002X SEGMENT=\"1\">\n" +
                "                <CAP_DATE>Y</CAP_DATE>\n" +
                "                <DEACT_DATE>Y</DEACT_DATE>\n" +
                "                <INITIAL_ACQ>Y</INITIAL_ACQ>\n" +
                "                <INITIAL_ACQ_YR>Y</INITIAL_ACQ_YR>\n" +
                "                <INITIAL_ACQ_PRD>Y</INITIAL_ACQ_PRD>\n" +
                "                <PLRET_DATE>Y</PLRET_DATE>\n" +
                "                <PURCH_ORD_DATE>Y</PURCH_ORD_DATE>\n" +
                "                <CAP_KEY>Y</CAP_KEY>\n" +
                "            </E1BP1022_FEGLG002X>\n" +
                "            <E1BP1022_FEGLG003 SEGMENT=\"1\">\n" +
                "                <FROM_DATE>20220907</FROM_DATE>\n" +
                "                <TO_DATE>20250907</TO_DATE>\n" +
                "                <BUS_AREA>1234</BUS_AREA>\n" +
                "                <COSTCENTER>cost_cen_1</COSTCENTER>\n" +
                "                <RESP_CCTR>cost_cen_1</RESP_CCTR>\n" +
                "                <ACTTYPE>act 1</ACTTYPE>\n" +
                "                <INTERN_ORD>1234567</INTERN_ORD>\n" +
                "                <MAINT_ORD>1234567</MAINT_ORD>\n" +
                "                <PLANT>123</PLANT>\n" +
                "                <LOCATION>CA</LOCATION>\n" +
                "                <ROOM>23</ROOM>\n" +
                "                <PLATE_NO>CKWM_258</PLATE_NO>\n" +
                "                <PERSON_NO>64794700</PERSON_NO>\n" +
                "                <SHIFT_FACT>0000</SHIFT_FACT>\n" +
                "                <SHUTDOWN>0</SHUTDOWN>\n" +
                "                <FUND>123000</FUND>\n" +
                "                <FUNC_AREA>fun1</FUNC_AREA>\n" +
                "                <GRANT_NBR>1287567</GRANT_NBR>\n" +
                "                <FUNC_AREA_LONG>1877456</FUNC_AREA_LONG>\n" +
                "                <FUNDS_CTR>1987364</FUNDS_CTR>\n" +
                "                <FUND_APC>145785</FUND_APC>\n" +
                "                <FUNC_AREA_APC>122454887</FUNC_AREA_APC>\n" +
                "                <GRANT_NBR_APC>112344788</GRANT_NBR_APC>\n" +
                "                <FUNDS_CTR_APC>124548</FUNDS_CTR_APC>\n" +
                "                <WBS_ELEMENT_COST>30000</WBS_ELEMENT_COST>\n" +
                "                <TAXJURCODE>124488</TAXJURCODE>\n" +
                "                <RL_EST_KEY>144577</RL_EST_KEY>\n" +
                "                <RL_EST_KEY_EXT>1447765</RL_EST_KEY_EXT>\n" +
                "                <BUDGET_PERIOD>20220909</BUDGET_PERIOD>\n" +
                "                <BUDGET_PERIOD_APC>20220909</BUDGET_PERIOD_APC>\n" +
                "                <SEGMENT>1234578</SEGMENT>\n" +
                "                <PROFIT_CTR>1447898</PROFIT_CTR>\n" +
                "            </E1BP1022_FEGLG003>\n" +
                "            <E1BP1022_FEGLG004 SEGMENT=\"1\">\n" +
                "                <EVALGROUP1>E001</EVALGROUP1>\n" +
                "                <EVALGROUP2>E002</EVALGROUP2>\n" +
                "                <EVALGROUP3>E003</EVALGROUP3>\n" +
                "                <EVALGROUP4>E004</EVALGROUP4>\n" +
                "                <EVALGROUP5>E005</EVALGROUP5>\n" +
                "                <INV_REASON>I0</INV_REASON>\n" +
                "                <ENVIR_INVEST>I001</ENVIR_INVEST>\n" +
                "                <ASSETSUPNO>124556782</ASSETSUPNO>\n" +
                "            </E1BP1022_FEGLG004>\n" +
                "            <E1BP1022_FEGLG004X SEGMENT=\"1\">\n" +
                "                <EVALGROUP1>Y</EVALGROUP1>\n" +
                "                <EVALGROUP2>Y</EVALGROUP2>\n" +
                "                <EVALGROUP3>Y</EVALGROUP3>\n" +
                "                <EVALGROUP4>Y</EVALGROUP4>\n" +
                "                <EVALGROUP5>Y</EVALGROUP5>\n" +
                "                <INV_REASON>Y</INV_REASON>\n" +
                "                <ENVIR_INVEST>Y</ENVIR_INVEST>\n" +
                "                <ASSETSUPNO>Y</ASSETSUPNO>\n" +
                "            </E1BP1022_FEGLG004X>\n" +
                "            <E1BP1022_FEGLG009 SEGMENT=\"1\">\n" +
                "                <VENDOR_NO>1234567890</VENDOR_NO>\n" +
                "                <VENDOR>ABC Supplies Ltd</VENDOR>\n" +
                "                <MANUFACTURER>XYZ Manufacturing Co.</MANUFACTURER>\n" +
                "                <PURCH_NEW>Y</PURCH_NEW>\n" +
                "                <TRADE_ID>654321</TRADE_ID>\n" +
                "                <COUNTRY>USA</COUNTRY>\n" +
                "                <COUNTRY_ISO>US</COUNTRY_ISO>\n" +
                "                <TYPE_NAME>Machine Type A</TYPE_NAME>\n" +
                "                <ORIG_ASSET>ORIG123456</ORIG_ASSET>\n" +
                "                <ORIG_ASSET_SUBNO>0012</ORIG_ASSET_SUBNO>\n" +
                "                <ORIG_ACQ_DATE>20231012</ORIG_ACQ_DATE>\n" +
                "                <ORIG_ACQ_YR>2023</ORIG_ACQ_YR>\n" +
                "                <ORIG_VALUE>100000</ORIG_VALUE>\n" +
                "                <CURRENCY>USD</CURRENCY>\n" +
                "                <CURRENCY_ISO>USD</CURRENCY_ISO>\n" +
                "                <INHOUSE_PROD_PERCENTAGE>50.000</INHOUSE_PROD_PERCENTAGE>\n" +
                "            </E1BP1022_FEGLG009>\n" +
                "            <E1BP1022_FEGLG007 SEGMENT=\"1\">\n" +
                "                <TAX_OFFICE>LocalTaxOffice</TAX_OFFICE>\n" +
                "                <TAX_NO>1234567890123456</TAX_NO>\n" +
                "                <ASSESSMENT_NOTICE_DATE>20231010</ASSESSMENT_NOTICE_DATE>\n" +
                "                <MUNICIPALITY>New York</MUNICIPALITY>\n" +
                "                <LNDREG_DATE>20220101</LNDREG_DATE>\n" +
                "                <LNDREG_ENTRY_DATE>20220201</LNDREG_ENTRY_DATE>\n" +
                "                <LNDREG_VOL>12345</LNDREG_VOL>\n" +
                "                <LNDREG_PG>23456</LNDREG_PG>\n" +
                "                <LNDREG_NO>7890</LNDREG_NO>\n" +
                "                <LNDREG_MAP_NO>4567</LNDREG_MAP_NO>\n" +
                "                <LNDREG_PLOT_NO>1234567890</LNDREG_PLOT_NO>\n" +
                "                <CONVEYANCE_DATE>20230101</CONVEYANCE_DATE>\n" +
                "                <AREA>5000.00</AREA>\n" +
                "                <AREA_UOM>M2</AREA_UOM>\n" +
                "                <AREA_UOM_ISO>MTR</AREA_UOM_ISO>\n" +
                "            </E1BP1022_FEGLG007>\n" +
                "            <E1BP1022_FEGLG007X SEGMENT=\"1\">\n" +
                "                <TAX_OFFICE>Y</TAX_OFFICE>\n" +
                "                <TAX_NO>Y</TAX_NO>\n" +
                "                <ASSESSMENT_NOTICE_DATE>Y</ASSESSMENT_NOTICE_DATE>\n" +
                "                <MUNICIPALITY>Y</MUNICIPALITY>\n" +
                "                <LNDREG_DATE>Y</LNDREG_DATE>\n" +
                "                <LNDREG_ENTRY_DATE>Y</LNDREG_ENTRY_DATE>\n" +
                "                <LNDREG_VOL>Y</LNDREG_VOL>\n" +
                "                <LNDREG_PG>Y</LNDREG_PG>\n" +
                "                <LNDREG_NO>Y</LNDREG_NO>\n" +
                "                <LNDREG_MAP_NO>Y</LNDREG_MAP_NO>\n" +
                "                <LNDREG_PLOT_NO>Y</LNDREG_PLOT_NO>\n" +
                "                <CONVEYANCE_DATE>Y</CONVEYANCE_DATE>\n" +
                "                <AREA>Y</AREA>\n" +
                "                <AREA_UOM>Y</AREA_UOM>\n" +
                "                <AREA_UOM_ISO>Y</AREA_UOM_ISO>\n" +
                "            </E1BP1022_FEGLG007X>\n" +
                "            <E1BP1022_FEGLG008 SEGMENT=\"1\">\n" +
                "                <TYPE>01</TYPE>\n" +
                "                <COMPANY>02</COMPANY>\n" +
                "                <POLICY_NO>INS1234567890</POLICY_NO>\n" +
                "                <TEXT>Comprehensive Insurance Policy</TEXT>\n" +
                "                <START_DATE>20230101</START_DATE>\n" +
                "                <PREMIUM>1000</PREMIUM>\n" +
                "                <INDEX>100</INDEX>\n" +
                "                <BASE_VALUE>1000000.00</BASE_VALUE>\n" +
                "                <CURRENCY>USD</CURRENCY>\n" +
                "                <CURRENCY_ISO>USD</CURRENCY_ISO>\n" +
                "                <BASE_VAL_MAN_UPD>Y</BASE_VAL_MAN_UPD>\n" +
                "                <MAN_INS_VAL>900000.00</MAN_INS_VAL>\n" +
                "                <MAN_INS_VAL_MAINT_YR>2023</MAN_INS_VAL_MAINT_YR>\n" +
                "                <CUR_INS_VALUE>950000.00</CUR_INS_VALUE>\n" +
                "                <CUR_INS_VALUE_MAINT_YR>2024</CUR_INS_VALUE_MAINT_YR>\n" +
                "            </E1BP1022_FEGLG008>\n" +
                "            <E1BP1022_FEGLG008X SEGMENT=\"1\">\n" +
                "                <TYPE>Y</TYPE>\n" +
                "                <COMPANY>Y</COMPANY>\n" +
                "                <POLICY_NO>Y</POLICY_NO>\n" +
                "                <TEXT>Y</TEXT>\n" +
                "                <START_DATE>Y</START_DATE>\n" +
                "                <PREMIUM>Y</PREMIUM>\n" +
                "                <INDEX>Y</INDEX>\n" +
                "                <BASE_VALUE>Y</BASE_VALUE>\n" +
                "                <CURRENCY>Y</CURRENCY>\n" +
                "                <CURRENCY_ISO>Y</CURRENCY_ISO>\n" +
                "                <BASE_VAL_MAN_UPD>Y</BASE_VAL_MAN_UPD>\n" +
                "                <MAN_INS_VAL>Y</MAN_INS_VAL>\n" +
                "                <MAN_INS_VAL_MAINT_YR>Y</MAN_INS_VAL_MAINT_YR>\n" +
                "            </E1BP1022_FEGLG008X>\n" +
                "            <E1BP1022_FEGLG005 SEGMENT=\"1\">\n" +
                "                <COMPANY>LeaseCo123</COMPANY>\n" +
                "                <AGRMNT_NO>AGRMNT456789012</AGRMNT_NO>\n" +
                "                <AGRMNTDATE>20231010</AGRMNTDATE>\n" +
                "                <NOTICEDATE>20241010</NOTICEDATE>\n" +
                "                <START_DATE>20230101</START_DATE>\n" +
                "                <LNGTH_YRS>005</LNGTH_YRS>\n" +
                "                <LNGTH_PRDS>060</LNGTH_PRDS>\n" +
                "                <TYPE>LT</TYPE>\n" +
                "                <BASE_VALUE>150000.00</BASE_VALUE>\n" +
                "                <CURRENCY>USD</CURRENCY>\n" +
                "                <CURRENCY_ISO>USD</CURRENCY_ISO>\n" +
                "                <PURCHPRICE>130000.00</PURCHPRICE>\n" +
                "                <TEXT>Leasing agreement for office space</TEXT>\n" +
                "                <NO_PAYMNTS>003000</NO_PAYMNTS>\n" +
                "                <CYCLE>012</CYCLE>\n" +
                "                <IN_ADVANCE>Y</IN_ADVANCE>\n" +
                "                <PAYMENT>5000.00</PAYMENT>\n" +
                "                <INTEREST>05.50000</INTEREST>\n" +
                "                <VALUE>120000.00</VALUE>\n" +
                "            </E1BP1022_FEGLG005>\n" +
                "            <E1BP1022_FEGLG005X SEGMENT=\"1\">\n" +
                "                <COMPANY>Y</COMPANY>\n" +
                "                <AGRMNT_NO>Y</AGRMNT_NO>\n" +
                "                <AGRMNTDATE>Y</AGRMNTDATE>\n" +
                "                <NOTICEDATE>Y</NOTICEDATE>\n" +
                "                <START_DATE>Y</START_DATE>\n" +
                "                <LNGTH_YRS>Y</LNGTH_YRS>\n" +
                "                <LNGTH_PRDS>Y</LNGTH_PRDS>\n" +
                "                <TYPE>Y</TYPE>\n" +
                "                <BASE_VALUE>Y</BASE_VALUE>\n" +
                "                <CURRENCY>Y</CURRENCY>\n" +
                "                <CURRENCY_ISO>Y</CURRENCY_ISO>\n" +
                "                <PURCHPRICE>Y</PURCHPRICE>\n" +
                "                <TEXT>Y</TEXT>\n" +
                "                <NO_PAYMNTS>Y</NO_PAYMNTS>\n" +
                "                <CYCLE>Y</CYCLE>\n" +
                "                <IN_ADVANCE>Y</IN_ADVANCE>\n" +
                "                <PAYMENT>Y</PAYMENT>\n" +
                "                <INTEREST>Y</INTEREST>\n" +
                "                <VALUE>Y</VALUE>\n" +
                "            </E1BP1022_FEGLG005X>\n" +
                "            <E1BP1022_DEP_AREAS SEGMENT=\"1\">\n" +
                "                <AREA>01</AREA>\n" +
                "                <DESCRIPT>IntegTesting</DESCRIPT>\n" +
                "                <DEACTIVATE>N</DEACTIVATE>\n" +
                "                <DEP_KEY>2916</DEP_KEY>\n" +
                "                <ULIFE_YRS>004</ULIFE_YRS>\n" +
                "                <ULIFE_PRDS>004</ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_YRS>002</EXP_ULIFE_YRS>\n" +
                "                <EXP_ULIFE_PRDS>001</EXP_ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_SDEP_YRS>003</EXP_ULIFE_SDEP_YRS>\n" +
                "                <EXP_ULIFE_SDEP_PRDS>002</EXP_ULIFE_SDEP_PRDS>\n" +
                "                <ORIG_ULIFE_YRS>006</ORIG_ULIFE_YRS>\n" +
                "                <ORIG_ULIFE_PRDS>002</ORIG_ULIFE_PRDS>\n" +
                "                <CHANGE_YR>2024</CHANGE_YR>\n" +
                "                <DEP_UNITS>12000</DEP_UNITS>\n" +
                "                <ODEP_START_DATE>20240808</ODEP_START_DATE>\n" +
                "                <SDEP_START_DATE>20240808</SDEP_START_DATE>\n" +
                "                <INTEREST_START_DATE>20240808</INTEREST_START_DATE>\n" +
                "                <READINESS>20240808</READINESS>\n" +
                "                <INDEX>00550</INDEX>\n" +
                "                <AGE_INDEX>00125</AGE_INDEX>\n" +
                "                <VAR_DEP_PORTION>5.0000</VAR_DEP_PORTION>\n" +
                "                <SCRAPVALUE>5.00</SCRAPVALUE>\n" +
                "                <CURRENCY>USD</CURRENCY>\n" +
                "                <CURRENCY_ISO>USD</CURRENCY_ISO>\n" +
                "                <NEG_VALUES>N</NEG_VALUES>\n" +
                "                <GRP_ASSET>GRPASSET001</GRP_ASSET>\n" +
                "                <GRP_ASSET_SUBNO>0012</GRP_ASSET_SUBNO>\n" +
                "                <ACQ_YR>2024</ACQ_YR>\n" +
                "                <ACQ_PRD>002</ACQ_PRD>\n" +
                "                <SCRAPVALUE_PRCTG>10.50000000000</SCRAPVALUE_PRCTG>\n" +
                "                <FROM_DATE>20240808</FROM_DATE>\n" +
                "                <TO_DATE>20240907</TO_DATE>\n" +
                "            </E1BP1022_DEP_AREAS>\n" +
                "            <E1BP1022_DEP_AREAS SEGMENT=\"1\">\n" +
                "                <AREA>02</AREA>\n" +
                "                <DESCRIPT>IntegTesting</DESCRIPT>\n" +
                "                <DEACTIVATE>N</DEACTIVATE>\n" +
                "                <DEP_KEY>2912</DEP_KEY>\n" +
                "                <ULIFE_YRS>004</ULIFE_YRS>\n" +
                "                <ULIFE_PRDS>004</ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_YRS>002</EXP_ULIFE_YRS>\n" +
                "                <EXP_ULIFE_PRDS>001</EXP_ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_SDEP_YRS>003</EXP_ULIFE_SDEP_YRS>\n" +
                "                <EXP_ULIFE_SDEP_PRDS>002</EXP_ULIFE_SDEP_PRDS>\n" +
                "                <ORIG_ULIFE_YRS>006</ORIG_ULIFE_YRS>\n" +
                "                <ORIG_ULIFE_PRDS>002</ORIG_ULIFE_PRDS>\n" +
                "                <CHANGE_YR>2024</CHANGE_YR>\n" +
                "                <DEP_UNITS>10000</DEP_UNITS>\n" +
                "                <ODEP_START_DATE>20240808</ODEP_START_DATE>\n" +
                "                <SDEP_START_DATE>20240808</SDEP_START_DATE>\n" +
                "                <INTEREST_START_DATE>20240808</INTEREST_START_DATE>\n" +
                "                <READINESS>20240808</READINESS>\n" +
                "                <INDEX>00560</INDEX>\n" +
                "                <AGE_INDEX>00120</AGE_INDEX>\n" +
                "                <VAR_DEP_PORTION>5.0000</VAR_DEP_PORTION>\n" +
                "                <SCRAPVALUE>5.00</SCRAPVALUE>\n" +
                "                <CURRENCY>EUR</CURRENCY>\n" +
                "                <CURRENCY_ISO>EUR</CURRENCY_ISO>\n" +
                "                <NEG_VALUES>N</NEG_VALUES>\n" +
                "                <GRP_ASSET>GRPASSET002</GRP_ASSET>\n" +
                "                <GRP_ASSET_SUBNO>0013</GRP_ASSET_SUBNO>\n" +
                "                <ACQ_YR>2024</ACQ_YR>\n" +
                "                <ACQ_PRD>002</ACQ_PRD>\n" +
                "                <SCRAPVALUE_PRCTG>10.50000000000</SCRAPVALUE_PRCTG>\n" +
                "                <FROM_DATE>20240808</FROM_DATE>\n" +
                "                <TO_DATE>20240907</TO_DATE>\n" +
                "            </E1BP1022_DEP_AREAS>\n" +
                "            <E1BP1022_DEP_AREASX SEGMENT=\"1\">\n" +
                "                <AREA>01</AREA>\n" +
                "                <DESCRIPT>Y</DESCRIPT>\n" +
                "                <DEACTIVATE>Y</DEACTIVATE>\n" +
                "                <DEP_KEY>Y</DEP_KEY>\n" +
                "                <ULIFE_YRS>Y</ULIFE_YRS>\n" +
                "                <ULIFE_PRDS>Y</ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_YRS>Y</EXP_ULIFE_YRS>\n" +
                "                <EXP_ULIFE_PRDS>Y</EXP_ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_SDEP_YRS>Y</EXP_ULIFE_SDEP_YRS>\n" +
                "                <EXP_ULIFE_SDEP_PRDS>Y</EXP_ULIFE_SDEP_PRDS>\n" +
                "                <ORIG_ULIFE_YRS>Y</ORIG_ULIFE_YRS>\n" +
                "                <ORIG_ULIFE_PRDS>Y</ORIG_ULIFE_PRDS>\n" +
                "                <CHANGE_YR>Y</CHANGE_YR>\n" +
                "                <DEP_UNITS>Y</DEP_UNITS>\n" +
                "                <ODEP_START_DATE>Y</ODEP_START_DATE>\n" +
                "                <SDEP_START_DATE>Y</SDEP_START_DATE>\n" +
                "                <INTEREST_START_DATE>Y</INTEREST_START_DATE>\n" +
                "                <READINESS>Y</READINESS>\n" +
                "                <INDEX>Y</INDEX>\n" +
                "                <AGE_INDEX>Y</AGE_INDEX>\n" +
                "                <VAR_DEP_PORTION>Y</VAR_DEP_PORTION>\n" +
                "                <SCRAPVALUE>Y</SCRAPVALUE>\n" +
                "                <CURRENCY>Y</CURRENCY>\n" +
                "                <CURRENCY_ISO>Y</CURRENCY_ISO>\n" +
                "                <NEG_VALUES>Y</NEG_VALUES>\n" +
                "                <GRP_ASSET>Y</GRP_ASSET>\n" +
                "                <GRP_ASSET_SUBNO>Y</GRP_ASSET_SUBNO>\n" +
                "                <ACQ_YR>Y</ACQ_YR>\n" +
                "                <ACQ_PRD>Y</ACQ_PRD>\n" +
                "                <SCRAPVALUE_PRCTG>Y</SCRAPVALUE_PRCTG>\n" +
                "                <FROM_DATE>Y</FROM_DATE>\n" +
                "                <TO_DATE>Y</TO_DATE>\n" +
                "            </E1BP1022_DEP_AREASX>\n" +
                "            <E1BP1022_DEP_AREASX SEGMENT=\"1\">\n" +
                "                <AREA>02</AREA>\n" +
                "                <DESCRIPT>Y</DESCRIPT>\n" +
                "                <DEACTIVATE>Y</DEACTIVATE>\n" +
                "                <DEP_KEY>Y</DEP_KEY>\n" +
                "                <ULIFE_YRS>Y</ULIFE_YRS>\n" +
                "                <ULIFE_PRDS>Y</ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_YRS>Y</EXP_ULIFE_YRS>\n" +
                "                <EXP_ULIFE_PRDS>Y</EXP_ULIFE_PRDS>\n" +
                "                <EXP_ULIFE_SDEP_YRS>Y</EXP_ULIFE_SDEP_YRS>\n" +
                "                <EXP_ULIFE_SDEP_PRDS>Y</EXP_ULIFE_SDEP_PRDS>\n" +
                "                <ORIG_ULIFE_YRS>Y</ORIG_ULIFE_YRS>\n" +
                "                <ORIG_ULIFE_PRDS>Y</ORIG_ULIFE_PRDS>\n" +
                "                <CHANGE_YR>Y</CHANGE_YR>\n" +
                "                <DEP_UNITS>Y</DEP_UNITS>\n" +
                "                <ODEP_START_DATE>Y</ODEP_START_DATE>\n" +
                "                <SDEP_START_DATE>Y</SDEP_START_DATE>\n" +
                "                <INTEREST_START_DATE>Y</INTEREST_START_DATE>\n" +
                "                <READINESS>Y</READINESS>\n" +
                "                <INDEX>Y</INDEX>\n" +
                "                <AGE_INDEX>Y</AGE_INDEX>\n" +
                "                <VAR_DEP_PORTION>Y</VAR_DEP_PORTION>\n" +
                "                <SCRAPVALUE>Y</SCRAPVALUE>\n" +
                "                <CURRENCY>Y</CURRENCY>\n" +
                "                <CURRENCY_ISO>Y</CURRENCY_ISO>\n" +
                "                <NEG_VALUES>Y</NEG_VALUES>\n" +
                "                <GRP_ASSET>Y</GRP_ASSET>\n" +
                "                <GRP_ASSET_SUBNO>Y</GRP_ASSET_SUBNO>\n" +
                "                <ACQ_YR>Y</ACQ_YR>\n" +
                "                <ACQ_PRD>Y</ACQ_PRD>\n" +
                "                <SCRAPVALUE_PRCTG>Y</SCRAPVALUE_PRCTG>\n" +
                "                <FROM_DATE>Y</FROM_DATE>\n" +
                "                <TO_DATE>Y</TO_DATE>\n" +
                "            </E1BP1022_DEP_AREASX>\n" +
                "        </E1FIXEDASSET_CREATE>\n" +
                "        <E1IDOCENHANCEMENT SEGMENT=\"1\">\n" +
                "            <IDENTIFIER>IDOCEnhance12345</IDENTIFIER>\n" +
                "            <DATA>This is a test data container for IDoc enhancement. It should be less than 970 characters but sufficient to meet the maximum size restriction.</DATA>\n" +
                "        </E1IDOCENHANCEMENT>\n" +
                "    </IDOC>\n" +
                "</FIXEDASSET_CREATE01>";

        String jsonResponse = "{\n" +
                "            \"FunctionalAreaLong\": \"1877456\",\n" +
                "            \"FirstAcquisitionFiscalYear\": null,\n" +
                "            \"DeactivationDate\": \"2024-09-07\",\n" +
                "            \"ReasonForEnvironmentalInvestment\": \"I001\",\n" +
                "            \"FirstAcquisitionDate\": null,\n" +
                "            \"MainAndSubNumber\": \"000001100180-0074\",\n" +
                "            \"CostCenter\": \"cost_cen_1\",\n" +
                "            \"ReasonForInvestment\": \"I0\",\n" +
                "            \"AssetCapitalizationDate\": \"2024-09-07\",\n" +
                "            \"AssetClass\": \"00003001\",\n" +
                "            \"Type\": \"AS\",\n" +
                "            \"WbsElement\": \"30000\",\n" +
                "            \"CostCenterResponsible\": \"cost_cen_1\",\n" +
                "            \"EvaluationGroup2\": \"E002\",\n" +
                "            \"EvaluationGroup1\": \"E001\",\n" +
                "            \"SubNumber\": \"0074\",\n" +
                "            \"AssetLocation\": \"CA\",\n" +
                "            \"FundsCenter\": \"1987364\",\n" +
                "            \"EvaluationGroup4\": \"E004\",\n" +
                "            \"EvaluationGroup3\": \"E003\",\n" +
                "            \"Fund\": \"123000\",\n" +
                "            \"FunctionalArea\": \"fun1\",\n" +
                "            \"Grant\": \"1287567\",\n" +
                "            \"Asset\": \"000001100180\",\n" +
                "            \"DepreciationAreas\": [\n" +
                "                {\n" +
                "                    \"ExpiredUsefulLifeInPeriodsFromSDep\": \"002\",\n" +
                "                    \"AcquisitionMonth\": \"002\",\n" +
                "                    \"VariableDepreciationPortion\": 5,\n" +
                "                    \"AcquisitionYear\": \"2024\",\n" +
                "                    \"Type\": \"AS\",\n" +
                "                    \"ValidityEndDate\": \"2024-09-07\",\n" +
                "                    \"OperatingReadinessDate\": \"2024-08-08\",\n" +
                "                    \"DepreciationAreaIsDeactivated\": \"N\",\n" +
                "                    \"ScrapValuePercentage\": 10.5,\n" +
                "                    \"ExpiredUsefulLifeInYears\": \"002\",\n" +
                "                    \"AssetScrapValue\": 5,\n" +
                "                    \"DepreciationKeyForChangeoverYear\": \"2024\",\n" +
                "                    \"ExpiredUsefulLifeInYearsFromSDep\": \"003\",\n" +
                "                    \"IndexSeriesForReplacementValuesByAge\": \"00125\",\n" +
                "                    \"IsoCurrencyCode\": \"USD\",\n" +
                "                    \"SpecialDepreciationStartDate\": \"2024-08-08\",\n" +
                "                    \"ValidityStartDate\": \"2024-08-08\",\n" +
                "                    \"OriginalUsefulLifeInYears\": \"006\",\n" +
                "                    \"MainAndSubNumber\": \"000001100180-0074\",\n" +
                "                    \"ShortNameForDepreciationArea\": \"IntegTesting\",\n" +
                "                    \"DepreciationStartDate\": \"2024-08-08\",\n" +
                "                    \"IndexSeriesForReplacementValues\": \"00550\",\n" +
                "                    \"DepreciationKey\": \"2916\",\n" +
                "                    \"PlannedUsefulLifeInPeriods\": \"004\",\n" +
                "                    \"OriginalUsefulLifeInPeriods\": \"002\",\n" +
                "                    \"InterestCalculationStartDate\": \"2024-08-08\",\n" +
                "                    \"NumberOfUnitsDepreciated\": \"12000\",\n" +
                "                    \"NegativeValuesAllowed\": \"N\",\n" +
                "                    \"RealDepreciationArea\": \"01\",\n" +
                "                    \"ExpiredUsefulLifeInPeriods\": \"001\",\n" +
                "                    \"CurrencyKey\": \"USD\",\n" +
                "                    \"PlannedUsefulLifeInYears\": \"004\",\n" +
                "                    \"GroupAsset\": \"GRPASSET001\",\n" +
                "                    \"GroupAssetSubnumber\": \"0012\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"ExpiredUsefulLifeInPeriodsFromSDep\": \"002\",\n" +
                "                    \"AcquisitionMonth\": \"002\",\n" +
                "                    \"VariableDepreciationPortion\": 5,\n" +
                "                    \"AcquisitionYear\": \"2024\",\n" +
                "                    \"Type\": \"AS\",\n" +
                "                    \"ValidityEndDate\": \"2024-09-07\",\n" +
                "                    \"OperatingReadinessDate\": \"2024-08-08\",\n" +
                "                    \"DepreciationAreaIsDeactivated\": \"N\",\n" +
                "                    \"ScrapValuePercentage\": 10.5,\n" +
                "                    \"ExpiredUsefulLifeInYears\": \"002\",\n" +
                "                    \"AssetScrapValue\": 5,\n" +
                "                    \"DepreciationKeyForChangeoverYear\": \"2024\",\n" +
                "                    \"ExpiredUsefulLifeInYearsFromSDep\": \"003\",\n" +
                "                    \"IndexSeriesForReplacementValuesByAge\": \"00120\",\n" +
                "                    \"IsoCurrencyCode\": \"EUR\",\n" +
                "                    \"SpecialDepreciationStartDate\": \"2024-08-08\",\n" +
                "                    \"ValidityStartDate\": \"2024-08-08\",\n" +
                "                    \"OriginalUsefulLifeInYears\": \"006\",\n" +
                "                    \"MainAndSubNumber\": \"000001100180-0074\",\n" +
                "                    \"ShortNameForDepreciationArea\": \"IntegTesting\",\n" +
                "                    \"DepreciationStartDate\": \"2024-08-08\",\n" +
                "                    \"IndexSeriesForReplacementValues\": \"00560\",\n" +
                "                    \"DepreciationKey\": \"2912\",\n" +
                "                    \"PlannedUsefulLifeInPeriods\": \"004\",\n" +
                "                    \"OriginalUsefulLifeInPeriods\": \"002\",\n" +
                "                    \"InterestCalculationStartDate\": \"2024-08-08\",\n" +
                "                    \"NumberOfUnitsDepreciated\": \"10000\",\n" +
                "                    \"NegativeValuesAllowed\": \"N\",\n" +
                "                    \"RealDepreciationArea\": \"02\",\n" +
                "                    \"ExpiredUsefulLifeInPeriods\": \"001\",\n" +
                "                    \"CurrencyKey\": \"EUR\",\n" +
                "                    \"PlannedUsefulLifeInYears\": \"004\",\n" +
                "                    \"GroupAsset\": \"GRPASSET002\",\n" +
                "                    \"GroupAssetSubnumber\": \"0013\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "        ";

        FstpContants.FIXED_ASSETS_MAP.putAll(FstpContants.DEPRECIATION_AREAS_MAP);
        compareXmlAndJson(xmlString, jsonResponse, FstpContants.FIXED_ASSETS_MAP);
    }
}

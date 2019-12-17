package com.interest.ids.biz.kpicalc.kpi.util;

import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

public class KpiResouceUtil {

    private static Logger logger = LoggerFactory.getLogger(KpiResouceUtil.class);

    //缓存配置主节点
    private static final String KPICACHENODE = "cachenode";
    private static final String KPICACHENODE_TABLES = "tables";
    private static final String KPICACHENODE_FIELD = "field";
    private static final String KPICACHENODE_TABLES_VALUE = "value";
    private static final String KPICACHENODE_VALIDE_CHECK = "validcheck";
    private static final String KPICACHENODE_PKFIELD = "pkfield";

    // 字段属性
    private static final String SUBNODE_FIELD_NAME = "fieldname";
    private static final String SUBNODE_CACHE_TYPE = "cachetype";
    private static final String SUBNODE_COMMONT = "comment";
    private static final String SUBNODE_FIELD_KEY = "fieldKey";
    // 数据有效范围判定符
    private static final String KEYNODE_RANGE = "range";
    private static final String RANGE_GE = "ge";
    private static final String RANGE_LE = "le";
    private static final String RANGE_GT = "gt";
    private static final String RANGE_LT = "lt";
    //key-value
    private static final String NODE_FIELD_KV = "kv";
    private static final String KV_MEMBER = "member";
    private static final String KV_SCORE = "score";

    /**
     * 缓存字段开始索引，用于匹配Object[] 中对应的字段
     * 其顺序与数据库中查询出的字段顺序保持一致
     */
    public static final int FIELDSTARTINDEX = 4;

    /**
     * 静态变量，保存从xml中加载的数据,避免重复与存储介质进行IO操作
     */
    private static Map<String, Map<String, KpiCacheNode>> cacheMap = new HashMap<>();

    /**
     * 从XML中加载配置，封装成对应的数据格式并存于内存中
     */
    @SuppressWarnings("unchecked")
    public synchronized static void loadCacheConfiguration() {
        if(cacheMap.size() == 0){
            try{
                InputStream is = KpiResouceUtil.class.getResourceAsStream("/config/kpi_cachefield.xml");
                SAXBuilder sax = new SAXBuilder();
                Document doc = sax.build(is);

                Element root = doc.getRootElement();
                List<Element> fieldList = root.getChildren(KPICACHENODE);
                if(null != fieldList){
                    for (Element node : fieldList){
                        Boolean valid = MathUtil.formatBoolean(node.getAttributeValue(KPICACHENODE_VALIDE_CHECK), false);
                        String pkField = MathUtil.formatString(node.getAttributeValue(KPICACHENODE_PKFIELD), "station_code");

                        boolean noTables = true;
                        Element tablesNodes = node.getChild(KPICACHENODE_TABLES);
                        if(null != tablesNodes){
                            List<Element> tables = tablesNodes.getChildren(KPICACHENODE_TABLES_VALUE);
                            if(CommonUtil.isNotEmpty(tables)){
                                List<Element> fileds = node.getChildren(KPICACHENODE_FIELD);
                                for (Element t : tables){
                                    String tableStr = t.getTextTrim();
                                    cacheMap.put(t.getText(), buildCacheNode(valid, pkField, fileds,
                                            cacheMap.get(tableStr)));
                                }

                                noTables = false;
                            }
                        }

                        if(noTables) {
                            throw new RuntimeException(
                                    KPICACHENODE + " must have table tables child node.");
                        }
                    }
                }
            } catch(Exception e){
                logger.error("load cache configuration got error.", e);
            }
        }
    }

    /**
     * 封装KpiCacheNode
     *
     * @param fields
     * @return
     */
    private static Map<String, KpiCacheNode> buildCacheNode(Boolean valid, String pkId,
                                                            List<Element> fields, Map<String, KpiCacheNode> lastMap) {
        if(null == lastMap){
            lastMap = new LinkedHashMap<>();
        }
        int order = FIELDSTARTINDEX;

        for (Element field : fields){
            String fieldName = field.getAttributeValue(SUBNODE_FIELD_NAME);
            String cacheType = field.getAttributeValue(SUBNODE_CACHE_TYPE);
            String comment = field.getAttributeValue(SUBNODE_COMMONT);
            KpiCacheNode fieldObj = new KpiCacheNode(fieldName, valid, pkId, (order++), cacheType, comment);

            String filedKey = field.getAttributeValue(SUBNODE_FIELD_KEY);
            if(StringUtils.isNotEmpty(filedKey)){
                fieldObj.setFieldKey(filedKey);
            }

            // set data range
            Element range = field.getChild(KEYNODE_RANGE);
            if(null != range){
                String ge = range.getAttributeValue(RANGE_GE);
                String gt = range.getAttributeValue(RANGE_GT);
                String le = range.getAttributeValue(RANGE_LE);
                String lt = range.getAttributeValue(RANGE_LT);
                fieldObj.setRange(ge, le, gt, lt);
            }

            // kv
            Element kv = field.getChild(NODE_FIELD_KV);
            String member = kv.getAttributeValue(KV_MEMBER);
            String score = kv.getAttributeValue(KV_SCORE);
            fieldObj.setmeberScore(member, score);

            lastMap.put(fieldName, fieldObj);
        }
        return lastMap;
    }

    /**
     * 检查数据是否在有效的范围内
     * @param nodeFiled
     * @param score
     * @return
     */
    public static boolean checkDataRange(KpiCacheNode nodeFiled, Double score) {
        if(null == score){
            return false;
        }

        Double ge = nodeFiled.getGe();
        Double le = nodeFiled.getLe();
        Double gt = nodeFiled.getGt();
        Double lt = nodeFiled.getLt();

        if(null != ge && score < ge){
            return false;
        }

        if(null != le && score > le){
            return false;
        }
        if(null != gt && score <= gt){
            return false;
        }
        if(null != lt && score >= lt){
            return false;
        }

        return true;
    }

    /**
     * 匹配对应缓存列的值
     * @param nodeField
     * @param collectData
     * @return
     */
    public static Double getKpiCacheScore(KpiCacheNode nodeField, Object[] collectData){
        String scoreValue = nodeField.getScore();
        Integer fieldIndex = null;
        if ("time".equals(scoreValue)){
            fieldIndex = 0;
        }else if ("value".equals(scoreValue)){
            fieldIndex = nodeField.getKpiKeyOrder();
        }

        return MathUtil.formatDouble(collectData[fieldIndex]);
    }

    public static String getKpiCacheMember(KpiCacheNode nodeField, Object[] collectData){
        String memberValue = nodeField.getMember();

        String member = null;
        if (memberValue.startsWith("str:")){
            member = memberValue.substring(4);
        }else if (memberValue.contains(",")){
            String[] arr = memberValue.split(",");
            StringBuffer sb = new StringBuffer();
            for (String memSeg: arr){
                if (memSeg.equals("time")){
                    sb.append(MathUtil.formatString(collectData[0]));
                }else if (memSeg.equals("value")){
                    sb.append(collectData[nodeField.getKpiKeyOrder()]);
                }

                sb.append(",");
            }

            member = sb.toString();
            //去除最后一个字符','
            member = member.substring(0, member.lastIndexOf(","));
        }

        return member;
    }

    /**
     * 获取缓存字段信息，封装成KpiCacheNode
     * @param cacheTableName
     * @param kpiKey
     * @return
     */
    public static KpiCacheNode getKpiCacheNodeField(String cacheTableName, String kpiKey) {
        loadCacheConfiguration();
        Map<String, KpiCacheNode> map = cacheMap.get(cacheTableName);
        if(null != map){
            return map.get(kpiKey);
        }
        return null;
    }

    /**
     * 获得配置的缓存该表下所有的字段集合
     * @param cacheTableName
     * @return
     */
    public static Set<String> getKpiCacheNodes(String cacheTableName) {
        loadCacheConfiguration();
        Map<String, KpiCacheNode> map = cacheMap.get(cacheTableName);
        if(null != map){
            return map.keySet();
        }
        return null;
    }
}

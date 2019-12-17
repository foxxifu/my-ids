package com.interest.ids.common.project.utils;

import org.apache.commons.lang.StringUtils;

import java.util.*;

public class ActiveRecord {

    private List<String> selectList = new ArrayList<String>();
    private List<String[]> whereList = new ArrayList<String[]>();
    private List<String[]> joinList = new ArrayList<String[]>();
    private List<String> orderByList = new ArrayList<String>();
    private List<String> groupByList = new ArrayList<String>();
    private Map<String, Collection<?>> inListMap = new HashMap<String, Collection<?>>();
    private List<String[]> havingList = new ArrayList<String[]>();

    private Map<String, Object> setMap = new HashMap<String, Object>();
    private Map<String, Object> setHqlMap = new HashMap<String, Object>();

    private List<String> filedList = new ArrayList<String>();

    private StringBuilder sqlString = new StringBuilder("");
    private StringBuilder hqlString = new StringBuilder("");
    private StringBuilder limitString = new StringBuilder("");

    private final String nullVal = "*NULL";

    private boolean distinct = false;

    private final String[] calsStrings = { ">=", "<=", "<>", ">", "<", "=", " in" };

    public ActiveRecord select(String filed){
        return select(filed, false);
    }

    /**
     * 是否采用公式模式， 采用公式模式只能传入一个字段， ActiveRecord 不会截断逗号
     * @param filed
     * @param formulaModel
     * @return
     */
    public ActiveRecord select(String filed, boolean formulaModel){
        if(StringUtils.isNotEmpty(filed) && !selectList.contains(filed)){
            String[] filedArr = new String[]{filed};
            if(!formulaModel){
                filedArr = filed.split(",");
            }
            for(String f : filedArr){
                if(StringUtils.isBlank(f)){
                    continue;
                }
                this.selectList.add(f.trim());
                int n = f.toLowerCase().lastIndexOf(" as ");
                if (n > 0) {
                    this.filedList.add(f.substring(n + 4).trim());
                }else{
                    this.filedList.add(f.trim());
                }
            }
        }
        return this;
    }

    public ActiveRecord set(String key, Object val){
        this.setMap.put(key, val);
        return this;
    }

    public ActiveRecord set(Map<String, ? extends Object> vals){
        this.setMap.putAll(vals);
        return this;
    }

    public ActiveRecord select(String[] fileds){
        for(String f : fileds) select(f);
        return this;
    }

    public ActiveRecord select(List<String> fileds){
        for(String f : fileds) select(f);
        return this;
    }

    public ActiveRecord distinct(boolean distinct){
        this.distinct = distinct;
        return this;
    }

    public StringBuilder delete(String table){
        getDeleteString(table);
        this.sqlString.append(getWhereString()[0]);
        this.hqlString.append(getWhereString()[1]);
        return this.hqlString;
    }

    public StringBuilder insert(String table){
        getInsertString(table);
        return hqlString;
    }

    public StringBuilder update(String table){
        getUpdateString(table);
        this.sqlString.append(getWhereString()[0]);
        this.hqlString.append(getWhereString()[1]);
        return hqlString;
    }

    public StringBuilder replace(String table){
        getReplaceString(table);
        return hqlString;
    }

    public StringBuilder from(String table){
        getSelectString(table);
        this.sqlString.append(getJoinString());
        this.sqlString.append(getWhereString()[0]);
        this.sqlString.append(getGroupString());
        this.sqlString.append(getHavingString()[0]);
        this.sqlString.append(getOrderByString());
        this.sqlString.append(getLimitString());

        this.hqlString.append(getJoinString());
        this.hqlString.append(getWhereString()[1]);
        this.hqlString.append(getGroupString());
        this.hqlString.append(getHavingString()[1]);
        this.hqlString.append(getOrderByString());
        this.hqlString.append(getLimitString());


        return hqlString;
    }

    public ActiveRecord where(String w){
        this.whereList.add(new String[]{"and", w, nullVal, ""});
        return this;
    }

    public ActiveRecord where(String key, Object val){
        if(StringUtils.isEmpty(getCalMode(key))){
            this.whereList.add(new String[]{"and", key, "=", MathUtil.formatString(val)});
        }else{
            this.whereList.add(new String[]{"and", key, "", MathUtil.formatString(val)});
        }
        return this;
    }

    public ActiveRecord where(Map<String,Object> w){
        for(Map.Entry<String, Object> e : w.entrySet()){
            where(e.getKey(),e.getValue());
        }
        return this;
    }

    public ActiveRecord orWhere(String ow){
        this.whereList.add(new String[]{"or", ow, nullVal, ""});
        return this;
    }

    public ActiveRecord orWhere(String key, Object val){
        if(StringUtils.isEmpty(getCalMode(key))){
            this.whereList.add(new String[]{"or", key, "=", MathUtil.formatString(val)});
        }else{
            this.whereList.add(new String[]{"or", key, "", MathUtil.formatString(val)});
        }
        return this;
    }

    public ActiveRecord neq(String key, Object val){
        this.whereList.add(new String[]{"and", key, "<>", MathUtil.formatString(val)});
        return this;
    }

    public ActiveRecord in(String key, String vals){
        return in(key, vals.split(","));
    }

    public ActiveRecord in(String key, String[] valArr){
        String[] strArr = {"and", key, "in", ""};
        List<String> tList = new ArrayList<String>();
        Collections.addAll(tList,valArr);
        this.inListMap.put(key, tList);
        this.whereList.add(strArr);
        return this;
    }

    public ActiveRecord in(String key, Collection<?> c ){
        String[] strArr = {"and", key, "in", ""};
        this.inListMap.put(key, c);
        this.whereList.add(strArr);
        return this;
    }

    public ActiveRecord and(ActiveRecord aRecord){
        return andOr(aRecord, "and");
    }

    public ActiveRecord or(ActiveRecord aRecord){
        return andOr(aRecord, "or");
    }

    public ActiveRecord between(String key, Object val1, Object val2){
        this.whereList.add(new String[]{"and", key, "between", MathUtil.formatString(val1), MathUtil.formatString(val2)});
        return this;
    }

    public ActiveRecord notBetween(String key, Object val1, Object val2){
        this.whereList.add(new String[]{"and", key, "not between", MathUtil.formatString(val1), MathUtil.formatString(val2)});
        return this;
    }

    public ActiveRecord orBetween(String key, Object val1, Object val2){
        this.whereList.add(new String[]{"or", key, "between", MathUtil.formatString(val1), MathUtil.formatString(val2)});
        return this;
    }

    public ActiveRecord orNotBetween(String key, Object val1, Object val2){
        this.whereList.add(new String[]{"or", key, "not between", MathUtil.formatString(val1), MathUtil.formatString(val2)});
        return this;
    }

    public ActiveRecord like(String key, Object val, String model){
        String[] strArr = {"and", key, "like", MathUtil.formatString(val)};
        this.whereList.add(dealLikeWhereArr(strArr, model));
        return this;
    }

    public ActiveRecord like(String key, Object val){
        return like(key, val, "all");
    }

    public ActiveRecord orLike(String key, Object val, String model){
        String[] strArr = {"or", key, "like", MathUtil.formatString(val)};
        this.whereList.add(strArr);
        return this;
    }

    public ActiveRecord orLike(String key, Object val){
        return orLike(key, val, "all");
    }

    public ActiveRecord notLike(String key, Object val, String model){
        String[] strArr = {"and", key, "not like", MathUtil.formatString(val)};
        this.whereList.add(dealLikeWhereArr(strArr, model));
        return this;
    }

    public ActiveRecord notLike(String key, Object val){
        return notLike(key, val, "all");
    }

    public ActiveRecord orNotLike(String key, String val, String model){
        String[] strArr = {"or", key, "not like", MathUtil.formatString(val)};
        this.whereList.add(dealLikeWhereArr(strArr, model));
        return this;
    }

    public ActiveRecord orNotLike(String key, String val){
        return orNotLike(key, val, "all");
    }

    public ActiveRecord join(String joinType, String joinTable, String joinOn){
        if (StringUtils.isEmpty(joinType)) {
            joinType = "inner join";
        }
        String[] join = {joinType, joinTable, joinOn};
        this.joinList.add(join);
        return this;
    }

    public ActiveRecord join(String joinTable, String joinOn){
        return join("inner join", joinTable, joinOn);
    }

    public ActiveRecord orderBy(String orderString){
        return orderBy(orderString, "");
    }

    public ActiveRecord orderBy(String orderString, String mode){
        this.orderByList.add(orderString + " " + mode);
        return this;
    }

    public ActiveRecord groupBy(String groupString){
        this.groupByList.add(groupString);
        return this;
    }

    public ActiveRecord having(String key, Object val){
        if(StringUtils.isEmpty(getCalMode(key))){
            this.havingList.add(new String[]{"and", key, "=", val.toString()});
        }else{
            this.havingList.add(new String[]{"and", key, "", val.toString()});
        }
        return this;
    }

    public ActiveRecord orHaving(String key, Object val){
        if(StringUtils.isEmpty(getCalMode(key))){
            this.havingList.add(new String[]{"or", key, "=", val.toString()});
        }else{
            this.havingList.add(new String[]{"or", key, "", val.toString()});
        }
        return this;
    }

    public ActiveRecord isNull(String key){
        String[] strArr = {"and", key, nullVal,  " is null"};
        this.whereList.add(strArr);
        return this;
    }

    public ActiveRecord isNotNull(String key){
        String[] strArr = {"and", key, nullVal,  " is not null"};
        this.whereList.add(strArr);
        return this;
    }

    public ActiveRecord limited(int pageNo, int pageSize){
        if(pageSize > 0){
            int startIndex = pageSize *  (pageNo - 1);   //起始位置=pageSize*(pageNow-1)
            this.limitString.append(" limit ").append(startIndex).append(", ").append(pageSize);
        }
        return this;
    }

    public void clear(){
        this.selectList.clear();
        this.whereList.clear();
        this.joinList.clear();
        this.orderByList.clear();
        this.inListMap.clear();
        this.groupByList.clear();
        this.havingList.clear();

        this.setMap.clear();
        this.setHqlMap.clear();
        this.filedList.clear();

        this.sqlString = new StringBuilder();
        this.hqlString = new StringBuilder();
        this.limitString = new StringBuilder();
    }

    public Map<String, Object> getSetHqlMap(){
        return this.setHqlMap;
    }

    public String getSqlString() {
        return this.sqlString.toString();
    }

    public String getHqlString() {
        return this.hqlString.toString();
    }

    public boolean hasInCondition(){
        return this.inListMap.size() > 0;
    }

    public List<String> getFiledList() {
        return filedList;
    }

    //------------------
    private String getCalMode(String str) {
        for (String s : calsStrings) {
            if (CommonUtil.isContainStr(s, str.toLowerCase())) {
                return s.trim();
            }
        }
        return "";
    }

    private String[] dealLikeWhereArr(String[] strArr, String model){
        if(StringUtils.isEmpty(model))
            return strArr;

        if("left".equals(model.toLowerCase()) || "l".equals(model.toLowerCase())){
            strArr[3] = "%" + strArr[3];
        }else if("right".equals(model.toLowerCase()) || "r".equals(model.toLowerCase())){
            strArr[3] += "%";
        }else if("all".equals(model.toLowerCase()) || "a".equals(model.toLowerCase())){
            strArr[3] = "%" + strArr[3] + "%";
        }
        return strArr;
    }

    private void getSelectString(String table){
        if (this.selectList.size() == 0) {
            this.sqlString.append("select * from ").append(table);
        }else{
            this.sqlString.append("select ");
            if(this.distinct){
                this.sqlString.append(" distinct ");
            }
            for (String sel : selectList) {
                this.sqlString.append(sel).append(", ");
            }
            this.sqlString.delete(this.sqlString.length()-2, this.sqlString.length()-1);
            this.sqlString.append(" from ").append(table);
        }
        this.hqlString.append(this.sqlString.toString());
    }


    private void getInsertString(String table){
        if(setMap.size() == 0) return;

        sqlString.append("insert into ").append(table).append(" (");
        for(Map.Entry<String, Object> en : setMap.entrySet()){
            sqlString.append(en.getKey()).append(", ");
        }
        sqlString.delete(sqlString.length()-2,sqlString.length()-1);
        sqlString.append(") values( ") ;

        hqlString.append(sqlString.toString());
        for(Map.Entry<String, Object> en : setMap.entrySet()){
            if(null == en.getValue()){
                sqlString.append("NULL, ");
            }else{
                sqlString.append("'").append(en.getValue()).append("', ");
            }
            hqlString.append(":").append(en.getKey()).append(", ");
            this.setHqlMap.put(en.getKey() , en.getValue());
        }
        sqlString.delete(sqlString.length()-2, sqlString.length()-1).append(")");
        hqlString.delete(hqlString.length()-2, hqlString.length()-1).append(")");
    }

    private void getUpdateString(String table){
        if(setMap.size() == 0) return;

        this.sqlString.append("update ").append(table).append(" set ") ;
        this.hqlString.append("update ").append(table).append(" set ") ;

        for(Map.Entry<String, Object> en : setMap.entrySet()){
            if(null == en.getValue()){
                sqlString.append(en.getKey()).append(" = NULL, ");
            }else{
                sqlString.append(en.getKey()).append(" = ") .append("'").append(en.getValue()).append( "', ");
            }
            hqlString.append(en.getKey()).append(" = ").append(":").append(en.getKey()).append(", ");
            this.setHqlMap.put(en.getKey() , en.getValue());
        }
        this.sqlString.delete(this.sqlString.length()-2, this.sqlString.length()-1);
        this.hqlString.delete(this.hqlString.length()-2, this.hqlString.length()-1);
    }

    private void getDeleteString(String table){
        this.sqlString.append("delete from ").append(table);
        this.hqlString.append("delete from ").append(table);
    }

    private void getReplaceString(String table){
        if(setMap.size() == 0) return;

        sqlString.append("replace into ").append(table).append(" (");
        for(Map.Entry<String, Object> en : setMap.entrySet()){
            sqlString.append(en.getKey()).append(", ");
        }
        sqlString.delete(sqlString.length()-2, sqlString.length()-1);
        sqlString.append(") values( ") ;

        hqlString.append(sqlString.toString());
        for(Map.Entry<String, Object> en : setMap.entrySet()){
            if(null == en.getValue()){
                sqlString.append("NULL, ");
            }else{
                sqlString.append("'").append(en.getValue()).append("', ");
            }
            hqlString.append(":").append(en.getKey()).append(", ");
            this.setHqlMap.put(en.getKey() , en.getValue());
        }
        this.sqlString.delete(this.sqlString.length()-2, this.sqlString.length()-1).append(")");
        this.hqlString.delete(this.hqlString.length()-2, this.hqlString.length()-1).append(")");
    }

    private String[] getWhereStringByModel(String model, List<String[]> data){
        StringBuilder whereString = new StringBuilder();
        StringBuilder whereHqlString = new StringBuilder();

        for (int i = 0; i < data.size(); i++) {
            whereString.append(i == 0 ? " " + model + " " : "");
            whereHqlString.append(i == 0 ? " " + model + " " : "");

            String[] strings = data.get(i);
            whereString.append(i > 0 ? " " + strings[0] + " " : "");
            whereHqlString.append(i > 0 ? " " + strings[0] + " " : "");
            if (CommonUtil.isContainStr("between", strings[2])) {
                whereString.append(strings[1]).append(" ").append(strings[2]).append(" '").append(strings[3]).append("' and '").append(strings[4]).append("'");
                String sVal = ":"+ model +"_start_between_" + i;
                String eVal = ":"+ model +"_end_between_" + i;
                whereHqlString.append(strings[1]).append(" ").append(strings[2]).append(" ").append(sVal).append(" and ").append(eVal);
                this.setHqlMap.put(sVal.substring(1), strings[3]);
                this.setHqlMap.put(eVal.substring(1), strings[4]);
            } else if(CommonUtil.isContainStr("in", strings[2])) {
                whereString.append(strings[1]).append(" ").append(strings[2]).append("(");
                whereHqlString.append(strings[1]).append(" ").append(strings[2]).append("(");
                for(Object iString : this.inListMap.get(strings[1])){
                    whereString.append("'").append(MathUtil.formatString(iString, "")).append("', ");
                }
                whereString.delete(whereString.length()-2, whereString.length()-1).append(") ");
                String tVal = model +"_inCodition_" + i;
                whereHqlString.append(":").append(tVal).append(") ");
                this.setHqlMap.put(tVal, this.inListMap.get(strings[1]));
            }else {
                if(nullVal.equals(strings[2])){
                    whereString.append(strings[1]).append(strings[3]);
                    whereHqlString.append(strings[1]).append(strings[3]);
                }else{
                    whereString.append(strings[1]).append(" ").append(strings[2]).append(" '").append(strings[3]).append("'");
                    String tVal = ":" + model + "_where_" + i;
                    whereHqlString.append(strings[1]).append(" ").append(strings[2] ).append(" ").append(tVal);
                    this.setHqlMap.put(tVal.substring(1), strings[3]);
                }
            }
        }
        return new String[]{whereString.toString(), whereHqlString.toString()};
    }

    private String[] getWhereString(){
        return getWhereStringByModel("where", this.whereList);
    }

    private String getGroupString(){
        if (this.groupByList.size() == 0) {
            return "";
        }
        StringBuilder groupByString = new StringBuilder(" group by ");
        for (int i = 0; i < this.groupByList.size(); i++) {
            groupByString.append(i > 0 ? ", " : "") ;
            groupByString.append(this.groupByList.get(i));
        }
        return groupByString.toString();
    }


    private String getOrderByString(){
        if (this.orderByList.size() == 0) {
            return "";
        }
        StringBuilder orderByString = new StringBuilder(" order by ");
        for (int i = 0; i < this.orderByList.size(); i++) {
            orderByString.append(i > 0 ? "," : "");
            orderByString.append(this.orderByList.get(i));
        }
        return orderByString.toString();
    }

    private String getLimitString(){
        return this.limitString.toString();
    }

    private String getJoinString(){
        StringBuilder joinString = new StringBuilder();
        for(String[] join : joinList){
            joinString.append(" ").append(join[0]).append(" ").append(join[1]).append(" on ").append(join[2]);
        }
        return joinString.toString();
    }

    private String[] getHavingString(){
        return getWhereStringByModel("having", this.havingList);
    }

    private ActiveRecord andOr(ActiveRecord aRecord, String model){
        if(!aRecord.equals(this)){
            if(!aRecord.whereList.isEmpty()){
                this.whereList.add(new String[] {model, "(", nullVal, ""});

                // where
                for(int i=0; i<aRecord.whereList.size(); i++){
                    String[] wp = aRecord.whereList.get(i);
                    if(i == 0){
                        wp[0] = "";
                    }
                    this.whereList.add(wp);
                }

                // in
                for(Map.Entry<String, Collection<?>> entr :  aRecord.inListMap.entrySet()){
                    this.inListMap.put(entr.getKey(), entr.getValue());
                }

                this.whereList.add(new String[]{"", ")", nullVal, ""});

            }
        }
        return this;
    }
}

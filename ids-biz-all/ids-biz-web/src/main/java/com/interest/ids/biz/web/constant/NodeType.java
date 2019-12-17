package com.interest.ids.biz.web.constant;

public enum NodeType {
    
    SYSTEM((byte)1, "系统级"),
    ENTERPRISE((byte)2, "企业级"),
    DOMAIN((byte)3, "区域级");
    
    private Byte typeId;
    
    private String desc;
    
    private NodeType(Byte typeId, String desc){
        this.typeId = typeId;
        this.desc = desc;
    }

    public Byte getTypeId() {
        return typeId;
    }

    public void setTypeId(Byte typeId) {
        this.typeId = typeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

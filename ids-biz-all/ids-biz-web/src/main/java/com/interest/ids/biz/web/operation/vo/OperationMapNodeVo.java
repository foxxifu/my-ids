package com.interest.ids.biz.web.operation.vo;

import java.util.List;

public class OperationMapNodeVo {

    /**
     * 节点标识：企业编号/区域编号
     */
    private Long nodeId;

    /**
     * 节点类型: 1：系统级 2：企业级 3：区域级 4：电站级
     */
    private Byte nodeType;

    /**
     * 节点名
     */
    private String nodeName;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 半径
     */
    private Double radius;

    /**
     * 当前节点下电站数量
     */
    private Integer stationNum;

    /**
     * 运维人员集
     */
    private List<OperationUserVo> operators;

    /**
     * 节点下的电站集：区域级节点才有值
     */
    private List<StationInfoVo> stationList;

    /**
     * 子节点
     */
    private List<OperationMapNodeVo> subNode;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Byte getNodeType() {
        return nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setNodeType(Byte nodeType) {
        this.nodeType = nodeType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getStationNum() {
        return stationNum;
    }

    public void setStationNum(Integer stationNum) {
        this.stationNum = stationNum;
    }

    public List<OperationUserVo> getOperators() {
        return operators;
    }

    public void setOperators(List<OperationUserVo> operators) {
        this.operators = operators;
    }

    public List<OperationMapNodeVo> getSubNode() {
        return subNode;
    }

    public void setSubNode(List<OperationMapNodeVo> subNode) {
        this.subNode = subNode;
    }

    public List<StationInfoVo> getStationList() {
        return stationList;
    }

    public void setStationList(List<StationInfoVo> stationList) {
        this.stationList = stationList;
    }

}

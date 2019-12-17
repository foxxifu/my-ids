package com.interest.ids.common.project.bean.alarm;


/**
 * 
 * @author lhq
 *
 *
 */
public class AlarmConstant {
	
	 /**
     * 1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）；
     * 5、已清除（用户清除）；6、已恢复（设备自动恢复）；
     */
    public enum AlarmStatus {
        ACTIVE(1, "active"), ACKNOWLEDGEMENT(2, "acknowledged"), PROCESSING(3, "processing"), PROCESSED(4, "processed"), CLEARED(
                5, "cleared"), RECOVERED(6, "recovered");

        private Integer typeId;
        private String msgKey;

        public Integer getTypeId() {
            return typeId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public String getMsgKey() {
            return msgKey;
        }

        public void setMsgKey(String msgKey) {
            this.msgKey = msgKey;
        }

        AlarmStatus(Integer typeId, String msgKey) {
            this.typeId = typeId;
            this.msgKey = msgKey;
        }

    }
    
    public enum AlarmACT {
        ACT(1), RECOVER(2), TELE_ACT(3), // 遥信转告警
        TELE_RECOVER(4), // 遥信转告警恢复
        // HAND_CLEAR(5), // 手动确认清除
        // WORK_FLOW_RECOVER(6), // 通过工作流恢复
        YX_ACT(7), // 遥测转告警
        YX_RECOVER(8), // 遥测转告警恢复
        DEFECT(9), // 工作一票
        TICKET_ONE(10), // 工作二票
        TICKET_TWO(11); // 操作票

        private Integer typeId;

        AlarmACT(Integer typeId) {
            this.typeId = typeId;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }
    }

}

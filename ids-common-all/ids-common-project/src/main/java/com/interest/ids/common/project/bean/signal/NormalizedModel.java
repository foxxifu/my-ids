package com.interest.ids.common.project.bean.signal;

import com.interest.ids.common.project.bean.BaseBean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ids_normalized_model_t")
public class NormalizedModel extends BaseBean {
    /**
     * 主键ID
     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private static final long serialVersionUID = 4563774585816506149L;

    /**
     * 设备类型
     */
    @Column(name = "dev_type")
    private Long devType;

    /**
     * 归一化信号点名称
     */
    @Column(name = "signal_name")
    private String signalName;

    /**
     * 归一化信号点名称(中文标识)
     */
    @Column(name = "display_name")
    private String displayName;


    /**
     * 信号点编号，对应性能数据库中的字段名
     */
    @Column(name = "column_name")
    private String columnName;

    /**
     * 是否持久化
     */
    @Column(name = "is_persistent")
    private Boolean isPersistent;

    /**
     * 排序字段
     */
    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取设备类型
     *
     * @return dev_type - 设备类型
     */
    public Long getDevType() {
        return devType;
    }

    /**
     * 设置设备类型
     *
     * @param devType 设备类型
     */
    public void setDevType(Long devType) {
        this.devType = devType;
    }

    
    public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	



	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
     * 获取信号点编号，对应性能数据库中的字段名
     *
     * @return column_name - 信号点编号，对应性能数据库中的字段名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置信号点编号，对应性能数据库中的字段名
     *
     * @param columnName 信号点编号，对应性能数据库中的字段名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取是否持久化
     *
     * @return is_persistent - 是否持久化
     */
    public Boolean getIsPersistent() {
        return isPersistent;
    }

    /**
     * 设置是否持久化
     *
     * @param isPersistent 是否持久化
     */
    public void setIsPersistent(Boolean isPersistent) {
        this.isPersistent = isPersistent;
    }

    /**
     * 获取排序字段
     *
     * @return order_num - 排序字段
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序字段
     *
     * @param orderNum 排序字段
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return modified_date
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
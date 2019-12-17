package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

/**
 * 
 * @author xm 电站共享环境检测仪
 */
public class StationShareemi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 缓存时用于作为hset的key值
	public static final String share_station_code = "shareStationCode";

	public static final String share_devcie_id = "shareDevId";

	private Long id;
	private String shareStationCode;// 共享的电站编码
	private String stationCode;// 电站编码
	private Long shareDeviceId;// 共享的设备id

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShareStationCode() {
		return shareStationCode;
	}

	public void setShareStationCode(String shareStationCode) {
		this.shareStationCode = shareStationCode;
	}

	public Long getShareDeviceId() {
		return shareDeviceId;
	}

	public void setShareDeviceId(Long shareDeviceId) {
		this.shareDeviceId = shareDeviceId;
	}

}

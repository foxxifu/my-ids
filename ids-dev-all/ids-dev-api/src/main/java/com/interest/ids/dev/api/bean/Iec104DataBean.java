package com.interest.ids.dev.api.bean;

import java.util.List;

import com.interest.ids.common.project.bean.TupleParam;
import com.interest.ids.common.project.bean.signal.SignalInfo;


/**
 * 
 * @author lhq
 *
 *
 */
public class Iec104DataBean {
	
	private Iec104DataType type;
	
	private List<TupleParam<SignalInfo, Object, Long>> values;
	
   
	public Iec104DataBean(Iec104DataType type,
			List<TupleParam<SignalInfo, Object, Long>> list) {
		this.type = type;
		this.values = list;
	}


	public Iec104DataType getType() {
		return type;
	}


	public void setType(Iec104DataType type) {
		this.type = type;
	}

	public List<TupleParam<SignalInfo, Object, Long>> getValues() {
		return values;
	}

	public void setValues(List<TupleParam<SignalInfo, Object, Long>> values) {
		this.values = values;
	}
	
	
	public enum Iec104DataType{
		YC,DOUBLE_YX,SINGLE_YX,YM,DOUBLE_SOE,SINGLE_SOE
	}

}

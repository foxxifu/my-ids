package com.interest.ids.common.project.bean;
public class TupleParam<K,V,T> implements java.io.Serializable{
	
	private static final long serialVersionUID = -1362064206304280033L;

	private K key;
	
	private V value;
	
	private T t;
	
	public TupleParam(){}
	
	public TupleParam(K k,V v){
		this.key = k;
		this.value =v;
	}
	
	
	public TupleParam(K k,V v,T t){
		this.key = k;
		this.value =v;
		this.t=t;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}


	public T getT() {
		return t;
	}


	public void setT(T t) {
		this.t = t;
	}


	@Override
	public String toString() {
		return "TupleParam [key=" + key + ", value=" + value + "]"+", t=" + t + "]";
	}
}


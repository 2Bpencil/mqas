package com.tyf.mqas.code.entity;

public class Tree {
	private String id;
	private String name;
	private String pId;
	private String value;
	private String icon;

	public Tree() {
		super();
	}

	public Tree(String id, String name, String pId, String value) {
		super();
		this.id = id;
		this.name = name;
		this.pId = pId;
		this.value = value;
	}

	public Tree(String id, String name, String pId) {
		super();
		this.id = id;
		this.name = name;
		this.pId = pId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}

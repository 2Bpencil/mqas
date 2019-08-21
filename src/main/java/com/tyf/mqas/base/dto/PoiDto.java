package com.tyf.mqas.base.dto;

import java.util.List;

/**
 * 为Poi写入Excel准备的参数类
 * 
 * @author XHP
 * @date 2016年12月6日
 */
public class PoiDto {
	private String sheetName;
	private String[] titles;
	private List<String[]> dataList;

	public PoiDto() {
		super();
	}

	public PoiDto(String sheetName, String[] titles, List<String[]> dataList) {
		super();
		this.sheetName = sheetName;
		this.titles = titles;
		this.dataList = dataList;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public List<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}

}

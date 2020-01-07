package com.zhiwei.campusbus.vo;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;

import javax.imageio.ImageIO;

public class FileImgVo implements Serializable {
	private String id;
	private String name;
	private Object img;
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
	public Object getImg() {
		return img;
	}
	public void setImg(Object img) {
		this.img = img;
	}
	
	
	
	
	
	

}

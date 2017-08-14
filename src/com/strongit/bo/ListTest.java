package com.strongit.bo;

import java.sql.Blob;
import java.util.Date;

public class ListTest {
	private Long id;
	private String name;
	private String money;
	private Date date;
	private int num;
	private String item;
	private String img1;
	private Blob img2;
	private TreeNode node;
	private String img3;
	
	public String getImg3() {
		return img3;
	}
	public void setImg3(String img3) {
		this.img3 = img3;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	public Blob getImg2() {
		return img2;
	}
	public void setImg2(Blob img2) {
		this.img2 = img2;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public TreeNode getNode() {
		return node;
	}
	public void setNode(TreeNode node) {
		this.node = node;
	}
	
}

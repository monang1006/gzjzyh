package com.strongit.oa.wap.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.strongit.oa.wap.util.Doc;
import com.strongit.oa.wap.util.Docs;
import com.strongit.oa.wap.util.Suggestion;
import com.strongit.oa.wap.util.Suggestions;
import com.strongmvc.exception.SystemException;

public class Data extends TestCase {

	private List<Row> rows;
	
	private Attachments attachments;
	
	private Form form ;
	
	private Status status;
	
	private Page page;
	
	private Item item;
	
	private Docs docs;//正文集合
	
	private Suggestions suggestions;//意见集合

	/**
	 * 解析Json行数据
	 * @param strData
	 * @return
	 */
	public static String ParseJsonRowData(String strData) {
		JSONObject result = JSONObject.fromObject(strData);
		String status = result.getString("status");
		System.out.println("返回状态码：" + status);
		if("0".equals(status)) {//成功
			JSONObject itemObj = result.getJSONObject("item");
			if(itemObj != null && !"null".equals(itemObj.toString())) { 
				String type = itemObj.getString("type");
				String value = itemObj.getString("value");
				System.out.println("返回数据类型：" + type + ";值为：" + value);
			} else {
				JSONArray rows = result.getJSONArray("rows");
				if(rows != null && rows.size() > 0) {
					JSONObject pageJson = result.getJSONObject("page");
					Integer totalCount = pageJson.getInt("totalCount");
					Integer totalPages = pageJson.getInt("totalPages");
					System.out.println("总记录数：" + totalCount + ";总页数：" + totalPages); 
					for(int i=0;i<rows.size();i++) {
						JSONObject row = rows.getJSONObject(i);
						JSONArray items = row.getJSONArray("row");
						if(items != null && items.size() > 0) {
							for(int j=0;j<items.size();j++) {
								JSONObject item = items.getJSONObject(j);
								JSONObject citem = item.getJSONObject("item");
								String type = citem.getString("type");
								String value = citem.getString("value");
								System.out.println("返回数据类型：" + type + ";值为：" + value);
							}
						}
					}
				}
			}
		} else {
			System.out.println(result.getString("fail-reason")); 
		}
		return null;
	}
	
	/**
	 * 解析Json行数据,并封装成Data对象
	 * @param strData
	 * @return
	 */
	public static Data ParseJsonRowDataToBean(String strData) {
		Data data = new Data();
		JSONObject result = JSONObject.fromObject(strData);
		Status beanStatus = new Status();
		String status = result.getString("status");
		beanStatus.setCode(status);
		if("0".equals(status)) {//成功
			JSONObject itemObj = result.getJSONObject("item");
			if(itemObj != null && !"null".equals(itemObj.toString())) { //返回单条记录
				String type = itemObj.getString("type");
				String value = itemObj.getString("value");
				Item item = new Item(type,value);
				data.setItem(item);
			} else {
				JSONArray rows = result.getJSONArray("rows");
				if(rows != null && rows.size() > 0) {
					JSONObject pageJson = result.getJSONObject("page");
					Integer totalCount = pageJson.getInt("totalCount");
					Integer totalPages = pageJson.getInt("totalPages");
					Page page = new Page(totalCount,totalPages);
					data.setPage(page);
					List<Row> rowsList = new ArrayList<Row>();
					for(int i=0;i<rows.size();i++) {
						JSONObject row = rows.getJSONObject(i);
						JSONArray items = row.getJSONArray("row");
						List<Item> itemsList = new ArrayList<Item>();
						Row rowBean = new Row();
						if(items != null && items.size() > 0) {
							for(int j=0;j<items.size();j++) {
								JSONObject item = items.getJSONObject(j);
								JSONObject citem = item.getJSONObject("item");
								String type = citem.getString("type");
								String value = citem.getString("value");
								Item itemBean = new Item(type,value);
								itemsList.add(itemBean);
							}
						}
						rowBean.setItems(itemsList);
						rowsList.add(rowBean);
					}
					data.setRows(rowsList);
				}			
			}
		} else {
			beanStatus.setFailReason(result.getString("fail-reason"));
		}
		data.setStatus(beanStatus);
		return data;
	}

	/**
	 * 生成表单数据
	 * @param data
	 * @return
	 */
	public static String GenerateJsonFormData(Data data) {
		Status status = data.getStatus();//操作是否成功的标记
		String code = status.getCode();//操作返回编码 0：成功；1：失败
		JSONObject result = new JSONObject();
		result.put("status", code);
		if("0".equals(code)) {//操作成功
			Form form = data.getForm();//得到表单数据
			if(form != null) {
				List<Item> formItems = form.getItems();//得到表单项
				if(formItems != null && !formItems.isEmpty()) {
					JSONArray formJson = new JSONArray();
					for(Item citem : formItems) {
						JSONObject jsonItem = new JSONObject();
						jsonItem.put("item", JSONObject.fromObject(citem));
						formJson.add(jsonItem);
					}
					result.put("form", formJson);
				}
			}
			Docs docs = data.getDocs();//得到正文集合
			if(docs != null) {
				JSONArray attachmentsJson = new JSONArray();
				List<Doc> attacmentsList = docs.getDocs();
				if(attacmentsList != null && !attacmentsList.isEmpty()) {
					for(Doc attachment : attacmentsList) {
						List<Item> attachemntItems = attachment.getItems();
						JSONArray attachmentJson = new JSONArray();
						if(attachemntItems != null && !attachemntItems.isEmpty()) {
							for(Item attachemntItem : attachemntItems) {
								JSONObject jsonItem = new JSONObject();
								jsonItem.put("item", JSONObject.fromObject(attachemntItem));
								attachmentJson.add(jsonItem);
							}
						}
						JSONObject json = new JSONObject();
						json.put("doc", attachmentJson);
						attachmentsJson.add(json);
					}
				}
				result.put("docs", attachmentsJson);
			}
			Suggestions suggestions = data.getSuggestions();//意见集合
			if(suggestions != null) {
				JSONArray attachmentsJson = new JSONArray();
				List<Suggestion> attacmentsList = suggestions.getSuggestions();
				if(attacmentsList != null && !attacmentsList.isEmpty()) {
					for(Suggestion attachment : attacmentsList) {
						List<Item> attachemntItems = attachment.getItems();
						JSONArray attachmentJson = new JSONArray();
						if(attachemntItems != null && !attachemntItems.isEmpty()) {
							for(Item attachemntItem : attachemntItems) {
								JSONObject jsonItem = new JSONObject();
								jsonItem.put("item", JSONObject.fromObject(attachemntItem));
								attachmentJson.add(jsonItem);
							}
						}
						JSONObject json = new JSONObject();
						json.put("suggestion", attachmentJson);
						attachmentsJson.add(json);
					}
				}
				result.put("suggestions", attachmentsJson);
			}
			Attachments attachments = data.getAttachments();//得到附件集合
			if(attachments != null) {
				JSONArray attachmentsJson = new JSONArray();
				List<Attachment> attacmentsList = attachments.getAtachments();
				if(attacmentsList != null && !attacmentsList.isEmpty()) {
					for(Attachment attachment : attacmentsList) {
						List<Item> attachemntItems = attachment.getItems();
						JSONArray attachmentJson = new JSONArray();
						if(attachemntItems != null && !attachemntItems.isEmpty()) {
							for(Item attachemntItem : attachemntItems) {
								JSONObject jsonItem = new JSONObject();
								jsonItem.put("item", JSONObject.fromObject(attachemntItem));
								attachmentJson.add(jsonItem);
							}
						}
						JSONObject json = new JSONObject();
						json.put("attachment", attachmentJson);
						attachmentsJson.add(json);
					}
				}
				result.put("attachments", attachmentsJson);
			}
		} else {//操作失败
			result.put("fail-reason", status.getFailReason());
		}
		return result.toString();
	}
	
	/**
	 * 生成Json格式行数据
	 * @param data
	 * @return
	 */
	public static String GenerateJsonRowData(Data data) {
		Item item = data.getItem();
		Status status = data.getStatus();//操作是否成功的标记
		JSONObject result = new JSONObject();
		String code = status.getCode();//操作返回编码 0：成功；1：失败
		result.put("status", code);
		if(item != null) {//只是返回单条数据
			if("0".equals(code)) {
				JSONObject itemObj = JSONObject.fromObject(item);
				result.put("item", itemObj);
			} else {
				result.put("fail-reason", status.getFailReason());
			}
		} else {//返回多行数据
			List<Row> rows = data.getRows();//返回记录行
			if(status == null) {
				throw new SystemException("返回状态不可为空！");
			}
			if("0".equals(code)) {
				JSONArray rowsJson = new JSONArray();
				if(rows != null && !rows.isEmpty()) {
					for(Row row : rows) {
						List<Item> items = row.getItems();
						JSONArray rowJson = new JSONArray();
						for(Item citem : items) {
							JSONObject jsonItem = new JSONObject();
							jsonItem.put("item", JSONObject.fromObject(citem));
							rowJson.add(jsonItem);
						}
						JSONObject json = new JSONObject();
						json.put("row", rowJson);
						rowsJson.add(json);
					}
					Page page = data.getPage();//分页信息
					if(page != null) {
						result.put("page", JSONObject.fromObject(page));
					}
				}
				result.put("rows", rowsJson);
			} else {//操作失败
				result.put("fail-reason", status.getFailReason());
			}
		}
		return result.toString();
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Attachments getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachments attachments) {
		this.attachments = attachments;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Docs getDocs() {
		return docs;
	}

	public void setDocs(Docs docs) {
		this.docs = docs;
	}

	public Suggestions getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(Suggestions suggestions) {
		this.suggestions = suggestions;
	}
}

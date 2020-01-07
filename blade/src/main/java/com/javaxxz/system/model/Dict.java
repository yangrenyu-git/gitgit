
package com.javaxxz.system.model;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.javaxxz.core.annotation.BindID;
import com.javaxxz.core.base.model.BaseModel;

@Table(name = "tfw_dict")
@BindID(name = "id")
@SuppressWarnings("serial")
//字典表
public class Dict extends BaseModel {
	private Integer id; //主键
	private String code; //字典编码
	private String name; //字典名
	private Integer num; //排序号
	private Integer pid; //父字典
	private String tips; //备注
	private Integer version; //版本号

	@AutoID
	@SeqID(name = "SEQ_DICT")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}

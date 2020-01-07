
package com.javaxxz.system.model;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.javaxxz.core.annotation.BindID;
import com.javaxxz.core.base.model.BaseModel;

@Table(name = "tfw_dept")
@BindID(name = "id")
@SuppressWarnings("serial")
//部门表
public class Dept extends BaseModel {
	private Integer id; //主键
	private String fullname; //全称
	private Integer num; //排序号
	private Integer pid; //上级部门
	private String simplename; //简称
	private String tips; //备注
	private Integer version; //版本号

	@AutoID
	@SeqID(name = "SEQ_DEPT")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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

	public String getSimplename() {
		return simplename;
	}

	public void setSimplename(String simplename) {
		this.simplename = simplename;
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

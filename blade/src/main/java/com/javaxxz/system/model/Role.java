
package com.javaxxz.system.model;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.javaxxz.core.annotation.BindID;
import com.javaxxz.core.base.model.BaseModel;

@Table(name = "tfw_role")
@BindID(name = "id")
@SuppressWarnings("serial")
//角色表
public class Role extends BaseModel {
	private Integer id; //主键
	private Integer deptid; //部门id
	private String name; //角色名
	private Integer num; //排序号
	private Integer pid; //父角色
	private String tips; //角色别名(用于Permission注解权限检查)
	private Integer version; //版本号

	@AutoID
	@SeqID(name = "SEQ_ROLE")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
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

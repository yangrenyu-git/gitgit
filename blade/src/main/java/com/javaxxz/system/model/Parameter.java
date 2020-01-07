
package com.javaxxz.system.model;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.javaxxz.core.annotation.BindID;
import com.javaxxz.core.base.model.BaseModel;

@Table(name = "tfw_parameter")
@BindID(name = "id")
@SuppressWarnings("serial")
//参数表
public class Parameter extends BaseModel {
	private Integer id; //主键
	private String code; //参数编号
	private String name; //参数名
	private Integer num; //排序号
	private String para; //参数值
	private Integer status; //状态
	private String tips; //备注
	private Integer version; //版本号

	@AutoID
	@SeqID(name = "SEQ_PARAMETER")
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

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

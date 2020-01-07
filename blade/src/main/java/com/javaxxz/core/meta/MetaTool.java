package com.javaxxz.core.meta;

import org.beetl.sql.core.annotatoin.Table;

import com.javaxxz.core.base.model.BaseModel;

public class MetaTool {

	public String getTableName(Class<? extends BaseModel> clazz) {
		return clazz.getAnnotation(Table.class).name();
	}
}

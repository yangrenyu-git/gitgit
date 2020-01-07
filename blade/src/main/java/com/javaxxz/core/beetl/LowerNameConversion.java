
package com.javaxxz.core.beetl;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.annotatoin.Table;


public class LowerNameConversion extends NameConversion {

	@Override
	public String getTableName(Class<?> c) {
		Table table = (Table)c.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}
		return c.getSimpleName().toLowerCase();
	}

	@Override
	public String getColName(Class<?> c, String attrName) {
		return attrName.toLowerCase();
	}

	@Override
	public String getPropertyName(Class<?> c, String colName) {
		return colName.toLowerCase();
	}

}

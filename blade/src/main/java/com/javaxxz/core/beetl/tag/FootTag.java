package com.javaxxz.core.beetl.tag;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Tag;

import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.kit.DateKit;

public class FootTag extends Tag {

	@Override
	@SuppressWarnings("unchecked")
	public void render() {
		
		try {
			String company = "admin@javaxxz.com";
			String customer = "www.javaxxz.com";
			if(args.length > 1){
				Map<String, String> param = (Map<String, String>) args[1];
			    company = (Func.isEmpty(param.get("tonbusoft"))) ? company : param.get("tonbusoft");
			    customer = (Func.isEmpty(param.get("customer"))) ? customer : param.get("customer");
			}
			String year = DateKit.getYear();

			StringBuilder sb = new StringBuilder();
			
			sb.append("<div class=\"footer\">");
			sb.append("	<div class=\"footer-inner\">");
			sb.append("		<div class=\"footer-content\" style=\"padding-top:12px;\">");
			sb.append("			<span class=\"bigger-120\">技术支持 :</span>");
			sb.append("			<span class=\"bigger-120\" id=\"support_tonbusoft\">" + company + "</span>");
			sb.append("			<span class=\"bigger-120\"  style=\"padding-left:15px;\">");
			sb.append("				© " + year);
			sb.append("			</span>");
			sb.append("		</div>");
			sb.append("	</div>");
			sb.append("</div>");
			sb.append("<a href=\"#\" id=\"btn-scroll-up\" class=\"btn-scroll-up btn btn-sm btn-inverse\">");
			sb.append(" <i class=\"ace-icon fa fa-angle-double-up icon-only bigger-110\">");
			sb.append("  顶部");
			sb.append(" </i>");
			sb.append("</a>");
			
			

			ctx.byteWriter.writeString(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

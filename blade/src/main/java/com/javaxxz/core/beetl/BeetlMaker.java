
package com.javaxxz.core.beetl;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import com.javaxxz.core.toolbox.kit.FileKit;


public abstract class BeetlMaker {


	public static void makeHtml(String tlPath, Map<String, Object> paras, String htmlPath) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(htmlPath), "UTF-8"));
			BeetlTemplate.buildTo(FileKit.readString(tlPath, "UTF-8"), paras, pw);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

}

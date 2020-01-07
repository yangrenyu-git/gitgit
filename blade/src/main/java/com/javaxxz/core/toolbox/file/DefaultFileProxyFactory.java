
package com.javaxxz.core.toolbox.file;

import java.io.File;
import java.util.Date;

import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.kit.DateKit;
import com.javaxxz.system.model.Attach;

public class DefaultFileProxyFactory implements IFileProxy {

	@Override
	public File rename(File f, String path) {
		File dest = new File(path);
		f.renameTo(dest);
		return dest;
	}

	@Override
	public String [] path(File f, String dir) {
		//避免网络延迟导致时间不同步
		long time = System.currentTimeMillis();
		
		StringBuilder uploadPath = new StringBuilder().append(File.separator)
		.append(getFileDir(dir, Cst.me().getUploadRealPath()))
		.append(time)
		.append(getFileExt(f.getName()));
		
		StringBuilder virtualPath = new StringBuilder()
		.append(getFileDir(dir, Cst.me().getUploadCtxPath()))
		.append(time)
		.append(getFileExt(f.getName()));
		
		return new String [] {uploadPath.toString(), virtualPath.toString()};
	}
	
	@Override
	public Object getFileId(BladeFile bf) {
		Attach attach = new Attach();
		attach.setCreater(Func.toInt(ShiroKit.getUser().getId(), 0));
		attach.setCreatetime(new Date());
		attach.setName(bf.getOriginalFileName());
		attach.setStatus(1);
		attach.setUrl(bf.getUploadVirtualPath());
		return Blade.create(Attach.class).saveRtStrId(attach);
	}


	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}


	public static String getFileDir(String dir, String saveDir) {
		StringBuilder newFileDir = new StringBuilder();
		newFileDir.append(saveDir)
				.append(File.separator).append(dir).append(File.separator).append(DateKit.getDays())
				.append(File.separator);
		return newFileDir.toString();
	}

}

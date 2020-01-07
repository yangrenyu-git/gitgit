
package com.javaxxz.core.toolbox.file;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.toolbox.kit.DateKit;

public class BladeFile {

	private Object fileId;
	

	private MultipartFile file;
	

	private String dir;
	

	private String uploadPath;
	

	private String uploadVirtualPath;
	

	private String fileName;
	

	private String originalFileName;

	public BladeFile() {
		
	}

	public BladeFile(MultipartFile file, String dir) {
		this.dir = dir;
		this.file = file;
		this.fileName = file.getName();
		this.originalFileName = file.getOriginalFilename();
		this.uploadPath = File.separator + Cst.me().getUploadRealPath() + File.separator + dir + File.separator + DateKit.getDays() + File.separator + this.originalFileName;
		this.uploadVirtualPath = Cst.me().getUploadCtxPath().replace(Cst.me().getContextPath(), "") + File.separator + dir + File.separator + DateKit.getDays() + File.separator + this.originalFileName;
	}

	public BladeFile(MultipartFile file, String dir, String uploadPath, String uploadVirtualPath) {
		this(file, dir);
		if (null != uploadPath){
			this.uploadPath = uploadPath;
			this.uploadVirtualPath = uploadVirtualPath;
		}
	}

	public void transfer() {
		IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
		this.transfer(fileFactory);
	}

	public void transfer(IFileProxy fileFactory) {
		try {
			File file = new File(uploadPath);
			
			if(null != fileFactory){
				String [] path = fileFactory.path(file, dir);
				this.uploadPath = path[0];
				this.uploadVirtualPath = path[1].replace(Cst.me().getContextPath(), "");
				file = fileFactory.rename(file, path[0]);
			}
			
			File pfile = file.getParentFile();
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			
			this.file.transferTo(file);
			
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

	public Object getFileId() {
		if(null == this.fileId) {
			IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
			this.fileId = fileFactory.getFileId(this);
		}
		return fileId;
	}
	
	public void setFileId(Object fileId) {
		this.fileId = fileId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUploadVirtualPath() {
		return uploadVirtualPath;
	}

	public void setUploadVirtualPath(String uploadVirtualPath) {
		this.uploadVirtualPath = uploadVirtualPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

}

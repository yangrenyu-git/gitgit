
package com.javaxxz.core.toolbox.file;

import java.io.File;

public interface IFileProxy {
	

	String [] path(File file, String dir);


	File rename(File f, String path);
	

	Object getFileId(BladeFile bf);
	
}

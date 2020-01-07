package com.javaxxz.core.toolbox.kit;

import com.javaxxz.core.toolbox.kit.LogKit;


public class PageKit {


	public static int[] transToStartEnd(int pageNo, int countPerPage) {
		if (pageNo < 1) {
			pageNo = 1;
		}

		if (countPerPage < 1) {
			countPerPage = 0;
			LogKit.warn("Count per page  [" + countPerPage + "] is not valid!");
		}

		int start = (pageNo - 1) * countPerPage;
		int end = start + countPerPage;

		return new int[] { start, end };
	}


	public static int totalPage(int totalCount, int numPerPage) {
		if (numPerPage == 0) {
			return 0;
		}
		return totalCount % numPerPage == 0 ? (totalCount / numPerPage)
				: (totalCount / numPerPage + 1);
	}
}

package com.yong.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 分页工具
 */
@SuppressWarnings("all")
public class Pagination<T> {
	private final static int PAGESIZE =20;//每页显示多少条数据
	private  int  viewPage=9;//显示总页数其中的N页 
	private  int  viewCurrentPage=4;//显示当前页的前几页和后几页
	private int custPageSize;	// 自定义每页显示数据条数，为0的话则使用默认PAGE
	
	private int totalPage;
	private int lastPage;
	private int firstPage;
	private int beginPage;
	private List<T> results = new ArrayList<T>();
	
	
	public Pagination() {
	}
	
	/**
	 * 创建分页并初始化分页
	 * @param total
	 * @param beginPage
	 */
	public Pagination(int total, int beginPage,int custPageSize) {
		
		this.custPageSize = custPageSize;
		init(total,beginPage);
		
	}

	/**
	 * 
	 * @param total 总数据条数
	 * @param beginPage 当前页
	 * @return page 分页用的信息{totlaPage:总页数;list:显示在界面的页码数;firstPage:是否第一页;
	 *                         lastPage:是否最后一页}
	 */	
	public Hashtable getPageInfo(int total, int beginPage) {
		Hashtable page = new Hashtable();
		int pageList = 0;
		int totlaPage = 0;// 总页数
		
		int intCustPageSize = PAGESIZE;		// 每页显示行数
		if(custPageSize > 0) {
			intCustPageSize = custPageSize;
		}
		
		if (total != 0) {
			pageList = total / intCustPageSize;
			if (total % intCustPageSize > 0) {
				totlaPage = pageList + 1;
			} else {
				totlaPage = pageList;
			}
		}
		List list = new ArrayList();// 显示在界面的页码数
		if (totlaPage <= this.getViewPage()) {
			for (int i = 1; i <= totlaPage; i++) {
				list.add(i);
			}
		} else {
			if (beginPage <= (this.getViewCurrentPage() + 1)) {
				for (int j = 1; j <= this.getViewPage(); j++) {
					list.add(j);
				}
			} else if (beginPage > this.getViewCurrentPage()
					&& beginPage < totlaPage - this.getViewCurrentPage()) {
				for (int i = beginPage - this.getViewCurrentPage(); i <= beginPage
						+ this.getViewCurrentPage(); i++) {
					list.add(i);
				}
			} else {
				for (int i = totlaPage - this.getViewCurrentPage() * 2; i <= totlaPage; i++) {
					list.add(i);
				}
			}
		}
		page.put("pageList", list);
		page.put("totalPage", totlaPage);
		if (beginPage == totlaPage) {
			page.put("lastPage", "0"); // 0:表示当前页是最后一页
		} else {
			page.put("lastPage", "1");
		}
		if (beginPage == 1) {
			page.put("firstPage", "0"); // 0:表示当前页是第一页
		} else {
			page.put("firstPage", "1");
		}
		return page;

	}
	
	/**
	 * 初始化分页
	 * @param total
	 * @param beginPage
	 */
	public void init(int total, int beginPage){
		Hashtable<String,Object> pageInfo = new Hashtable<String,Object>();
		pageInfo = getPageInfo(total,beginPage);
		
		setFirstPage(Integer.valueOf(pageInfo.get("firstPage").toString()));
		setTotalPage(Integer.valueOf(pageInfo.get("totalPage").toString()));
		setLastPage(Integer.valueOf(pageInfo.get("lastPage").toString()));
	}
	
	public int getViewCurrentPage() {
		return viewCurrentPage;
	}
	public void setViewCurrentPage(int viewCurrentPage) {
		this.viewCurrentPage = viewCurrentPage;
	}
	public int getViewPage() {
		return viewPage;
	}
	public void setViewPage(int viewPage) {
		this.viewPage = viewPage;
	}

	public int getCustPageSize() {
		return custPageSize;
	}

	public void setCustPageSize(int custPageSize) {
		this.custPageSize = custPageSize;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}


	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getBeginPage() {
		return beginPage;
	}

	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}
}

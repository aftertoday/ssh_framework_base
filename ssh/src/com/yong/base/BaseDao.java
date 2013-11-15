package com.yong.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.yong.util.Pagination;

@SuppressWarnings("all")
@Component
public abstract class BaseDao<T> {

	@Resource
	SessionFactory sf;
	
	public T findById(Serializable id) {
		return (T) this.getSession().get(this.getTypeClass(), id);
	}

	public Serializable save(T t) {
			return this.getSession().save(t);
	}

	public void update(T t) {
		this.getSession().update(t);
	}

	public void delete(T t) {
		this.getSession().delete(t);
	}
	
	public void merge(T t){
		this.getSession().merge(t);
	}

	/**
	 * 查询表所有数据
	 * @return
	 */
	public List<T> findAll() {
		return (List<T>)getEmptyCriteria().list();
	}

	/**
	 * 统计表所有记录数
	 * @return
	 */
	public int findAllCount() {
		Criteria c = this.getEmptyCriteria();
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.rowCount());
		c.setProjection(proList);
		return Integer.valueOf(c.list().get(0).toString());
	}
	
	/**
	 * 根据条件统计所有记录
	 * @param t
	 * @return
	 */
	public int findAllCountByCondtion(T t){
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.rowCount());
		Criteria c = this.getCriteria(t);
		c.setProjection(proList);
		return Integer.valueOf(c.list().get(0).toString());
	}
	
	/**
	 * 根据criteria统计查询结果
	 * @param c
	 * @return
	 */
	public int findAllCountByCriteria(Criteria c){
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.rowCount());
		c.setProjection(proList);
		int total = Integer.valueOf(c.list().get(0).toString());
		//清空统计条件
		c.setProjection(null);
		return total;
	}
	
	/**
	 * 根据条件分页查询
	 * @return
	 */
	public Pagination<T> findPagination(final int startPage,final int pageSize,T t) {
		int start = startPage > 0? startPage - 1:startPage;
		List<T> list  = findByConditions(t, start * pageSize, pageSize);
		int total = findAllCountByCondtion(t);
		int currentPage = startPage == 0?startPage + 1:startPage;
		Pagination<T> pageInfo = new Pagination<T>(total, currentPage,pageSize);
		pageInfo.setCustPageSize(pageSize);
		pageInfo.setResults(list);
		pageInfo.setBeginPage(currentPage);
		return pageInfo;
	}
	
	/**
	 * 根据criteria 分页查询
	 * @return
	 */
	public Pagination<T> findPagination(final int startPage,final int pageSize,Criteria c) {
		int start = startPage > 0? startPage - 1:startPage;
		int total = 0;
		c.setFirstResult(start*pageSize);
		c.setMaxResults(pageSize);
		//查询所有记录
		List<T> list  = c.list();
		//统计记录总数
		total = this.findAllCountByCriteria(c);
		int currentPage = startPage == 0?startPage + 1:startPage;
		Pagination<T> pageInfo = new Pagination<T>(total, currentPage,pageSize);
		pageInfo.setCustPageSize(pageSize);
		pageInfo.setResults(list);
		pageInfo.setBeginPage(currentPage);
		return pageInfo;
	}
	
	/**
	 * 根据条件查询所有记录
	 * @param t
	 * @return
	 */
	public List<T> findByConditions(T t){
		return getCriteria(t).list();
	}
	
	/**
	 * 查询指定区间记录
	 * @param t
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<T> findByConditions(T t,int firstResult, int maxResults){
		Criteria c =  getCriteria(t);
		c.setFirstResult(firstResult);
		c.setMaxResults(maxResults);
		return c.list();
	}
	
	/**
	 * 根据条件获取criteria，并对关联对象判空
	 * @param t
	 * @return
	 */
	public Criteria getCriteria(T t){
		Criteria c = this.getSession().createCriteria(t.getClass());
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields){
			try{
				Method getter = t.getClass().getMethod(getterMethodName(field.getName()));
				Object value = getter.invoke(t);
				if(!isFieldEmpty(value)){
					c.add(Restrictions.eq(field.getName(), value));
				}
			}
			catch(Exception e){
			}
		}
		return c;
	}
	
	/**
	 * 获取泛型空criteria
	 * @return
	 */
	public Criteria getEmptyCriteria(){
		Criteria c = this.getSession().createCriteria(getTypeClass());
		return c;
	}
	
	
	public String getterMethodName(String fieldName){
		String MethodField = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		return "get"+MethodField;
	}
	
	public String setterMethName(String field){
		if(field == null || field.replaceAll(" ", "").equals("")){
			return null;
		}else{
			return "set"+field.substring(0,1).toUpperCase()+field.substring(1);
		}
	}
	
	/**
	 * 判断字段是否为空
	 * @param o
	 * @return
	 */
	private  Boolean isFieldEmpty(Object o){
			Boolean result = true;
			//系统类型只判断null
			if(o == null){
				result = true;
			}else{
				String ojbectScope = o.getClass().getPackage().getName();
				//string 类型做 null 和空字符串两层判断
				if(o.getClass().getName().equals("java.lang.String")){
					if(StringUtils.isEmpty(o.toString())){
						result = true;
					}else{
						result = false;
					}
				}
				//非 string jdk 对象非空判断
				else if(ojbectScope.startsWith("java.")||ojbectScope.startsWith("javax.")||ojbectScope.startsWith("org.")){
					result = false;
				}
				//自定义组件对象非空判断,只要有一个字段不为空则视为非空
				else{
					Field[] fields = o.getClass().getDeclaredFields();
					for(Field field : fields){
						try{
							Method getter = o.getClass().getMethod(getterMethodName(field.getName()));
							Object value = getter.invoke(o);
							String packageName = value.getClass().getPackage().toString();
							if(value != null){
								result = false;
								break;
							}
						}
						catch(Exception e){
						}
					}
				}
			}
			return result;
	}

	
	/**
	 * 获取session
	 * @return
	 */
	protected Session getSession(){
		return sf.getCurrentSession();
	}
	
	/**
	 * 获取泛型类型
	 */
	private Class<T> getTypeClass() {
		 Class typeCls = getClass();
		    //用于继承情况下获取父类泛型类型
		    Type genType = typeCls.getGenericSuperclass();
		    while (true) {
		            if (!(genType instanceof ParameterizedType)) {
		                typeCls = typeCls.getSuperclass();
		                genType = typeCls.getGenericSuperclass();
		            }
		            else {
		                break;
		            }
		        }
		        return  (Class<T>) ((ParameterizedType) genType).getActualTypeArguments()[0];
	}
	
	public SessionFactory getSf() {
		return sf;
	}

	public void setSf(SessionFactory sf) {
		this.sf = sf;
	}
}

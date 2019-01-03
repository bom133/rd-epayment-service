package th.go.rd.rdepaymentservice.repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.ScrollableResults;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.ScrollableResultsImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.DataTableSortable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.util.SqlUtil;
import th.go.rd.rdepaymentservice.util.datatable.AnnotationResultTransformer;

@Repository
@Transactional
public class SimpleDataTableRepository {
	private static final Logger logger = LoggerFactory.getLogger(SimpleDataTableRepository.class);
	
	@Autowired
	private EntityManager entityManager;
		
	public <T> List<T> getList(String sql, Map<String, Object> params, Class<T> clazz) {
		Query query = this.prepareStatement(sql, params);
		NativeQueryImpl nativeQuery = (NativeQueryImpl) query; 
		nativeQuery.setResultTransformer(new AnnotationResultTransformer(clazz)); 
		@SuppressWarnings("unchecked")
		List<T> list = nativeQuery.list();
		return list;
	}
	
	public <T> DataTable<T> getPagingData(String sql, PagingModel page, Map<String, Object> params, Class<T> clazz) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(sql);
		DataTableSortable paging = DataTableSortable.getRequestSortable(page);
		return getPagingData(stringBuilder, paging, params, clazz);
	}
	
	public <T> DataTable<T> getPagingData(StringBuilder sqlBuilder, DataTableSortable paging, Map<String, Object> params, Class<T> clazz) {
		paging.setOrderByCause(sqlBuilder, clazz);
		String sql = sqlBuilder.toString();
		return getPagingData(sql, params, paging.skip, paging.length, clazz);
	}
	
	public <T> DataTable<T> getPagingData(String sql, Map<String, Object> params, int skip, int length, Class<T> clazz) {
		Query query = this.prepareStatement(sql, params);
		NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
		ScrollableResultsImplementor scroll = null;
		try {
			scroll = nativeQuery.scroll();
//			String[] columnNamesFromSql = SqlUtil.getColumnNamesFromSql(sql);
			String[] columnNamesFromSql = SqlUtil.getColumnNamesResultSetFromHackishScrollAbleResult(scroll);
			return getPagingByCursor(scroll, columnNamesFromSql, skip, length, clazz);
		} finally {
			if (scroll != null) {
				scroll.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	<T> DataTable<T> getPagingByCursor(ScrollableResults cursor, String[] colNames, int skip, int length, Class<T> clazz) {
		cursor.last();
		int total = cursor.getRowNumber() + 1;
		
		DataTable<T> dataTable = new DataTable<T>();
		dataTable.setRecordsTotal(total);
		dataTable.setRecordsFiltered(total);
		int totalPerPage = total/length;
		int fractionRecord = (total%length)>0?1:0;
		dataTable.setTotalPages(totalPerPage+fractionRecord);
		if (!cursor.first()) {
			dataTable.setData(Collections.<T>emptyList());
			return dataTable;
		}
		if (cursor.scroll(skip)) {
			List<T> data = new ArrayList<T>();
			AnnotationResultTransformer art = new AnnotationResultTransformer(clazz);
			for (int i = 0; i < length; i++) {
				data.add((T) art.transformTuple(cursor.get(), colNames));
				if(!cursor.next()) {
					break;
				}
			}
			dataTable.setData(art.transformList(data));
			
		} else {
			dataTable.setData((List<T>)Collections.emptyList());
		}
		return dataTable;
	}
	
	private Query prepareStatement(String sql, Map<String, Object> params) {
		List<Object> paramList = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder();
		Pattern regEx = Pattern.compile(":(\\w+)");
		Matcher matcher = regEx.matcher(sql);
		Integer lastIndex = 0;
		while (matcher.find()) {
			if (sql.charAt(matcher.start() - 1) == '(' && sql.charAt(matcher.end()) == ')') {
				sqlBuilder.append(sql.substring(lastIndex, matcher.start() - 1));
				lastIndex = matcher.end() + 1;
			} else {				
				sqlBuilder.append(sql.substring(lastIndex, matcher.start()));
				lastIndex = matcher.end();
			}
			String key = matcher.group(1);
			Object paramValue = params.get(key);
			if (paramValue instanceof Object[] || paramValue instanceof Collection) {
				String prefix = "";
				int length = 0;
				if (paramValue instanceof Object[]) {
					length = ((Object[]) paramValue).length;
					for (Object o : (Object[]) paramValue ) {
						paramList.add(o);
					}
				} else {
					length = ((Collection) paramValue).size();
					for (Object o : (Collection) paramValue ) {
						paramList.add(o);
					}
				}
				sqlBuilder.append("(");
				for (int i = 0 ; i < length ; i++) {
					sqlBuilder.append(prefix).append("?");
					prefix = ",";
				}
				sqlBuilder.append(")");
			} else {
				sqlBuilder.append("?");
				paramList.add(params.get(key));				
			}
			
		}
		sqlBuilder.append(sql.substring(lastIndex));
		logger.debug("sqlBuilder>>>>"+sqlBuilder.toString());
		Query query = entityManager.createNativeQuery(sqlBuilder.toString());
		for (int i = 0 ; i < paramList.size() ; i++) {
			query.setParameter(i + 1, paramList.get(i));
		}
		return query;
	}

	public <T>DataTable<T> pageToDataTable(Page<Object[]>page,DataTable<T>dataTable){
		if(page.getContent()!=null&&page.getContent().size()!=0) {
			dataTable.setDraw(page.getSize());
			dataTable.setRecordsFiltered((int)(long)page.getTotalElements());
			dataTable.setRecordsTotal((int)(long)page.getTotalElements());
			dataTable.setTotalPages(page.getTotalPages());
			dataTable.setData((List<T>)page.getContent());
		}else {
			dataTable.setData((List<T>)Collections.emptyList());
		}	
		return dataTable;
	}
	
	public <T>DataTable<T> pageObjetToDataTable(Page<T>page,DataTable<T>dataTable){
		if(page.getContent()!=null&&page.getContent().size()!=0) {
			dataTable.setDraw(page.getSize());
			dataTable.setRecordsFiltered((int)(long)page.getTotalElements());
			dataTable.setRecordsTotal((int)(long)page.getTotalElements());
			dataTable.setTotalPages(page.getTotalPages());
			dataTable.setData((List<T>)page.getContent());
		}else {
			dataTable.setData((List<T>)Collections.emptyList());
		}	
		return dataTable;
	}
	
}
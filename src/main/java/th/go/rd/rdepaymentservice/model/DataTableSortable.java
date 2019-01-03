package th.go.rd.rdepaymentservice.model;

import java.util.ArrayList;
import java.util.List;

import th.go.rd.rdepaymentservice.util.datatable.AnnotationResultTransformer;



public class DataTableSortable {
	
	public static class Order {
		String colNo;
		String colDir;
		private Order(String a, String b) {
			colNo = a;
			colDir = b;
		}
	}
	
	public List<Order> orders;
	public List<String> colNames;
	public int skip;
	public int length;
	
	public void setOrderByCause(StringBuilder builder, Class<?> clazz) {
		if (orders.isEmpty()) {
			return;
		}
		AnnotationResultTransformer art = new AnnotationResultTransformer(clazz);
		boolean first = true;
		for (Order order : orders) {
			if (first) {
				builder.append(" order by ");
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(art.getColumnNameFromPropertyName(order.colNo));
			if (order.colDir.equals("ascend")) {
				builder.append(" ASC");
			} else {
				builder.append(" DESC");
			}
		}
	}
	
	public static DataTableSortable getRequestSortable(PagingModel page) {
		DataTableSortable result = new DataTableSortable();
		int skip = (page.getPage() * page.getResults())-page.getResults() ;
		result.skip = skip;
		result.length = page.getResults();
		result.orders = new ArrayList<>();
		if((page.getSortField() != null && page.getSortField().size()> 0) && (page.getSortOrder() != null && page.getSortOrder().size() >0)) {
			for (int i =0 ;i<page.getSortField().size(); i++) {
//				if (StringUtils.isEmpty(page.getSortField().get(i))||StringUtils.isEmpty(page.getSortOrder().get(i))) {
//					break;
//				}
				result.orders.add(new Order(page.getSortField().get(i),page.getSortOrder().get(i)));
			}
		}	
		return result;
	}
	
}

package th.go.rd.rdepaymentservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;


public class PagingModel {
	@NotEmpty
	private int  page;
	@NotEmpty
	private int results;
	private List<String> sortField = new ArrayList<>() ;
	private List<String> sortOrder = new ArrayList<>(); 
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getResults() {
		return results;
	}
	public void setResults(int results) {
		this.results = results;
	}
	public List<String> getSortField() {
		return sortField;
	}
	public void setSortField(List<String> sortField) {
		this.sortField = sortField;
	}
	public List<String> getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(List<String> sortOrder) {
		this.sortOrder = sortOrder;
	}
}

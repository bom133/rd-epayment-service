package th.go.rd.rdepaymentservice.model;

import java.io.Serializable;
import java.util.List;

public class DataTable<T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1914939987735345229L;
	
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private int totoalElments;
	private int totalPages;
	
	private List<T> data;
	
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getTotoalElments() {
		return totoalElments;
	}
	public void setTotoalElments(int totoalElments) {
		this.totoalElments = totoalElments;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}

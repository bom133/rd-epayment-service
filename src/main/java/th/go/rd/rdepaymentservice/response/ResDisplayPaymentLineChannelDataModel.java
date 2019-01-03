package th.go.rd.rdepaymentservice.response;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import th.go.rd.rdepaymentservice.model.ReceiverUnitModel;
import th.go.rd.rdepaymentservice.model.TaxInfoModel;


public class ResDisplayPaymentLineChannelDataModel {
	private String payLineCode;	
	
	private List<ReceiverUnitModel> listRec;
	
	private TaxInfoModel taxInfo;
	
	public String getPayLineCode() {
		return payLineCode;
	}
	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}
	
	public TaxInfoModel getTaxInfo() {
		return taxInfo;
	}
	public void setTaxInfo(TaxInfoModel taxInfo) {
		this.taxInfo = taxInfo;
	}
	public List<ReceiverUnitModel> getListRec() {
		return listRec;
	}
	public void setListRec(List<ReceiverUnitModel> listRec) {
		this.listRec = listRec;
	}
	public ResDisplayPaymentLineChannelDataModel(String payLineCode, List<ReceiverUnitModel> listRec,
			TaxInfoModel taxInfo) {
		super();
		this.payLineCode = payLineCode;
		this.listRec = listRec;
		this.taxInfo = taxInfo;
	}
	
	
	
}

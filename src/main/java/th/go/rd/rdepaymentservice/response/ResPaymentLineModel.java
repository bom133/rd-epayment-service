package th.go.rd.rdepaymentservice.response;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundDetail;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;

public class ResPaymentLineModel {
    private ResponseModel response;
    private MasterPaymentLine masterPaymentLine;
    private EpayTaxPaymentInfo taxPaymentInfo;
    private EpayTaxPaymentOutbound taxPaymentOutbound;
    private EpayTaxPaymentInboundDetail taxPaymentInboundDetail;
    private EpayTaxPaymentInbound taxPaymentInbound;
    
	public ResponseModel getResponse() {
		return response;
	}
	public void setResponse(ResponseModel response) {
		this.response = response;
	}
	public MasterPaymentLine getMasterPaymentLine() {
		return masterPaymentLine;
	}
	public void setMasterPaymentLine(MasterPaymentLine masterPaymentLine) {
		this.masterPaymentLine = masterPaymentLine;
	}
	public EpayTaxPaymentInfo getTaxPaymentInfo() {
		return taxPaymentInfo;
	}
	public void setTaxPaymentInfo(EpayTaxPaymentInfo taxPaymentInfo) {
		this.taxPaymentInfo = taxPaymentInfo;
	}
	public EpayTaxPaymentOutbound getTaxPaymentOutbound() {
		return taxPaymentOutbound;
	}
	public void setTaxPaymentOutbound(EpayTaxPaymentOutbound taxPaymentOutbound) {
		this.taxPaymentOutbound = taxPaymentOutbound;
	}
	public EpayTaxPaymentInboundDetail getTaxPaymentInboundDetail() {
		return taxPaymentInboundDetail;
	}
	public void setTaxPaymentInboundDetail(EpayTaxPaymentInboundDetail taxPaymentInboundDetail) {
		this.taxPaymentInboundDetail = taxPaymentInboundDetail;
	}
	public EpayTaxPaymentInbound getTaxPaymentInbound() {
		return taxPaymentInbound;
	}
	public void setTaxPaymentInbound(EpayTaxPaymentInbound taxPaymentInbound) {
		this.taxPaymentInbound = taxPaymentInbound;
	}
	
}

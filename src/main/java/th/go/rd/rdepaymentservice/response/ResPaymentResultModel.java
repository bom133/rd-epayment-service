package th.go.rd.rdepaymentservice.response;

import th.go.rd.rdepaymentservice.model.TaxInfoModel;

import th.go.rd.rdepaymentservice.model.PaymentResultDataModel;

public class ResPaymentResultModel {
	
    private PaymentResultDataModel paymentResult;
	
    private TaxInfoModel taxInfo;

	public PaymentResultDataModel getPaymentResult() {
		return paymentResult;
	}

	public void setPaymentResult(PaymentResultDataModel paymentResult) {
		this.paymentResult = paymentResult;
	}

	public TaxInfoModel getTaxInfo() {
		return taxInfo;
	}

	public void setTaxInfo(TaxInfoModel taxInfo) {
		this.taxInfo = taxInfo;
	}

	@Override
	public String toString() {
		return "ResPaymentResultModel [paymentResult=" + paymentResult + ", taxInfo=" + taxInfo + "]";
	}

}

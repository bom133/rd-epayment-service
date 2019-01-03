package th.go.rd.rdepaymentservice.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.modelmapper.ModelMapper;

import th.go.rd.rdepaymentservice.entity.EpayParameter;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfoDetail;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.xml.EpayReqPaymentXMLModel;
import th.go.rd.rdepaymentservice.xml.EpayStdPaymentInfoDetailModel;
import th.go.rd.rdepaymentservice.xml.EpayStdPaymentInfoModel;
import th.go.rd.rdepaymentservice.xml.EpayStdReqPaymentXMLModel;
import th.go.rd.rdepaymentservice.xml.EpayStdReturnInfoModel;

public class GenXmlHelper {
	
	public String ePayRdefReqPaymentXmlStd(String transactionNo, 
			EpayTaxPaymentOutbound epayTaxPaymentOutbound, 
			EpayReceiverPaymentLine epayReceiverPaymentLine,
			List<EpayParameter> lEpayParameter) throws JAXBException {
		
		Date today = new Date();
		
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo(); 
		
		String departmentCode =
		lEpayParameter.stream().filter(o -> o.getParamCode().equals("DepartmentCode")).findFirst().orElse(new EpayParameter()).getParamValue();
		String payTo = 
		lEpayParameter.stream().filter(o -> o.getParamCode().equals("PayTo")).findFirst().orElse(new EpayParameter()).getParamValue();
		String mid = 
		lEpayParameter.stream().filter(o -> o.getParamCode().equals("MID")).findFirst().orElse(new EpayParameter()).getParamValue();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String transmitDate = dateFormat.format(today);
		
		ModelMapper modelMapper = new ModelMapper();
		EpayStdPaymentInfoModel ePayStdInfo = modelMapper.map(epayTaxPaymentInfo, EpayStdPaymentInfoModel.class);
		List<EpayTaxPaymentInfoDetail> epayTaxPaymentInfoDetails = epayTaxPaymentInfo.getEpayTaxPaymentInfoDetails();
		EpayStdPaymentInfoDetailModel[] ePayStdInfoDetails = new EpayStdPaymentInfoDetailModel[epayTaxPaymentInfoDetails.size()]; 
		int i=0;
		for (EpayTaxPaymentInfoDetail e : epayTaxPaymentInfoDetails) {
			ePayStdInfoDetails[i] = modelMapper.map(e, EpayStdPaymentInfoDetailModel.class);
			ePayStdInfoDetails[i].setDepartmentCode(departmentCode);
			ePayStdInfoDetails[i].setPayTo(payTo);
			i++;
		}
		ePayStdInfo.setPaymentDetail(ePayStdInfoDetails);
		ePayStdInfo.setMid(mid);
		ePayStdInfo.setRdTransactionNo(transactionNo);
		ePayStdInfo.setTransmitDate(transmitDate);
		ePayStdInfo.setBankCode(epayReceiverPaymentLine.getMasterReceiverUnit().getRecCode());
		ePayStdInfo.setTerminalId(epayReceiverPaymentLine.getTerminalId());
		ePayStdInfo.setMerchantId(epayReceiverPaymentLine.getMerchantId());
		ePayStdInfo.setExpDate(dateFormat.format(epayTaxPaymentInfo.getExpDate()));
		String ordTransDateStr = 
				(epayTaxPaymentInfo.getOrderTranDate()!=null) ? 
						dateFormat.format(epayTaxPaymentInfo.getOrderTranDate()) : "";
		ePayStdInfo.setOrderTranDate(ordTransDateStr);
		ePayStdInfo.setPaymentLine(epayReceiverPaymentLine.getMasterPaymentLine().getPayLineCode());
		
		EpayStdReturnInfoModel ePayStdRetInfo = modelMapper.map(epayReceiverPaymentLine, EpayStdReturnInfoModel.class);
		EpayStdReqPaymentXMLModel ePayStdXML = new EpayStdReqPaymentXMLModel(ePayStdInfo,ePayStdRetInfo);

		JAXBContext jaxbContext = JAXBContext.newInstance(EpayStdReqPaymentXMLModel.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(ePayStdXML, sw);
		String xmlString = sw.toString();
		
		return xmlString;
	}
	
	public String convertToXmlString(Object object, Boolean jaxbFragement) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		
		/* jaxbFragement  = true : the Marshaller won't generate an xml declaration.
		 * jaxbFragement  = false : the Marshaller generate an xml declaration.
		 */
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, jaxbFragement);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(object, sw);
		String xmlString = sw.toString();
		
		return xmlString;
	}
	
	public Object convertToXmlObject(String src, Object dest) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(dest.getClass());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		InputStream inputStream = new ByteArrayInputStream(src.getBytes());
		dest = unmarshaller.unmarshal(inputStream);
		return dest;
	}
	
	public String ePayRdefReqPaymentXml(String transactionNo, 
			EpayTaxPaymentOutbound epayTaxPaymentOutbound, 
			EpayReceiverPaymentLine epayReceiverPaymentLine,
			List<EpayParameter> lEpayParameter) throws JAXBException {
		
		Date today = new Date();
		
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo(); 
		
		String mid = 
		lEpayParameter.stream().filter(o -> o.getParamCode().equals("MID")).findFirst().orElse(new EpayParameter()).getParamValue();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String refDate = dateFormat.format(today);
		String expDate = dateFormat.format(epayTaxPaymentInfo.getExpDate());
		
		ModelMapper modelMapper = new ModelMapper();
		EpayReqPaymentXMLModel epayReqPaymentXMLModel = modelMapper.map(epayTaxPaymentInfo, EpayReqPaymentXMLModel.class);
		epayReqPaymentXMLModel.setMid(mid);
		epayReqPaymentXMLModel.setRefDate(refDate);
		epayReqPaymentXMLModel.setExpDate(expDate);
		epayReqPaymentXMLModel.setRdTransactionNo(transactionNo);
		epayReqPaymentXMLModel.setFormCode(
				epayTaxPaymentInfo.getEpayTaxPaymentInfoDetails().stream().findFirst().orElse(new EpayTaxPaymentInfoDetail()).getFormCode());
		epayReqPaymentXMLModel.setRdRedirectUrl(epayReceiverPaymentLine.getRdRedirectUrl());
		epayReqPaymentXMLModel.setRdDirectUrl(epayReceiverPaymentLine.getRdDirectUrl());
		epayReqPaymentXMLModel.setTerminalId(epayReceiverPaymentLine.getTerminalId());
		
		JAXBContext jaxbContext = JAXBContext.newInstance(EpayReqPaymentXMLModel.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(epayReqPaymentXMLModel, sw);
		String xmlString = sw.toString();
		
		return xmlString;
	}
}

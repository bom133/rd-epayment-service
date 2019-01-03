package th.go.rd.rdepaymentservice.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.DecimalFormat;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoDetailRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentOutboundRepository;
import th.go.rd.rdepaymentservice.util.ConvertThaiBahtHelper;

@Service
@Transactional
public class GeneratePDFForPayInSlipService {
    private static final Logger logger = LoggerFactory.getLogger(GeneratePDFForPayInSlipService.class);

	@Autowired
	EpayTaxPaymentOutboundRepository epayTaxPaymentOutboundRepository;

	@Autowired
	EpayTaxPaymentInfoRepository epayTaxPaymentInfoRepository;

	@Autowired
	EpayTaxPaymentInfoDetailRepository epayTaxInfoDetailRepository;

	public byte[] getBillPdf(String uuid) throws Exception {

		Optional<EpayTaxPaymentOutbound> optionalPaymentOutbound = epayTaxPaymentOutboundRepository.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = optionalPaymentOutbound.orElse(new EpayTaxPaymentOutbound());
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();

		SimpleDateFormat dateEn = new SimpleDateFormat("d MMMM yyyy");
		SimpleDateFormat dateTh = new SimpleDateFormat("d MMMM yyyy", new Locale("th", "TH"));
		DecimalFormat formatNum = new DecimalFormat("#,##0.00");
		
		// read totalAmount to thai
		ConvertThaiBahtHelper convertThaiBahtHelper = new ConvertThaiBahtHelper();

		String isRound = String.valueOf(epayTaxPaymentInfo.getIsRound());
		String text = "ท่านได้รับยกเว้นไม่ต้องชำระเศษของบาท";

		List<EpayTaxPaymentOutbound> listOutbound = epayTaxPaymentInfo.getEpayTaxPaymentOutbounds();

		// Get jasper report
		String jrxmlFileName = null;
		if(epayTaxPaymentInfo.getRefNo().substring(0, 1).equals("T")) {
			jrxmlFileName = "report/PayInSlip_2.jrxml";
		}else {
			jrxmlFileName = "report/PayInSlip_1.jrxml";
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("refNo", epayTaxPaymentInfo.getRefNo());
		paramMap.put("expDateTh", dateTh.format(epayTaxPaymentInfo.getExpDate()));
		paramMap.put("expDateEn", dateEn.format(epayTaxPaymentInfo.getExpDate()));
		paramMap.put("createDate", dateTh.format(epayTaxPaymentInfo.getCreateDate()));
		paramMap.put("agentName", epayTaxPaymentInfo.getAgentName());
		paramMap.put("agentId", epayTaxPaymentInfo.getAgentId());
		for (EpayTaxPaymentOutbound e : listOutbound) {
			if (e.getCtlCode() != null && e.getMasterStatus().getStatus().equals("A")) {
				paramMap.put("controlCode", e.getCtlCode());
			}
		}
		if (epayTaxPaymentInfo.getTaxAmount() != null) {
			paramMap.put("taxAmount", formatNum.format(epayTaxPaymentInfo.getTaxAmount()));
		}
		else {
			paramMap.put("taxAmount", "-");
		}
		if (epayTaxPaymentInfo.getSurchargeAmount() != null) {
			paramMap.put("surchargeAmount", formatNum.format(epayTaxPaymentInfo.getSurchargeAmount()));
		}
		else {
			paramMap.put("surchargeAmount", "-");
		}
		if (epayTaxPaymentInfo.getCriminalFinesAmount() != null) {
			paramMap.put("criminalAmount", formatNum.format(epayTaxPaymentInfo.getCriminalFinesAmount()));
		}
		else {
			paramMap.put("criminalAmount", "-");
		}
		if (epayTaxPaymentInfo.getTotalAmount() != null) {
			paramMap.put("totalAmount", formatNum.format(epayTaxPaymentInfo.getTotalAmount()));
		}
		else {
			paramMap.put("totalAmount", "-");
		}
		paramMap.put("amountText", convertThaiBahtHelper.getThaiBaht(epayTaxPaymentInfo.getTotalAmount()));
		if (isRound.equals("Y")) {
			paramMap.put("isRoundStr", text);
		} else {
			paramMap.put("isRoundStr", "-");
		}
		String taxId = epayTaxPaymentInfo.getAgentId() + " 00";
		String refNo1 = epayTaxPaymentInfo.getAgentId();
		String refNo2 = null;
		for (EpayTaxPaymentOutbound e : listOutbound) {
			if (e.getCtlCode() != null && e.getMasterStatus().getStatus().equals("A")) {
				refNo2 = e.getCtlCode();
			}
		}
		String[] amount = String.valueOf(epayTaxPaymentInfo.getTotalAmount()).split("\\.");

		String barcodeStr = "| " + taxId + " " + refNo1 + " " + refNo2 + " " + amount[0] + amount[1];

		BufferedImage barcodeBuff = this.barcode128(barcodeStr);
		paramMap.put("barCodeImg", barcodeBuff);

		BufferedImage qrCodeBuff = this.qrBarcode(barcodeStr);
		paramMap.put("barCodeQr", qrCodeBuff);

		ClassLoader classLoader = getClass().getClassLoader();
		byte[] output = null;
		try {
			JasperReport jasperReport = JasperCompileManager
					.compileReport(classLoader.getResourceAsStream(jrxmlFileName));
			// Generate jasper print
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, paramMap,
					new JREmptyDataSource());

			// Export pdf file
			output = JasperExportManager.exportReportToPdf(jprint);
		} catch (Exception e) {
			logger.error("context", e);
		}

		return output;
	}

	private BufferedImage barcode128(String barcodeStr) throws IOException {
		try {
			Code128Bean barcode128Bean = new Code128Bean();

			barcode128Bean.setCodeset(Code128Constants.CODESET_B);
			final int dpi = 100;
			barcode128Bean.setModuleWidth(1.0);
			barcode128Bean.setBarHeight(50.0);
			barcode128Bean.setFontSize(10.0);
			barcode128Bean.setQuietZone(10.0);
			barcode128Bean.doQuietZone(false);

			BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false,
					0);

			barcode128Bean.generateBarcode(canvasProvider, barcodeStr);

			canvasProvider.finish();

			return canvasProvider.getBufferedImage();
		} catch (Exception e) {
			logger.error("context", e);
		}
		return null;
	}

	private BufferedImage qrBarcode(String barcodeStr) throws IOException {
		byte[] imageInByte;
		ByteArrayOutputStream output = QRCode.from(barcodeStr).to(ImageType.PNG).stream();
		imageInByte = output.toByteArray();
		return ImageIO.read(new ByteArrayInputStream(imageInByte));
	}

}

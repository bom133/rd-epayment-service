package th.go.rd.rdepaymentservice.model;


	public class ReceiverUnitModel {
		private long recPaymentLineId;
		private String recShortNameTh;
		private String recShortNameEn;
		private String recNameTh;
		private String recNameEn;
		private String payLineCode;
		private String imagePath;
		private String ImageFileType;
		private String url;
		
	
		public long getRecPaymentLineId() {
			return recPaymentLineId;
		}
		public void setRecPaymentLineId(long recPaymentLineId) {
			this.recPaymentLineId = recPaymentLineId;
		}
		public String getRecShortNameTh() {
			return recShortNameTh;
		}
		public void setRecShortNameTh(String recShortNameTh) {
			this.recShortNameTh = recShortNameTh;
		}
		public String getRecShortNameEn() {
			return recShortNameEn;
		}
		public void setRecShortNameEn(String recShortNameEn) {
			this.recShortNameEn = recShortNameEn;
		}
		public String getRecNameTh() {
			return recNameTh;
		}
		public void setRecNameTh(String recNameTh) {
			this.recNameTh = recNameTh;
		}
		
	
		public String getRecNameEn() {
			return recNameEn;
		}
		public void setRecNameEn(String recNameEn) {
			this.recNameEn = recNameEn;
		}
		
		
		public String getPayLineCode() {
			return payLineCode;
		}
		public void setPayLineCode(String payLineCode) {
			this.payLineCode = payLineCode;
		}
		public String getImagePath() {
			return imagePath;
		}
		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}
		public String getImageFileType() {
			return ImageFileType;
		}
		public void setImageFileType(String imageFileType) {
			ImageFileType = imageFileType;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		
		
		
		/*public ReceiverUnitModel(String recCode, String recShortName, String recNameTh, String recNameEn,
				String payLineCode, String imagePath) {
			super();
			this.recCode = recCode;
			this.recShortName = recShortName;
			this.recNameTh = recNameTh;
			this.recNameEn = recNameEn;
			this.payLineCode = payLineCode;
			this.imagePath = imagePath;
		}*/
}
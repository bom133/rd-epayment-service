package th.go.rd.rdepaymentservice.response;

public class ResSubmitPayTaxDataModel {
	private String UUID;

	public ResSubmitPayTaxDataModel(String uUID) {
		super();
		UUID = uUID;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	@Override
	public String toString() {
		return "ResSubmitPayTaxDataModel [UUID=" + UUID + "]";
	}
	
}

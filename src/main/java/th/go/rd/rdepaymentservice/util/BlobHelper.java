package th.go.rd.rdepaymentservice.util;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

public class BlobHelper {

	public Blob multipartFileToBlob(MultipartFile multipartFile) throws IOException, SerialException, SQLException {
		byte[] byteFile = multipartFile.getBytes();
		return new SerialBlob(byteFile);
	}
}

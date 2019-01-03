package th.go.rd.rdepaymentservice.component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDecimalSerializer extends JsonSerializer<BigDecimal>{

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		if (value == null) {
			gen.writeString("");
			return;
		}
		gen.writeString(decimalFormat.format(value));
	}

}

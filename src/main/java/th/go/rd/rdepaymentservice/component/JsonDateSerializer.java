package th.go.rd.rdepaymentservice.component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date>{
	
	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", LocaleContextHolder.getLocale());
		if (value == null) {
			gen.writeString("");
			return;
		}
		gen.writeString(format.format(value));
	}
	
}

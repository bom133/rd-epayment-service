package th.go.rd.rdepaymentservice.component;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonPersonalNoSerializer extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		if (value == null) {
			gen.writeString("");
			return;
		}else{
			String newString = "";
			for(int x=0; x<value.length();x++){
				String y =  value.substring(x,x+1);
				if(x==0 ||x==4||x==9||x==11){
					newString += y+'-';
				}else{
					newString += y;
				}
			}
			gen.writeString(newString);
			return;
		}
			
	}

}

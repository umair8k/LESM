package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RecordNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	String errorMessage;
	String errorCode;
	

}
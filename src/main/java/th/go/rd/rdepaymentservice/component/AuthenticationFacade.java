package th.go.rd.rdepaymentservice.component;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements Serializable {
 
	private static final long serialVersionUID = 1590403495252059713L;

	public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

package th.go.rd.rdepaymentservice.util;

import javax.servlet.http.HttpServletRequest;

public class IPAddressUtil {
	
	private static final String[] HEADERS_TO_TRY = { 
		    "X-Forwarded-For",
		    "Proxy-Client-IP",
		    "WL-Proxy-Client-IP",
		    "HTTP_X_FORWARDED_FOR",
		    "HTTP_X_FORWARDED",
		    "HTTP_X_CLUSTER_CLIENT_IP",
		    "HTTP_CLIENT_IP",
		    "HTTP_FORWARDED_FOR",
		    "HTTP_FORWARDED",
		    "HTTP_VIA",
		    "REMOTE_ADDR" };
	
	public static String getClientAddress(HttpServletRequest request) {
		String ip = null;
		for (String header : HEADERS_TO_TRY) {
	        ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	             break;
	        }
	    }
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		// filter ipv6 [TTMS ip address column is max length = 20
		if(ip != null && ip.length() > 20) {
			ip = ip.substring(0, 19);
		}
		
	    return ip;
	}
	
}

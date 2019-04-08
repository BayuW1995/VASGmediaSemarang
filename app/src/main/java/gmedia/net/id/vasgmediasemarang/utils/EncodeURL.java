package gmedia.net.id.vasgmediasemarang.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodeURL {
	public static String encodeURL(String s){
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}

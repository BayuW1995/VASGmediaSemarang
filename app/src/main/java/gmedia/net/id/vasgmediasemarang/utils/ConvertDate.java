package gmedia.net.id.vasgmediasemarang.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDate {
	private static Date date = null;

	public static String convert(String fromFormatDate, String toFormatDate, String inputan) {
		DateFormat inputFormat = new SimpleDateFormat(fromFormatDate);
		DateFormat outputFormat = new SimpleDateFormat(toFormatDate);
//        String inputDateStr = inputan;

		try {
			date = inputFormat.parse(inputan);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String outputDateStr = outputFormat.format(date);
		return outputDateStr;
	}
}

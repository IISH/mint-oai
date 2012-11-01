package gr.ntua.ivml.mint.oai.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	public static String formatDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
	}
}

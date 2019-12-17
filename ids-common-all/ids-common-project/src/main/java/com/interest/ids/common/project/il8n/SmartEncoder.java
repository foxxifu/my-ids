package com.interest.ids.common.project.il8n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartEncoder {
	public static final int MIN_GB2312_CODE_VALUE = 10000;
	public static final String GB2312 = "gb2312";
	public static final String ISO_8859_1 = "iso-8859-1";
	public static String db_acceptable_encoding = "gb2312";
	public static String file_acceptable_encoding = "gb2312";
	public static String page_acceptable_encoding = "iso-8859-1";

	static char[] validFileChars = { '.', '-', '!', '~', '`', '#', '$', '%',
			'^', '&', '(', ')', '=', ',', '[', ']', '{', '}' };
	public static final String UNKNOWN_ENCODING = "XXX_Encoding";
	private static List<Encoding> supportedEncodings = new ArrayList<Encoding>();

	public static boolean isGB2312String(String aString) {
		if ((aString == null) || (aString.length() < 1))
			return false;
		int len = aString.length();
		for (int i = 0; i < len; i++) {
			if (aString.charAt(i) > '✐')
				return true;
		}
		return false;
	}

	public static boolean isEnglishString(String aStr) {
		if (aStr == null) {
			return true;
		}
		Arrays.sort(validFileChars);
		for (int i = 0; i < aStr.length(); i++) {
			char c = aStr.charAt(i);
			if ((!Character.isJavaIdentifierPart(c))
					&& (Arrays.binarySearch(validFileChars, c) < 0)) {
				return false;
			}
		}
		return true;
	}

	static void printOutCharValue(String src) {
		for (int i = 0; i < src.length(); i++)
			System.out.print(src.charAt(i) + ",");
	}

	public static final String transformString(String aString,
			String targetEncoding) {
		if (aString == null) {
			return null;
		}
		String orginalEncodingName = getEncodingName(aString);
		if (!orginalEncodingName.equals("XXX_Encoding")) {
			return transformString(aString, orginalEncodingName, targetEncoding);
		}

		return aString;
	}

	public static final String transformString(String src, String aSrcEncoding,
			String aDestEncoding) {
		if (aSrcEncoding.equals(aDestEncoding))
			return src;
		try {
			return new String(src.getBytes(aSrcEncoding), aDestEncoding);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return src;
	}

	public static String getEncodingName(String src) {
		if (isGB2312String(src)) {
			return "gb2312";
		}

		return "iso-8859-1";
	}

	static {
		supportedEncodings.add(new Encoding(20000, 50000, "gb2312"));

		supportedEncodings.add(new Encoding(1, 256, "iso-8859-1"));
	}

	static class Encoding {
		int minVal;
		int maxVal;
		String encodingName;

		public Encoding(int minVal, int maxVal, String encodingName) {
			this.minVal = minVal;
			this.maxVal = maxVal;
			this.encodingName = encodingName;
		}
	}
}
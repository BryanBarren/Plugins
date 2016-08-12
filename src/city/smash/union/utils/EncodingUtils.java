package city.smash.union.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class EncodingUtils {
	public static String encodeString(String string) {
		return Base64Coder.encodeString(string);
	}
	public static String decodeBase64(String base64) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(base64));
	}
}

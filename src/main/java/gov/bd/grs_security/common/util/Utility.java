package gov.bd.grs_security.common.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.InflaterOutputStream;

public class Utility {

    public static String decompress(String b64Compressed) {

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream ios = new InflaterOutputStream(os);
            ios.write(Base64.getDecoder().decode(b64Compressed));
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

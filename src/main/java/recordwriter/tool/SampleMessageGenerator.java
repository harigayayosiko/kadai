package recordwriter.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KAJIWARAYutaka on 2015/07/06.
 */
public class SampleMessageGenerator {
    /*
	 * This message is delivered if a platform specific message is not specified
	 * for the end point. It must be set. It is received by the device as the
	 * value of the key "default".
	 */
    public static final String defaultMessage = "This is the default message";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static enum Platform {
        // Google Cloud Messaging
        GCM
    }

    public static String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw (RuntimeException) e;
        }
    }

    private static Map<String, String> getData(String message) {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("message",message );
        return payload;
    }


    public static String getSampleAndroidMessage(String message) {
        Map<String, Object> androidMessageMap = new HashMap<String, Object>();
        androidMessageMap.put("collapse_key", "Welcome");
        androidMessageMap.put("data", getData(message));
        androidMessageMap.put("delay_while_idle", true);
        androidMessageMap.put("time_to_live", 125);
        androidMessageMap.put("dry_run", false);
        return jsonify(androidMessageMap);
    }

}

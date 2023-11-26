import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String strIPAddress;
    private final String strUserName;
    private final String strDateTime;
    private final String strRequest;

    private final METHOD enumMethod;
    private final String strResponse;
    private final String strBytesSent;
    private final String strReferer;
    private final String strUserAgent;
    String strLogSample = "123.45.67.89 - - [27/Oct/2000:09:27:09 -0400] \"GET /java/javaResources.html "
            + "HTTP/1.0\" 200 10450 \"-\" \"Mozilla/4.6 [en] (X11; U; OpenBSD 2.8 i386; Nav)\"";



    public LogEntry(String line) {

        String sIPAddress = "";
        String sUserName = "";
        String sDateTime = "";
        String sRequest = "";
        METHOD eMethod = METHOD.GET;
        String sResponse = "";
        String sBytesSent = "";
        String sReferer = "";
        String sUserAgent = "";

        String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"(.+?)\"";


        Pattern p = Pattern.compile(regex);
        //System.out.println("log input line: " + strLogSample);
        Matcher matcher = p.matcher(line);
        if (matcher.find()) {
            sIPAddress = matcher.group(1);
            sUserName = matcher.group(3);
            sDateTime = matcher.group(4);
            sRequest = matcher.group(5);

            if (sRequest.startsWith("POST"))
                eMethod = METHOD.POST;

            sResponse = matcher.group(6);
            sBytesSent = matcher.group(7);

            if (!matcher.group(8).equals("-"))
                sReferer = matcher.group(8);

            sUserAgent = matcher.group(9);
        }

        strIPAddress = sIPAddress;
        strUserName = sUserName;
        strDateTime = sDateTime;
        strRequest = sRequest;
        enumMethod = eMethod;
        strResponse = sResponse;
        strBytesSent = sBytesSent;
        strReferer = sReferer;
        strUserAgent = sUserAgent;
    }


    public String getStrIPAddress() {
        return strIPAddress;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public String getStrDateTime() {
        return strDateTime;
    }

    public String getStrRequest() {
        return strRequest;
    }

    public METHOD getEnumMethod() {
        return enumMethod;
    }

    public String getStrResponse() {
        return strResponse;
    }

    public String getStrBytesSent() {
        return strBytesSent;
    }

    public String getStrReferer() {
        return strReferer;
    }

    public String getStrUserAgent() {
        return strUserAgent;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "strIPAddress='" + strIPAddress + '\'' +
                ", strUserName='" + strUserName + '\'' +
                ", strDateTime='" + strDateTime + '\'' +
                ", strRequest='" + strRequest + '\'' +
                ", enumMethod=" + enumMethod +
                ", strResponse='" + strResponse + '\'' +
                ", strBytesSent='" + strBytesSent + '\'' +
                ", strReferer='" + strReferer + '\'' +
                ", strUserAgent='" + strUserAgent + '\'' +
                '}';
    }
}

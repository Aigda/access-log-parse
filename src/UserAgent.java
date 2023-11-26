public class UserAgent {

    private final String browserName;
    private final String browserVersion;
    private final String browserOperatingSystem;

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getBrowserOperatingSystem() {
        return browserOperatingSystem;
    }

    public UserAgent(String sUserAgent) {

        UserAgentParser userAgentParser = new UserAgentParser(sUserAgent);

        browserName = userAgentParser.getBrowserName();
        browserVersion = userAgentParser.getBrowserVersion();
        browserOperatingSystem = userAgentParser.getBrowserOperatingSystem();

    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", browserOperatingSystem='" + browserOperatingSystem + '\'' +
                '}';
    }
}

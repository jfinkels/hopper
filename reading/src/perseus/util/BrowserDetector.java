package perseus.util;

/**
 * A simple set of routines for gathering information about the current user
 * agent.
 */
public class BrowserDetector {

    private String userAgent;
    private boolean ie6 = false;
    private boolean ie5 = false;
    private boolean ie5Mac = false;
    private boolean webKit = false;
    private boolean khtml = false;
    private boolean netscape4 = false;
    private boolean gecko = false;
    private boolean opera = false;

    /**
     * Class Constructor
     */
    public BrowserDetector(String ua) {
	userAgent = ua;

	if (userAgent != null) {
	    ie6 = (userAgent.indexOf("MSIE 6") != -1 &&
		    userAgent.indexOf("Opera") == -1);
	    ie5 = (userAgent.indexOf("MSIE 5") != -1 &&
		    userAgent.indexOf("Mac_PowerPC") == -1);
	    ie5Mac = (userAgent.indexOf("MSIE 5") != -1 &&
		    userAgent.indexOf("Mac_PowerPC") != -1);
	    webKit = (userAgent.indexOf("AppleWebKit") != -1);
	    khtml = (userAgent.indexOf("KHTML") != -1);

	    netscape4 = (userAgent.indexOf("Mozilla/4") != -1 &&
		    userAgent.indexOf("compatible") == -1);

	    gecko = (userAgent.indexOf("Mozilla/5") != -1 &&
		    !isRunningKHTML() && !isRunningOpera());

	    opera = (userAgent.indexOf("Opera") != -1);
	} else {
	    // if something doesn't have a user-agent header, then
	    // make a guess...
	    ie6 = true;
	    ie5 = true;
	}
    }

    /**
     * Test if running Internet Explorer 6
     *
     * @return true or false
    */
    public boolean isRunningIE6() {
	return ie6;
    }

    /**
     * Test if running Internet Explorer 5
     *
     * @return true or false
     */
    public boolean isRunningIE5() {
	return ie5;
    }

    /**
     * Test if running Internet Explorer 5 for Macs
     *
     * @return true or false
    */
    public boolean isRunningIE5Mac() {
	return ie5Mac;
    }

    /** Test if running OmniWeb or Safari
     *
     * @return true or false
     */
    public boolean isRunningWebKit() {
	return webKit;
    }

    /**
     * Test if running OmniWeb, Safari, and Konqueror
     *
     * @return true or false
     */
    public boolean isRunningKHTML() {
	return khtml;
    }

    /**
     * Test if running Netscape 4
     *
     * @return true or false
     */
    public boolean isRunningNetscape4() {
	return netscape4;
    }

    /**
     * Test if running Gecko
     *
     * @return true or false
     */
    public boolean isRunningGecko() {
	return gecko;
    }

    /**
     * Test if running Opera
     *
     * @return true or false
    */
    public boolean isRunningOpera() {
	return opera;
    }

    /**
     * Test if the browser supports XMLHttpRequest
     *
     * @return true or false
    */
    public boolean supportsXMLHttpRequest() {
	return (isRunningWebKit() ||
		isRunningGecko() ||
		isRunningIE6() ||
		isRunningIE5());
    }

    /**
     * Test if the browser supports the Border Box Model for CSS
     *
     * @return true or false
     */
    public boolean supportsBorderBoxModel() {
	return (isRunningIE5() ||
		isRunningGecko() ||
		isRunningIE5Mac());
    }

    /**
     * Tests whether the browser faithfully renders fractional percentage
     * widths. Note that this function has not been extensively tested across
     * multiple browsers!
     *
     * @return true if the browser renders them properly, false if it
     *	truncates them
     */
    public boolean supportsFractionalPercentages() {
	return isRunningGecko();
    }
}

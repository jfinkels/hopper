/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@
*/
package perseus.artarch;

public class FormatType {

    private final String name;
    private final String displayName;

    private FormatType(String name, String displayName) {
	this.name = name;
	this.displayName = displayName;
    }

    public String toString() { return displayName; }

    public static final FormatType TEI = new FormatType("Tei", "TEI");
}


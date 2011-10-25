package perseus.ie.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class reprsenting a place of some sort, probably a city but possibly a state
 * or nation. Also represents the place's location, in the form of longitude
 * and latitude.
 */

public class Place extends AbstractEntity {

    /** The nation in which this place exists */
    private String nation;
    /** The state/province/territory in which this place exists */
    private String state;
    /** The actual name of the place in question */
    private String siteName;

    private double longitude;
    private double latitude;

    public Place() {}

    public Place(String a, String d, String sn, String n, String s) {
        super(a, d);
        siteName = sn;
        nation = n;
        state = s;
    }

    public Place(String a, String d, String sn, String n, String s,
            double lon, double lat) {

        this(a, d, sn, n, s);
        longitude = lon;
        latitude = lat;
    }

    public String getSiteName() { return siteName; }
    public String getNation() { return nation; }
    public String getState() { return state; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }

    public boolean hasNation() {
        return (nation != null);
    }

    public boolean hasState() {
        return (state != null);
    }

    public void setSiteName(String sn) { siteName = sn; }
    public void setNation(String n) { nation = n; }
    public void setState(String s) { state = s; }
    public void setLongitude(double lon) { longitude = lon; }
    public void setLatitude(double lat) { latitude = lat; }

    public String getSortableString() { return displayName; }

    public String toString() {
        return getDisplayName();
    }

    public String getDisplayName() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(siteName);

        if (state != null || nation != null) {
            buffer.append(" (");
            if (state != null) {
                buffer.append(state);
            }
            if (nation != null) {
                if (state != null) {
                    buffer.append(", ");
                }
                buffer.append(nation);
            }
            buffer.append(")");
        }

        return buffer.toString();
    }

    protected String toXMLHelper() {
        StringBuffer output = new StringBuffer();
        output.append("<siteName>").append(siteName).append("</siteName>\n");

        if (hasState()) {
            output.append("<state>").append(state).append("</state>\n");
        }
        if (hasNation()) {
            output.append("<nation>").append(nation).append("</nation>\n");
        }

        output.append("<longitude>").append(longitude)
            .append("</longitude>\n");
        output.append("<latitude>").append(latitude).append("</latitude>\n");

        return output.toString();
    }

    private final static String NAME_PART = "[A-Za-z ]+[A-Za-z]";
    private static final Pattern SITE_PATTERN = Pattern.compile(NAME_PART);
    private static final Pattern CITY_STATE_PATTERN =
        Pattern.compile(String.format("(%s),\\s*(%s)", NAME_PART, NAME_PART));
    private static final Pattern CITY_STATE_NATION_PATTERN =
        Pattern.compile(String.format("(%s)\\s+\\((%s),\\s*(%s)\\)",
                    NAME_PART, NAME_PART, NAME_PART));

    public static Place parseDisplayName(String displayName) {
        Place place = new Place();
        place.setDisplayName(displayName);

        doMatching(place, displayName);

        if (place.getSiteName() == null) {
            throw new EntityParseException(Place.class, displayName);
        }

        StringBuilder authName = new StringBuilder().append("perseus");
        String[] names = new String[] {
            place.getSiteName(), place.getState(), place.getNation()
        };
        for (String name : names) {
            if (name != null) { authName.append(",").append(name); }
        }

        place.setAuthorityName(authName.toString());
        return place;
    }

    private static void doMatching(Place place, String displayName) {
        Matcher csMatcher = CITY_STATE_PATTERN.matcher(displayName);
        if (csMatcher.matches()) {
            place.setSiteName(csMatcher.group(1));
            place.setState(csMatcher.group(2));
            return;
        }

        Matcher csnMatcher = CITY_STATE_NATION_PATTERN.matcher(displayName);
        if (csnMatcher.matches()) {
            place.setSiteName(csnMatcher.group(1));
            place.setState(csnMatcher.group(2));
            place.setNation(csnMatcher.group(3));
            return;
        }

        Matcher cMatcher = SITE_PATTERN.matcher(displayName);
        if (cMatcher.matches()) {
            place.setSiteName(displayName);
        }
    }
}

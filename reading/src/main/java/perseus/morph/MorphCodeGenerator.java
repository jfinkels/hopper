package perseus.morph;

import java.util.Map;

/**
 * An interface defined for working with morphological code
*/
public interface MorphCodeGenerator {
    
    String getCode(Map<String, String> features);
    Map<String, String> getFeatures(String code);
}

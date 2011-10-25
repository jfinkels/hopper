package perseus.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This interface should be implemented by objects that can, in some way, be
 * treated as being cached on disk, regardless of whether they're caching
 * themselves or one of their child objects or something else entirely. The
 * TimelineRenderer class, for example, implements this interface; it does not
 * actually cache an instance of itself, but rather an instance of the
 * BufferedImage it creates.
 */
public interface Cacheable {

    public abstract void readCacheableData(FileInputStream fis) throws IOException;

    public abstract void writeCacheableData(FileOutputStream fos) throws IOException;

    public abstract String getCacheFilename();
}

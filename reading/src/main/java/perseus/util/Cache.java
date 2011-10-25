package perseus.util;

import java.io.*;

import org.apache.log4j.Logger;

/**
 * Represents a generic framework for caching objects. Any class that wishes
 * to use this class must implement the Cacheable interface, which defines
 * interfaces for reading and writing data to the cache.
 */
public class Cache {

    private static Logger logger = Logger.getLogger(Cache.class);

    static File cachePath = new File(Config.getCachePath());

    private static final Cache timelineImageCache =
	newCache(Config.getTimelineImageCacheDirectory());

    File cacheDirectory;

    /**
     * Class constructor
     * Create a cache in the specified directory in the cache path.
     * (We don't want the outside world to see this constructor.)
     * 
     * @param directory create cache file here
     */
    private Cache(File directory) {
	cacheDirectory = directory;
    }

    /**
     * Create a new cache in the subdirectory.  If the subdirectory exists
     * then return a new Cache object that uses this subdirectory.  If the
     * subdirectory does not exist then create a new Cache object that uses this subdirectory.
     *
     * @param subdirectory The name of the subdirectory 
     * @return a new Cache object
    */
    public static Cache newCache(String subdirectory) {
	File fullPath = new File(cachePath, subdirectory);
	boolean success = true;

	if (!fullPath.exists()) {
	    success = fullPath.mkdirs();
	    if (!success) {
		logger.error("Couldn't create cache directory: " + fullPath);

		return null;
	    }
	}

	if (!fullPath.canRead()) {
	    logger.error("Couldn't read cache at " + fullPath);
	    success = false;
	}

	if (!fullPath.canWrite()) {
	    logger.error("Couldn't write cache at " + fullPath);
	    success = false;
	}

	if (!success) {
	    // Problem!
	    return null;
	}

	return new Cache(fullPath);
    }

    /**
     * Write the value to the cache
     *
     * @param value The value to cache
     * @return true if the value is written to cache.  false if the value was not
     * successfully cached
    */
    public boolean write(Cacheable value) {
	String key = value.getCacheFilename();
	File file = new File(cacheDirectory, key);
	File parentDir = file.getParentFile();

	if (!parentDir.exists()) {
	    parentDir.mkdirs();
	}

	try {
	    FileOutputStream fos = new FileOutputStream(file);
	    value.writeCacheableData(fos);
	    fos.close();
	} catch (IOException ioe) {
	    logger.error("Problem writing to cache file: " + ioe);
	    return false;
	}

	return true;
    }
    
    /**
     * Test if the cache contains the specified value
     *
     * @param value look for this value in the cache
     * @return true if the value is in the cache, false if it is not in the cache
    */
    public boolean contains(Cacheable value) {
	String key = value.getCacheFilename();
	File file = new File(cacheDirectory, key);

	return file.exists() && file.canRead();
    }

    /**
     * Retrieve a value from the cache
     * 
     * @param value the value which was cached
     * @return true or false if the value can be read from the cache
    */
    public boolean read(Cacheable value) {
	String key = value.getCacheFilename();
	File file = new File(cacheDirectory, key);

	if (!file.exists()) {
	    logger.error("Attempt to read from non-existent cache " + "file " + file);
	    return false;
	}

	try {
	    FileInputStream fis = new FileInputStream(file);
	    value.readCacheableData(fis);
	    fis.close();
	} catch (IOException ioe) {
	    logger.error("Problem reading cache file: " + file);
	    return false;
	}
	return true;
    }

    /**
     * Return the timeline image cache.
     *
     * @return Cache   for the timeline images
     */
    public static Cache getTimelineImageCache() {
	return timelineImageCache;
    }
}

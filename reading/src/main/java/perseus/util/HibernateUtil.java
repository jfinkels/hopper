package perseus.util;

import java.util.Properties;

import javax.naming.NoInitialContextException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * The main class used to manage Hibernate sessions and transactions.
 * Based on the code in the online Hibernate documentation.
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory = null;

	private static Logger logger = Logger.getLogger(HibernateUtil.class);

	private static final ThreadLocal<Session> threadSession =
		new ThreadLocal<Session>();
	private static final ThreadLocal<Transaction> threadTransaction =
		new ThreadLocal<Transaction>();


	private static Configuration configuration;

	static {
		configuration = null;
		boolean tryAgain = false;

		try {
			// Create the SessionFactory from hibernate.cfg.xml
			configuration = new Configuration().configure();

			Properties props = configuration.getProperties();
			//comment out for now with the introduction of C3P0, which needs user/pass info
//			if (props.containsKey(Environment.DATASOURCE)) {
//				props.remove(Environment.USER);
//				props.remove(Environment.PASS);
//				configuration.setProperties(props);
//			}

			sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			// There was no initial context. As with SQLHandler, this is a
			// cheesy way of figuring out whether we're running inside
			// Tomcat or offline. If we can't find the data source
			// provided by Tomcat, remove it and try again.
			if (hasNoInitialContext(ex)) {
				tryAgain = true;

				Properties properties = configuration.getProperties();
				properties.remove("hibernate.connection.datasource");
				// Also, we need to set the username and password, since
				// they need *not* to be set if we're running Tomcat.
				properties.setProperty(Environment.USER,
						Config.getProperty(Config.DATABASE_USERNAME));
				properties.setProperty(Environment.PASS,
						Config.getProperty(Config.DATABASE_PASSWORD));
				properties.setProperty(Environment.URL,
						Config.getProperty(Config.DATABASE_URL));
				properties.setProperty(Environment.DRIVER,
						Config.getProperty(Config.DATABASE_DRIVER));
				configuration.setProperties(properties);

				//configuration = createConfiguration();
			} else {
				logger.fatal("Initial SessionFactory creation failed. " + ex);
			}
		} finally {
			if (tryAgain) {
				try {
					sessionFactory = configuration.buildSessionFactory();
				} catch (Throwable ex) {
					// Make sure you log the exception, as it might be swallowed
					logger.fatal("Initial CONTEXTLESS SessionFactory "
							+ "creation failed: " + ex);
					throw new ExceptionInInitializerError(ex);
				}
			}
		}
	}

	// Was the given exception caused by a NoInitialContextException at some
	// point in the past?
	private static boolean hasNoInitialContext(Throwable ex) {
		while (ex != null && !(ex instanceof NoInitialContextException)) {
			ex = ex.getCause();
		}

		return (ex instanceof NoInitialContextException);
	}

	/**
	 * Returns the value of one of Hibernate's configuration properties.
	 *
	 * @param key the key to search for
	 * @return the value of the key
	 */
	public static String getProperty(String key) {
		return configuration.getProperties().getProperty(key);
	}

	/*private static Configuration createConfiguration() {
		Configuration cfg = new Configuration()
		.addClass(perseus.document.Chunk.class) 
		.addClass(perseus.document.TableOfContents.class)
		.addClass(perseus.document.WordCount.class)
		.addClass(perseus.ie.entity.Entity.class) 
		.addClass(perseus.ie.entity.EntityOccurrence.class) 
		.addClass(perseus.ie.freq.Frequency.class)
		.addClass(perseus.ie.Citation.class)
		.addClass(perseus.morph.Parse.class)
		.addClass(perseus.util.Language.class)
		.addClass(perseus.voting.Vote.class)
		.setProperty("hibernate.connection.driver_class",
		"com.mysql.jdbc.Driver") 
		.setProperty("hibernate.connection.url",
		"jdbc:mysql://localhost:3306/sor?useUnicode=true&characterEncoding=utf8") 
		.setProperty("hibernate.connection.username", "webuser") 
		.setProperty("hibernate.connection.password", "webuser") 
		.setProperty("hibernate.dialect",
		"org.hibernate.dialect.MySQLInnoDBDialect")
		.setProperty("hibernate.cache.use_query_cache", "true")
		.setProperty("hibernate.jdbc.batch_size", "30")
		.setProperty("hibernate.cglib.use_reflection_optimizer", "false")
		.setProperty("hibernate.show_sql", "true");

		// Tomcat should take care of the connection pool...I hope
		//.setProperty("hibernate.connection.pool_size", "150");

		return cfg;
	}*/


	public static Session getSession() {
		Session s = threadSession.get();
		// Open a new Session, if this thread has none yet
		try {
			if (s == null) {
				s = sessionFactory.openSession();
				threadSession.set(s);
			}
		} catch (HibernateException ex) {
			handleException(ex);
		}
		return s;
	}

	public static void closeSession() {
		try {
			Session s = threadSession.get();
			threadSession.set(null);
			if (s != null && s.isOpen()) {
				s.close();
			}
		} catch (HibernateException ex) {
			logger.error("*** Caught EXCEPTION: " + ex);
			handleException(ex);
		}
	}

	public static void beginTransaction() {
		Transaction tx = threadTransaction.get();
		try {
			if (tx == null) {
				tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (HibernateException ex) {
			handleException(ex);
		}
	}

	public static void commitTransaction() {
		Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
				tx.commit();
			threadTransaction.set(null);
		} catch (HibernateException ex) {
			rollbackTransaction();
			handleException(ex);
		}
	}

	public static void rollbackTransaction() {
		Transaction tx = threadTransaction.get();
		try {
			threadTransaction.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				tx.rollback();
			}
		} catch (HibernateException ex) {
			handleException(ex);
		} finally {
			closeSession();
		}
	}

	public static Object getById(Class clazz, int id) {
		return getSession().load(clazz, new Integer(id));
	}

	private static void handleException(Exception ex) {
		ex.printStackTrace();
		logger.warn("Hibernate threw an exception: " + ex);
	}

	/**
	 * Helper method to apply first-result/maximum-results restrictions
	 * to a given query.
	 */
	public static void setResultRestrictions(Query query,
			int firstHit, int maxHits) {
		if (firstHit > -1) query.setFirstResult(firstHit);
		if (maxHits > -1) query.setMaxResults(maxHits);
	}

	/**
	 * Helper method to apply first-result/maximum-results restrictions
	 * to a given Criteria-based query.
	 */
	public static void setResultRestrictions(Criteria criteria,
			int firstHit, int maxHits) {
		if (firstHit > -1) criteria.setFirstResult(firstHit);
		if (maxHits > -1) criteria.setMaxResults(maxHits);
	}

	public static StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}
}

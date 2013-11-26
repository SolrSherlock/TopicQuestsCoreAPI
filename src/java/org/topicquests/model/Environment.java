/**
 * 
 */
package org.topicquests.model;

import java.util.Hashtable;
import java.util.Map;

import org.nex.config.ConfigPullParser;
import org.topicquests.model.api.IDataProvider;
import org.topicquests.model.api.IMergeImplementation;
import org.topicquests.model.api.IQueryModel;
import org.topicquests.util.LoggingPlatform;
import org.topicquests.util.Tracer;

/**
 * @author park
 *
 */
public class Environment {
	public LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String,Object>props;
	public IDataProvider database;
	public IQueryModel model;

	/**
	 * 
	 * @param p
	 * @param isExtended
	 */
	public Environment(Map<String,Object>p, boolean isExtended) {
		props = p;
		init(isExtended);
	}
	
	/**
	 * Assumes <em>config-props.xml</em> is in the install directory
	 * @param isExtended
	 */
	public Environment(boolean isExtended) {
		ConfigPullParser p = new ConfigPullParser("config-props.xml");
		props = p.getProperties();
	}
	
	/**
	 * <code>isExtended</code> will be <code>true</code> if this
	 * class is extended by another class, in which case, the
	 * database and model features will be handled by it.
	 * @param p
	 * @param isExtended
	 */
	void init(boolean isExtended) {
		if (!isExtended) {
			try {
				//TODO finish this
				int cachesize = Integer.parseInt(getStringProperty("MapCacheSize"));
				String cp = (String)props.get("DataProviderClass");
				Object o;
				if (cp != null) {
					o = 
					database = (IDataProvider)Class.forName(cp).newInstance();
					database.init(this, cachesize);
				}
				IMergeImplementation merger;
				cp = (String)props.get("MergeImplementation");
				//this installation might not deal with merge bean
				if (cp != null) {
					merger = (IMergeImplementation)Class.forName(cp).newInstance();
					merger.init(this);
					database.setMergeBean(merger);
				}
				model = new QueryModel(this);
				String bs = (String)props.get("ShouldBootstrap");
				boolean shouldBootstrap = false; // default value
				if (bs != null)
					shouldBootstrap = bs.equalsIgnoreCase("Yes");
				if (shouldBootstrap)
					bootstrap();
				 
			} catch (Exception e) {
	System.out.println("Environment error "+e.getMessage());
				logError(e.getMessage(),e);
				e.printStackTrace();
			}
		}
		logDebug("Environment Started");
	}
	
	/**
	 * Available to extensions if needed
	 */
	public void bootstrap() {
		CoreBootstrap cbs = new CoreBootstrap(database);
		cbs.bootstrap();
		BiblioBootstrap bbs = new BiblioBootstrap(database);
		bbs.bootstrap();
		RelationsBootstrap rbs = new RelationsBootstrap(database);
		rbs.bootstrap();
	}
	
	public IDataProvider getDataProvider() {
		return database;
	}
		
	public Map<String,Object> getProperties() {
		return props;
	}
	
	public String getStringProperty(String key) {
		return (String)props.get(key);
	}
	
	public void shutDown() {
		//
	}

	/////////////////////////////
	// Utilities
	public void logDebug(String msg) {
		log.logDebug(msg);
	}
	
	public void logError(String msg, Exception e) {
		log.logError(msg,e);
	}
	
	public void record(String msg) {
		log.record(msg);
	}

	public Tracer getTracer(String name) {
		return log.getTracer(name);
	}

}

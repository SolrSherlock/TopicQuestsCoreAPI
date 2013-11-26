/**
 * 
 */
package org.topicquests.model.api;

import java.sql.Connection;

/**
 * @author park
 *
 */
public interface IRdbmsDataProvider extends IDataProvider {
	
	Connection getConnection() throws Exception;
	
	void shutDown();

}

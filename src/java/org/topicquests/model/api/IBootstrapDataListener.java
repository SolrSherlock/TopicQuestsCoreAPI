/**
 * 
 */
package org.topicquests.model.api;

/**
 * @author park
 * <p>This interface defines a listener which will
 * allows users to gain access to terminology and to
 * topics which use that terminology as names (labels)</p>
 */
public interface IBootstrapDataListener {
	//TODO move this to TopicQuestsCoreAPI codebase
	/**
	 * Data built during any topic bootstrapping process, which
	 * could include built-in bootstrapping, and any data importing
	 * such as ontologies, word-lists which become topics, etc.
	 * @param label
	 * @param topicLocator
	 */
	void acceptBootstrapData(String label, String topicLocator);
}

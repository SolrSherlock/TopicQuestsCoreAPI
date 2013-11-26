/**
 * 
 */
package org.topicquests.model.api;

import java.util.Set;


/**
 * @author park
 *
 */
public interface IQueryModel {
	/**
	 * <p>Return an iterator which provides instances of {@link INode} which contain
	 * <code>label</code> for the given <code>language</code>.</p>
	 * @param label
	 * @param language
	 * @param count
	 * @param credentials
	 * @return
	 */
	IQueryIterator listNodesByLabel(String label, String language, int count, Set<String>credentials);
	
	/**
	 * <p>Return an iterator which provides instances of {@link INode} which contain
	 * <code>details</code> for the given <code>language</code>.</p>
	 * @param details
	 * @param language
	 * @param count
	 * @param credentials
	 * @return
	 */
	IQueryIterator listNodesByDetails(String details, String language, int count, Set<String> credentials);
	
	/**
	 * <p>Return an iterator which provides instances of {@link INode} which are
	 *  for the given <code>relationType>. Note, each resulting node must be
	 *  cast to {@link ITuple}</p>
	 * @param relationType
	 * @param count
	 * @param credentials
	 * @return
	 */
	IQueryIterator listTuplesByRelation(String relationType, int count, Set<String>credentials);
	
	/**
	 * <p>Return an iterator which provides instances of {@link INode} which are instances of
	 * <code>nodeTypeLocator</code></p>
	 * @param nodeTypeLocator
	 * @param count
	 * @param credentials
	 * @return
	 */
	IQueryIterator listNodeInstances(String nodeTypeLocator, int count, Set<String>credentials);
	
	/**
	 * <p>Return an iterator which provides instances of {@link INode} which are subclasses of
	 * <code>superClassLocator</code></p>
	 * @param superClassLocator
	 * @param count TODO
	 * @param credentials TODO
	 * @return
	 */
	IQueryIterator listNodeSubclasses(String superClassLocator, int count, Set<String> credentials);
	
}

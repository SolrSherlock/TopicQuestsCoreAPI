/**
 * 
 */
package org.topicquests.model;

import java.util.Set;

import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.api.IQueryIterator;
import org.topicquests.model.api.IQueryModel;
import org.topicquests.common.QueryUtil;

/**
 * @author park
 *
 */
public class QueryModel implements IQueryModel {
	private Environment environment;
	private final String labelQuery = ITopicQuestsOntology.LABEL_PROPERTY;
	private final String detailsQuery = ITopicQuestsOntology.DETAILS_PROPERTY;
	private final String instanceQuery = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":";
	private final String subClassQuery = ITopicQuestsOntology.SUBCLASS_OF_PROPERTY_TYPE+":";

	/**
	 * 
	 */
	public QueryModel(Environment env) {
		environment = env;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryModel#listNodesByLabel(java.lang.String, java.lang.String, int, java.util.Set)
	 */
	@Override
	public IQueryIterator listNodesByLabel(String label, String language,
			int count, Set<String> credentials) {
		IQueryIterator itr = new QueryIterator(environment);
		
		itr.start(makeField(labelQuery,language)+":"+QueryUtil.escapeQueryCulprits(label), count, credentials);
		return itr;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryModel#listNodesByDetails(java.lang.String, java.lang.String, int, java.util.Set)
	 */
	@Override
	public IQueryIterator listNodesByDetails(String details, String language,
			int count, Set<String> credentials) {
		IQueryIterator itr = new QueryIterator(environment);
		itr.start(makeField(detailsQuery,language)+":"+QueryUtil.escapeQueryCulprits(details), count, credentials);
		return itr;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryModel#listTuplesByRelation(java.lang.String, int, java.util.Set)
	 */
	@Override
	public IQueryIterator listTuplesByRelation(String relationType, int count,
			Set<String> credentials) {
		IQueryIterator itr = new QueryIterator(environment);
		itr.start(instanceQuery+relationType, count, credentials);
		return itr;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryModel#listNodeInstances(java.lang.String, int, java.util.Set)
	 */
	@Override
	public IQueryIterator listNodeInstances(String nodeTypeLocator, int count,
			Set<String> credentials) {
		IQueryIterator itr = new QueryIterator(environment);
		itr.start(instanceQuery+nodeTypeLocator, count, credentials);
		return itr;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryModel#listNodeSubclasses(java.lang.String, int, java.util.Set)
	 */
	@Override
	public IQueryIterator listNodeSubclasses(String superClassLocator,
			int count, Set<String> credentials) {
		IQueryIterator itr = new QueryIterator(environment);
		itr.start(subClassQuery+superClassLocator, count, credentials);
		return itr;
	}

	/**
	 * Calculate the appropriate Solr field
	 * @param fieldBase
	 * @param language
	 * @return
	 */
	String makeField(String fieldBase, String language) {
		//TODO sort this out: do we still need it for non-SOLR topic maps?
		String result = fieldBase;
		if (!language.equals("en"))
			result += language;
		return result;
	}

}

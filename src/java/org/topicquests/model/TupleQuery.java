/**
 * 
 */
package org.topicquests.model;

import java.util.Set;

import org.topicquests.common.api.IResult;
import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.api.IDataProvider;
import org.topicquests.model.api.ITupleQuery;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class TupleQuery implements ITupleQuery {
	private LoggingPlatform log = LoggingPlatform.getLiveInstance();
	private IDataProvider database;

	/**
	 * 
	 */
	public TupleQuery(IDataProvider db) {
		database = db;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listTuplesBySubject(java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listTuplesBySubject(String subjectLocator, int start,
			int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.TUPLE_SUBJECT_PROPERTY+":"+subjectLocator;
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listTuplesByObjectLocator(java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listTuplesByObjectLocator(String objectLocator, int start,
			int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.TUPLE_OBJECT_PROPERTY+":"+objectLocator;
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listTuplesByPredTypeAndObject(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listTuplesByPredTypeAndObject(String predType, String obj,
			int start, int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+predType+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_PROPERTY+":"+obj; // an object
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listTuplesBySubjectAndPredType(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listTuplesBySubjectAndPredType(String subjectLocator,
			String predType, int start, int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.TUPLE_SUBJECT_PROPERTY+":"+subjectLocator+
				" AND "+ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+predType;
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listSubjectNodesByObjectAndRelation(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listSubjectNodesByObjectAndRelation(String objectLocator,
			String relationLocator, int start, int count,
			Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+relationLocator+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_PROPERTY+":"+objectLocator; // an object
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		//TODO THIS DOES NOT RETURN THE NODES, JUST THE TUPLES
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listObjectNodesBySubjectAndRelation(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listObjectNodesBySubjectAndRelation(String subjectLocator,
			String relationLocator, int start, int count,
			Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+relationLocator+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_TYPE_PROPERTY+":"+ITopicQuestsOntology.NODE_TYPE+ //require only nodes, not literals
				" AND "+ITopicQuestsOntology.TUPLE_SUBJECT_PROPERTY+":"+subjectLocator; // a subject
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		//TODO THIS DOES NOT RETURN THE NODES, JUST THE TUPLES
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listObjectNodesBySubjectAndRelationAndScope(java.lang.String, java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listObjectNodesBySubjectAndRelationAndScope(
			String subjectLocator, String relationLocator, String scopeLocator,
			int start, int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+relationLocator+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_TYPE_PROPERTY+":"+ITopicQuestsOntology.NODE_TYPE+ //require only nodes, not literals
				" AND "+ITopicQuestsOntology.TUPLE_SUBJECT_PROPERTY+":"+subjectLocator + // a subject
				" AND "+ITopicQuestsOntology.SCOPE_LIST_PROPERTY_TYPE+":"+scopeLocator; // the scope
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString,start, count, credentials);
		//TODO THIS DOES NOT RETURN THE NODES, JUST THE TUPLES
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listSubjectNodesByObjectAndRelationAndScope(java.lang.String, java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listSubjectNodesByObjectAndRelationAndScope(
			String objectLocator, String relationLocator, String scopeLocator,
			int start, int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+relationLocator+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_PROPERTY+":"+objectLocator; // an object
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString, start, count, credentials);
		//TODO THIS DOES NOT RETURN THE NODES, JUST THE TUPLES
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listSubjectNodesByRelationAndObjectRole(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listSubjectNodesByRelationAndObjectRole(
			String relationLocator, String objectRoleLocator, int start,
			int count, Set<String> credentials) {
		String queryString = ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE+":"+relationLocator+ //the relation
				" AND "+ITopicQuestsOntology.TUPLE_OBJECT_ROLE_PROPERTY+":"+objectRoleLocator; // an object
		//NOTE: we need to run this as an iterator
		IResult result = database.runQuery(queryString, start, count, credentials);
		//TODO THIS DOES NOT RETURN THE NODES, JUST THE TUPLES
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listSubjectNodesByRelationAndSubjectRole(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listSubjectNodesByRelationAndSubjectRole(
			String relationLocator, String subjectRoleLocator, int start,
			int count, Set<String> credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listObjectNodesByRelationAndSubjectRole(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listObjectNodesByRelationAndSubjectRole(
			String relationLocator, String subjectRoleLocator, int start,
			int count, Set<String> credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.ITupleQuery#listObjectNodesByRelationAndObjectRole(java.lang.String, java.lang.String, int, int, java.util.Set)
	 */
	@Override
	public IResult listObjectNodesByRelationAndObjectRole(
			String relationLocator, String objectRoleLocator, int start,
			int count, Set<String> credentials) {
		// TODO Auto-generated method stub
		return null;
	}

}

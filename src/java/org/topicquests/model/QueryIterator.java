/**
 * 
 */
package org.topicquests.model;

import java.util.Set;

import org.topicquests.common.api.IResult;
import org.topicquests.model.api.IDataProvider;
import org.topicquests.model.api.IQueryIterator;

/**
 * @author park
 *
 */
public class QueryIterator implements IQueryIterator {
	private Environment environment;
	private IDataProvider database;
	private String _query;
	private int _count;
	private int _cursor;
	private Set<String>_credentials;

	/**
	 * 
	 */
	public QueryIterator(Environment env) {
		environment = env;
		database = environment.getDataProvider();
		
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryIterator#start(java.lang.String, int, java.util.Set)
	 */
	@Override
	public void start(String queryString, int hitCount, Set<String> credentials) {
		_query = queryString;
		_count = hitCount;
		_cursor = 0;
		_credentials = credentials;		
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryIterator#reset()
	 */
	@Override
	public void reset() {
		_cursor = 0;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryIterator#next()
	 */
	@Override
	public IResult next() {
		IResult result = runQuery();
		_cursor += _count;
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.IQueryIterator#previous()
	 */
	@Override
	public IResult previous() {
		IResult result = runQuery();
		_cursor -= _count;
		if (_cursor < 0)
			_cursor = 0;
		return result;
	}
	
	private IResult runQuery() {
		return database.runQuery(_query, _cursor, _count, _credentials);
	}

}

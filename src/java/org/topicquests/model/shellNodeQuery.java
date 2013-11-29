/*
 * Copyright 2013, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.model;

import java.util.*;

import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.model.api.IDataProvider;
import org.topicquests.model.api.INode;
import org.topicquests.model.api.INodeQuery;
import org.topicquests.model.api.IPredicate;
import org.topicquests.model.api.ITuple;
import org.topicquests.model.api.ITupleQuery;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 * <p>Following tinkerpop blueprints DefaultVertexQuery</p>
 * <p>NOTE: this is a "shell" since it has unimplemented methods.
 * It serves as a body of hints about how to implement the interface.
 * Mostly, querys need to be implemented</p>
 */
public class shellNodeQuery implements INodeQuery {
    private static final String[] EMPTY_LABELS = new String[]{};
	private LoggingPlatform log = LoggingPlatform.getLiveInstance();
	private IDataProvider database;
	private INode me;
    public List<HasContainer> hasContainers = new ArrayList<HasContainer>();
    private List<String>foundTuples = null;
    private List<ITuple>filteredTuples = null;
    private List<INode>filteredNodes = null;
    public String[] labels = EMPTY_LABELS;
    private boolean isFiltered = false;


	/**
	 * @param n
	 * @param dp
	 */
	public shellNodeQuery(INode n, IDataProvider dp) {
		me = n;
		this.database = dp;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#tuples()
	 */
	@Override
	public IResult tuples(Set<String> credentials) {
		IResult result = new ResultPojo();
		if (!isFiltered) {
			IResult r = executeQuery(credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			isFiltered = true;
		}
		result.setResultObject(filteredTuples);
		return result;
	}
	
	private IResult executeQuery(Set<String> credentials) {
		IResult result = new ResultPojo();
		filteredTuples = new ArrayList<ITuple>();
		if (foundTuples != null && !foundTuples.isEmpty() && !hasContainers.isEmpty()) {
			filteredTuples = new ArrayList<ITuple>();
			HasContainer x;
			Iterator<String>itr = foundTuples.iterator();
			Iterator<HasContainer>itx;
			IResult r;
			ITuple t;
			String lox;
			while (itr.hasNext()) {
				lox = itr.next();
				r = database.getTuple(lox, credentials);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				if (r.getResultObject() != null) {
					t = (ITuple)r.getResultObject();
					itx = hasContainers.iterator();
					while (itx.hasNext()) {
						x = itx.next();
						if (x.isLegal(t))
							filteredTuples.add(t);
					}
				} else {
					result.addErrorString("shellNodeQuery missing tuple for "+lox);
				}
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#nodes()
	 */
	@Override
	public IResult nodes(Set<String> credentials) {
		IResult result = new ResultPojo();
		if (!isFiltered) {
			IResult r = executeQuery(credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			isFiltered = true;
		}
		if (filteredTuples != null && !filteredTuples.isEmpty()) {
			INode n;
			ITuple t;
			IResult r;
			filteredNodes = new ArrayList<INode>();
			result.setResultObject(filteredNodes);
			Iterator<ITuple>itr = filteredTuples.iterator();
			String lox;
			while (itr.hasNext()) {
				t = itr.next();
				if (!t.getSubjectLocator().equals(me.getLocator()))
					lox = t.getSubjectLocator();
				else
					lox = t.getObject();
				r = database.getNode(lox, credentials);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				if (r.getResultObject() != null)
					filteredNodes.add((INode)r.getResultObject());
				else
					result.addErrorString("shellNodeQuery missing node for "+lox);
			}
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#count()
	 */
	@Override
	public long count() {
		//cardinality of this node's tuples as filtered by labels
		if (foundTuples != null)
			return (long)foundTuples.size();
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#nodeLocators()
	 */
	@Override
	public IResult nodeLocators(Set<String> credentials) {
		IResult result = new ResultPojo();
		if (foundTuples == null)
			result.addErrorString("No tuples found in this query");
		//that error means that somebody didn't setLabels
		else {
			List<String>lox = new ArrayList<String>();
			result.setResultObject(lox);
			IResult r;
			//here, we must fetch every tuple and get its subject or object
			ITuple t;
			Iterator<String>itr = foundTuples.iterator();
			while(itr.hasNext()) {
				r = database.getTuple(itr.next(), null);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				if (r.getResultObject() != null) {
					t = (ITuple)r.getResultObject();
					if (!t.getSubjectLocator().equals(me.getLocator()))
						lox.add(t.getSubjectLocator());
					else
						lox.add(t.getObject());
				}
			}
			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#has(java.lang.String, java.lang.Object)
	 */
	@Override
	public INodeQuery tupleHas(String key, Object value) {
        this.hasContainers.add(new HasContainer(key, Compare.EQUAL, value));
        return this;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#hasNot(java.lang.String, java.lang.Object)
	 */
	@Override
	public INodeQuery tupleHasNot(String key, Object value) {
        this.hasContainers.add(new HasContainer(key, Compare.NOT_EQUAL, value));
        return this;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#has(java.lang.String, java.lang.Object)
	 */
	@Override
	public INodeQuery tupleHas(String key) {
        this.hasContainers.add(new HasContainer(key, Compare.NOT_EQUAL, null));
        return this;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#hasNot(java.lang.String, java.lang.Object)
	 */
	@Override
	public INodeQuery tupleHasNot(String key) {
        this.hasContainers.add(new HasContainer(key, Compare.EQUAL, null));
        return this;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeQuery#has(java.lang.String, org.topicquests.model.api.IPredicate, java.lang.Object)
	 */
	@Override
	public INodeQuery tupleHas(String key, IPredicate predicate, Object value) {
        this.hasContainers.add(new HasContainer(key, predicate, value));
        return this;
	}

    ////////////////////


    protected class HasContainer {
        public String key;
        public Object value;
        public IPredicate predicate;

        public HasContainer(final String key, final IPredicate predicate, final Object value) {
            this.key = key;
            this.value = value;
            this.predicate = predicate;
        }

        public boolean isLegal(final INode element) {
            return this.predicate.evaluate(element.getProperty(this.key), this.value);
        }
    }

	@Override
	public INodeQuery setRelationType(String relationType, int start,
			int count, Set<String> credentials) {
		// TODO Auto-generated method stub
		return null;
	}




}

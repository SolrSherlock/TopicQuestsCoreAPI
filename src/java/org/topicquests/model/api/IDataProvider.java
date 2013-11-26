/*
 * Copyright 2012, TopicQuests
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
package org.topicquests.model.api;


import org.topicquests.common.api.IMergeRuleMethod;
import org.topicquests.common.api.IResult;
import org.topicquests.model.Environment;

/**
 * @author park
 * Serves as a core API,to be extended for different databases
 */
public interface IDataProvider extends ITopicDataProvider {
	
	/**
	 * This allows us to create plug-in dataproviders
	 * @param env
	 * @param cachesize
	 * @return
	 */
	IResult init(Environment env, int cachesize);
	
	  /**
	   * Return the {@link INodeModel} installed in this system
	   * @return
	   */
	  INodeModel getNodeModel();
	
	  /**
	   * Return the {@link ITupleQuery installed in this system
	   * @return
	   */
  	  ITupleQuery getTupleQuery();
	
	  /**
	   * Remove an {@link INode} from the internal cache
	   * @param nodeLocator
	   */
	  void removeFromCache(String nodeLocator);
	
	  /**
	   * Returns a UUID String
	   * @return
	   */
	  String getUUID();
	  
	  /**
	   * Returns a UUID String with a <code>prefix</code>
	   * @param prefix
	   * @return
	   */
	  String getUUID_Pre(String prefix);
	  
	  /**
	   * Return a UUID String with a <code>suffix</code>
	   * @param suffix
	   * @return
	   */
	  String getUUID_Post(String suffix);
	  
  
	  /**
	   * <p>Create an {@link INode} which represents a merge rule based on the
	   * given {@link IMergeRuleMethod}</p>
	   * <p>This rule's locator, referenced in the method, will be added as a
	   * scope to any merge tuple when merge events occur</p>
	   * @param theMethod
	   * @return
	   */
	  IResult createMergeRule(IMergeRuleMethod theMethod);
	  
}
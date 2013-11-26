/**
 * 
 */
package org.topicquests.model;

import java.util.*;

import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.common.api.ITopicQuestsOntology;
import org.topicquests.model.Node;
import org.topicquests.model.api.IDataProvider;
import org.topicquests.model.api.IMergeImplementation;
import org.topicquests.model.api.INode;
import org.topicquests.model.api.INodeModel;
import org.topicquests.model.api.ITuple;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class NodeModel implements INodeModel {
	private LoggingPlatform log = LoggingPlatform.getLiveInstance();
	private IDataProvider database;
	private IMergeImplementation merger;

	/**
	 * 
	 */
	public NodeModel(IDataProvider db, IMergeImplementation m) {
		database = db;
		merger = m;
		if (merger != null)
			merger.setNodeModel(this);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newNode(String locator, String label, String description,
			String lang, String userId, String smallImagePath,
			String largeImagePath, boolean isPrivate) {
		INode n = new Node();
		System.out.println("NodeModel.newNode- "+locator);
		IResult result = new ResultPojo();
		result.setResultObject(n);
		n.setLocator(locator);
		n.setCreatorId(userId);
		Date d = new Date();
		n.setDate(d); 
		n.setLastEditDate(d);
		if (label != null)
			n.addLabel(label, lang, userId, false);
		
		if (smallImagePath != null)
			n.setSmallImage(smallImagePath);
		if (largeImagePath != null)
			n.setImage(largeImagePath);
		if (description != null)
			n.addDetails(description, lang, userId, false);
		n.setIsPrivate(isPrivate);
		System.out.println("NodeModel.newNode+ "+locator);		
		return result;	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newNode(String label, String description, String lang,
			String userId, String smallImagePath, String largeImagePath,
			boolean isPrivate) {
		IResult result = newNode(database.getUUID(),label,description,lang,userId,smallImagePath,largeImagePath,isPrivate);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newSubclassNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newSubclassNode(String locator, String superclassLocator,
			String label, String description, String lang, String userId,
			String smallImagePath, String largeImagePath, boolean isPrivate) {
		IResult result = newNode(locator,label,description,lang,userId,smallImagePath,largeImagePath,isPrivate);
		INode n = (INode)result.getResultObject();
		n.addSuperclassId(superclassLocator);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newSubclassNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newSubclassNode(String superclassLocator, String label,
			String description, String lang, String userId,
			String smallImagePath, String largeImagePath, boolean isPrivate) {
		IResult result = newNode(label,description,lang,userId,smallImagePath,largeImagePath,isPrivate);
		INode n = (INode)result.getResultObject();
		n.addSuperclassId(superclassLocator);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newInstanceNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newInstanceNode(String locator, String typeLocator,
			String label, String description, String lang, String userId,
			String smallImagePath, String largeImagePath, boolean isPrivate) {
		IResult result = newNode(locator,label,description,lang,userId,smallImagePath,largeImagePath,isPrivate);
		INode n = (INode)result.getResultObject();
		n.setNodeType(typeLocator);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#newInstanceNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public IResult newInstanceNode(String typeLocator, String label,
			String description, String lang, String userId,
			String smallImagePath, String largeImagePath, boolean isPrivate) {
		IResult result = newNode(label,description,lang,userId,smallImagePath,largeImagePath,isPrivate);
		INode n = (INode)result.getResultObject();
		n.setNodeType(typeLocator);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#updateNode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.util.Set)
	 */
	@Override
	public IResult updateNode(String nodeLocator, String updatedLabel,
			String updatedDetails, String language, String oldLabel,
			String oldDetails, String userId, boolean isLanguageAddition,
			Set<String> credentials) {
		IResult result = database.getNode(nodeLocator, credentials);
		if (result.getResultObject() != null) {
			INode n = (INode)result.getResultObject();
			if ((updatedLabel != null && !updatedLabel.equals("")) &&
				(oldLabel == null || oldLabel.equals("")))
				n.addLabel(updatedLabel, language, userId, isLanguageAddition);
			if ((updatedDetails != null && !updatedLabel.equals("")) && 
				(oldDetails == null || oldLabel.equals("")))
				n.addDetails(updatedDetails, language, userId, isLanguageAddition);
			List<String>val;
			String field;
			if (oldLabel != null) {
				field = n.makeField(ITopicQuestsOntology.LABEL_PROPERTY, language);
				val = (List<String>)n.getProperty(field);
				val.remove(oldLabel);
				val.add(updatedLabel);
				n.setProperty(field, val);
			}
			if (oldDetails != null) {
				field = n.makeField(ITopicQuestsOntology.DETAILS_PROPERTY, language);
				val = (List<String>)n.getProperty(field);
				val.remove(oldDetails);
				val.add(updatedDetails);
				n.setProperty(field, val);
			}
			return database.updateNode(n);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#changePropertyValue(org.topicquests.model.api.INode, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult changePropertyValue(INode node, String key, String newValue) {
		IResult result = null;
		Object o = node.getProperty(key);
		if (o instanceof List) {
			//not a real case so we do nothing here
			result = new ResultPojo();
		} else {
			String v = (String)o;
			if (!v.equals(newValue)) {
				node.setProperty(key, newValue);
				result = database.putNode(node);
				database.removeFromCache(node.getLocator());
			} else
				result = new ResultPojo();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#addPropertyValueInList(org.topicquests.model.api.INode, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult addPropertyValueInList(INode node, String key,
			String newValue) {
		IResult result = null;
		String sourceNodeLocator = node.getLocator();
		Map<String,Object> myMap = node.getProperties();
		List<String>values = makeListIfNeeded( myMap.get(key));
		if (!values.contains(newValue)) {
			values.add(newValue);
			result = database.putNode(node);
			database.removeFromCache(sourceNodeLocator);
		} else
			result = new ResultPojo();
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#relateNodes(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public IResult relateNodes(String sourceNodeLocator,
			String targetNodeLocator, String relationTypeLocator,
			String userId, String smallImagePath, String largeImagePath,
			boolean isTransclude, boolean isPrivate) {
		IResult result = new ResultPojo();
		Set<String> credentials = getDefaultCredentials(userId);
		//fetch the source actor node
		IResult x = database.getNode(sourceNodeLocator, credentials);
		INode nxA = (INode)x.getResultObject();
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		if (nxA != null) {
			//fetch target actor node
			IResult y = database.getNode(targetNodeLocator,credentials);
			INode nxB = (INode)y.getResultObject();
			if (y.hasError())
				result.addErrorString(y.getErrorString());
			if (nxB != null) {
				y = relateExistingNodes(nxA, nxB, relationTypeLocator,userId,smallImagePath,largeImagePath,isTransclude,isPrivate);
				if (y.hasError())
					result.addErrorString(y.getErrorString());
				result.setResultObject(y.getResultObject());
			}
				
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#relateExistingNodes(org.topicquests.model.api.INode, org.topicquests.model.api.INode, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public IResult relateExistingNodes(INode sourceNode, INode targetNode,
			String relationTypeLocator, String userId, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate) {
		IResult result = new ResultPojo();
		String signature = sourceNode.getLocator()+relationTypeLocator+targetNode.getLocator();
		ITuple t = (ITuple)this.newInstanceNode(relationTypeLocator, relationTypeLocator, 
				sourceNode.getLocator()+" "+relationTypeLocator+" "+targetNode.getLocator(), "en", userId, smallImagePath, largeImagePath, isPrivate).getResultObject();
		t.setIsTransclude(isTransclude);
		t.setObject(targetNode.getLocator());
		t.setObjectType(ITopicQuestsOntology.NODE_TYPE);
		t.setSubjectLocator(sourceNode.getLocator());
		t.setSubjectType(ITopicQuestsOntology.NODE_TYPE);
		t.setSignature(signature);
		String tLoc = t.getLocator();
		if (isPrivate) {
			sourceNode.addRestrictedTuple(tLoc);
			targetNode.addRestrictedTuple(tLoc);
		} else {
			sourceNode.addTuple(tLoc);
			targetNode.addTuple(tLoc);
		}
		IResult x = database.putNode(sourceNode);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		x = database.putNode(targetNode);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		database.putNode(t);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		log.logDebug("NodeModel.relateNewNodes "+sourceNode.getLocator()+" "+targetNode.getLocator()+" "+t.getLocator()+" | "+result.getErrorString());
		result.setResultObject(tLoc);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#relateNewNodes(org.topicquests.model.api.INode, org.topicquests.model.api.INode, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public IResult relateNewNodes(INode sourceNode, INode targetNode,
			String relationTypeLocator, String userId, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate) {
		IResult result = new ResultPojo();
		String signature = sourceNode.getLocator()+relationTypeLocator+targetNode.getLocator();
		ITuple t = (ITuple)this.newInstanceNode(relationTypeLocator, relationTypeLocator, 
				sourceNode.getLocator()+" "+relationTypeLocator+" "+targetNode.getLocator(), "en", userId, smallImagePath, largeImagePath, isPrivate).getResultObject();
		t.setIsTransclude(isTransclude);
		t.setObject(targetNode.getLocator());
		t.setObjectType(ITopicQuestsOntology.NODE_TYPE);
		t.setSubjectLocator(sourceNode.getLocator());
		t.setSubjectType(ITopicQuestsOntology.NODE_TYPE);
		t.setSignature(signature);
		String tLoc = t.getLocator();
		if (isPrivate) {
			sourceNode.addRestrictedTuple(tLoc);
			targetNode.addRestrictedTuple(tLoc);
		} else {
			sourceNode.addTuple(tLoc);
			targetNode.addTuple(tLoc);
		}
		IResult x = database.putNode(sourceNode);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		x = database.putNode(targetNode);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		database.putNode(t);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		log.logDebug("NodeModel.relateNewNodes "+sourceNode.getLocator()+" "+targetNode.getLocator()+" "+t.getLocator()+" | "+result.getErrorString());
		result.setResultObject(tLoc);
		return result;	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#assertMerge(java.lang.String, java.lang.String, java.util.Map, double, java.lang.String)
	 */
	@Override
	public IResult assertMerge(String sourceNodeLocator,
			String targetNodeLocator, Map<String, Double> mergeData,
			double mergeConfidence, String userLocator) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#assertPossibleMerge(java.lang.String, java.lang.String, java.util.Map, double, java.lang.String)
	 */
	@Override
	public IResult assertPossibleMerge(String sourceNodeLocator,
			String targetNodeLocator, Map<String, Double> mergeData,
			double mergeConfidence, String userLocator) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#assertUnmerge(java.lang.String, org.topicquests.model.api.INode, java.util.Map, double, java.lang.String)
	 */
	@Override
	public IResult assertUnmerge(String sourceNodeLocator,
			INode targetNodeLocator, Map<String, Double> mergeData,
			double mergeConfidence, String userLocator) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#removeNode(java.lang.String)
	 */
	@Override
	public IResult removeNode(String locator) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#getDefaultCredentials(java.lang.String)
	 */
	@Override
	public Set<String> getDefaultCredentials(String userId) {
		Set<String>result = new HashSet<String>();
		result.add(userId);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.model.api.INodeModel#dateToSolrDate(java.util.Date)
	 */
	@Override
	public String dateToSolrDate(Date d) {
		throw new RuntimeException("NodeModel.dateToSolrDate not implemented");
	}
	/**
	 * 
	 * @param o
	 * @return can return <code>null</code>
	 */
	List<String> makeListIfNeeded(Object o) {
		if (o == null)
			return null;
		List<String>result = null;
		if (o instanceof List)
			result = (List<String>)o;
		else {
			result = new ArrayList<String>();
			result.add((String)o);
		}
		return result;
	}

}
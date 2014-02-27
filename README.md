TopicQuestsCoreAPI
==================
**This project is replaced with [https://github.com/SolrSherlock/OpenSherlockCoreAPI](https://github.com/SolrSherlock/OpenSherlockCoreAPI "OpenSherlockCoreAPI")**

Core topic map APIs which can support topic map projects of various kinds. These were abstracted away from the code in SolrPlatformCore to allow for other developments.

Status: *alpha*<br/>

INodeQuery is based loosely on the TinkerPop Blueprints VertexQuery; with this, you can take any node, and walk out its list of tuples to find other nodes.

## Update History ##
20140209 Numerous cleanup changes, some re-alignment of code to suit advanced topic mapping needs

20131129 Added licenses to files; Updated ITupleQuery and INodeQuery, added features to support INodeQuery. Renamed several implementations to "shell"... since they are just hints at how those implementations can be performed; at issue is the query string necessary: that will vary according to the storage implementation.

20131126 Fixed bug in Node date handling, filled in some code.

20131125 First commit

## ToDo ##
Mavenize the project<br/>
Create a full unit test suite

## License ##
Apache 2


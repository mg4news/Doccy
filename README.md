# Doccy
Scala + MongoDB based document tracker

- Partly an exercise for me to get a handle on Mongo
- Bit of an academic exercise about ClassTag
- Partly a useful tool to track documents

This application does not actually put the documents themselves in the DB. It issues an ID, and tracks the ID in the database. The user embeds the ID in the document name (could be the entire document name, or part of it). Then you simply use the system to search for documents containing "ID"

## May 2019 Update
Added a web service based on akka-http. Next step, develop a web frontend for the system. Time to learn typescript and Vue or React...

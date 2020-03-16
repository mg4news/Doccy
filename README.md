# Doccy
Scala + MongoDB based document tracker

- Partly an exercise for me to get a handle on Mongo
- Bit of an academic exercise about ClassTag
- Partly a useful tool to track documents

This application does not actually put the documents themselves in the DB. It issues an ID, and tracks the ID in the database. The user embeds the ID in the document name (could be the entire document name, or part of it). Then you simply use the system to search for documents containing "ID"

Future features would be:
- a web interface to the app
- link with underlying system, to allow search from the app

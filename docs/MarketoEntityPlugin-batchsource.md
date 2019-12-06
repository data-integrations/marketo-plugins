# Marketo Entity Batch Source

Description
-----------
This plugin is used to query Leads or Activities entities for specified date range from Marketo.

Properties
----------
### General

**Reference Name:** Name used to uniquely identify this source for lineage, annotating metadata, etc.

**Rest API endpoint:** Marketo rest API endpoint, unique for each client.

**Entity Type:** Type of entity.
### Authentication

**Client ID:** Client ID.

**Client Secret:** Client secret.

### Advanced

**Splits Count:** Parallel splits count.

**Max Results Per Page:** Maximum number of records to retrieve by single request.

API limits notes
----------------
All entities will be fetched at once. 

Total requests count depends on **Max Results Per Page**, leave it with default
value(200 - maximum possible value) to minimize risk of reaching limits.

Default request limit is 50000 per day, this will allow to fetch ~10.000.000 entities, but you must consider this limit
if there are several pipelines or other applications that uses API.
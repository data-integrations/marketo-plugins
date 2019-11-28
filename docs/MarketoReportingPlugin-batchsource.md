# Marketo Reporting Batch Source

Description
-----------
This plugin is used to query Leads or Activities entities for specified date range from Marketo.

Properties
----------
### General

**Reference Name:** Name used to uniquely identify this source for lineage, annotating metadata, etc.

**Rest API endpoint:** Marketo rest API endpoint, unique for each client.
### Authentication

**Client ID:** Client ID.

**Client Secret:** Client secret.

### Report

**Report Type:** Type of report. One of 'leads' or 'activities'.

**Start Date:** Start date of report. In ISO 8601 format(1997-07-16T19:20:30+01:00).

**End Date:** End date of report. In ISO 8601 format(1997-07-16T19:20:30+01:00).
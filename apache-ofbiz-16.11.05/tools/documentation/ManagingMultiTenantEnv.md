

## Managing Multi-Tenant Environment

The steps in this document guides the developer/administrator to manage the multi-tenant environment on an ongoing basis. These are needed to upgrade/update either Schema changes or Seed data changes to tenants
> This document assumes you have following MultiTenantSetup.md document and multi-tenant environment is working fine.

### What kind of changes?

Two main kind of changes needs to be managed:
1. Entity/Schema change: when we add/update new entity, or add/update fields in existing entity
2. Add/modify seed data

### Updating Template Database

~~~
gradlew loadTenant -PtenantId=template -PtenantReaders=seed,seed-initial
~~~
This command will update any entity changes, and also load latest seed and seed-initial data on Template database.


### Updating Tenant Database


~~~
gradlew loadTenant -PtenantId=<tenantId> -PtenantReaders=seed,seed-initial
~~~
For the given tenantId, latest entit changes and seed data will be updated.

### Bulk-Update Tenant Databases

To bulk update all tenants, we will need to build a service which can invoked to perform this action on all tenant databases.
To-Be-Done

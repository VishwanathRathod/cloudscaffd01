
<!-- Sidebar -->
<div class="bg-dark border-right" id="sidebar-wrapper" >
  <div class="sidebar-heading">AutoPatt Admin</div>
  <div class="list-group ">
    <a href="<@ofbizUrl>home</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'HOME'>active</#if>">
      <i class="material-icons">dashboard</i> Admin Home</a>

    <a href="<@ofbizUrl>customers</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'CUSTOMERS'>active</#if>">
      <i class="material-icons">pages</i> Customers</a>

    <a href="<@ofbizUrl>manage_products</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_PRODUCTS'>active</#if>">
    <i class="material-icons">account_tree</i> Manage Plans</a>

    <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_USERS'>active</#if>">
      <i class="material-icons">people</i> Admin Users</a>

    <a href="<@ofbizUrl>reports</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'REPORTS'>active</#if>">
      <i class="material-icons">assessment</i> Reports</a>

    <a href="<@ofbizUrl>reports</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'SYSTEM_SETTINGS'>active</#if>">
      <i class="fa fa-cogs sidebar-icons" aria-hidden="true"></i> System</a>

  </div>
</div>
<!-- /#sidebar-wrapper -->




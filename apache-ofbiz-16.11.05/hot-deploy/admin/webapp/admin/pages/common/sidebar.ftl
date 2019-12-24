
<!-- Sidebar -->
<div class="bg-dark border-right" id="sidebar-wrapper" >
  <div class="sidebar-heading">AutoPatt Admin</div>
  <div class="list-group ">
    <a href="<@ofbizUrl>home</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'HOME'>active</#if>">
      <i class="material-icons icon-indianred <#if currentViewId == 'HOME'>icon-color-active</#if>">dashboard</i> Admin Home</a>

    <a href="<@ofbizUrl>customers</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'CUSTOMERS'>active</#if>">
      <i class="material-icons icon-darkmagenta <#if currentViewId == 'CUSTOMERS'>icon-color-active</#if>">business</i> Customers</a>

    <a href="<@ofbizUrl>manage_plans</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_PLANS'>active</#if>">
    <i class="material-icons icon-darkgreen <#if currentViewId == 'MANAGE_PLANS'>icon-color-active</#if>">account_tree</i> Manage Plans</a>

    <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_USERS'>active</#if>">
      <i class="material-icons icon-orange <#if currentViewId == 'MANAGE_USERS'>icon-color-active</#if>">people</i> Admin Users</a>

    <a href="<@ofbizUrl>reports</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'REPORTS'>active</#if>">
      <i class="material-icons icon-chocolate <#if currentViewId == 'REPORTS'>icon-color-active</#if>">assessment</i> Reports</a>

    <a href="<@ofbizUrl>system_settings</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'SYSTEM_SETTINGS'>active</#if>">
      <i class="fa fa-cogs sidebar-icons icon-midnightblue <#if currentViewId == 'SYSTEM_SETTINGS'>icon-color-active</#if>" aria-hidden="true"></i> System</a>

  </div>
</div>
<!-- /#sidebar-wrapper -->




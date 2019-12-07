
<!-- Sidebar -->
<div class="bg-light border-right" id="sidebar-wrapper">
  <div class="sidebar-heading">
    <a href="<@ofbizUrl>home</@ofbizUrl>">AutoPatt Console</a></div>
  <div class="list-group">
    <a href="<@ofbizUrl>home</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'HOME'>active</#if>"><i class="fa fa-dashboard"></i> <span>Dashboard</span></a>
    <a href="<@ofbizUrl>productAPC</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'PRODUCT_APC'>active</#if>" ><i class="fa fa-cloud"></i> <span>APC</span></a>

    <#if security.hasEntityPermission("PORTAL", "_VIEW_USERS", session)>
    <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_USERS'>active</#if>" >
      <i class="fa fa-group"></i> <span>Users</span>
    </a>
    </#if>
  </div>

</div>
<!-- /#sidebar-wrapper -->



<!-- Sidebar -->
<div class="bg-light border-right" id="sidebar-wrapper">
  <div class="sidebar-heading">
    <a href="<@ofbizUrl>home</@ofbizUrl>">AutoPatt Console</a></div>
  <div class="list-group">
    <a href="<@ofbizUrl>home</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'HOME'>active</#if>">
      <i class="fa fa-dashboard sidebar-icons icon-indianred <#if currentViewId == 'HOME'>icon-color-active</#if>"></i> <span>Dashboard</span></a>
    <a href="<@ofbizUrl>productAPC</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'PRODUCT_APC'>active</#if>" >
      <i class="fa fa-cloud sidebar-icons icon-green <#if currentViewId == 'PRODUCT_APC'>icon-color-active</#if>"></i> <span>APC</span></a>

    <#if security.hasEntityPermission("PORTAL", "_VIEW_USERS", session)>
      <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_USERS'>active</#if>" >
        <i class="fa fa-group icon-orange sidebar-icons <#if currentViewId == 'MANAGE_USERS'>icon-color-active</#if>"></i> <span>Users</span>
      </a>
    </#if>

    <#if security.hasEntityPermission("PORTAL", "_ADMIN", session)>
      <a href="<@ofbizUrl>settings</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_SETTINGS'>active</#if>" >
        <i class="fa fa-cog sidebar-icons icon-midnightblue <#if currentViewId == 'MANAGE_SETTINGS'>icon-color-active</#if>"></i> <span>Settings</span>
      </a>
    </#if>
  </div>

</div>
<!-- /#sidebar-wrapper -->


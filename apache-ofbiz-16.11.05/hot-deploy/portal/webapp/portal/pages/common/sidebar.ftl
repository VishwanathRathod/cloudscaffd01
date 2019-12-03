
<!-- Sidebar -->
<div class="bg-light border-right" id="sidebar-wrapper">
  <div class="sidebar-heading">AutoPatt Console</div>
  <div class="list-group ">
    <a href="<@ofbizUrl>home</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'HOME'>active</#if>"><i class="material-icons">dashboard</i> Dashboard</a>
    <a href="<@ofbizUrl>productAPC</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'PRODUCT_APC'>active</#if>">
      <i class="material-icons">account_tree</i> APC</a>

    <#--  <a href="#" class="list-group-item list-group-item-action bg-light">Product2</a>
    <a href="#" class="list-group-item list-group-item-action bg-light">Product3</a>  -->
    <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="list-group-item list-group-item-action <#if currentViewId == 'MANAGE_USERS'>active</#if>">
    <i class="material-icons">people</i> Users
    </a>
  </div>
</div>
<!-- /#sidebar-wrapper -->


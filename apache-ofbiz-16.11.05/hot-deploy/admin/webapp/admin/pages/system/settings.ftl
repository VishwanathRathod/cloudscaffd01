<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>System Maintenance</h2>
            </div>
            <div class="col-sm-7">
                <#--<a href="<@ofbizUrl>new_user</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">&#xE147;</i> <span>Add New Admin</span></a>-->
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <div class="table-content">
        <ul class="nav nav-tabs" id="systemSettingsTabs" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="system-settings-tab" data-toggle="tab" href="#system_settings_tab" role="tab" 
                aria-controls="system_settings" aria-selected="true">Settings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="seed-data-tab" data-toggle="tab"
                   href="#system_seed_data_tab" role="tab" aria-controls="seed-data" aria-selected="false">Seed Data</a>
            </li>
            <#--<li class="nav-item">
                <a class="nav-link" id="subscriptions-tab" data-toggle="tab" href="#email_settings_tab" role="tab" 
                aria-controls="email_settings_tab" aria-selected="false">Emails Setting</a>
            </li>-->
        </ul>
        <div class="tab-content p-4" id="systemSettingsTabsContent">
            <div class="tab-pane fade show active" id="system_settings_tab" role="tabpanel" aria-labelledby="system_settings_tab">
                Basic System level settings
            </div>
            <div class="tab-pane fade" id="system_seed_data_tab" role="tabpanel" aria-labelledby="seed-data-tab">
                Manage system seed data
            </div>
            <div class="tab-pane fade" id="email_settings_tab" role="tabpanel" aria-labelledby="email-settings-tab">
                Email settings
            </div>
        </div>
    </div>

</div>
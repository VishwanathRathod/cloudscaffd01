

<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>Manage <b>${organizationName!}</b> </h2>
            </div>
            <div class="col-sm-7">
                <a href="<@ofbizUrl>customers</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">keyboard_backspace</i> <span>Back</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <br/>
    <div class="table-content">

        <ul class="nav nav-tabs" id="customerTab" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="details-tab" data-toggle="tab" href="#customer_details" role="tab" aria-controls="customer_details" aria-selected="true">Details</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="employees-tab" data-toggle="tab" href="#customer_employees" role="tab" aria-controls="customer_employees" aria-selected="false">Employees</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="subscriptions-tab" data-toggle="tab" href="#customer_subscriptions" role="tab" aria-controls="customer_subscriptions" aria-selected="false">Subscriptions</a>
            </li>
        </ul>
        <div class="tab-content p-4" id="customerTabContent">
            <div class="tab-pane fade show active" id="customer_details" role="tabpanel" aria-labelledby="details-tab">
                ${screens.render("component://admin/widget/AdminCustomerScreens.xml#customer_details_tab")}
            </div>
            <div class="tab-pane fade" id="customer_employees" role="tabpanel" aria-labelledby="employees-tab">
                ${screens.render("component://admin/widget/AdminCustomerScreens.xml#customer_employees_tab")}
            </div>
            <div class="tab-pane fade" id="customer_subscriptions" role="tabpanel" aria-labelledby="subscriptions-tab">
                ${screens.render("component://admin/widget/AdminCustomerScreens.xml#customer_subscriptions_tab")}
            </div>
        </div>

    </div>

</div>



<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>Customers</h2>
            </div>
            <div class="col-sm-7">
                <a href="<@ofbizUrl>new_customer</@ofbizUrl>" class="btn btn-primary" title="Onboard New Customer"><i class="material-icons">&#xE147;</i> <span>New Customer</span></a>
                <a href="<@ofbizUrl>customers</@ofbizUrl>" class="btn btn-primary" title="Refresh"><i class="material-icons">refresh</i> <span>Refresh</span></a>
            </div>
        </div>
    </div>

    <#if requestParameters.createInitiated?? && requestParameters.createInitiated=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> Customer creation process has been initiated....
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>

    <div class="table-content">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Org Number</th>
                <th>Organization Name</th>
                <th>Date Created</th>
                <th>Subscription</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <#if tenantOrgParties?? && tenantOrgParties?size &gt; 0 >
                <#list tenantOrgParties as org>
                    <tr>
                        <td>${org_index + 1}</td>
                        <td>${org.tenantId!}</td>
                        <td><a href="<@ofbizUrl>manage_customer?orgPartyId=${org.orgPartyId!}</@ofbizUrl>"><i class="material-icons" style="font-size:1.6em;">business</i>
                                ${Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(delegator, org.orgPartyId!, false)}
                            </a></td>
                        <td>${org.createdStamp!?date}</td>
                        <td><span class="status text-success" >&bull;</span> <span>Active</span></td>
                        <td width="20%">
                            <a href="<@ofbizUrl>manage_customer?orgPartyId=${org.orgPartyId!}</@ofbizUrl>" class="btn btn-outline-primary" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                            <a href="#" class="btn btn-outline-danger" title="Disable" data-toggle="tooltip"><i class="material-icons">remove_circle</i></a>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr>
                    <td colspan="10">No customers onboarded yet.</td>
                </tr>
            </#if>


            </tbody>
        </table>
    </div>


</div>
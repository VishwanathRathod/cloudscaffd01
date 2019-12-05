

<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>New Customer</h2>
            </div>
            <div class="col-sm-7">
                <a href="<@ofbizUrl>customers</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">keyboard_backspace</i> <span>Back</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <br/>
    <div class="table-content">
        <div class="row">
            <div class="col-7">
                <form>
                    <div class="form-group">
                        <label for="organizationName">Organization Name</label>
                        <input type="text" class="form-control" id="organizationName" placeholder="XYZ Corporation" required="true" >
                    </div>

                    <div class="form-group">
                        <label for="organizationName">Organization Id</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="basic-addon1">https://</span>
                            </div>
                            <input type="text" class="form-control" placeholder="xyz-org" aria-label="" aria-describedby="basic-addon2" required="true">
                            <div class="input-group-append">
                                <span class="input-group-text" id="basic-addon2">.autopatt.com</span>
                            </div>
                        </div>
                    </div>

                    <div class="card form-group">
                        <div class="card-header">
                            Contact Details
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="contactFirstName">First Name</label>
                                        <input type="text" class="form-control" id="contactFirstName" placeholder="" required="true" >
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="contactFirstName">Last Name</label>
                                        <input type="text" class="form-control" id="contactFirstName" placeholder="" required="true">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="contactFirstName">Email Address</label>
                                        <input type="email" class="form-control" id="contactFirstName" placeholder="" required="true">
                                        <small id="emailHelp" class="form-text text-muted">This email will be used as username to login to console</small>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="contactFirstName">Password</label>
                                        <input type="password" class="form-control" id="contactFirstName" placeholder="" required="true">
                                        <small id="emailHelp" class="form-text text-muted">User will be forced to change password on first login</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <input type="checkbox" checked data-toggle="toggle" data-on="Yes" data-off="No" data-style="slow">
                        <span>Send Email notification to the organization contact person?</span>
                    </div>

                    <button type="submit" class="btn btn-primary"><i class="material-icons">check_box</i> Create Customer</button>
                    <a href="<@ofbizUrl>customers</@ofbizUrl>" class="btn btn-secondary">
                        <i class="material-icons">cancel</i> <span>Cancel</span></a>

                    <div>
                        <br/>
                        <small id="emailHelp" class="form-text text-muted">
                            <i>Note: Setting up a new organization might take few minutes. Please try to login to new organization console after few mins.</i>
                        </small>
                    </div>

                </form>
            </div>
        </div>
        <br/><br/><br/>
    </div>



</div>
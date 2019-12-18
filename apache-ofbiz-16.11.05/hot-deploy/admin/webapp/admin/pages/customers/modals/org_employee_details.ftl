

<form action="<@ofbizUrl>updateEmployee</@ofbizUrl>" method="post">
    <div class="col-md-12 my-3">
        <div class="form-group row required">
            <label for="fnamacc" class="col-sm-10 col-form-label">First Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value=" " name="firstname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="lnameacc" class="col-sm-10 col-form-label">Last Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value=" " name="firstname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="emailacc" class="col-sm-10 col-form-label"> E-mail </label>
            <div class="col-sm-10">
                <input type="text" class="form-control"  value=" " name="email"   required>
            </div>
        </div>
        <div class="form-group row">
            <label for="roleacc" class="col-sm-10 col-form-label">Role</label>
            <div class="col-sm-10">
                <select name="securityGroupId" class="form-control" required>
                    <option value="AP_PLANNER">Planner</option>
                    <option value="AP_APPROVER">Approver</option>
                    <option value="AP_DEPLOYER">Deployer</option>
                    <option value="AP_FULLADMIN">Administrator</option>
                </select>

            </div>
        </div>
    </div>
</form>

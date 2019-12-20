

<form action="<@ofbizUrl>updateEmployee</@ofbizUrl>" method="post">
    <input type="hidden" name="partyId" value="${partyId!}"/>
    <input type="hidden" name="orgPartyId" value="${orgPartyId!}"/>
    <div class="col-md-12 my-3">
        <div class="form-group row required">
            <label for="fnamacc" class="col-sm-10 col-form-label">First Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value=" ${employee.firstName}" name="firstname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="lnameacc" class="col-sm-10 col-form-label">Last Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value="${employee.lastName}" name="lastname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="emailacc" class="col-sm-10 col-form-label"> E-mail </label>
            <div class="col-sm-10">
                <input type="text" class="form-control"  value="${email}" name="email" readonly  required>
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
        <div class="modal-footer">

            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            <button type="submit" class="btn btn-primary">Update</button>

        </div>
    </div>
</form>


<div class="container-fluid">
    <div>&nbsp;</div>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-4">
                <h2>My Account </h2>
            </div>
            <div class="col-sm-8">
            </div>
        </div>
    </div>

    <form action="<@ofbizUrl>UpdateMyProfile</@ofbizUrl>" method="post">
        <div class="col-md-9 my-3">
            <div class="form-group row required">
                <label for="fnamacc" class="col-sm-2 col-form-label">First Name <span class="mandatory">*</span></label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" value="${person.firstName}" name="firstname" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="lnameacc" class="col-sm-2 col-form-label">Last Name <span class="mandatory">*</span></label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" value="${person.lastName}" name="lastname" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="emailacc" class="col-sm-2 col-form-label"> E-mail </label>
                <div class="col-sm-10">
                     <input type="text" class="form-control"  value="${email}" name="email"  readonly required>
                 </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-2">&nbsp;</div>
                <div class="col-sm-10">
                    <button type="submit" class="btn btn-primary ">Update</button>
                </div>
            </div>
        </div>
</div>
</form>
<style>
.mandatory{
    color:red;
}

</style>

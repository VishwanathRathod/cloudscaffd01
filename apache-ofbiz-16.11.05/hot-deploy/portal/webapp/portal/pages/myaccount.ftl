
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

    <form>
        <div class="col-md-8 my-4">
            <div class="form-group row required">
                <label for="fnameacc" class="col-sm-2 col-form-label">First Name <span class="mandatory">*</span></label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" value="${person.firstName}" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="lnameacc" class="col-sm-2 col-form-label">Last Name <span class="mandatory">*</span></label>
                <div class="col-sm-10">
                   <input type="text" class="form-control" value="${person.lastName}" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="emailacc" class="col-sm-2 col-form-label ">E-mail</label>
                <div class="col-sm-10">
                    <input type="email" class="form-control" readonly value="${email}">
                </div>
            </div>
            <div class="form-group row">
                <label for="roleacc" class="col-sm-2 col-form-label">Role</label>
                <div class="col-sm-10">
                   <input type="email" class="form-control" readonly>
                </div>
             </div>
            <div class="form-group row">
                <div class="col-sm-2">&nbsp;</div>
                    <div class="col-sm-10">
                      <button type="submit" class="btn btn-primary">Update</button>
                    </div>
                </div>
            </div>
        </div>
    </form>



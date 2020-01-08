  </div>
  <!-- /#wrapper -->

  <!-- Bootstrap core JavaScript -->
  <script src="../static/vendor/jquery/jquery.min.js"></script>
  <script src="https://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>


  <script src="../static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js"></script>

  <script src="../static/js/validations.js"></script>
  <script src="../static/js/ajax_requests.js"></script>
  <script src="../static/js/modal_popups.js"></script>
  <script src="../static/js/toast_utils.js"></script>
  <script src="../static/js/admin_users.js"></script>

  <!-- Menu Toggle Script -->
  <script>
    $(document).ready(function(){
      $("#menu-toggle").click(function(){
        $(".text").toggleClass("collapse");
        $("#wrapper").toggleClass("collapse");
      });
    });

    $(function() {
      setTimeout(function() {
        $("#page_loading").hide();
        $("#wrapper").show(300,"swing");
      }, 200);

      $("#password_eye").click(function() {
        togglePasswordField("password");
      });
      $("#newPassword_eye").click(function() {
        togglePasswordField("newPassword");
      });
      $("#newPasswordVerify_eye").click(function() {
        togglePasswordField("newPasswordVerify");
      });
    });

    function togglePasswordField(pwdFieldId){
      $("#"+pwdFieldId+"_eye").toggleClass('active');
      var pwdField = $("#"+pwdFieldId);
      if(pwdField.attr("type") === 'password') pwdField.attr("type","text");
      else pwdField.attr("type","password");
    }
  </script>

  <script>
    function getUrl (uri) {
      var appContext = "<@ofbizUrl>/</@ofbizUrl>";
      return appContext + uri;
    }
  </script>

  </body>
</html>

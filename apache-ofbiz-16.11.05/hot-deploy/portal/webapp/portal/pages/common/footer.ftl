  </div>
  <!-- /#wrapper -->

  <!-- Bootstrap core JavaScript -->
  <script src="../static/vendor/jquery/jquery.min.js"></script>
  <script src="../static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <script src="../static/js/portal_modals.js"></script>
  <script src="../static/js/portal_users.js"></script>

  <!-- Menu Toggle Script -->
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });

    $(function() {
      setTimeout(function() {
        $("#page_loading").hide();
        $("#wrapper").show(300,"swing");
      }, 200);
    });

    function getAppUrl (uri) {
      var appContext = "<@ofbizUrl>/</@ofbizUrl>";
      return appContext + uri;
    }

  </script>

</body>

</html>

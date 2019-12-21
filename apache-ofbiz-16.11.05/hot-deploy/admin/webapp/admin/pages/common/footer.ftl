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
  </script>

  <script>
    function getUrl (uri) {
      var appContext = "<@ofbizUrl>/</@ofbizUrl>";
      return appContext + uri;
    }
  </script>


  <script>
    $(document).ready(function(){

    });
  </script>

  </body>

</html>

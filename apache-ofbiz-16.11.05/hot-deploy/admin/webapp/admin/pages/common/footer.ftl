  </div>
  <!-- /#wrapper -->

  <!-- Bootstrap core JavaScript -->
  <script src="../static/vendor/jquery/jquery.min.js"></script>
    <script src="../static/vendor/jquery/jquery.slim.min.js"></script>

  <script src="../static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js"></script>
  <!-- Menu Toggle Script -->
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });

    $(function() {
      setTimeout(function() {
        $("#page_loading").hide();
        $("#wrapper").show("slow","swing");
      }, 400);
    });
  </script>

</body>

</html>

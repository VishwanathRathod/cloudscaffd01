  </div>
  <!-- /#wrapper -->

  <!-- Bootstrap core JavaScript -->
  <script src="../static/vendor/jquery/jquery.min.js"></script>
    <script src="../static/vendor/jquery/jquery.slim.min.js"></script>

  <script src="../static/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Menu Toggle Script -->
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>


  <script>
    $(function() {
      $('#deleteConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var deletingPartyId = button.data('party-id') // Extract info from data-* attributes
        var deletingPartyName = button.data('party-name');
        if(deletingPartyName == null) deletingPartyName = "";

        var modal = $(this)
        modal.find('#deletePartyName').text(deletingPartyName)
      })
  });

  </script>

</body>

</html>

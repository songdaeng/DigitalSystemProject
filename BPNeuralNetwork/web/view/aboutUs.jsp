
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/view/style.css">

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
      <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  
  </head>
<body>
<div class="row">

      <div id="side-bar" class="col s12 m4 l3"> <!-- Note that "m4 l3" was added -->
        <!-- Grey navigation panel
        
              This content will be:
          3-columns-wide on large screens,
          4-columns-wide on medium screens,
          12-columns-wide on small screens  -->
        

        <jsp:include page="/view/side-navigation-module.jsp"/>

      </div>
            
<!--        <ul id="slide-out" class="sidenav">
            <li><a class="sidenav-close" href="#!">Clicking this will close Sidenav</a></li>
        </ul>
        <a href="#" data-target="slide-out" class="sidenav-trigger"><i class="material-icons">menu</i></a>-->
      <div class="col s12 m8 l9 " id="backgroundImage"> <!-- Note that "m8 l9" was added -->

        
        <div class="row">
            <div class="card blue-grey darken-1 center">
              <div class="card-content white-text">
                  <h2><span class="card-title">ABOUT US </span></h2>
                <p>Thank you very much for viewing my project</p>
              </div>
            </div>
        </div>

  
        <div class="row" >
            <div class="card blue-grey darken-1">

            </div>
        </div>    

      </div>

    </div>
 
</body>

</html>


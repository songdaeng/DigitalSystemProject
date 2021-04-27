
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/view/style.css">

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
      <div class="col s12 m8 l9"> <!-- Note that "m8 l9" was added -->
        <!-- Teal page content
        
              This content will be:
          9-columns-wide on large screens,
          8-columns-wide on medium screens,
          12-columns-wide on small screens  -->
        
        <div class="row">
            <div class="card blue-grey darken-1 center">
              <div class="card-content white-text">
                <span class="card-title">Train backpropagation</span>
                <p>Enter variables to train the network</p>
              </div>
            </div>
            </div>
            <div class="row">
             <div id="line_top_x"></div>
            </div>

            <div class="row">
                <form action="${pageContext.request.contextPath}/trainBPANN" method="POST">
                <div class="row">
                    <div class="input-field col s6">
                      <input id="minError" value="${minError}" name="minError" type="number" step="0.0001">
                      <label for="minError">Enter the Minimum normalised Desired Error</label>
                      <span class="helper-text">${minErrorError}</span>
                    </div>
                    <div class="input-field col s6">
                      <input name="learningRate" value="${learningRate}" id="learningRate" type="number"  step="0.001" >
                      <label for="learningRate">Learning Rate</label>
                      <span class="helper-text">${learningRateError}</span>
                    </div>
                  </div>
                <div class="row">
                    <div class="input-field col s6">
                      <input id="layers" value="${layers}" name="layers" type="number" step="1" >
                      <label for="layers">Enter Number of Layer</label>
                      <span class="helper-text">${layersError}</span>
                    </div>
                    <div class="input-field col s6">
                        <input  name="epochWithoutImprovement" value="${epochWithoutImprovement}" id="epochWithoutImprovement" type="number" step="1" >
                      <label for="epochWithoutImprovement">Enter the maximum number of generation Without Improvement </label>
                      <span class="helper-text">${epochWithoutImprovementError}</span>
                    </div>
                  </div>
                   <div class="input-field col s6">
                    <button class="waves-effect waves-light btn" onclick="document.getElementById('loader').style.display = 'block'" name="option" value="submit"> Submit </button>
                </div>
                <div class="row">
                 <img class="loader" id="loader" src="loader.gif"  />
                </div>
          </form>>
          <h5>${success}</h5>>
        </div>    
        
      </div>

    </div>
 
</body>

</html>


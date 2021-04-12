
<html>
  <head onload="google.charts.setOnLoadCallback(drawChart(${GAError}, 'line_top_GA'))">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/UI.css">

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
      <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.sidenav');
        var instances = M.Sidenav.init(elems);
      });
      google.charts.load('current', {'packages':['line']});

     // google.charts.setOnLoadCallback(drawChart(test, ));
     //googlee chart have problem reloading chart adn can only do one onload
      function drawChart(input, input2, input3) {
         
         var options = {
        chart: {
          title: 'Training Dataset Prediction Graph',
          subtitle: 'Comparison between actual, GA and BP '
        },
        width: window.screenX,
        height: 500,
        axes: {
          x: {
            0: {side: 'top'}
          }
        }
      };        
      var options2 = {
        chart: {
          title: 'Validation Dataset Prediction Graph',
          subtitle: 'Comparison between actual, GA and BP '
        },
        width: 500,
        height: 500
      };
        var options3 = {
        chart: {
          title: 'Testing Dataset Prediction Graph',
          subtitle: 'Comparison between actual, GA and BP '
        },
        width: window.screenX,
        height: 500
      };
        createGraph('Year', 'BP Training Prediction', 'GA Training Prediction', 'Actual Training Value', input, 'line_top_training', options);
        createGraph('Year', 'BP Validation Prediction', 'GA Validation Prediction', 'Actual Validation Value', input, 'line_top_validation', options2);
        createGraph('Year', 'BP Testing Prediction', 'GA Testing Prediction', 'Actual Testing value', input, 'line_top_testing', options3);

      
      }
      function createGraph(column1, column2, column3, column4, input, graph, options)
      {
        var data = new google.visualization.DataTable();
        data.addColumn('string', column1);
        data.addColumn('number', column2);
        data.addColumn('number', column3);
        data.addColumn('number', column4);
    
        data.addRows(input);
        var chart = new google.charts.Line( document.getElementById(graph));
        chart.draw(data, google.charts.Line.convertOptions(options));
      }
                 
    </script>
  </head>
<body onload="google.charts.setOnLoadCallback(drawChart(${trainingGraph},${validationGraph}, ${testingGraph}))">

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
                <span class="card-title">Training and validation Error graph  </span>
              </div>
            </div>
        </div>
        <table>
        
         
         <table>
            <tr>
            <th><div  id="line_top_training"></div></th>
            <th><div id="line_top_validation"></div></th>
            </tr>
            <div  id="line_top_testing"></div>
        </table>
            
        <table>
            <tr>
            <th>GA Training MSE</th>
            <th>GA Training MAD</th>
            <th>GA Validation MSE</th>
            <th>GA Validation MAD</th>
            <th>GA Testing MSE</th>
            <th>GA Testing MAD</th>
            </tr>
            <tr>
                <th>${trainingMSEGA}</th>
                <th>${trainingMADGA}</th>
                <th>${validationMSEGA}</th>
                <th>${validationMADGA}</th>
                <th>${testingMSEGA}</th>
                <th>${testingMADGA}</th>
            </tr>
            <tr>
            <th>BP Training MSE</th>
            <th>BP Training MAD</th>
            <th>BP Validation MSE</th>
            <th>BP Validation MAD</th>
            <th>BP Testing MSE</th>
            <th>BP Testing MAD</th>
            </tr>
            <tr>
                <th>${trainingMSEBP}</th>
                <th>${trainingMADBP}</th>
                <th>${validationMSEBP}</th>
                <th>${validationMADBP}</th>
                <th>${testingMSEBP}</th>
                <th>${testingMADBP}</th>
            </tr>
        </table>
        

      </div>

    </div>
 
</body>

</html>


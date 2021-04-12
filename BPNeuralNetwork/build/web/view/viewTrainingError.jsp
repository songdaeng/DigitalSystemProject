
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
      function drawChart(input, input2) {
         
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Epoch');
        data.addColumn('number', 'Training Error');
        data.addColumn('number', 'Validation Error');
    
        data.addRows(input);

        var options = {
        chart: {
          title: 'BP First 2000 Epoch ',
          subtitle: 'Normalised Training and Validation Error'
        },
        width: 500,
        height: 500,
        axes: {
          x: {
            0: {side: 'top'}
          }
        }
      };
      
      var data2 = new google.visualization.DataTable();
        data2.addColumn('number', 'Epoch');
        data2.addColumn('number', 'Training Error');
        data2.addColumn('number', 'Validation Error');
    
        data2.addRows(input2);

        var options2 = {
        chart: {
          title: 'GA First 2000 Epoch ',
          subtitle: 'Normalised Training and Validation Error'
        },
        width: 500,
        height: 500,
      };

      var chart = new google.charts.Line( document.getElementById('line_top_BP'));
      var chart2 = new google.charts.Line( document.getElementById('line_top_GA'));

      chart.draw(data, google.charts.Line.convertOptions(options));
      chart2.draw(data2, google.charts.Line.convertOptions(options2));
      }
      
                 
    </script>
  </head>
<body onload="google.charts.setOnLoadCallback(drawChart(${BPError}, ${GAError}))">

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
            <th><div  id="line_top_GA"></div></th>
            <th><div id="line_top_BP"></div></th>
            </tr>

        </table>
            
        <table>
            <tr>
            <th>GA final normalised Absolute Training Error</th>
            <th>GA final normalised Absolute Validation Error</th>
            <th>BP final normalised Absolute Training Error</th>
            <th>BP final normalised Absolute Validation Error</th>
            </tr>
            <tr>
            <th>${GAFinalTrainingError}</th>
            <th>${GAFinalValidationError}</th>
            <th>${BPFinalTrainingError}</th>
            <th>${BPFinalValidationError}</th>
            </tr>
        </table>
        

      </div>

    </div>
 
</body>

</html>


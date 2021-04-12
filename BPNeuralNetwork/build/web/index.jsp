
<html>
  <head>
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




var chart, chartData, dataView;

      var columns = [
        {
          type: 'string',
          label: 'Year'
        },
        {
          type: 'number',
          label: 'BP ANN Prediction',
          color: 'red',
          disabledColor: '#FFD9D9',
          visible: true
        },
        {
          type: 'number',
          label: 'GA ANN Prediction',
          color: 'blue',
          disabledColor: '#D9D9FF',
          visible: true,
        },
        {
          type: 'number',
          label: 'Actual Value',
          color: 'green',
          disabledColor: '#C3E6C3',
          visible: true,
        }
      ];

var nullFunc = function() {return null;};

google.load("visualization", "1", {packages: ["corechart"]});


function drawChart(input) {

  if (!chart) {
 
        chartData = new google.visualization.DataTable();
        chartData.addColumn('string', 'Year');
        chartData.addColumn('number', 'BP ANN Prediction');
        chartData.addColumn('number', 'GA ANN Prediction');
        chartData.addColumn('number', 'Actual Value');

        chartData.addRows(input);
  
                                                      
    
    dataView = new google.visualization.DataView(chartData);

    chart = new google.visualization.LineChart(document.getElementById('line_top_x'));

    // Toggle visibility of data series on click of legend.
    google.visualization.events.addListener(chart, 'click', function (target) {
      if (target.targetID.match(/^legendentry#\d+$/)) {    
        var index = parseInt(target.targetID.slice(12)) + 1;
        columns[index].visible = !columns[index].visible;
        drawChart(input);
      }
    });
  }

  var visibleColumnIndexes = [0];
  var colors = [];

  for (var i = 1; i < columns.length; i++) {
    if (columns[i].visible) {
      colors.push(columns[i].color);

      visibleColumnIndexes.push(i);
    }
    else {
      colors.push(columns[i].disabledColor);

      visibleColumnIndexes.push({
        calc: nullFunc,
        type: columns[i].type,
        label: columns[i].label,
      });
    }
  };
  dataView.setColumns(visibleColumnIndexes);
    var options = {
        chart: {
          title: 'Prediction ',
          subtitle: 'in dollars (USD)'
        },
        width: window.screenX,
        height: 500,
        axes: {
          x: {
            0: {side: 'top'}
          }
        }
      };
  chart.draw(
    dataView, options
  );
};


    </script>
  </head>
<body onload="google.charts.setOnLoadCallback(drawChart(${result}))" >

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
                <span class="card-title">Model Prediction </span>
                <p>This is the prediction from two models BP Neural Network and GA Hybird System.</p>
              </div>
            </div>
        </div>
        <div class="row">
         <div id="line_top_x"></div>
        </div>
      
        <div class="row">
            <div class="card blue-grey darken-1">
              <div class="card-content white-text">
                <span class="card-title center">Mean Squared Error  and Mean Absolute Deviation Error </span>
                <table id="errorTable">
                 <tr>
                <th></th>
                <th>Backpropagation</th>
                <th>GA</th>
                </tr>
                <tr>
                <th>MSE Error</th>
                <th>${MSEBP}</th>
                <th>${MSEGA}</th>
              </tr>
              <tr>
                <td>MAD Error</td>
                <td>${MADBP}</td>
                <td>${MADGA}</td>
              </tr>
 
                </table>
              </div>
            </div>
        </div>    
        <form action="${pageContext.request.contextPath}/Prediction" method="POST">
            <div class="row">
                <div class="card blue-grey darken-1 center">
                  <div class="card-content white-text">
                    <div class="row">
                        <div class="input-field col s6">
                          <input placeholder="Placeholder" name="marketData" id="marketData" type="text" required>
                          <label for="marketData">Enter feature sets: 3000, 3000, 3000 </label>
                        
                        
                        </div>
                        <div class="input-field col s6">
                            <button class="waves-effect waves-light btn" name="option" value="submit"> Submit </button>
                        </div>
                        <table id="errorTable">
                         <tr>
                        <th>Backpropagation</th>
                        <th>GA</th>
                        </tr>
                        <tr>
                        <th>${BPPrediction}</th>
                         </tr>
                      <tr>
                        <td>${GAPrediction}</td>
                      </tr>
 
                        </table>
                  </div>
                </div>
            </div>
        </form>>
      </div>

    </div>
 
</body>

</html>


// 내 선호 운동 종류, 유저 평균과 내 데이터 분석 등
<script>
var userName = new Array();
var count = new Array();
var arrCount = null;

function topList(){
  var day = $("#day").text();
  var year = $("#date").text();
  var type = $("#type").text();

  $.ajax({
    url:"selectChart.do",
    data : {
    day : day,
    year : year,
    type : type
    },
    dataType:"json",
    success:function(data){
    	$.each(data, function(index, value){
    		// 차트에 값을 넣어주기 위해 각각 담아줌.
    		userName[index] = value.name;
    		count[index] = value.count;
    	});
    	arrCount = JSON.parse("[" + count + "]");

    	google.charts.load('current', {'packages':['corechart']});
    	google.charts.setOnLoadCallback(drawChart);

    },error:function(){
    	//console.log("실패");
    }
  });
};

function drawChart() {
  var columArray = ['UserName', 'Count', { role: 'style' }, { role: 'annotation' } ]; // 컬럼 및 효과
  var dataArray = [];
  dataArray.push(columArray);

  for(var i=0; i<userName.length; i++){
  	dataArray.push([userName[i], arrCount[i], 'lightblue', arrCount[i]]);
  }

  var data = new google.visualization.arrayToDataTable(dataArray);
  var options = {'width':800, 'height':500};
  var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
  chart.draw(data, options);
};

<script>
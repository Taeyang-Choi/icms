//전체 합계 막대그래프
let ChartManager =  {
    key: {},
    init: function() {
    },
    renderChart: function(canvas, labels, values, type) {
        if(type == 'bar') {
            this._renderBar(canvas, labels, values);
        }
        else {
            this._renderCircle(canvas, labels, values);
        }
    },
    _renderBar: function(canvas, labels, values) {
        //막대 그래프
        const ctx = document.getElementById(canvas).getContext('2d');
        const barChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: 'rgba(104, 104, 187, 255)',
                    border:1,
                    borderRadius: 5,
                }]
            },
            options: {
                legend:{
                    display:false
                },
                scales: {
                    y: {
                        beginAtZero: true
                    },
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: Math.max.apply(null, values),
                            fontSize : 12,
                        }
                    }]
                }

            },
        });
    },
    _renderCircle: function(canvas, labels, values) {
        //도넛 그래프
        const ctx = document.getElementById(canvas).getContext('2d');
        const doughnutChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['rgba(255, 0, 0, 255)',
                        'rgba(255, 165, 0, 255)',
                        'rgba(255, 255, 0, 255)',
                        'rgba(0, 128, 0, 255)',
                        'rgb(0,0,255)',
                        'rgba(3, 13, 78, 255)',
                        'rgba(128, 0, 128, 255)'],
                }],
            },
            options: {
                legend: {
                    position:'right'
                }
            },
        });
    }
}
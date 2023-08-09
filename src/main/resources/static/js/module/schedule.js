let ShiftManager = {
    _default: {color:'AAAAAA'},
    fetchAll: function(callback = console.log) {
        ShiftManager.fetchAllCallback = callback;
        AJAX.get('/schedule/shifts', '', function(list) {
            ShiftManager.data = [];
            for(const item of list) {
                ShiftManager.data.push(new Shift(item));
            }
            ShiftManager.fetchAllCallback(ShiftManager.data);
        });
    },
    getHours: function(shift) {
        let start = parseInt(shift.workStart.substring(0, 2));
        let end = parseInt(shift.workEnd.substring(0, 2));
        let result = end - start;
        return (result >= 0) ? result : 24+result;
    },
    findByCode: function(str) {
        return this.data.find(e => e.shiftCode == str);
    }
}

let MemberScheduleManager = {
    getByTeamAndDate: function(team, date, callback) {
        this.getByTeamAndDateCallback = callback;
        AJAX.get(`/teams/schedules/${team}/${date}`, '', function(data) {
            MemberScheduleManager.getByTeamAndDateCallback(data);
        });
    }
}

let MemberManager = {
    fetchAllByGrade: function(option, callback) {
        option = $.extend({grade:3}, option);
        this.fetchAllByGradeCallback = callback;

        AJAX.get('/schedule/agents', '', function (list) {
            MemberManager.list = list;
            MemberManager.fetchAllByGradeCallback(MemberManager.list);
        });
    },
    findAllByTeam: function(team) {
        return this.list.filter(e => e.team == team);
    },
    findById: function(id) {
        return this.list.filter(e => e.id == id);
    },
    findByUserId: function(userid) {
        return this.list.find(e => e.userid == userid);
    }
}

let PatternManager = {
    fetchAll: function(option, callback) {
        this.fetchAllCallback = callback;
        option = $.extend({}, option);
        AJAX.get(`/schedules/patterns`, '', function(list) {
            PatternManager.list = [];
            for(const item of list) {
                PatternManager.list.push(new Pattern(item));
            }
            PatternManager.fetchAllCallback(PatternManager.list);
        });
    },
    findById: (id) => {
        return PatternManager.list.find(e => {return e.id == id});
    }
}

let LeaveManager = {
    fetchByMonth: function(year, month, callback) {
        LeaveManager.fetchByMonthCallback = callback;
        let yearMonth = year+'-'+month;
        AJAX.get(`/leaves/list?searchStartDate=${yearMonth}-01&searchEndDate=${yearMonth}-${DateUtil.getLastDateOfMonth(year,month)}`, '', function(list) {
            LeaveManager.list = list;
            LeaveManager.fetchByMonthCallback(list);
        });
    },
    
}

let TeamScheduleManager = {
    fetchByTeam: function(team, month, callback) {
        this.fetchByTeamCallback = callback;
        AJAX.get(`/leaves/list?startDate=${yearMonth}-01&endDate=${yearMonth}-31`, '', function(list) {
            TeamScheduleManager.list = list;
            LeaveManager.fetchByMonthCallback(list);
        });
    },
    fetchByMonth: function(year, month, callback) {
        this.fetchByMonthCallback = callback;
        AJAX.get(`/teams/schedules/${year+month}`,'', function(list) {
            TeamScheduleManager.list = list;
            TeamScheduleManager.fetchByMonthCallback(list);
        });
    },
    findByDate: function(date, team) {
        return this.list.find(e => {return e.date === date && e.team == team});
    }
}

let StatisticsManager = {
    data : {},
    append: function(key, shift) {
        // 1. 해당 멤버 데이터 획득
        let data = StatisticsManager.findByKey(key);
        if(!isValid(data)) data = StatisticsManager._newData(key);
        
        // 2. 데이터 추가
        data.list.push(shift);
    },
    _newData: function(key) {
        let obj = {key:key, list:[], total: 0, acc:0, night:0, holiday:0, extra:0};
        this.data[key] = obj;
        return this.data[key];
    },
    calculate: function(key) {
        let data = StatisticsManager.findByKey(key);

        // 계산
        for(const shift of data.list) {
            data.total += shift.getWorkTime();

            switch (shift.workform) {
                case '01':
                    data.acc += shift.getWorkTime();
                    break;

                case '02':
                    data.total -= shift.getWorkTime();
                    data.acc += shift.getWorkTime();
                    data.extra += shift.getWorkTime();
                    break;

                case '03':
                    data.total -= shift.getWorkTime();
                    data.acc += shift.getWorkTime();
                    data.extra += shift.getWorkTime();
                    break;

                case '04':
                    data.acc += shift.getWorkTime();
                    data.holiday += shift.getWorkTime();
                    break;

                case '05':
                    data.acc += shift.getWorkTime();
                    data.night += shift.getWorkTime();
                    break;
            }
        }
    },
    findByKey: function(key) {
        if(this.data.hasOwnProperty(key)) return this.data[key];
        return this._newData(key);
    },
    getResult: function() {
        return this.data;
    },
    findShiftByKeyAndDate: function(key, date) {
        if(this.data.hasOwnProperty(key)) {
            return this.data[key].list.find(e => e.date === date);
        }
        return null
    }
}

class Pattern {
    patterns;
    constructor(obj = {}) {
        obj.list = obj.list??'비,비,비,비,비,비,비,비';

        for(let pair of Object.entries(obj)){
            this[pair[0]] = pair[1];
        }

        this.shiftList = [];
        for(const i of obj.list.split(',')) {
            this.shiftList.push(ShiftManager.findByCode(i));
        }
    }

    getWeekWorkTime() {
        console.log(this);
        let acc = 0;
        for(const shift of this.shiftList) {
            acc += shift.getWorkTime();
        }
        return acc;
    }

    getWeekRestTime() {
        let acc = 0;
        for(const i of this.shiftList) {
            for(const i of this.shiftList) {
                acc += i.getRestTime();
            }
        }
        return acc;
    }

    getWeekTotalTime() {
        let acc = 0;
        for(const i of this.shiftList) {
            for(const i of this.shiftList) {
                acc += i.getTotalTime();
            }
        }
        return acc;
    }

    toString() {
        return JSON.stringify(this);
    }
}

class Shift {
    workform = '01';
    shiftCode;

    constructor(obj = {}) {
        for(let pair of Object.entries(obj)){
            this[pair[0]] = pair[1];
        }

        if(this.shiftCode === '야') this.workform = '05';
    }

    getWorkTime() {
        return this.getTotalTime() - this.getRestTime();
    }

    getRestTime() {
        let start = parseInt(this.restStart.substring(0, 2), 0);
        let end = parseInt(this.restEnd.substring(0, 2), 0);
        let result = end - start;
        return (result >= 0) ? result : 24+result;
    }

    getTotalTime() {
        let start = parseInt(this.workStart.substring(0, 2));
        let end = parseInt(this.workEnd.substring(0, 2));
        let result = end - start;
        return (result >= 0) ? result : 24+result;
    }

    toString() {
        return JSON.stringify(this);
    }
}
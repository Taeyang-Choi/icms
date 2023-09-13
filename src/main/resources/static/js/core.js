let pagectx = {};

let CodeManager = {
    get: function(name, code, defaultValue = '삭제된코드') {
        return CodeManager.getCode(name, code, defaultValue).n;
    },
    getCode: function(name, code, _defaultValue = '삭제된코드') {
        let defaultValue = {c:code, n:_defaultValue, r:''}
        if(!isValid(this[name])) return defaultValue;
        for(let i = 0; i < this[name].length; i++) {
            if(this[name][i].c == code) {
                return this[name][i];
            }
        }
        return defaultValue;
    }
};

let ServerConfig = {
    name: function () {

    },
    logo: function (type) {
    }
}

let largeCategory = [{n:'방문자대장 관리',f:'visit', t:true, g:3}, {n:'근무일지',f:'dailyreport', t:true, g:3}, {n:'모니터링현황',f:'monitor', t:true, g:3}, {n:'근태관리',f:'attend', t:true, g:3},
    {n:'팀 스케줄',f:'schedule', t:true, g:1}, {n:'자산관리',f:'cctv', t:true, g:1}, {n:'휴가',f:'leave', t:false, g:3}, {n:'공통관리',f:'common', t:true, g:3}];
let menus = [[{n:'방문자대장 관리',f:'visit/list'}],
    [{n:'근무일지현황',f:'dailyreport/list'},{n:'공지사항',f:'board/list?n=notice'},{n:'출근부',f:'schedule/calendar/list'}],
    [{n:'모니터링현황',f:'monitor/list2'}],
    [{n:'근무자정보',f:'attend/member/list'},{n:'월간근무통계',f:'attend/statistics'},{n:'휴가신청',f:'attend/leave/list'},{n:'출퇴근',f:'attend/time-sheet'}],
    [{n:'팀스케줄',f:'schedule/main'},{n:'월별 근무팀',f:'schedule/monthly'}],
    [{n:'카메라별 장애관리',f:'cctv/error/list'},{n:'카메라 등록수정',f:'cctv/info/list2'},{n:'카메라지도',f:'cctv/map'}],
    [{n:'휴가신청',f:'leave/write'},{n:'휴가신청내역',f:'leave/list'}],
    [{n:'관리자관리',f:'common/admin/list'},{n:'코드관리',f:'common/code/list'},{n:'라이센스',f:'common/license/list2'}]]

//{n:'근로정보',f:'workreport/list'},
let statFlag = {
    list:['작성중','결재중','name']
}

let SessionManager = {
    set: function (sesobj) {
        if (isValid(sesobj)) {
            Params.set("cfgSesobj", sesobj);
        }
    },
    reset: function () {
        Params.clear("cfgSesobj");
    },
    get: function () {
        let sesobj = Params.get("cfgSesobj", {usrobj:{grade:4}});
        return sesobj;
    }
}

let AJAX2 = {
    get: async function(url) {
        return axios.get(url).then(function (res){return res.data;});
    },
    post: async function(url, data) {
        return this.send('post', url, data).then(function (res){return res.data;});
    },
    put: async function(url, data) {
        return this.send('put', url, data).then(function (res){return res.data;});
    },
    patch: async function(url, data) {
        return this.send('patch', url, data).then(function (res){return res.data;});
    },
    delete: async function(url, data) {
        return this.send('delete', url, data).then(function (res){return res.data;});
    },
    send: async function (method, url, data) {
        return axios({method: method, url: url, data: data});
    },
    save: async function(data) {
        return data;
    }
}


let Page = {
    session: {},
    init: function (option) {
        this.loadConfig();
        let src = location.pathname;

        // network connection check first
        document.addEventListener("offline", function () {
            window.location.href = "error";
        }, false);

        let sesObj = SessionManager.get();
        if (isValid(sesObj)) {
            Page._buildParams(src, sesObj, option);
        } else {
            AJAX.get("/session", "", function (sesObj) {
                Page._buildParams(src, sesObj, option);
            });
        }
    },
    reload: function() {
        location.reload();
    },
    _buildParams: function (src, sesObj, option) {
        $("body").removeClass("hide");

        this.session = sesObj;

        // now build parameters to call cbfunc()
        let params = SessionStore.get(src);
        if (!isValid(params)) params = {};

        // override usrobj and addrobj from session info
        params.usrobj = sesObj.usrobj;

        if(option.auth && isValid(!params.usrobj)) {
            $('#main').html('');
            Dialog.alert(`접근 권한이 없습니다.로그인 하시겠습니까?`, function() {
               Page.go('/');
            });
            return;
        }

        Page._start(src, params);
    },
    _start: function (src, params) {
        GlobalMenuNav.init(params);
        //this.setSideNavigation(params);

        init(params);
    },
    loadConfig: function(params) {
        AJAX.get('/sel-codes', '', function(data) {
            let temp = {};

            data.sort(function(a, b)  {
                return a.seq - b.seq;
            });

            for(const e of data) {
                if(!e.active) continue;
                //console.log(e);
                if (!temp.hasOwnProperty(e.kindCode)){
                    temp[e.kindCode] = [];
                }

                temp[e.kindCode].push({c:e.code, n:e.name, r:e.remarks, o:e.seq});
            }

            CodeManager = $.extend(CodeManager, temp);
            Params.set('selcode', temp);
        });
    },
    updateSession: function (data) {
        let url = Params.get("ssoLastUrl", SSO.getMainUrl());
        if(data.usrobj.grade == 1) url = '/dashboard';
        Params.clear('ssoLastUrl');
        Page.session = data;

        SessionManager.set(data);
        Page.move(url);
    },
    getPageName: function(cpr) {
        cpr = cpr.split('.')[0];
        let pageScope = $('body').data('page-scope');

        //console.log(cpr +", " + pageScope);
        // 20220709 새로운 방식
        if(isValid(pageScope)) {
            if(pageScope === cpr) return true;
        }
        else {
            return false;
        }
    },
    setFooter: function (params) {
    },
    go: function (url, params) {
        if (isValid(params)) {
            SessionStore.set(url, params);
        }
        window.location.href = (url);
    },

    move: function (url, params) {
        if (isValid(params)) {
            SessionStore.set(url, params);
        }
        window.location.replace(url);
    },
    back: function () {
        //history.back();
        location.href = document.referrer;
    },
    setPaging: function(option) {
        let _d = {totalPages:10000, totalElements: 1000};
        option = $.extend(_d, option);

        let page = parseInt(request.getParameter("page", 1));
        page = Math.clamp(page, 0, option.totalPages);

        let center = (page < 5) ? 5 : page;

        // 기존 search 유지
        let search = '';
        let paramArr = location.search.replaceAll('?','').split('&');
        for (let i = 0; i < paramArr.length; i++) {
            if(!paramArr[i].startsWith('page') && paramArr[i].length > 1) search += paramArr[i] + '&';
        }

        $('#pageSection').html('');

        $('#pageSection').append(`<ul class="pagination justify-content-center"></ul>`);
        $('#pageSection ul').append(`<li class="prev page-item"><a class="page-link"
                                          href="${location.pathname+'?'+search+'page='+(Math.clamp(page-10,1, option.totalPages))}"
                                          aria-label="Previous"><span aria-hidden="true"><</span></a></li>`);


        //console.log(center+4 >= option.totalPages);
        //console.log(center+4);
        let max = (center+4 < option.totalPages) ? center+4 : option.totalPages;
        for(let i = center - 4; i <= max; i++) {
            // 페이지
            $('#pageSection ul').append(`<li class="page-item ${(i == page)?'active':''}"><a class="page-link" href="${location.pathname+'?'+search+'page='+i}">${i}</a></li>`);
        }

        // next 버튼
        let lastPage = (Math.clamp((page+10),1, option.totalPages));
        $('#pageSection ul').append(`<li class="next page-item"><a class="page-link" href="${location.pathname+'?'+search+'page='+lastPage}" aria-label="Next"><span aria-hidden="true">></span></a></li>`);

    }
}
// -----------------------------------------------------------------------------------------
// Dialog object
let Dialog = {
    cbFunc: null,
    cbFuncCancel: null,
    data: {},
    init: function () {
        Dialog._generate();
        Dialog.modal = new bootstrap.Modal(document.getElementById('dialog'),{});

        $(window).resize(function() {
            Dialog.resize();
        });
    },
    _generate: function () {
        let str = `<div id="dialog" class="modal show" tabindex="-1">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title"></h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                </div>
                                <div class="modal-footer">
                                    <button id="dialog-cancel" type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                                    <button id="dialog-ok" type="button" class="btn btn-sm btn-primary">확인</button>
                                </div>
                            </div>
                        </div>
                    </div>`;

        $("body").append(str);

        $('#dialog-ok').on('click', function() {
            Dialog.reset();
            if (isValid(Dialog.cbFunc)) {
                Dialog.cbFunc(Dialog.data);
                Dialog.cbFunc = null;
            }
            if (isValid(Dialog.cbFuncCancel)) {
                Dialog.cbFuncCancel = null;
            }
        });

        $('#dialog-cancel').on('click', function() {
            Dialog.reset();
            if (isValid(Dialog.cbFuncCancel)) {
                Dialog.cbFuncCancel(Dialog.data);
                Dialog.cbFuncCancel = null;
            }
            if (isValid(Dialog.cbFunc)) {
                Dialog.cbFunc = null;
            }
        });
    },
    show: function () {
        Dialog.modal.show();
        Dialog.resize();
    },
    resize: function () {
    },
    hide: function () {
        Dialog.modal.hide();
    },
    set: function (showDel) {
        this.show();

        $("body").on('keyup', function(e) {
            if (e.keyCode == 27) {
                Dialog.onCancel();
            }
            else if (e.keyCode == 13) {
                if ($('#dialog-ok').length) {
                    //Dialog.onConfirm();
                }
            }
        });
    },
    reset: function () {
        this.hide();
        $("body").off('keyup');
    },

    // Other dialogs ---------------------------------------------------------------
    //

    alert: function (msg, cbfunc, data) {
        Dialog.cbFunc = cbfunc;
        Dialog.cbFuncCancel = cbfunc;
        Dialog.data = data;

        let str = `<div>${msg}</div>`;
        $('#dialog .modal-title').html("");
        $('#dialog .modal-body').html(str);
        $('#dialog-cancel').hide();
        Dialog.set();
    },

    confirm: function (msg, cbfunc, cbfunc2, data) {
        Dialog.cbFunc = cbfunc;
        Dialog.cbFuncCancel = cbfunc2;
        Dialog.data = data;

        let str = `<div>${msg}</div>`;
        $('#dialog .modal-title').html("");
        $('#dialog .modal-body').html(str);
        this.set();
    },

    cbSelect: null,
    select: function (list, cbfunc, showicn) {
        this.cbSelect = cbfunc;

        var str = "<div class='dlg-sel'>";
        var cnt = 0;
        for (var i=0; i<list.length; i++) {
            if (i == 0) {
                var title = (list[0].icon == "title") ? list[0].text : "명령창";
                str += "<div class='title'>" + title + "</div>";
                str += "<div class='section py-2'>";
            }
            if (list[i].icon == "title") continue;

            var cmd = (list[i].disabled != true) ? " onclick='Dialog.onSelect(" + i + ")'" : "";
            var gray = (list[i].disabled == true) ? " lightgray" : "";
            var bdtop = (cnt++ > 0) ? " border-top" : "";

            if (list[i].sep == true) {
                str += "<div class='section mtop-7 mbot-7 bdtop-ddd'></div>";
                bdtop = "";
            }

            str += "<div class='section py-2" + bdtop + "'" + cmd + ">";
            if (showicn == true) {
                str += "<div class='icon pointer " + list[i].icon + "'></div>";
                str += "<div class='text" + gray + "'>" + list[i].text + "</div>";
            }
            else {
                str += '<div role="button" class="text wid-100' + gray + '">' + list[i].text + '</div>';
            }
            str += "</div>";
        }
        if (list.length > 0) {
            str += "</div>";
        }
        str += "</div>";

        $('#dialog-cnt').html(str);

        this.set(true);
    },
    onSelect: function (index) {
        this.reset();
        if (isValid(this.cbSelect)) {
            this.cbSelect(index);
            this.cbSelect = null;
        }
    }
};

jQuery.fn.center = function (parent) {
    var top = ($(parent).height() - $(this).outerHeight()) / 2 + $(parent).scrollTop();
    var left = (($(parent).width() - $(this).outerWidth()) / 2) + $(parent).scrollLeft();

    this.css("position", "absolute");
    this.css("top", Math.max(0, top) + "px");
    this.css("left", Math.max(0, left) + "px");

    //console.log($(parent).height() + " > " + $(this).outerHeight());
    return this;
}

var Params = {
    set: function (name, val, location) {
        name = Params.filter(name);
        let storage = (location === 'session') ? sessionStorage : localStorage;
        try {
            storage[name] = JSON.stringify(val);
        } catch (ex) {
            console.log("Params.set(): " + ex + " onn setting '" + name + "'\n\n" + new Error().stack);
            Audit.showSessionMem();
        }
    },
    get: function (name, defaultValue, location) {
        name = Params.filter(name);
        let storage = (location === 'session') ? sessionStorage : localStorage;
        var str = storage[name];
        return (isValid(str)) ? JSON.parse(str) : defaultValue;
    },
    clear: function (name, location) {
        name = Params.filter(name);
        let storage = (location === 'session') ? sessionStorage : localStorage;
        delete storage[name];
    },
    filter: function (name) {
        if (name.startsWith('/')) name = name.substring(1);
        return name;
    }
};


let SessionStore = {
    set: function (name, val) {
        Params.set(name, val, "session");
    },
    get: function (name, defaultValue) {
        return Params.get(name, defaultValue, "session");
    },
    clear: function (name) {
        Params.clear(name, "session");
    }
};

function isValid(param) {
    return (param != null && param != "" && typeof param != "undefined");
}

String.prototype.replaceAll = function (org, dest) {
    return this.split(org).join(dest);
}

String.prototype.insert = function(index, str){
    return this.slice(0,index) + str + this.slice(index)
}

String.prototype.startsWith = function (search, pos) {
    return this.substr(!pos || pos < 0 ? 0 : +pos, search.length) === search;
}

Number.prototype.padStart = function(maxLength, fill) {
    return this.toString().padStart(maxLength, fill);
}

// -----------------------------------------------------------------------------------------
// AJAX call control
let AJAX = {
    load: function (url, onSuccess, onFail, progMsg) {
        if (isValid(progMsg)) Dialog.showProgress(progMsg);

        $.ajax({
            url: url, dataType: "script", timeout: 3000
        }).done(function () {
            if (isValid(onSuccess)) onSuccess();
            if (isValid(progMsg)) Dialog.hideProgress();
        })
            .fail(function () {
                if (isValid(onFail)) onFail();
                if (isValid(progMsg)) Dialog.hideProgress();
            });
    },
    handleError: function (xhr) {
        console.log(xhr);
        if (xhr.responseJSON.code == -101) {
            Dialog.alert(xhr.responseJSON.result,
                function () {
                    SessionManager.reset();
                    Params.set('ssoLastUrl', location.pathname + location.search);

                    AJAX.get('/account/signout', "", function (data) {
                        Page.move(SSO.getLoginUrl());
                    });
                }
            );
        } else {
            var str = "code:" + xhr.status + "\n" + "message:" + xhr.responseText + "\n" + "error:" + error;
            console.log(str);
            Dialog.alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
        }
    },
    get: function (url, params, onSuccess, dataType) {
        if (!isValid(dataType)) dataType = 'JSON';
        let usrobj = SSO.getMember();

        if(!url.includes('?')) {
            url += `?`;
        }
        else {
            url += '&';
        }
        url += `authKey=${usrobj.userid}&authToken=${usrobj.userpwd}`;

        let callobj = {};
        callobj["url"] = '/api' + (url);
        callobj["type"] = "GET";
        callobj["data"] = params;
        callobj["cache"] = false;
        callobj["dataType"] = dataType;
        callobj["success"] = onSuccess;
        callobj["error"] = function (xhr, status, error) {
            if (xhr.status == 0) {
                Dialog.alert("네트워크 접속이 원할하지 않습니다.");
            } else {
                AJAX.handleError(xhr);
            }
        };

        jQuery.ajax(callobj);
    },
    post: function (url, formData, data, onSuccess) {
        AJAX.call(url, formData, "POST", onSuccess, data);
    },
    put: function (url, formData, data, onSuccess) {
        AJAX.call(url, formData, "PUT", onSuccess, data);
    },
    remove: function (url, formData, data, onSuccess) {
        AJAX.call(url, formData, "DELETE", onSuccess, data);
    },
    call: function (url, formData, method, onSuccess, data) {
        let callObj = {};
        let usrobj = Page.session.usrobj;
        let authObject = {};

        if(!isValid(formData)) formData = new FormData();

        if(isValid(usrobj)) {
            formData.append('authId', usrobj.userid);
            formData.append('authKey', usrobj.userpwd);
        }

        callObj["url"] = '/api' + (url);
        callObj["type"] = method;
        callObj["data"] = formData;
        callObj["processData"] = false;
        callObj["contentType"] = false;
        callObj["cache"] = false;
        callObj["dataType"] = "json";
        callObj["success"] = data;

        // RequestBody 데이터가 없다면
        if(typeof data != 'function') {
            callObj["contentType"] = "application/json";
            callObj["type"] = method;
            callObj["data"] = JSON.stringify(data);
            callObj["success"] = onSuccess;
        }

        callObj["error"] = function (xhr, status, error) {
            if (xhr.status == 0) {
                Dialog.alert("네트워크 접속이 원할하지 않습니다.");
            } else {
                AJAX.handleError(xhr);
            }
        };

        jQuery.ajax(callObj);
    }
};

function formatNum(val) {
    if (!isValid(val)) val = 0;
    if (typeof val == "string") val = parseInt(val);
    while (/(\d+)(\d{3})/.test(val.toString())) {
        val = val.toString().replace(/(\d+)(\d{3})/, '$1' + ',' + '$2');
    }
    return val;
}
function dateFormat(x, y) {
    if(typeof x === 'string') {
        if(x.includes('T')) {
            return dateFormat(new Date(x), 'yyyy-MM-dd hh:mm:ss');
        }
        y = x;
        x = new Date();
    }
    if(x instanceof Date && typeof y == 'undefined') {
        y = 'yyyy-MM-dd hh:mm:ss';
    }
    if(!isValid(x)) {
        return dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss');
    }
    let z = {
        M: x.getMonth() + 1,
        d: x.getDate(),
        h: x.getHours(),
        m: x.getMinutes(),
        s: x.getSeconds(),
    };
    y = y.replace(/(M+|d+|h+|m+|s+)/g, function (v) {
        return ((v.length > 1 ? '0' : '') + eval('z.' + v.slice(-1))).slice(-2);
    });

    return y.replace(/(y+)/g, function (v) {
        return x.getFullYear().toString().slice(-v.length);
    });
}

let PopupModalManager = {
    count: 0,
    str: `<div id="popup-0" class="modal fade popup-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Modal title</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button id="popup-cancel" type="button" class="btn btn-sm btn-danger" data-bs-dismiss="modal">취소</button>
                    <button id="popup-ok" type="button" class="btn btn-sm btn-primary">확인</button>
                </div>
            </div>
        </div>
    </div>`,
    init: function(title, str, ok, cancel) {
        if($('.popup-modal').length == 0) {
            this.count++;
            $('body').append(this.str);
        }

        const myModal = new bootstrap.Modal(document.getElementById('popup-0'), {backdrop:'static'});
        myModal.show();

        $('#popup-0 .modal-header .modal-title').html(title);
        $('#popup-0 .modal-body').html(str);

        if(ok == null) $('#popup-0 #popup-ok').html("");
        if(cancel == null) $('#popup-0 #popup-cancel').html("");
        if(ok == null && cancel == null) $('#popup-0 .modal-footer').html("");

        $('#popup-0 #popup-ok').html(ok);
        $('#popup-0 #popup-cancel').html(cancel);
        $('#popup-0 #popup-cancel').on('click', function() {
            $('#popup-0').hide();
        });
    }
}

let request = {
    requestParam: '',
    getParameter: function (param, defaultValue = null, utf8 = true) {
        let url = utf8 == true ? location.href : unescape(location.href);
        let paramArr = url.substring(url.indexOf('?') + 1, url.length).split('&');

        for (let i = 0; i < paramArr.length; i++) {
            let temp = paramArr[i].split('=');
            if (temp[0].toUpperCase() == param.toUpperCase()) {
                this.requestParam = paramArr[i].split('=')[1];
                if (utf8 == true) {
                    this.requestParam = decodeURIComponent(this.requestParam);
                }
                break;
            }
        }
        let temp = this.requestParam;
        this.requestParam = '';
        if(Utils.isNull(temp)) return defaultValue;
        return temp;
    },
    getPathValue() {
        return location.pathname.split('/').pop();
    }
};

$(document).ready(function() {
    let option = $.extend({auth:true}, $('body').data('option'));

    Dialog.init();
    Page.init(option);
});

function get(v, d) {
    try {
        if (isValid(v)) {
            return v;
        } else {
            return d;
        }
    }
    catch (e) {
        return d;
    }
}

let SSO = {
    check: function () {
        if (!SSO.isLogined()) {
            Dialog.alert('로그인이 필요한 서비스입니다.<br>로그인하시겠습니까?', function () {
                    Params.set('ssoLastUrl', location.pathname);
                    Page.move(SSO.getLoginUrl());
                }
            );
            return false;
        } else {
            return true;
        }
    },
    getLoginUrl: function () {
        return "/";
    },
    isLogined: function () {
        var sesobj = Page.session;
        if (!isValid(sesobj) || !isValid(sesobj.usrobj)) {
            return false;
        }
        return true;
    },
    getMainUrl: function () {
        return "/board/list?n=notice";
    },
    logout: function () {
        AJAX.remove('/logout', null, function (data) {
            SessionManager.reset();
            console.log(data);
            Page.go(SSO.getLoginUrl());
        });
    },
    login: function (id, pass) {
        let params = 'id=' + id +'&pass=' + pass;
        AJAX.post('/login?'+ params, null, function (data) {
            let usrobj = data.data;

            if(data.code == 0) {
                Params.set('lastLoginedID', id);

                Page.updateSession({usrobj: usrobj});
                return false;
            }
            else {
                Dialog.alert(data.message, function() {
                    location.reload();
                });
                return false;
            }
        });
    },
    getMember: function() {
        let member = SessionManager.get().usrobj;
        return $.extend({userid:'',userpwd:'',grade:4}, member);
    }
};

let StringUtils = {
    brToCr: function(str) {
        if(!isValid(str)) return '';
        return str.split('<br>').join("\r\n");
    },
    snakeToCamel: function(s) {
        return s.replace(/([-_][a-z])/ig, ($1) => {
            return $1.toUpperCase()
                .replace('-', '')
                .replace('_', '');
        });
    },
    camelToSnake: function(s) {
        let result = s.replace( /([A-Z])/g, " $1" );
        return result.split(' ').join('_').toLowerCase();
    },
    boolToString: function(val) {
        if(val) return '예'
        else return '아니요'
    },
    trySplit: function (str, token, def) {
        if(Utils.isValid(str) && str.length > 0) {
            return str.split(token);
        }
        return def;
    }
}

let DateUtil = {
    getDateCount: function(year, month, date) {
        if(this.firstDate == null) this.firstDate = new Date(`${year}-01-01`);
        let currentDate = new Date(`${year}-${month}-${date}`);
        return Math.ceil((currentDate.getTime()-this.firstDate.getTime())/24/60/60/1000);
    },
    getDateFromDate: function(defDate, plus) {
        return new Date(defDate.getTime() + plus*24*60*60*1000);
    },
    getLastDateOfMonth: function(year, month) {
        return new Date(year, month, 0).getDate();
    }
}

let SideMenuManager = {
    initialized: false,
    $main: null,
    $header: null,
    $side: null,
    init: function() {
        if(this.initialized) return;
        this.initialized = true;
        this.$main = $('#main');
        this.$header = $('#header');
        this.$side = $('#side-nav');
        $('#side-nav').on('click', function(e) {
            if($(this).hasClass('closed')) {
                e.preventDefault();
                e.stopPropagation();
                SideMenuManager.open();
            }
        });

        $('#side-brand').on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            SideMenuManager.toggle();
        });
    },
    open: function() {
        this.$main.css('left', '280px');
        this.$header.css('left', '280px');
        this.$side.removeClass('closed');
    },
    close: function() {
        this.$main.css('left', '76px');
        this.$header.css('left', '76px');
        this.$side.addClass('closed');
    },
    toggle: function() {
        if(this.$side.hasClass('closed')) {
            this.open();
        }
        else {
            this.close();
        }
    }
}

let GlobalMenuNav = {
    initialized: false,
    $main: null,
    $header: null,
    $side: null,
    init: function(params) {
        if(this.initialized) return;
        this.initialized = true;
        this.$main = $('#main');
        this.$header = $('#header');

        //console.log(params.usrobj)
        let str = `<nav>
        <div style="background-color:white">
            <div class="container">
                <div id="header-login" class="d-flex justify-content-end gap-2" style="align-items:center;color:black;padding:6.5px 0">
                </div>
            </div>
        </div>
            <nav id="gnb" class="navbar" style="background:#122037;border-bottom:1px solid #C4C4C4;box-shadow: 0px 5px 7px -6px #c4c4c4;">
                <div class="container">
                <div id="header-login" style="align-items:center;color:white;padding:5px 0">
                    <div style="height:33px"><a class="navbar-brand fs-40" href="/dashboard" style="color:white;font-family:PlayfairDisplay;position:relative;bottom:17px">ICMS</a></div>
                    <p class="fs-18">통합관제센터 관리시스템</p>
                </div>
                    <ul class="nav"></ul>
                </nav>
            </div>
        </div>`;

        // 헤더 추가
        $('#header').html(str);

        // 대분류 추가
        let usrobj = SSO.getMember();
        let grade = parseInt(usrobj.grade);
        for(let i = 0; i < largeCategory.length; i++) {
            if(!largeCategory[i].t) continue;
            if(grade > largeCategory[i].g) continue;
            if(largeCategory[i].t == 'test' && location.host != 'localhost') continue;
            $('#gnb .nav').append(`
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="" role="button" data-bs-toggle="dropdown" aria-expanded="true" style="color:white;padding: 0.5rem 31px;">
                        ${largeCategory[i].n}
                    </a>
                    <ul id="largeCategory-${i}" class="dropdown-menu dropdown-menu-light">
                    </ul>
                </li>`);

            // 소분류 추가
            for(let a = 0; a < menus[i].length; a++) {

                if(typeof(ServerPreference)!= 'undefined' ) if(ServerPreference[activeProfile].blackList.includes(menus[i][a].f)) continue;
                //console.log(typeof(ServerPreference), ServerPreference[activeProfile].blackList.includes(menus[i][a].f))
                    $('#largeCategory-'+i).append(`
                <li><a class="dropdown-item" style="border-bottom:1px solid #E9E9E9;" href="/${menus[i][a].f}">${menus[i][a].n}</a></li>`);

                // 해당 메뉴가 그 메뉴면
                if(Page.getPageName(menus[i][a].f)) {
                    $('#gnb .dropdown-item').last().addClass('active');
                    $('#gnb .dropdown-toggle').last().addClass('active');
                }
            }
        }

        // 최신 공지 상단에
        AJAX.get('/board/notices/latest', '', function(list) {
            $('#header').append(`<div id="ntc" class="container" style="padding-top:108px;"></div>`);

            for(const item of list) {
                $('#ntc').append(`<a href="/board/detail?n=notice&no=${item.id}"><div class="fs-14" style="padding:14px 20px;display:flex;background-color:white;">
                    <div class="fw-bold" style="color:blue">공지</div>
                    <div style="margin:0 16px;">${item.title}</div>
                    <div style="color:#C4C4C4;">${dateFormat(item.createdAt)}</div>
                </div></a>`);
            }
        });


        if(SSO.isLogined()) {
            $('#header-login').append(`<div><a href="#" role="link" onclick="SSO.logout()" style="color:black;">로그아웃</a></div>
            <div><span class="text-primary">${params.usrobj.name}(${params.usrobj.userid})</span> 님 로그인중입니다.</div>`);
        }
        else {
            $('#header-login').append(`<div></div><div style="align-self:center"><a href="${SSO.getLoginUrl()}" class="btn btn-sm btn-primary">로그인</a></div>`);
        }

        $('#side-nav').on('click', function(e) {
            if($(this).hasClass('closed')) {
                e.preventDefault();
                e.stopPropagation();
                GlobalMenuNav.open();
            }
        });

        $('#side-brand').on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            GlobalMenuNav.toggle();
        });
    },
    open: function() {
        this.$main.css('left', '280px');
        this.$header.css('left', '280px');
        this.$side.removeClass('closed');
    },
    close: function() {
        this.$main.css('left', '76px');
        this.$header.css('left', '76px');
        this.$side.addClass('closed');
    },
    toggle: function() {
        if(this.$side.hasClass('closed')) {
            this.open();
        }
        else {
            this.close();
        }
    }
}

let SearchManager = {
    setSearch: function(option) {
        let _d = {target:'#search-section', changeState: true}
        option = $.extend(_d, option);

        // select 등 설정...
        let $selects = $(option.target +' select');
        for(let i = 0; i < $selects.length; i++) {
            let name = $selects.eq(i).attr('name');
            if(!isValid(name)) continue;
            let arr = CodeManager[StringUtils.camelToSnake(name)] || [];

            for(let x = 0; x < arr.length; x++) {
                $selects.eq(i).append(`<option value="${arr[x].c}">${arr[x].n}</option>`);
            }
        }

        // 검색 버튼 입력시
        $('#btn-search').on('click', function() {
            SearchManager._doSearch(option);
        });

        $("#search-section").on("keydown", function(event) {
            if(event.which === 13)
                SearchManager._doSearch(option);
        });
    },
    _doSearch(option) {
        let $inputs = $(`${option.target} .form-control, ${option.target} .form-select`);
        console.log($inputs);
        let $searchKeys = $(`${option.target} .search-key`);
        let q = '';
        let doSearch = false;

        for(let i = 0; i < $inputs.length; i++) {
            let name = $inputs.eq(i).attr('name');
            let val = $inputs.eq(i).val();
            if(!isValid(name)) continue;
            if(!isValid(val)) continue;

            doSearch = true;
            q += name +'=' +val + '&';
        }

        for(const searchKey of $searchKeys) {
            let name = $(searchKey).val();
            let val = $($(searchKey).data('sfor')).val();

            if(!isValid(name)) continue;
            if(!isValid(val)) continue;

            doSearch = true;
            q += name +'=' +val + '&';
        }
        //console.log(q);

        q = q.substr(0, q.length-1);

        // 메인 검색은 주소를 변경
        if(option.changeState) history.pushState('', '', location.pathname+"?" + q + '&page=1');

        AJAX.get(option.src + '?' + q + '&page=1', '', function(data) {
            Page.setPaging({});
            option.callBack(data);
        });
    }
}

// 기본 유틸 목록
let Utils = {
    isValid: function(something) {
        return (something != null && something != "" && typeof something != "undefined");
    },
    isNull: function(something) {
        return !Utils.isValid(something);
    },
    formToObject: function(formIdOrFormData, option) {
        let rawFormData = formIdOrFormData;
        if(typeof rawFormData != 'object') {
            rawFormData = new FormData($(formIdOrFormData)[0]);
        }
        let data = {};

        for(let pair of rawFormData.entries()){
            if($(`input[name=${pair[0]}]`).hasClass("pass")) continue;
            let label = $('[aria-label=' + pair[0]+']').text();
            if(!Utils.isValid(pair[1]) && $(`input[name=${pair[0]}].required`).length > 0) {
                /*
                Dialog.alert(label + '을(를) 입력해주세요.', function() {
                    $(`input[name=${pair[0]}].required`).trigger('focus');
                });
                 */
                return null;
            }
            data[pair[0]] = pair[1];
        }
        return data;
    },
    getHourBetween: function(dt2, dt1) {
        let diff =(dt2.getTime() - dt1.getTime()) / 1000;
        diff /= (60 * 60);
        return Math.abs(Math.round(diff));
    },
    tryParse: function(str, defaultValue = {}) {
        try {
            return JSON.parse(str);
        } catch (e) {
            return defaultValue;
        }
    },
    formatNum: function(val) {
        if (!Utils.isValid(val)) val = 0;
        if (typeof val == "string") val = parseInt(val);
        while (/(\d+)(\d{3})/.test(val.toString())) {
            val = val.toString().replace(/(\d+)(\d{3})/, '$1' + ',' + '$2');
        }
        return val;
    },
    getImage: function (file, type = 'food', size = 'xs') {
        // size 는 xl, md, lg 중 한 타입

        // 이미지 여러개면 첫장만
        if(typeof file == 'object' && Utils.isValid(file)) {
            file = file[0];
        }

        // TOAST 외부 이미지
        if (Utils.isValid(file)) {
            file = file.replace('http://','https://');
            if(Utils.isValid(size)) {
                file = file.replace('.png', `_${size}.png`)
            }
            return file;
        }

        // 없으면 기본 이미지
        return `/assets/images/bg_${type}.jpg`
    }
};

Math.clamp = function(num, min, max) {
    return Math.min(Math.max(num, min), max);
};

(function () {
    //console.log(Params.get('selcode', {}));
    $.extend(CodeManager, Params.get('selcode', {}));
    window.addEventListener('resize', handleResize);
})();



function handleResize() {
    // Code to be executed when the browser size changes
    console.log('Browser size changed!');
    // Call your specific function here
}

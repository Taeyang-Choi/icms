let DailyReport = {

};

let Monitoring = {
    getBarrierLevel: (item) => {
        let barrierLevel = item.barrierLevel;
        let deletedCode = {c:barrierLevel, n:'삭제된코드', r:''};

        if(!isValid(barrierLevel)) return deletedCode;
        let code = $.extend(deletedCode, CodeManager.barrier_level.find(code => code.c == barrierLevel));

        // 상세 상황 선택
        if(item.workDgubun == 'D02') {
            code = $.extend(deletedCode, CodeManager.situ.find(code => code.c == barrierLevel.substr(0,2)));
            let situDetails = code.r.split(',');
            code.c = barrierLevel;
            let codeIndex = parseInt(barrierLevel.substr(2,1));
            if(!isNaN(codeIndex)) code.n = situDetails[codeIndex];
            else code.n = '삭제된코드';
        }

        return code;
    }
};
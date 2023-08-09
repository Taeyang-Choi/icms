class Leave {
    constructor(obj = {}) {
        for(let pair of Object.entries(obj)){
            this[pair[0]] = pair[1];
        }
    }

    toString() {
        return JSON.stringify(this);
    }
}
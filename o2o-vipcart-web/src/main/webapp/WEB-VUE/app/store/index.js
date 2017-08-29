import * as calendar from '../utils/calendar';

class Store {

	constructor() {

		this.computed = {
		};

		this.state = {
			cityList: [],
			activityList: [],
			cityId: "-1",
			activityId: "-1",
			cityName: "",
			activityName: "",
			startTime: "",
			enums: {
				activityState: {
					create:{code: 1, name: '新建'},
					lock: {code: 4, name: '已锁定'},
					open: {code: 3, name: '进行中'}
				}
			}
		};
	}

	loadActivityAndCity(callback) {
		$.ajax({
			url: '/plan/actList',
			type: "GET",
			success: (data) => {
				if(data.code == "0") {
					this.state.activityList = data.result;
				}
				callback && callback();
			},
			error: () => {
				callback && callback();
			}
		});
	}
}

export default new Store();

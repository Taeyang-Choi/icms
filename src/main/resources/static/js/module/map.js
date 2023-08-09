let MapUtils = {
    map: null,
    markers: [],
    renderMaps: function() {
        let mapContainer = document.getElementById('map'); // 지도를 표시할 di
        let mapOption = {
            center: new kakao.maps.LatLng(34.9773528, 128.3155509), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

        MapUtils.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        let zoomControl = new kakao.maps.ZoomControl();
        MapUtils.map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
    },
    addMarker: function(obj, callback) {
        let imageSrc = "/images/marker-1.svg";
        let imageSize = new kakao.maps.Size(27, 34);
        let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

        // 마커를 생성합니다
        let marker = new kakao.maps.Marker({
            map: MapUtils.map,
            position: new kakao.maps.LatLng(obj.lat, obj.lng),
            title: obj.name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image: markerImage, // 마커 이미지
            id: obj.assetId
        });

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(MapUtils.map);
        obj.marker = marker;

        // 생성된 마커를 배열에 추가합니다
        this.markers.push(obj);

        kakao.maps.event.addListener(marker, 'click', function() {
            // 마커 위에 인포윈도우를 표시합니다
            MapUtils.info(marker);
        });
    },
    // 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
    setMarkers: function(marker) {
        markers[i].setMap(map);
    },
    info: function(marker) {
        let name = (typeof(marker) == 'object') ? marker.getTitle() : marker;
        let obj = MapUtils.findByName(name);
        pagectx.selectedAsset = obj;
        console.log(obj);

        $('#aside-panel').removeClass('d-none');
        $('#asset-name .asset-name').html(obj.name);

        if(isValid(obj.vmsId)) {
            let lastEvent = pagectx.errors.find(item => item.cctvId == obj.vmsId);
            if(isValid(lastEvent)) {
                $('#asset-event .asset-event').html(CodeManager.get('barrier_level',lastEvent.barrierLevel) + ' - '
                    +CodeManager.get('work_jochi',lastEvent.actionCode));
                $('#asset-report .asset-agent-name').html(lastEvent.userid);
                $('#asset-report a').attr('href','/dailyreport/' + lastEvent.dailyreportId);
            }
        }

        // 핑 테스트 관련
        let ip = (Utils.isValid(obj.jsonobj.ip)) ? obj.jsonobj.ip : obj.jsonobj.p;
        if(Utils.isValid(ip)) {
            $('#ping-test-list').html('');
            $('#asset-ip').html(ip);
        }

        // 이동할 위도 경도 위치를 생성합니다
        let moveLatLon = new kakao.maps.LatLng(obj.lat, obj.lng);

        // 지도 중심을 부드럽게 이동시킵니다
        // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
        this.map.panTo(moveLatLon);
    },
    findByName: function (name) {
        for (const marker of MapUtils.markers) {
            if(marker.name == name) return marker;
        }
    }
}
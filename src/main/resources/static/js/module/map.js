let MapUtils = {
    map: null,
    markers: [],
    groups: {},
    selectedMarker: null,
    selectedDir: null,
    markerImage: {},
    selectedMarkerImage: {},
    showOverlay: null,
    allowClick: false,
    renderMaps: function(option) {
        let mapContainer = document.getElementById('map'); // 지도를 표시할 di
        let mapOption = {
            center: new kakao.maps.LatLng(34.9773528, 128.3155509), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

        MapUtils.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        let zoomControl = new kakao.maps.ZoomControl();
        MapUtils.map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

        // 이미지 생성
        let imageSrc = "/images/marker-1.svg";
        let imageSize = new kakao.maps.Size(27, 34);
        let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        this.markerImage.d = markerImage;

        let selectedImageSrc = "/images/marker-3.svg";
        let selectedMarkerImage = new kakao.maps.MarkerImage(selectedImageSrc, imageSize);
        this.selectedMarkerImage.d = selectedMarkerImage;

        if (isValid(option)) this.addClickEvent(option);
    },
    addMarker: function(obj, option) {
        if (!isValid(option)) option = {};
        // 방향값을 설정합니다
        let content = `<div class="dir__image" style="transform-origin: top; transform: rotate(${obj.jsonobj.direction}deg);"></div>`;
        let direction = new kakao.maps.CustomOverlay({
            map: MapUtils.map,
            position: new kakao.maps.LatLng(obj.lat, obj.lng),
            content: content,
            yAnchor: 0.5,
            zIndex: 0,
        })
        obj.dir = direction;

        let markerImage = null;
        if (isValid(option.purpose)) {
            const selCode = option.purpose[obj.purpose];
            if (selCode) {
                const userFile = JSON.parse(selCode.userFile);
                if (isValid(userFile)) {
                    const d = JSON.parse(userFile.default);

                    if (isValid(d)) {
                        // 이미지 생성
                        let imageSrc = `http://${window.location.host}/attach/purpose/0/${d.store}/${d.real}`;
                        let imageSize = new kakao.maps.Size(27, 34);
                        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
                        MapUtils.markerImage[obj.purpose] = markerImage;
                    }

                    const f = JSON.parse(userFile.focus);
                    if (isValid(f)) {
                        // 이미지 생성
                        let imageSrc = `http://${window.location.host}/attach/purpose/0/${f.store}/${f.real}`;
                        let imageSize = new kakao.maps.Size(27, 34);
                        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
                        MapUtils.selectedMarkerImage[obj.purpose] = markerImage;
                    }
                }
            }
        }

        // 마커를 생성합니다
        let marker = new kakao.maps.Marker({
            map: MapUtils.map,
            position: new kakao.maps.LatLng(obj.lat, obj.lng),
            title: obj.name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image: markerImage == null ? this.markerImage.d : this.markerImage[obj.purpose], // 마커 이미지
            id: obj.assetId,
            zIndex: 2,
        });

        //if (markerImage != null) console.log(markerImage)
        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(MapUtils.map);
        obj.marker = marker;

        // 생성된 마커를 배열에 추가합니다
        this.markers.push(obj);

        // 48번 그룹화
        let g = `${obj.lat}__${obj.lng}`;
        if (this.groups.hasOwnProperty(g)) {
            this.groups[g].push(obj);
        } else {
            this.groups[g] = [obj];
        }

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

        // 48번 그룹화
        if (Utils.isValid(this.showOverlay)) this.showOverlay.setMap(null);
        let g = `${obj.lat}__${obj.lng}`;
        let grouped = MapUtils.groups[g];
        let isGrouped = grouped.length !== 1;

        $('#aside-panel').addClass('d-none');
        if (!isGrouped) this.showAside(obj);

        // 핑 테스트 관련
        let ip = (Utils.isValid(obj.jsonobj.ip)) ? obj.jsonobj.ip : obj.jsonobj.p;
        if(Utils.isValid(ip)) {
            $('#ping-test-list').html('');
            $('#asset-ip').html(ip);
        }

        // 47번 선택된마커 이미지 수정
        let selectedMarker = MapUtils.getSelectedMarker();
        let selectedDir = MapUtils.getSelectedDir();

        if (isGrouped) {
            obj = grouped[grouped.length - 1];
        }

        if (!selectedMarker || selectedMarker.marker !== obj.marker) {
            if (selectedMarker) {
                if (isValid(MapUtils.markerImage[selectedMarker.purpose])) {
                    !!selectedMarker.marker && selectedMarker.marker.setImage(MapUtils.markerImage[selectedMarker.purpose]);
                } else {
                    !!selectedMarker.marker && selectedMarker.marker.setImage(MapUtils.markerImage.d);
                }
                !!selectedDir && selectedDir.setContent(`<div class="dir__image" style="transform-origin: top; transform: rotate(${selectedMarker.jsonobj.direction}deg);"></div>`);
            }
            if (isValid(MapUtils.selectedMarkerImage[obj.purpose])) {
                !!obj.marker && obj.marker.setImage(MapUtils.selectedMarkerImage[obj.purpose]);
            } else {
                !!obj.marker && obj.marker.setImage(MapUtils.selectedMarkerImage.d);
            }
            if (!isGrouped) obj.dir.setContent(`<div class="dir__image selected" style="transform-origin: top; transform: rotate(${obj.jsonobj.direction}deg);"></div>`);
        }

        MapUtils.setSelectedMarker(obj);
        if (!isGrouped) MapUtils.setSelectedDir(obj.dir);

        // 48번 선택된 마커 그룹화
        if (isGrouped) {
            let content = `
            <div class="map__overlay">
                <div class="map__title__box">
                    <div class="title">${obj.name.split("-")[0]} (${grouped.length})</div>
                    <div class="icon" onclick="MapUtils.closeOverlay()"></div>
                </div>
                ${grouped.map(d => {
                return `<div class="map__value" onclick="MapUtils.showAside('${d.name}')">${d.name}</div>`
            }).join('')}
            </div>`;

            let overlay = new kakao.maps.CustomOverlay({
                map: MapUtils.map,
                position: new kakao.maps.LatLng(obj.lat, obj.lng),
                content: content,
                yAnchor: 1.5,
                zIndex: 2,
            })
            this.showOverlay = overlay;
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
    },
    showAside: function (marker) {
        let name = (typeof(marker) == 'object') ? marker.name : marker;
        let obj = MapUtils.findByName(name);

        $('#aside-panel').removeClass('d-none');
        $('#asset-name .asset-name').html(obj.name);
        $('#asset-purpose .asset-purpose').html(obj.purpose);

        if(isValid(obj.vmsId)) {
            let errors = pagectx.errors.find(item => item.cctvId == obj.vmsId);
            let situs = pagectx.situs.find(item => item.cctvId == obj.vmsId);

            if (isValid(errors)) {
                $("#asset__event").removeClass("d-none");
                $('#asset-event .asset-event').html(CodeManager.get('barrier_level',errors.barrierLevel) + ' - ' + CodeManager.get('barrier_level',errors.barrierLevel));
                $("#asset-event .asset-jochi").html(CodeManager.get('work_jochi',errors.actionCode));
                $('#asset-report .asset-agent-name').html(errors.userid);
                $("#report-anchor a").attr('href','/dailyreport/' + errors.dailyreportId);
            } else if (isValid(situs)) {
                $("#asset__event").removeClass("d-none");
                $('#asset-event .asset-event').html(CodeManager.get('work_dgubun', situs.workDgubun) + ' - ' + Monitoring.getBarrierLevel(situs).n);
                $("#asset-event .asset-jochi").html(CodeManager.getCode((situs.workDgubun === 'D01') ? "work_jochi" : "situ_result", situs.actionCode, "미조치").n);
                $('#asset-report .asset-agent-name').html(situs.userid);
                $("#report-anchor a").attr('href','/dailyreport/' + situs.dailyreportId);
            } else {
                $("#asset__event").addClass("d-none");
            }
        }

        $("#detail-anchor").click(() => {
            Page.go('/cctv/info/detail2', {item: {name:obj.name}});
        })

        // 50번 방향 보이기
        const selectedDir = MapUtils.getSelectedDir();
        const selectedMarker = MapUtils.getSelectedMarker();
        !!selectedDir && selectedDir.setContent(`<div class="dir__image" style="transform-origin: top; transform: rotate(${selectedMarker.jsonobj.direction}deg);"></div>`);
        obj.dir.setContent(`<div class="dir__image selected" style="transform-origin: top; transform: rotate(${obj.jsonobj.direction}deg);"></div>`);
        MapUtils.setSelectedDir(obj.dir);

    },
    setSelectedMarker: function (marker) {
        this.selectedMarker = marker;
    },
    getSelectedMarker: function() {
        return this.selectedMarker;
    },
    setSelectedDir: function (dir) {
        this.selectedDir = dir;
    },
    getSelectedDir: function () {
        return this.selectedDir;
    },
    closeOverlay: function () {
        if (Utils.isValid(this.showOverlay)) {
            $('#aside-panel').addClass('d-none');
            this.showOverlay.setMap(null);
            this.showOverlay = null;
        }
    },
    addClickEvent: function (option) {
        if (isValid(option.coord)) {
            let marker = new kakao.maps.Marker({position:this.map.getCenter()});
            marker.setMap(this.map);
            kakao.maps.event.addListener(this.map, 'click', function (e) {
                let latlng = e.latLng;
                $(`input[name=lat]`).val(latlng.getLat().toString().substring(0, 10));
                $(`input[name=lng]`).val(latlng.getLng().toString().substring(0, 10));
                marker.setPosition(latlng);
            })
        } else if (isValid(option.coords)) {
            kakao.maps.event.addListener(this.map, 'click', function (e) {
                if (MapUtils.allowClick) {
                    let latlng = e.latLng;
                    $(`input[name=lat]`).val(latlng.getLat().toString().substring(0, 10));
                    $(`input[name=lng]`).val(latlng.getLng().toString().substring(0, 10));
                    MapUtils.allowClick=false;
                }
            })
        }
    },
    clear: function () {
      this.markers.map(d => {
          d.marker.setMap(null);
          d.dir.setMap(null);
      });
      this.markers = [];
      this.groups = {};
    },
}
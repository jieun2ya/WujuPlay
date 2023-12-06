// 마커를 담을 배열입니다
let markers = [];
let datas = [];
let cnt = [];
let page = 1;

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
mapOption = {
    center: new kakao.maps.LatLng(37.51776825666304, 126.88626710468067), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
};

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
//var infowindow = new kakao.maps.InfoWindow({zIndex:1});

document.getElementById("meetAdd").style.display = 'none';
// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        document.getElementById("list-div").style.display = "block";
        document.getElementById("all-div").innerHTML = "";

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);
        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {

        alert('검색 결과 중 오류가 발생했습니다.');
        return;

    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {
    const json = JSON.stringify(places);

    var listEl = document.getElementById('placesList'),
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(),
    bounds = new kakao.maps.LatLngBounds(),
    listStr = '';

    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();

    // 정제된 데이터를 저장할 배열
    var refinedData = [];

    // 각 객체에서 필요한 정보 추출하여 새로운 객체로 만들기
    places.forEach(function(place) {
        var refinedPlace = {
            locationId: place.id,
            addressName: place.address_name,
            placeName: place.place_name,
            x: place.x,
            y: place.y,
            phone: place.phone,
            placeUrl: place.place_url
        };
        // 필요한 정보를 가진 객체를 배열에 추가
        refinedData.push(refinedPlace);
    });
    // 정제된 데이터 확인
    // console.log(refinedData);

    // 검색된 loaction 정보 추가하기
    $.ajax({
        url : "locationAdd",
        type : "POST",
        data : JSON.stringify(refinedData),
        traditional : true,
        contentType: "application/json;charset=UTF-8",
        dataType : "json",
        /* ajax를 통해 받아오는 데이터 형식은 json 형식 */
        success : function(data) {

             if (Array.isArray(data)) {
                 // cnt 배열값 초기화 후 List<Integer> 값을 순서대로 배열에 추가
                 cnt = [];
                 cnt.push(...data);

                 // 지도에 마커표시 (장소이름+모집중인 모임숫자 표시)
                 add(cnt);
             } else {
                 console.error("유효하지 않거나 빈 데이터를 받았습니다.");
             }
        }
    });

    // 지도에 마커표시 (장소이름+모집중인 모임숫자 표시)
    function add(cnt){
        for ( var i=0; i<places.length; i++ ) {
            datas[i] = places[i];
            // 마커를 생성하고 지도에 표시합니다
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
            // LatLngBounds 객체에 좌표를 추가합니다
            bounds.extend(placePosition);

            // 마커와 검색결과 항목에 mouseover 했을때
            // 해당 장소에 인포윈도우에 장소명을 표시합니다
            // mouseout 했을 때는 인포윈도우를 닫습니다
            (function(marker, title, cnt) {
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    var updatedTitle = title + (cnt != 0  ? ' <br/>모집모임:'+cnt : '');
                    displayInfowindow(marker, updatedTitle);
                });

                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    infowindow.close();
                });

                itemEl.onmouseover =  function () {
                    var updatedTitle = title + (cnt != 0  ? ' <br/>모집모임:'+cnt : '');
                    displayInfowindow(marker, updatedTitle);
                };

                itemEl.onmouseout =  function () {
                    infowindow.close();
                };
            })(marker, places[i].place_name, cnt[i]);

            fragment.appendChild(itemEl);

        }
        // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
        listEl.appendChild(fragment);
        //menuEl.scrollTop = 0;

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds);
    }
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {
    const json = JSON.stringify(places);

    var el = document.createElement('li'),
    itemStr = '<div onclick="detailList(' + index
                + ')"><span class="markerbg marker_' + (index+1) + '"></span>'
                +  '<div class="info">'
                +  '<h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone  + ' <a href="'+datas[index].place_url+'" target="_blank">상세보기</a></span>';
    if(cnt[index]!= 0){
        itemStr += '<span>모집중인 모임'+ cnt[index] +'개 </span>';
    }
    itemStr += '</div><div style="display:none;" id="detailList' +index+ '"></div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }
    markers = [];
}


// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;width:160px;height:100px;">' + title + '</div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);
}


var iwContent = '<div style="padding:5px;">Hello World! <br><a href="https://map.kakao.com/link/map/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">길찾기</a></div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다

// 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({
    position : iwPosition,
    content : iwContent
});

// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
infowindow.open(map, marker);

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;
    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }
    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    page = i;
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 모임열기 함수
function meetAdd(index){

    $.ajax({
        url : "checkMemberDetail",
        type : "GET",
        success : function(data) {

            if(data == "3"){
                alert("모임열기는 로그인후 가능합니다.");
            }else if(data == "1"){
                // 모임열기 form 활성화
                const listEl = document.getElementById('placesList');
                removeAllChildNods(listEl);
                const placeData = datas[index];

                document.getElementById("pagination").style.display = 'none';
                document.getElementById("meetAdd").style.display = 'block';
                document.getElementById("placeName").value = placeData.place_name;
                document.getElementById("placeAdd").value = placeData.address_name;
                document.getElementById("locationId").value = placeData.id;

             }else{
                var userConfirmed = confirm("모임열기 및 참여를 위해서는 최초1회 정보등록이 필요합니다. 해당페이지로 이동하시겠습니까?");

                // 확인 버튼을 눌렀을 경우
                if (userConfirmed) {
                    // 원하는 URL로 이동
                    window.location.href = "/auth/signupDetail"; // 대상 URL을 원하는 주소로 변경
                }

             }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Ajax 오류 발생: " + textStatus, errorThrown);
        }
    });

}

// 장소 클릭시 해당 장소에 해당하는 모임list+모임열기 버튼 활성화
function detailList(index){
    let listElement = document.getElementById("detailList"+index);

    if(listElement.style.display == "none"){
        $.ajax({
                url : "listDetail",
                type : "POST",
                data : JSON.stringify({ locationId: datas[index].id }),
                traditional : true,
                contentType: "application/json;charset=UTF-8",
                dataType : "text",
                /* ajax를 통해 받아오는 데이터 형식은 json 형식 */
                success : function(data) {

                    if(data != null){
                        var jsonData = JSON.parse(data);

                         // 해당장소에 모집중인 모임 리스트 추가
                         if (Array.isArray(jsonData)) {
                                let addHTML = ' <span class="add"'+(index+1)+'>' + '<button onclick="meetAdd('+index+');" class="common_btn_blue">모임열기</button>' + '</span>';
                                for(key in jsonData){
                                            addHTML += "<div class='div-all'>"
                                            addHTML += "<div>" + jsonData[key].meetName + " | " + jsonData[key].sportsId.name + "</div>";
                                            addHTML += "<div>" + jsonData[key].meetDate.substr(0, 16) + "</div>";
                                            addHTML += "<div class='div-meetmore'><div id='1'><button class='btn-list2''></button>" + jsonData[key].currNumber + "/" + jsonData[key].maxNumber + "</div>";
                                            addHTML += "<div class='div2'><span class='meet_more' onclick=\"location.href='/meet/meetDetail/" + jsonData[key].meetId + "'\"><span class='plus'>상세보기</span></span></div>";
                                            addHTML += "</div></div>"
                                 }

                                addHTML += '</div>';
                                let listElement = document.getElementById("detailList"+index);
                                listElement.innerHTML =addHTML;

                         } else {
                             console.error("유효하지 않거나 빈 데이터를 받았습니다.");
                         }
                     }

                }
            });
        listElement.style.display = "block";
    }else{
        listElement.style.display = "none";
    }

}
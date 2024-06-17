// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        // 성공 시 처리하는 함수
        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles'); // 삭제 후 목록 페이지로 이동
        }

        // 실패 시 처리하는 함수
        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/articles'); // 실패 시 목록 페이지로 이동
        }

        // DELETE 메서드로 서버에 요청을 보내는 함수 호출
        httpRequest('DELETE', `/blogapi/articles/${id}`, null, success, fail);
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        // 수정할 데이터를 JSON 형식으로 준비
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        // 성공 시 처리하는 함수
        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`); // 수정 후 상세 페이지로 이동
        }

        // 실패 시 처리하는 함수
        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`); // 실패 시 상세 페이지로 이동
        }

        // PUT 메서드로 서버에 요청을 보내는 함수 호출
        httpRequest('PUT', `/blogapi/articles/${id}`, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        // 생성할 데이터를 JSON 형식으로 준비
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        // 성공 시 처리하는 함수
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles'); // 등록 후 목록 페이지로 이동
        };

        // 실패 시 처리하는 함수
        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles'); // 실패 시 목록 페이지로 이동
        };

        // POST 메서드로 서버에 요청을 보내는 함수 호출
        httpRequest('POST', '/blogapi/articles', body, success, fail);
    });
}

// 로그아웃 기능
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        // 로그아웃 요청 성공 시 처리하는 함수
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰 삭제
            localStorage.removeItem('access_token');
            // 쿠키에 저장된 리프레시 토큰 삭제
            deleteCookie('refresh_token');
            // 로그인 페이지로 이동
            location.replace('/login');
        }

        // 로그아웃 요청 실패 시 처리하는 함수
        function fail() {
            alert('로그아웃 실패했습니다.');
        }

        // DELETE 메서드로 서버에 로그아웃 요청을 보내는 함수 호출
        httpRequest('DELETE', '/tokenapi/token', null, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// 쿠키를 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success(); // 성공 상태 코드 처리
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            // 401 Unauthorized와 쿠키에 리프레시 토큰이 있는 경우, 토큰 재발급 요청
            fetch('/tokenapi/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json(); // 재발급 요청 성공 시 응답을 JSON 형태로 처리
                    }
                })
                .then(result => { // 재발급 성공 시 새로운 액세스 토큰으로 로컬 스토리지 갱신 후 다시 요청 보냄
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail()); // 재발급 요청 실패 시 실패 처리 함수 호출
        } else {
            return fail(); // 그 외의 상황에서는 실패 상태 코드 처리
        }
    });
}

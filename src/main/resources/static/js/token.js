const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token) // URL 쿼리 파라미터에서 'token' 값을 가져와 로컬 스토리지에 저장
}

/**
 * 현재 페이지 URL의 쿼리 파라미터에서 주어진 key에 해당하는 값을 반환하는 함수입니다.
 * @param {string} key 가져올 쿼리 파라미터의 키
 * @returns {string | null} 해당 키에 대응하는 쿼리 파라미터 값, 없으면 null
 * 페이지에서 전달된 토큰 값을 가져와 로컬 스토리지에 저장
 */
function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}

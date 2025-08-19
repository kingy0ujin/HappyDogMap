package com.example.pet_project_frontend.presentation.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

@Composable
fun MapScreen() {
	AndroidView(
		modifier = Modifier.fillMaxSize(),
		factory = { context ->
			MapView(context)
		},
		update = { mapView ->
			// MapView의 생명주기와 준비 상태를 감지하는 콜백을 등록합니다.
			mapView.start(object : MapLifeCycleCallback() {
				// [가장 중요] 지도 API가 시작되거나 실행 중에 에러가 발생하면 이 함수가 호출됩니다.
				override fun onMapError(error: Exception) {
					Log.e("KakaoMapError", "지도 에러 발생: ${error.message}", error)
				}
			}, object : KakaoMapReadyCallback() {
				// 지도가 성공적으로 준비되었을 때 필요한 작업을 여기에 추가할 수 있습니다.
				override fun onMapReady(kakaoMap: com.kakao.vectormap.KakaoMap) {
					Log.d("KakaoMapSuccess", "지도 준비 완료!")
				}
			})
		}
	)
}
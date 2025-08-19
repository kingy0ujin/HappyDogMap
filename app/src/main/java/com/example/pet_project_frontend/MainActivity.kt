package com.example.pet_project_frontend

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pet_project_frontend.core.navigation.BottomNavigation
import com.example.pet_project_frontend.core.navigation.PetCareNavHost
import com.example.pet_project_frontend.core.theme.PetCareTheme
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @Named("NATIVE_APP_KEY")
    lateinit var nativeAppKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", "새로운 키 해시: $keyHash")

        KakaoMapSdk.init(this, nativeAppKey)

        setContent {
            PetCareTheme {
                // 1. 네비게이션을 관리하는 컨트롤러를 만듭니다.
                val navController = rememberNavController()
                // 2. 현재 어떤 탭이 선택되었는지 상태를 확인합니다.
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 3. 화면의 전체적인 구조(하단 탭 포함)를 정의합니다.
                Scaffold(
                    bottomBar = {
                        // 4. BottomNavigation에 네비게이션 컨트롤러를 전달합니다.
                        BottomNavigation(
                            currentRoute = currentRoute ?: "",
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    // 백스택 관리 (뒤로가기 시 앱이 종료되도록)
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // 5. Scaffold의 내용 부분에 네비게이션 시스템을 배치합니다.
                    PetCareNavHost(
                        navController = navController,
                        // 아래 Modifier에 적용해줍니다.
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
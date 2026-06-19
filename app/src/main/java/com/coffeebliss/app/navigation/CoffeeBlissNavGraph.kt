package com.coffeebliss.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coffeebliss.app.CoffeeBlissApplication
import com.coffeebliss.app.ui.screens.HomeScreen
import com.coffeebliss.app.ui.screens.LoginScreen
import com.coffeebliss.app.ui.screens.RedeemScreen
import com.coffeebliss.app.ui.screens.RegisterScreen
import com.coffeebliss.app.util.SessionManager
import com.coffeebliss.app.viewmodel.AuthViewModel
import com.coffeebliss.app.viewmodel.HomeViewModel
import com.coffeebliss.app.viewmodel.HomeViewModelFactory
import com.coffeebliss.app.viewmodel.RedeemViewModel
import com.coffeebliss.app.viewmodel.ViewModelFactory

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home/{memberId}"
    const val REDEEM = "redeem/{memberId}"

    fun home(memberId: Long) = "home/$memberId"
    fun redeem(memberId: Long) = "redeem/$memberId"
}

@Composable
fun CoffeeBlissNavGraph(
    application: CoffeeBlissApplication,
    sessionManager: SessionManager,
    navController: NavHostController = rememberNavController()
) {
    val factory = ViewModelFactory(application)
    val startDestination = sessionManager.loggedInMemberId?.let { Routes.home(it) } ?: Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            val authViewModel: AuthViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = { memberId ->
                    navController.navigate(Routes.home(memberId)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            val authViewModel: AuthViewModel = viewModel(factory = factory)
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = { memberId ->
                    navController.navigate(Routes.home(memberId)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.HOME,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(application, memberId)
            )
            val member by homeViewModel.member.collectAsState()

            HomeScreen(
                viewModel = homeViewModel,
                memberId = memberId,
                onNavigateToRedeem = {
                    navController.navigate(Routes.redeem(memberId))
                },
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.REDEEM,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(application, memberId)
            )
            val redeemViewModel: RedeemViewModel = viewModel(factory = factory)
            val member by homeViewModel.member.collectAsState()

            RedeemScreen(
                viewModel = redeemViewModel,
                memberId = memberId,
                currentPoints = member?.totalPoints ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

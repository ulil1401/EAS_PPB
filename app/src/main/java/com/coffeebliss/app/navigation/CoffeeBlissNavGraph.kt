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
import com.coffeebliss.app.ui.screens.AddMemberScreen
import com.coffeebliss.app.ui.screens.EditProfileScreen
import com.coffeebliss.app.ui.screens.MemberCardScreen
import com.coffeebliss.app.ui.screens.MemberListScreen
import com.coffeebliss.app.ui.screens.RewardScreen
import com.coffeebliss.app.ui.screens.SplashScreen
import com.coffeebliss.app.ui.screens.TransactionScreen
import com.coffeebliss.app.viewmodel.AddMemberViewModel
import com.coffeebliss.app.viewmodel.EditProfileViewModel
import com.coffeebliss.app.viewmodel.MemberDetailViewModel
import com.coffeebliss.app.viewmodel.MemberDetailViewModelFactory
import com.coffeebliss.app.viewmodel.MemberListViewModel
import com.coffeebliss.app.viewmodel.RedeemViewModel
import com.coffeebliss.app.viewmodel.TransactionViewModel
import com.coffeebliss.app.viewmodel.ViewModelFactory

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val ADD_MEMBER = "add_member"
    const val MEMBER_CARD = "member_card/{memberId}"
    const val EDIT_PROFILE = "edit_profile/{memberId}"
    const val TRANSACTION = "transaction/{memberId}"
    const val REWARD = "reward/{memberId}"

    fun memberCard(memberId: Long) = "member_card/$memberId"
    fun editProfile(memberId: Long) = "edit_profile/$memberId"
    fun transaction(memberId: Long) = "transaction/$memberId"
    fun reward(memberId: Long) = "reward/$memberId"
}

@Composable
fun CoffeeBlissNavGraph(
    application: CoffeeBlissApplication,
    navController: NavHostController = rememberNavController()
) {
    val factory = ViewModelFactory(application)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            val viewModel: MemberListViewModel = viewModel(factory = factory)
            MemberListScreen(
                viewModel = viewModel,
                onAddMember = { navController.navigate(Routes.ADD_MEMBER) },
                onMemberClick = { memberId ->
                    navController.navigate(Routes.memberCard(memberId))
                }
            )
        }

        composable(Routes.ADD_MEMBER) {
            val viewModel: AddMemberViewModel = viewModel(factory = factory)
            AddMemberScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onSaved = { memberId ->
                    navController.navigate(Routes.memberCard(memberId)) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(
            route = Routes.MEMBER_CARD,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val detailFactory = MemberDetailViewModelFactory(application, memberId)
            val viewModel: MemberDetailViewModel = viewModel(factory = detailFactory)
            val member by viewModel.member.collectAsState()
            val transactions by viewModel.transactions.collectAsState()
            val redemptions by viewModel.redemptions.collectAsState()

            MemberCardScreen(
                member = member,
                transactions = transactions,
                redemptions = redemptions,
                onNavigateBack = { navController.popBackStack() },
                onNavigateTransaction = { navController.navigate(Routes.transaction(memberId)) },
                onNavigateReward = { navController.navigate(Routes.reward(memberId)) },
                onNavigateEditProfile = { navController.navigate(Routes.editProfile(memberId)) }
            )
        }

        composable(
            route = Routes.EDIT_PROFILE,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val detailFactory = MemberDetailViewModelFactory(application, memberId)
            val viewModel: EditProfileViewModel = viewModel(factory = detailFactory)

            EditProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TRANSACTION,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val detailFactory = MemberDetailViewModelFactory(application, memberId)
            val detailViewModel: MemberDetailViewModel = viewModel(factory = detailFactory)
            val txViewModel: TransactionViewModel = viewModel(factory = detailFactory)
            val member by detailViewModel.member.collectAsState()

            TransactionScreen(
                viewModel = txViewModel,
                memberName = member?.name ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.REWARD,
            arguments = listOf(navArgument("memberId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: return@composable
            val detailFactory = MemberDetailViewModelFactory(application, memberId)
            val detailViewModel: MemberDetailViewModel = viewModel(factory = detailFactory)
            val redeemViewModel: RedeemViewModel = viewModel(factory = detailFactory)
            val member by detailViewModel.member.collectAsState()

            RewardScreen(
                viewModel = redeemViewModel,
                memberId = memberId,
                memberName = member?.name ?: "",
                currentPoints = member?.points ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

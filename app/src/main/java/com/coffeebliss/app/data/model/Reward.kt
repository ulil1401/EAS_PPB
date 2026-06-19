package com.coffeebliss.app.data.model

data class Reward(
    val name: String,
    val pointsRequired: Int,
    val description: String
)

object RewardCatalog {
    val rewards = listOf(
        Reward(
            name = "Espresso",
            pointsRequired = 50,
            description = "Espresso gratis 1 gelas"
        ),
        Reward(
            name = "Cappuccino",
            pointsRequired = 100,
            description = "Cappuccino gratis 1 gelas"
        ),
        Reward(
            name = "Latte Gratis",
            pointsRequired = 150,
            description = "Latte gratis 1 gelas"
        )
    )
}

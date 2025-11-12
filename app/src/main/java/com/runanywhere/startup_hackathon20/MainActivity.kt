package com.runanywhere.startup_hackathon20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.runanywhere.startup_hackathon20.ui.theme.Startup_hackathon20Theme
import android.util.Log
import com.runanywhere.startup_hackathon20.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "🚀 onCreate() starting - enabling UI")
        enableEdgeToEdge()
        setContent {
            Startup_hackathon20Theme {
                NeurolensApp()
            }
        }
        Log.i("MainActivity", "✅ Main UI displayed - background initialization continuing...")
    }
}

@Composable
fun NeurolensApp() {
    val navController = rememberNavController()
    val viewModel: ChatViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToFeature = { feature ->
                    navController.navigate(feature)
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("emotional_journal") {
            EmotionalJournalScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable("task_management") {
            TaskManagementScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable("breathing_exercises") {
            BreathingExercisesScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("goal_setting") {
            GoalSettingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable("smart_reminders") {
            SmartRemindersScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("voice_journaling") {
            VoiceJournalingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
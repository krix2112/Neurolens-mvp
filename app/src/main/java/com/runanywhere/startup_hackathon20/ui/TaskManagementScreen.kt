package com.runanywhere.startup_hackathon20.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.runanywhere.startup_hackathon20.ChatViewModel
import com.runanywhere.startup_hackathon20.ui.theme.*

data class Task(
    val id: Int,
    val title: String,
    val priority: String,
    var isCompleted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagementScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    var taskInput by remember { mutableStateOf("") }
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Morning meditation", "High", false),
                Task(2, "Review project plans", "Medium", false),
                Task(3, "Call mom", "High", false),
                Task(4, "Grocery shopping", "Low", true)
            )
        )
    }
    var showAIHelper by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Task Management",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "${tasks.count { !it.isCompleted }} tasks pending",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeutralGray
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CalmBlue
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAIHelper = !showAIHelper }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "AI Helper",
                            tint = MotivatedOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkGray
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGray)
        ) {
            // AI Helper Card
            AnimatedVisibility(visible = showAIHelper) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MotivatedOrange.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MotivatedOrange
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "AI Task Tips",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MotivatedOrange
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "🎯 Tackle your highest priority task first\n" +
                                    "⏰ Use Pomodoro: 25 min focus, 5 min break\n" +
                                    "📊 Break large tasks into smaller steps\n" +
                                    "🌅 Do hard tasks during peak energy hours",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkGray
                        )
                    }
                }
            }

            // Priority Stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PriorityBadge(
                        "High",
                        tasks.count { it.priority == "High" && !it.isCompleted },
                        AngryRed
                    )
                    PriorityBadge(
                        "Medium",
                        tasks.count { it.priority == "Medium" && !it.isCompleted },
                        WarmOrange
                    )
                    PriorityBadge(
                        "Low",
                        tasks.count { it.priority == "Low" && !it.isCompleted },
                        WarmGreen
                    )
                }
            }

            // Task List
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = {
                            tasks = tasks.map {
                                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted)
                                else it
                            }
                        },
                        onDelete = {
                            tasks = tasks.filter { it.id != task.id }
                        }
                    )
                }
            }

            // Add Task Input
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = taskInput,
                        onValueChange = { taskInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("Add a new task...", color = NeutralGray)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = WarmGreen,
                            unfocusedIndicatorColor = NeutralGray.copy(alpha = 0.3f)
                        )
                    )

                    FloatingActionButton(
                        onClick = {
                            if (taskInput.isNotBlank()) {
                                tasks = tasks + Task(
                                    id = tasks.maxOfOrNull { it.id }?.plus(1) ?: 1,
                                    title = taskInput,
                                    priority = "Medium",
                                    isCompleted = false
                                )
                                taskInput = ""
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = if (taskInput.isBlank()) NeutralGray else WarmGreen,
                        contentColor = Color.White
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityBadge(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = NeutralGray
            )
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val priorityColor = when (task.priority) {
        "High" -> AngryRed
        "Medium" -> WarmOrange
        "Low" -> WarmGreen
        else -> NeutralGray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) NeutralGray.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = WarmGreen
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (task.isCompleted) NeutralGray else DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = priorityColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = task.priority,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = AngryRed.copy(alpha = 0.5f)
                )
            }
        }
    }
}
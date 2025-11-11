# Frontend Integration Prompt for AI Assistant

**Context**: You are helping integrate a NeuroLens2 Android application that uses Jetpack Compose
for UI and RunAnywhere SDK for on-device AI inference.

---

## 📋 Project Overview

**Project Name**: NeuroLens2  
**Framework**: Android (Jetpack Compose + Material3)  
**Backend**: RunAnywhere SDK for on-device LLM inference  
**Language**: Kotlin  
**Architecture**: MVVM (ViewModel + StateFlow)

---

## 🎯 Current Issue

The frontend is not visible when the application is built and run. This typically indicates one of
the following problems:

1. **Build/Configuration Issues**: Missing dependencies, incorrect Gradle setup, or compilation
   errors
2. **Runtime Initialization Issues**: SDK failing to initialize, causing app crashes
3. **UI Rendering Issues**: Compose UI not being set up correctly in MainActivity
4. **Manifest Configuration Issues**: Application class or MainActivity not properly declared

---

## 🏗️ Application Architecture

### Entry Point Flow

```
1. MyApplication.onCreate() → Initializes RunAnywhere SDK
2. MainActivity.onCreate() → Sets up Compose UI
3. ChatScreen() composable → Main UI entry point
4. ChatViewModel → Manages state and business logic
```

### Key Files

#### 1. MyApplication.kt

- **Purpose**: Application-level initialization
- **Key Responsibilities**:
    - Initialize RunAnywhere SDK with development API key
    - Register LlamaCppServiceProvider
    - Register AI models (SmolLM2 360M, Qwen 2.5 0.5B)
    - Scan for downloaded models
- **Critical**: Must be declared in AndroidManifest.xml as `android:name=".MyApplication"`

#### 2. MainActivity.kt

- **Purpose**: Single Activity hosting Compose UI
- **Key Components**:
    - `ChatScreen()`: Main composable with Material3 Scaffold
    - `ModelSelector()`: Shows available AI models
    - `MessageBubble()`: Displays chat messages
    - `ModelItem()`: Individual model list item
- **Critical**: Must call `setContent { ChatScreen() }` in onCreate()

#### 3. ChatViewModel.kt

- **Purpose**: State management and business logic
- **StateFlow Properties**:
    - `messages`: List of chat messages
    - `isLoading`: Loading indicator state
    - `availableModels`: List of registered models
    - `downloadProgress`: Model download progress (0.0-1.0)
    - `currentModelId`: Currently loaded model ID
    - `statusMessage`: User-facing status text
- **Key Functions**:
    - `downloadModel()`: Downloads AI model with progress tracking
    - `loadModel()`: Loads model into memory (or fallback to mock mode)
    - `sendMessage()`: Sends user message and gets AI response
    - `refreshModels()`: Reloads available models list

#### 4. Services Layer

- `HookService.kt`: Orchestrates emotion detection and advice
- `EmotionService.kt`: AI-powered emotion detection
- `ReminderService.kt`: Generates contextual advice
- `ConversionState.kt`: Data class for conversation state

---

## 🔧 Required Dependencies

### Critical .aar Files (in app/libs/)

```
RunAnywhereKotlinSDK-release.aar (4.01MB)
runanywhere-llm-llamacpp-release.aar (2.12MB)
```

### Key Dependencies (from build.gradle.kts)

```kotlin
// Compose
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.material3)
implementation(libs.androidx.activity.compose)
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

// Networking (for SDK)
implementation("io.ktor:ktor-client-core:3.0.3")
implementation("io.ktor:ktor-client-okhttp:3.0.3")

// Room, WorkManager (for SDK)
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.work:work-runtime-ktx:2.10.0")
```

---

## ⚠️ Common Integration Problems & Solutions

### Problem 1: Blank Screen

**Cause**: MainActivity not setting up Compose content  
**Solution**: Verify MainActivity.kt has:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
        Startup_hackathon20Theme {
            ChatScreen()
        }
    }
}
```

### Problem 2: App Crashes on Launch

**Cause**: SDK initialization failure  
**Solution**:

- Check Logcat for "MyApp" tag errors
- Verify both .aar files exist in app/libs/
- Ensure AndroidManifest.xml has `android:name=".MyApplication"`
- Confirm `android:largeHeap="true"` is set in manifest

### Problem 3: Models Not Appearing

**Cause**: SDK not initialized or async delay  
**Solution**:

- Wait 3-5 seconds after app launch
- Check Logcat for "SDK initialized successfully"
- Tap "Refresh" button in Models section
- Verify `registerModels()` is called in MyApplication

### Problem 4: Build Failures

**Cause**: Missing dependencies or Gradle sync issues  
**Solution**:

```bash
./gradlew clean
./gradlew --refresh-dependencies
./gradlew assembleDebug
```

### Problem 5: Input Field Disabled

**Cause**: Normal behavior - no model loaded yet  
**Solution**: This is expected. Load a model via "Models" → "Download" → "Load"

---

## 🧪 Testing Checklist

After integration, verify:

1. **App Launches**: App opens without crashes
2. **UI Visible**: "AI Chat" title and status bar are visible
3. **Models Button**: Tapping "Models" shows 2 models
4. **Status Message**: Shows "Ready — Download or load a model"
5. **Model Download**: Can download SmolLM2 model with progress bar
6. **Model Loading**: Can load model, status changes to "✅ Model loaded successfully"
7. **Message Sending**: Can type and send messages
8. **AI Response**: Receives streamed AI responses word-by-word

---

## 🐛 Debug Commands

```bash
# Check Logcat for errors
adb logcat | grep -E "MyApp|ChatVM|AndroidRuntime"

# Verify app installation
adb shell pm list packages | grep startup_hackathon20

# Check model files
adb shell ls /storage/emulated/0/Android/data/com.runanywhere.startup_hackathon20/files/models/

# Clear app data (if needed)
adb shell pm clear com.runanywhere.startup_hackathon20

# Reinstall app
./gradlew installDebug
```

---

## 📱 Device Requirements

- **Minimum**: Android 7.0 (API 24), 2GB RAM
- **Recommended**: Physical device with 4GB+ RAM, Android 12+
- **Note**: Emulators may have memory constraints for model loading

---

## 🚀 Integration Instructions

### Step 1: Verify File Structure

```
NeuroLens2/
├── app/libs/
│   ├── RunAnywhereKotlinSDK-release.aar ✓
│   └── runanywhere-llm-llamacpp-release.aar ✓
├── app/src/main/
│   ├── AndroidManifest.xml ✓
│   └── java/com/runanywhere/startup_hackathon20/
│       ├── MainActivity.kt ✓
│       ├── ChatViewModel.kt ✓
│       ├── MyApplication.kt ✓
│       └── services/ ✓
```

### Step 2: Verify AndroidManifest.xml

Must have:

```xml
<application
    android:name=".MyApplication"
    android:largeHeap="true"
    ...>
    <activity android:name=".MainActivity" android:exported="true" ...>
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>

<uses-permission android:name="android.permission.INTERNET" />
```

### Step 3: Sync and Build

```bash
./gradlew --refresh-dependencies
./gradlew assembleDebug
```

### Step 4: Install and Test

```bash
adb devices
./gradlew installDebug
adb logcat | grep -E "MyApp|ChatVM"
```

### Step 5: Verify UI

1. App should show "AI Chat" title
2. Status bar should show "Ready — Download or load a model"
3. Tapping "Models" should show 2 models
4. Download and load a model
5. Send a test message

---

## 🎨 UI Component Structure

```
ChatScreen (Scaffold)
├── TopAppBar
│   ├── Title: "AI Chat"
│   └── Actions: "Models" button
├── StatusBar (Surface)
│   ├── statusMessage text
│   └── downloadProgress (LinearProgressIndicator)
├── ModelSelector (conditional, collapsible)
│   ├── Header with "Refresh" button
│   └── LazyColumn of ModelItem cards
├── Messages (LazyColumn)
│   └── MessageBubble cards
└── Input Row
    ├── TextField (disabled until model loaded)
    └── Send Button (disabled until model loaded)
```

---

## 🔍 Key State Flows

### App Launch Flow

```
1. MyApplication.onCreate() - runs on IO dispatcher
2. SDK initializes asynchronously
3. Models registered (SmolLM2, Qwen 2.5)
4. MainActivity launches
5. ChatViewModel.init() calls loadAvailableModels()
6. UI renders with statusMessage = "Initializing..."
7. After 2-3 seconds: statusMessage = "Ready — Download or load a model"
```

### Model Download Flow

```
1. User taps "Download" on a model
2. ChatViewModel.downloadModel(modelId) called
3. downloadProgress StateFlow updates (0.0 → 1.0)
4. statusMessage shows "Downloading: X%"
5. On completion: statusMessage = "✅ Download complete — tap Load"
6. Model's isDownloaded property becomes true
```

### Model Loading Flow

```
1. User taps "Load" on downloaded model
2. ChatViewModel.loadModel(modelId) called
3. statusMessage = "⏳ Loading model..."
4. Attempts to load via reflection (due to SDK limitations)
5. Success: currentModelId set, statusMessage = "✅ Model loaded successfully"
6. Failure: currentModelId = "mock", statusMessage = "⚡ Mock Mode Active"
7. Input field becomes enabled
```

### Message Send Flow

```
1. User types message and taps "Send"
2. User message added to messages list
3. isLoading = true
4. If mock mode: HookService generates canned response
5. If live mode: RunAnywhere.generateStream() called
6. Tokens streamed and appended to messages list
7. isLoading = false
```

---

## 📊 Expected Logcat Output

### Successful Initialization

```
I/MyApp: ⚙️ Initializing RunAnywhere SDK...
I/MyApp: ✅ LlamaCpp service provider registered
I/MyApp: 📦 Registered SmolLM2 360M Q8_0
I/MyApp: 📦 Registered Qwen 2.5 0.5B Instruct Q6_K
I/MyApp: ✅ Model registration complete
I/MyApp: 🔍 Scanned for downloaded models
I/MyApp: 🎯 SDK initialized successfully
I/ChatVM: 📦 Models available: [SmolLM2 360M Q8_0, Qwen 2.5 0.5B Instruct Q6_K]
```

### Successful Model Download

```
I/ChatVM: 🚀 Starting model download: <modelId>
D/ChatVM: 📦 Download progress: 5%
D/ChatVM: 📦 Download progress: 10%
...
I/ChatVM: 🎯 Model download complete: <modelId>
```

### Successful Model Load

```
D/ChatVM: 🧠 Attempting to load model: <modelId>
I/ChatVM: 🔗 Resolved model name: SmolLM2 360M Q8_0
I/ChatVM: 📂 Found local model file: /storage/.../smollm2-360m-q8_0.gguf
I/ChatVM: 📦 loadModel(path) invoked via reflection → true
I/ChatVM: 🎯 Model successfully loaded at <path>
```

---

## 🎯 Success Criteria

The integration is complete when:

1. ✅ App launches without crashes
2. ✅ UI is fully visible with all components
3. ✅ Status message shows "Ready — Download or load a model"
4. ✅ Models button shows 2 available models
5. ✅ Can download SmolLM2 model (119 MB)
6. ✅ Can load model successfully
7. ✅ Can send messages and receive AI responses
8. ✅ No errors in Logcat during normal operation

---

## 🚨 Critical Points

1. **Do NOT modify** `MyApplication.kt` - SDK initialization is fragile
2. **Do NOT remove** `.aar` files from `app/libs/`
3. **Do NOT change** package name or namespace
4. **Do ensure** `largeHeap="true"` in manifest
5. **Do ensure** both .aar files are tracked in version control
6. **Do test** on physical device if emulator fails
7. **Do check** Logcat for every issue

---

## 📝 Integration Prompt for AI

**Use this prompt when asking an AI assistant to help with integration:**

```
I need help integrating the NeuroLens2 Android application. The frontend is not visible when I build and run the app.

Project details:
- Android app using Jetpack Compose + Material3
- Backend: RunAnywhere SDK for on-device AI
- Language: Kotlin
- MVVM architecture with StateFlow

Current setup:
- Two .aar files in app/libs/ (RunAnywhereKotlinSDK-release.aar and runanywhere-llm-llamacpp-release.aar)
- MyApplication.kt initializes SDK
- MainActivity.kt has ChatScreen composable
- ChatViewModel.kt manages state

Expected behavior:
- App should launch and show "AI Chat" interface
- Should see Models button and status message
- Can download and load AI models
- Can send messages to AI

Current issue:
[Describe specific symptoms: blank screen, crash, build error, etc.]

Please help me:
1. Diagnose the root cause
2. Verify configuration files
3. Check for common integration errors
4. Provide step-by-step fixes
5. Verify the solution works

Reference documentation is in FRONTEND_BACKEND_INTEGRATION.md
```

---

## 📚 Additional Resources

- Full integration guide: `FRONTEND_BACKEND_INTEGRATION.md`
- SDK documentation: `RUNANYWHERE_SDK_COMPLETE_GUIDE.md`
- Quick start: `app/src/main/java/com/runanywhere/startup_hackathon20/QUICK_START_ANDROID.md`

---

**Last Updated**: December 2024  
**Version**: 1.0

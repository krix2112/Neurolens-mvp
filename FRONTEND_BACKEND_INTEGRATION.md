# Frontend-Backend Integration Guide for NeuroLens2

## 🎯 Overview

This guide explains how to properly integrate and run the NeuroLens2 Android application. This app
combines:

- **Frontend**: Jetpack Compose UI with Material3 design
- **Backend**: RunAnywhere SDK for on-device AI inference
- **Services**: Emotion detection and conversation state management

## ⚠️ Common Issue: Frontend Not Visible

If your teammate reports that "the frontend is not visible," it typically means one of the
following:

### Issue 1: App Not Launching

**Symptoms**: App crashes immediately or doesn't start
**Solutions**:

1. Check that `MyApplication` is properly declared in `AndroidManifest.xml`
2. Verify all dependencies are properly synced
3. Check Logcat for initialization errors

### Issue 2: Blank Screen

**Symptoms**: App launches but shows nothing
**Solutions**:

1. Verify `MainActivity` is setting content with `setContent {}`
2. Check that Compose dependencies are included
3. Ensure theme is properly applied

### Issue 3: Build Failures

**Symptoms**: Gradle build fails
**Solutions**:

1. Ensure all `.aar` files are in `app/libs/` directory
2. Run `./gradlew clean build`
3. Sync Gradle files in Android Studio

---

## 📋 Prerequisites

### Required Software

- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: Version 17 (configured in project)
- **Gradle**: 8.x (wrapper included)
- **Android SDK**: API 24+ (minimum), API 36 (target)

### Required Files

The following files MUST be present in `app/libs/`:

```
app/libs/RunAnywhereKotlinSDK-release.aar (4.01MB)
app/libs/runanywhere-llm-llamacpp-release.aar (2.12MB)
```

**If these files are missing**, download them from the RunAnywhere SDK GitHub releases.

---

## 🚀 Step-by-Step Integration

### Step 1: Clone and Setup

```bash
# Clone the repository
git clone <repository-url>
cd NeuroLens2

# Verify project structure
ls -la app/libs/  # Should show two .aar files
```

### Step 2: Verify AndroidManifest.xml

Ensure `app/src/main/AndroidManifest.xml` contains:

```xml
<application
    android:name=".MyApplication"
    android:largeHeap="true"
    ...>
    <activity
        android:name=".MainActivity"
        android:exported="true"
        ...>
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

**Critical attributes**:

- `android:name=".MyApplication"` - Initializes SDK on app start
- `android:largeHeap="true"` - Required for AI model loading
- `android:exported="true"` - Makes MainActivity launchable

### Step 3: Sync Gradle Dependencies

```bash
# Option A: Command line
./gradlew --refresh-dependencies

# Option B: Android Studio
File → Sync Project with Gradle Files
```

### Step 4: Build the Project

```bash
# Debug build
./gradlew assembleDebug

# Or in Android Studio: Build → Make Project (Ctrl+F9)
```

### Step 5: Run on Device/Emulator

```bash
# List connected devices
adb devices

# Install debug APK
./gradlew installDebug

# Or in Android Studio: Run → Run 'app' (Shift+F10)
```

---

## 🏗️ Architecture Overview

### Application Initialization Flow

```
MyApplication.onCreate()
    ↓
Initialize RunAnywhere SDK
    ↓
Register LlamaCppServiceProvider
    ↓
Register AI models (SmolLM2, Qwen 2.5)
    ↓
Scan for downloaded models
    ↓
MainActivity launches
    ↓
ChatScreen composable renders
```

### UI Component Hierarchy

```
MainActivity (Activity)
    ↓
ChatScreen (Composable)
    ├── TopAppBar (with Models button)
    ├── StatusBar (shows download/loading state)
    ├── ModelSelector (collapsible, shows available models)
    ├── LazyColumn (message list)
    └── TextField + Button (input area)
```

### ViewModel State Management

`ChatViewModel` manages:

- `messages`: List of chat messages (StateFlow)
- `isLoading`: Loading state during generation
- `availableModels`: List of registered models
- `downloadProgress`: Model download progress (0.0 to 1.0)
- `currentModelId`: Currently loaded model ID
- `statusMessage`: User-facing status text

### Backend Services

1. **HookService**: Orchestrates emotion detection and advice generation
2. **EmotionService**: Detects emotion from text using AI model
3. **ReminderService**: Provides task-based advice based on emotion

---

## 🔧 Configuration Files

### build.gradle.kts (app level)

Key configurations:

```kotlin
android {
    namespace = "com.runanywhere.startup_hackathon20"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

### settings.gradle.kts

Ensure repositories are configured:

```kotlin
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

---

## 🧪 Testing the Integration

### Test 1: App Launches Successfully

1. Install and launch the app
2. You should see "AI Chat" at the top
3. Status should show: "Ready — Download or load a model"

### Test 2: Model Download

1. Tap "Models" button in top bar
2. You should see 2 models listed:
    - SmolLM2 360M Q8_0 (119 MB)
    - Qwen 2.5 0.5B Instruct Q6_K (374 MB)
3. Tap "Download" on SmolLM2
4. Progress bar should appear showing download progress

### Test 3: Model Loading

1. After download completes, tap "Load"
2. Status should show: "✅ Model loaded successfully"
3. Input field should become enabled

### Test 4: Message Sending

1. Type a message like "Hello"
2. Tap "Send"
3. AI response should stream in word-by-word

---

## 🐛 Troubleshooting

### Problem: App crashes on launch

**Solution**:

```bash
# Check Logcat for errors
adb logcat | grep "MyApp\|ChatVM\|AndroidRuntime"

# Common causes:
# - Missing .aar files in app/libs/
# - SDK initialization failure
# - Memory issues (try on physical device)
```

### Problem: Models not appearing in list

**Solution**:

1. Wait 3-5 seconds after app launch (SDK initializes asynchronously)
2. Tap "Refresh" button
3. Check Logcat: `adb logcat | grep "MyApp"`
4. Verify `registerModels()` is called in `MyApplication.kt`

### Problem: Download fails

**Solution**:

```xml
<!-- Verify in AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
```

- Check internet connectivity
- Check available storage (need ~400MB free)
- Try smaller model first (SmolLM2)

### Problem: Model loads but generation fails

**Causes**:

- Insufficient device memory (try physical device instead of emulator)
- Model file corrupted (delete and re-download)
- Emulator has limited RAM (increase in AVD settings)

**Check model file**:

```bash
# Find model storage location
adb shell
cd /storage/emulated/0/Android/data/com.runanywhere.startup_hackathon20/files/models/llama_cpp/
ls -lh
```

### Problem: Frontend shows but input is disabled

**Solution**:

- This is normal behavior until a model is loaded
- Load a model by tapping "Models" → Select model → "Load"
- Check status message for current state

### Problem: Blank screen / UI not rendering

**Solution**:

```kotlin
// Verify MainActivity.kt has:
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

---

## 📱 Device Requirements

### Minimum Requirements

- Android 7.0 (API 24) or higher
- 2GB RAM (4GB+ recommended)
- 500MB free storage
- Internet connection (for model download)

### Recommended Setup

- Physical device (emulators may have memory constraints)
- Android 12+ (API 31+)
- 6GB+ RAM
- ARM64 processor (for optimal performance)

---

## 🔐 Permissions Required

The app requires these permissions:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
                 android:maxSdkVersion="28" />
```

**Note**: Storage permission is only needed on Android 7-9. Android 10+ uses scoped storage
automatically.

---

## 📊 Monitoring and Debugging

### Enable Detailed Logging

```bash
# Filter for app-specific logs
adb logcat -s MyApp:I ChatVM:D HookService:D

# Clear log buffer first
adb logcat -c

# Save logs to file
adb logcat -d > debug_log.txt
```

### Key Log Tags

- `MyApp`: SDK initialization
- `ChatVM`: ViewModel operations (download, load, generation)
- `HookService`: Emotion detection and advice
- `EmotionService`: AI inference for emotion
- `ReminderService`: Task advice generation

### Check App State

```bash
# Check if app is running
adb shell ps | grep startup_hackathon20

# Check app data size
adb shell du -sh /data/data/com.runanywhere.startup_hackathon20

# Check model files
adb shell ls -lh /storage/emulated/0/Android/data/com.runanywhere.startup_hackathon20/files/
```

---

## 🔄 Mock Mode vs Live Mode

The app supports two modes:

### Mock Mode

- Activated when model loading fails or is skipped
- Uses `HookService` for simple emotion detection
- Provides canned advice responses
- Good for UI testing without downloading models
- Status: "⚡ Mock Mode Active"

### Live Mode

- Activated after successfully loading an AI model
- Uses actual on-device inference
- Provides real-time streaming responses
- Requires model download (119MB - 374MB)
- Status: "✅ Model loaded successfully — Live mode active"

**How to switch modes**:

- Mock mode is automatic fallback
- Live mode requires: Models → Download → Load

---

## 🚨 Critical Integration Checklist

Before reporting issues, verify:

- [ ] Android Studio is up to date
- [ ] JDK 17 is configured
- [ ] Both `.aar` files exist in `app/libs/`
- [ ] `AndroidManifest.xml` declares `MyApplication`
- [ ] `MainActivity` is set as launcher activity
- [ ] Internet permission is granted
- [ ] Gradle sync completed without errors
- [ ] Build succeeds without errors
- [ ] App installs on device/emulator
- [ ] Logcat shows "SDK initialized successfully"
- [ ] Models appear after tapping "Models" button

---

## 📚 File Structure Reference

```
NeuroLens2/
├── app/
│   ├── libs/
│   │   ├── RunAnywhereKotlinSDK-release.aar
│   │   └── runanywhere-llm-llamacpp-release.aar
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/runanywhere/startup_hackathon20/
│   │   │   ├── MainActivity.kt          (UI layer)
│   │   │   ├── ChatViewModel.kt         (Business logic)
│   │   │   ├── MyApplication.kt         (SDK initialization)
│   │   │   ├── services/
│   │   │   │   ├── HookService.kt       (Orchestration)
│   │   │   │   ├── EmotionService.kt    (AI emotion detection)
│   │   │   │   ├── ReminderService.kt   (Advice generation)
│   │   │   │   └── ConversionState.kt   (Data model)
│   │   │   └── ui/theme/
│   │   └── res/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🤝 Team Coordination

### For Backend Developers

- **Do not modify**: `MainActivity.kt`, UI components
- **Modify freely**: Services layer, data models
- **Test integration**: Use mock mode first, then test with live models

### For Frontend Developers

- **Do not modify**: `MyApplication.kt`, SDK initialization
- **Modify freely**: Compose UI, themes, layouts
- **Test UI**: Use mock mode to avoid model download during UI dev

### For Integration Team

- Follow this guide step-by-step
- Report specific error messages from Logcat
- Test on physical device if emulator fails
- Verify each checkpoint before proceeding

---

## 📞 Getting Help

If you encounter issues not covered here:

1. **Check Logcat**: `adb logcat | grep -E "MyApp|ChatVM|AndroidRuntime"`
2. **Verify setup**: Run through Critical Integration Checklist above
3. **Try clean build**: `./gradlew clean build`
4. **Test on physical device**: Emulators have memory limits
5. **Check SDK docs**: See `RUNANYWHERE_SDK_COMPLETE_GUIDE.md`

---

## 🎓 Additional Resources

- [Quick Start Guide](app/src/main/java/com/runanywhere/startup_hackathon20/QUICK_START_ANDROID.md)
- [RunAnywhere SDK Documentation](RUNANYWHERE_SDK_COMPLETE_GUIDE.md)
- [Jetpack Compose Basics](https://developer.android.com/jetpack/compose)
- [Android ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)

---

## 📝 Version History

- **v1.0**: Initial integration guide
- Project uses RunAnywhere SDK v0.1.3-alpha
- Android Gradle Plugin 8.x
- Kotlin 1.9+

---

**Last Updated**: December 2024
**Maintainer**: NeuroLens2 Development Team

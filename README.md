# 🧠 Mental Health AI Companion

**An AI-powered mental health journaling app with comprehensive emotional support, task management,
breathing exercises, and evidence-based therapeutic guidance.**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)](https://developer.android.com/jetpack/compose)

---

## 🌟 Features

### 💬 **Emotional Journal**

Recognize and process 8 different emotional states with detailed, ChatGPT-quality responses:

- **Anxious** - Breathing techniques, grounding methods, long-term management
- **Sad** - Compassion strategies, behavioral activation, support resources
- **Happy** - Savoring techniques, amplification strategies
- **Angry** - Pause techniques, communication strategies, healthy release
- **Tired** - Types of exhaustion, energy boosters, rest strategies
- **Motivated** - Channeling energy, avoiding burnout, creating systems
- **Calm** - Deepening peace, protective strategies
- **Grateful** - Gratitude practices, neuroscience-backed techniques

### 📝 **Task Management**

Organize your life with AI-powered productivity strategies:

- Priority matrix and time management
- Pomodoro technique guidance
- Energy-based scheduling
- Breaking down overwhelming tasks

### ⏰ **Smart Reminders**

Set up effective self-care reminders:

- Optimal timing strategies
- Actionable phrasing tips
- Mental health "breaker" reminders
- Category organization (health, tasks, emotional check-ins)

### 🎙️ **Voice Journaling**

Express yourself naturally:

- Benefits of voice vs. text journaling
- Emotional processing techniques
- Problem-solving strategies
- Gratitude practice guidance

### 🫁 **Breathing Exercises**

Multiple evidence-based techniques:

- **4-7-8 Breathing** - For anxiety and sleep
- **Box Breathing** - For focus and calm
- **Physiological Sigh** - Quick stress reset
- **Alternate Nostril** - Balance and focus

### 🎯 **Goal Setting**

Achieve your goals with proven frameworks:

- SMART goals methodology
- Kaizen approach (tiny habits)
- Implementation intentions
- Progress tracking strategies

---

## 🚀 Quick Start

### Prerequisites

- Android 7.0 (API 24) or higher
- 500MB+ free RAM
- 400MB+ free storage (for local models)

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd <your-repo-name>
   ```

2. **Build and install**
   ```bash
   ./gradlew clean assembleDebug
   ./gradlew installDebug
   ```

3. **Activate Mock Mode** (Recommended for demo)
    - Open the app
    - Click "Activate Mock Mode" button
    - Status will show: "Mock Mode Active"

4. **Start using!**
    - Type a message like: "I'm feeling stressed"
    - Get detailed, professional therapeutic responses

---

## 💡 Usage Examples

### Emotional Support

```
You: "I'm stressed about my deadline"

AI: I hear that you're feeling anxious right now. That's completely valid...
[500+ word detailed response with breathing techniques, grounding methods, 
and evidence-based anxiety management strategies]
```

### Task Management

```
You: "Help me organize my tasks"

AI: I can help you organize your tasks! Based on what you've shared...
[Detailed response with priority matrix, Pomodoro technique, energy-based scheduling]
```

### Breathing Exercises

```
You: "I need a breathing exercise"

AI: Let's do a breathing exercise together. Breathing work is one of the fastest...
[4 different techniques with step-by-step instructions]
```

---

## 🏗️ Architecture

### Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with StateFlow
- **Async**: Kotlin Coroutines & Flow
- **AI SDK**: RunAnywhere SDK (optional for local models)

### Project Structure

```
app/src/main/java/com/runanywhere/startup_hackathon20/
├── ChatViewModel.kt          # Main ViewModel (state management)
├── MainActivity.kt           # UI implementation
├── MyApplication.kt          # App initialization
└── services/
    ├── HookService.kt        # Enhanced mock service (1600+ lines)
    ├── ConversionState.kt    # Data models
    ├── OllamaService.kt      # Ollama integration
    ├── EmotionService.kt     # Emotion detection
    └── ReminderService.kt    # Reminder logic
```

### Key Components

**ChatViewModel** - Main interface for all features

```kotlin
viewModel.sendMessage(text: String)
viewModel.activateMockMode()
val messages by viewModel.messages.collectAsState()
val isLoading by viewModel.isLoading.collectAsState()
```

**HookService** - ChatGPT-quality response engine

- 500-1000 word detailed responses
- Evidence-based therapeutic content
- Context-aware natural language processing
- 7 feature categories, 8 emotional states

---

## 🧪 Testing

### Quick Test (5 minutes)
```bash
# 1. Build and install
./gradlew installDebug

# 2. Test these inputs:
- "I'm stressed about my deadline"
- "Help me organize my tasks"
- "I need a breathing exercise"
- "Tell me about voice journaling"
- "Help me achieve my goals"
```

All should return detailed, helpful responses (500+ words).

### Full Test Suite

See `TEST_ALL_FEATURES.md` for complete test cases (24 scenarios).

---

## 🎨 Frontend Integration

For frontend developers integrating with this backend:

1. **Read the integration guide**: `FRONTEND_INTEGRATION_GUIDE.md`
2. **Main interface**: `ChatViewModel`
3. **Simple integration**:
   ```kotlin
   val viewModel: ChatViewModel = viewModel()
   viewModel.sendMessage("User message")
   val messages by viewModel.messages.collectAsState()
   ```

Full examples, patterns, and troubleshooting in the integration guide.

---

## 🎯 Operating Modes

### 1. **Mock Mode** (Default - Recommended)

- ✅ Works instantly (no model loading)
- ✅ ChatGPT-quality responses
- ✅ All features supported
- ✅ Perfect for demos and development

**Activate**: Click "Activate Mock Mode" button

### 2. **Ollama Mode** (External Server)

- ✅ Fast responses (uses PC/server GPU)
- ✅ No phone storage needed
- ✅ Reliable for demos

**Setup**: Configure with Ollama server URL

### 3. **Local GGUF Mode** (On-Device)

- ✅ Fully offline
- ✅ Privacy-focused
- ⚠️ Requires model download (~100-400MB)
- ⚠️ Loading time: 2-5 minutes

**Setup**: Download → Load model

---

## 📊 Response Quality

### What Makes This Special

- **500-1000 word responses** - Not just bullet points
- **Evidence-based advice** - Research citations included
- **Therapeutic quality** - Professional mental health practices
- **Natural conversation** - ChatGPT-level dialogue
- **Personalized** - Context-aware and empathetic

### Example Response Comparison

**Basic Chatbot:**
```
Feeling anxious? Try deep breathing and exercise.
```

**Our App:**

```
I hear that you're feeling anxious right now. That's completely valid, 
and I want you to know that anxiety is your brain trying to protect you...

🧠 What's Happening In Your Body:
When you're anxious, your amygdala activates...
[Continues with 500+ words of detailed guidance]
```

---

## 🎬 Demo Video

Record a demo following `DEMO_SCRIPT_AND_VIDEO_GUIDE.md`:

- 24 test cases included
- 2-3 minute script provided
- Recording tips and best practices

---

## 📚 Documentation

- **[FRONTEND_INTEGRATION_GUIDE.md](FRONTEND_INTEGRATION_GUIDE.md)** - Complete integration guide (
  700+ lines)
- **[DEMO_SCRIPT_AND_VIDEO_GUIDE.md](DEMO_SCRIPT_AND_VIDEO_GUIDE.md)** - 24 test cases + recording
  guide
- **[TEST_ALL_FEATURES.md](TEST_ALL_FEATURES.md)** - Quick 5-minute test
- **[START_HERE_README.md](START_HERE_README.md)** - Quick start for teams
- **[PRE_PUSH_CHECKLIST.md](PRE_PUSH_CHECKLIST.md)** - Validation checklist
- **[⭐_FINAL_SUMMARY.md](⭐_FINAL_SUMMARY.md)** - Complete feature overview

---

## 🛠️ Development

### Build

```bash
./gradlew clean assembleDebug
```

### Install

```bash
./gradlew installDebug
```

### Run Tests

```bash
./gradlew test
```

### View Logs

```bash
adb logcat | grep "ChatVM\|MyApp\|HookService"
```

---

## 🐛 Troubleshooting

### App not responding after sending message

- ✅ Ensure Mock Mode is activated
- ✅ Check logs for errors
- ✅ Restart app and try again

### Responses are too short

- ✅ Verify `HookService.kt` is latest version
- ✅ Check `ConversionState.kt` has `detailedResponse` field
- ✅ Rebuild app: `./gradlew clean assembleDebug`

### Model won't load (Local GGUF mode)

- ✅ Use Mock Mode or Ollama instead (faster, more reliable)
- ✅ Ensure device has 500MB+ free RAM
- ✅ Wait full 5 minutes for loading

### Integration issues

- 📖 See `FRONTEND_INTEGRATION_GUIDE.md` troubleshooting section

---

## 🚀 Deployment

### For Hackathon Submission

1. **Build release APK**
   ```bash
   ./gradlew assembleRelease
   ```

2. **Record demo video** (use provided script)

3. **Prepare submission**
    - Video demo (2-3 minutes)
    - APK file
    - GitHub repository link
    - README.md (this file)

### For Production

- Add proper API keys for RunAnywhere SDK
- Implement user authentication
- Add data persistence (Room database)
- Implement privacy policy
- Add crash reporting (Firebase Crashlytics)

---

## 🏆 Competitive Advantages

### Why This App Stands Out

1. **Quality Responses** - ChatGPT-level vs. basic chatbots
2. **Multiple Features** - Not just chat, but tasks, reminders, breathing, goals
3. **Evidence-Based** - Research-backed therapeutic practices
4. **Professional Grade** - Rivals paid mental health apps
5. **User-Friendly** - Natural language, no jargon
6. **Versatile** - 3 operating modes (Mock, Ollama, Local)

---

## 📈 Future Roadmap

- [ ] Mood tracking and analytics dashboard
- [ ] Journal history with search
- [ ] Customizable reminders with notifications
- [ ] Integration with calendar apps
- [ ] Export journal entries
- [ ] Multi-language support
- [ ] Voice input for journaling
- [ ] Integration with wellness apps
- [ ] Premium AI models
- [ ] Community support features

---

## 👥 Team

[Add your team members here]

---

## 🙏 Acknowledgments

- **RunAnywhere SDK** - On-device AI inference
- **Jetpack Compose** - Modern Android UI
- **Mental Health Resources** - Evidence-based practices from psychology research

---

## 📄 License

[Add your license here]

---

## 📞 Contact & Support

For questions about integration or features:

- Check documentation files first
- Review `FRONTEND_INTEGRATION_GUIDE.md`
- See `TROUBLESHOOTING` section above

---

## ⭐ Stats

- **Code**: 4,600+ lines (backend + documentation)
- **Features**: 7 categories, 8 emotions
- **Response Quality**: 500-1000 words per response
- **Test Cases**: 24 documented scenarios
- **Documentation**: 3,000+ lines
- **Time to Demo**: 5 minutes
- **Integration Time**: 30-60 minutes

---

**Built with ❤️ for mental health and wellness**

**🚀 Ready to help people understand and manage their emotions better**

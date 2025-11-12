# 📦 Local GGUF Models - Download & Load Guide

## ✨ New Feature: Direct Download & Load

You can now download and load local GGUF models directly from the Settings screen!

---

## 🎯 **Quick Start**

### **Step 1: Open Settings**

1. Launch Neurolens app
2. Tap **Settings** (gear icon in top right)
3. Scroll to "📦 Local GGUF Models" section

### **Step 2: Choose a Model**

Two models are available:

| Model | Size | Best For |
|-------|------|----------|
| **SmolLM2 360M Q8_0** | ~360 MB | Faster responses, lower RAM |
| **Qwen 2.5 0.5B Instruct Q6_K** | ~500 MB | Better quality responses |

### **Step 3: Download**

1. Tap **"⬇️ Download"** on your chosen model
2. Wait for download to complete (2-5 minutes)
3. Progress bar shows download status
4. Model card will show **"✓ Downloaded"** when ready

### **Step 4: Load**

1. Once downloaded, tap **"🚀 Load Model"**
2. Wait 2-5 minutes for loading (patience required!)
3. Status message will update when model is ready
4. You can now use the model!

### **Step 5: Chat**

1. Go to **Emotional Journal**
2. Turn OFF **Mock Mode** in Settings
3. Start chatting with the local AI!

---

## 📊 **What You'll See**

### **Model Card States**

#### **Not Downloaded:**

```
┌─────────────────────────────────┐
│ SmolLM2 360M Q8_0               │
│ ~360 MB                         │
│                [Not Downloaded] │
│                                 │
│  [⬇️ Download]                  │
└─────────────────────────────────┘
```

#### **Downloading:**

```
┌─────────────────────────────────┐
│ SmolLM2 360M Q8_0               │
│ ~360 MB                         │
│                [Not Downloaded] │
│                                 │
│ ████████░░░░░░░░░░ 45%          │
│ Downloading: 45%                │
│                                 │
│  [Downloading...]               │
└─────────────────────────────────┘
```

#### **Downloaded:**

```
┌─────────────────────────────────┐
│ SmolLM2 360M Q8_0               │
│ ~360 MB                         │
│                [✓ Downloaded]   │
│                                 │
│  [🚀 Load Model]                │
└─────────────────────────────────┘
```

---

## ⚙️ **Features**

### **✅ What's Included:**

1. **One-Click Download**
    - Direct download from HuggingFace
    - Progress tracking with percentage
    - Automatic model registration

2. **Easy Loading**
    - Single tap to load model
    - Status updates during loading
    - Automatic mode switching

3. **Smart UI**
    - Shows download status badges
    - Disables buttons during operations
    - Clear progress indicators

4. **Model Management**
    - Refresh button to scan for models
    - Automatic detection of downloaded models
    - Clear model information

---

## ⚠️ **Important Information**

### **System Requirements:**

- ✅ **Device**: ARM64 architecture required
- ✅ **RAM**: 500MB+ free memory
- ✅ **Storage**: 360-500 MB per model
- ✅ **Time**: 2-5 minutes to load model

### **Loading Time:**

| Stage | Time | What's Happening |
|-------|------|------------------|
| Download | 2-5 min | Downloading model file |
| Scan | 5-10 sec | Detecting downloaded models |
| Load | 2-5 min | Loading model into memory |
| Ready | Instant | Model ready to use |

### **Limitations:**

⚠️ **Loading is SLOW**

- Takes 2-5 minutes on first load
- Device may appear frozen (it's not!)
- Be patient and don't close the app

⚠️ **RAM Intensive**

- Uses 500MB+ RAM
- May cause other apps to close
- Not recommended for low-end devices

⚠️ **Device Compatibility**

- ARM64 only (most modern phones)
- Older devices may not work
- Emulator may crash (use real device)

---

## 🚀 **Step-by-Step: Full Workflow**

### **Complete Example: Download & Use SmolLM2**

**1. Download Model (5 minutes)**

```
Settings → Local GGUF Models → SmolLM2 360M Q8_0 → Download
```

- Progress bar shows: 0% → 25% → 50% → 75% → 100%
- Status updates during download
- Model card shows "✓ Downloaded" when complete

**2. Refresh Model List (optional)**

```
Settings → Local GGUF Models → 🔄 Refresh Model List
```

- Scans for downloaded models
- Updates model status
- Takes 5-10 seconds

**3. Load Model (3 minutes)**

```
Settings → Local GGUF Models → SmolLM2 360M Q8_0 → 🚀 Load Model
```

- Status message shows: "Loading model..."
- **Wait patiently** (2-5 minutes)
- Status updates to: "Live mode active - Local GGUF model loaded"

**4. Disable Mock Mode**

```
Settings → App Mode → Mock Mode → Toggle OFF
```

**5. Test It!**

```
Home → Emotional Journal → Type: "I'm feeling stressed"
```

- Watch local AI respond!
- Responses stream in real-time
- No internet required!

---

## 🔍 **Troubleshooting**

### **Issue: "Model not found for download"**

**Solution:**

1. Tap **"🔄 Refresh Model List"**
2. Wait 10 seconds
3. Models should appear

### **Issue: Download fails**

**Solutions:**

1. Check internet connection
2. Ensure sufficient storage (500MB+ free)
3. Restart app and try again
4. Check logcat for detailed error

### **Issue: Download stuck at 0%**

**Solutions:**

1. Wait 30 seconds (may be connecting)
2. Cancel and restart download
3. Check firewall/VPN isn't blocking

### **Issue: Load button doesn't appear**

**Solutions:**

1. Model isn't fully downloaded
2. Tap **"🔄 Refresh Model List"**
3. Check storage for downloaded file

### **Issue: Loading takes forever**

**Expected Behavior:**

- Loading takes 2-5 minutes (normal!)
- Device may seem frozen (it's working!)
- No progress bar during loading (patience required)

**If stuck after 10 minutes:**

1. Check logcat for errors
2. Restart app
3. Try smaller model (SmolLM2)

### **Issue: App crashes during load**

**Solutions:**

1. Close other apps to free RAM
2. Restart device
3. Use physical device instead of emulator
4. **Recommended**: Use Ollama instead

---

## 💡 **Best Practices**

### **✅ Do:**

1. **Use WiFi for downloads** (not mobile data)
2. **Keep app in foreground** during download/load
3. **Be patient** during loading (2-5 minutes)
4. **Close other apps** before loading
5. **Test with simple prompts** first

### **❌ Don't:**

1. **Don't close app** during download
2. **Don't switch apps** during loading
3. **Don't expect instant loading** (it's slow!)
4. **Don't use on low-end devices**
5. **Don't use emulator** (may crash)

---

## 📊 **Comparison: Ollama vs Local Models**

| Feature | Local GGUF | Ollama | Mock Mode |
|---------|------------|--------|-----------|
| **Setup Time** | 10 minutes | 5 minutes | 0 minutes |
| **Loading Time** | 2-5 minutes | Instant | Instant |
| **Response Speed** | Slow | Fast | Instant |
| **RAM Usage** | 500MB+ | 0 MB | Minimal |
| **Internet Required** | Download only | No | No |
| **Quality** | Good | Excellent | Good |
| **Reliability** | Medium | High | Perfect |

### **🎯 Recommendation:**

- **Hackathon Demo**: Use **Mock Mode**
- **Development**: Use **Ollama**
- **Offline Demo**: Use **Local GGUF** (if needed)
- **Production**: Use **Ollama** (better performance)

---

## 🔧 **Advanced: Manual Model Management**

### **Model File Locations:**

```
Android/data/com.runanywhere.startup_hackathon20/files/models/llama_cpp/
├── smollm2-360m-q8_0.gguf
└── qwen2.5-0.5b-instruct-q6_k.gguf
```

### **Check Download Status:**

```bash
adb shell ls -lh /storage/emulated/0/Android/data/com.runanywhere.startup_hackathon20/files/models/llama_cpp/
```

### **Remove Model:**

```bash
adb shell rm /storage/emulated/0/Android/data/com.runanywhere.startup_hackathon20/files/models/llama_cpp/smollm2-360m-q8_0.gguf
```

Then refresh model list in app.

---

## 📱 **UI Features Explained**

### **Status Badges:**

- **"Not Downloaded"** (Gray) - Model available for download
- **"✓ Downloaded"** (Green) - Model downloaded and ready to load
- **"Loading..."** (Blue) - Model currently loading

### **Action Buttons:**

- **"⬇️ Download"** - Download model from internet
- **"🚀 Load Model"** - Load downloaded model into memory
- **"🔄 Refresh Model List"** - Scan for downloaded models

### **Progress Indicators:**

- **Blue progress bar** - Download progress (0-100%)
- **Percentage text** - Current download progress
- **Status message** - Current operation status

---

## ✅ **Success Checklist**

After successful setup, you should see:

- [ ] Model card shows **"✓ Downloaded"** badge
- [ ] Status message: **"Live mode active - Local GGUF model loaded"**
- [ ] Mock Mode is **OFF**
- [ ] Emotional Journal responds with AI messages
- [ ] Responses stream in (not instant, but streaming)

---

## 🎉 **You're Ready!**

Your local GGUF model is now:

- ✅ Downloaded to device
- ✅ Loaded into memory
- ✅ Ready for offline AI chat
- ✅ Works without internet

**Note:** Remember to be patient with loading times. Local models are slower but work completely
offline!

---

## 📚 **Additional Resources**

- See `OLLAMA_SETUP_GUIDE.md` for Ollama setup (recommended alternative)
- See `QUICK_START.md` for general app setup
- See `UI_IMPLEMENTATION_SUMMARY.md` for app overview

---

*For best experience during hackathon, we still recommend using Ollama or Mock Mode for faster
performance.*

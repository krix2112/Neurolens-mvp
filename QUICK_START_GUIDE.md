# ⚡ QUICK START - Get Running in 5 Minutes

## 🚀 Fastest Way to Demo (Recommended)

### Option 1: Use Ollama (Best for Demo)

**Setup Time: 2-3 minutes**

1. **Install Ollama on your PC:**
    - Download from: https://ollama.com
    - Install and run

2. **Start Ollama and pull a model:**
   ```bash
   ollama serve
   ollama pull llama2
   ```

3. **Get your PC's IP address:**
    - Windows: Open CMD → `ipconfig` → Look for "IPv4 Address"
    - Example: `192.168.1.5`

4. **Connect your phone to the same WiFi network as your PC**

5. **In the app:**
    - Open app → Go to settings/config
    - Enter Ollama URL: `http://192.168.1.5:11434`
    - Select model: `llama2`
    - Click "Activate Ollama Model"
    - ✅ **Ready to chat!**

**Why this is best:**

- ⚡ Works in 2-3 minutes
- 🚀 Fast responses (uses PC GPU/CPU)
- 💾 No phone storage needed
- 🎯 Reliable for demos

---

### Option 2: Use On-Device Model

**Setup Time: 7-10 minutes (first time)**

1. **Rebuild the app:**
   ```bash
   ./gradlew clean assembleDebug
   ```

2. **Install on device**

3. **Download model:**
    - Open app
    - Click "SmolLM2 360M Q8_0"
    - Click "Download"
    - Wait for 100% (3-5 minutes)

4. **Load model:**
    - Click "Load Model"
    - **WAIT 2-5 MINUTES** - Don't close app!
    - Watch status: "Loading model... (this may take 2-5 minutes)"
    - Wait for: "Live mode active ✓"

5. **Start chatting!**

**Note:** Model loading can take up to 5 minutes on some devices. Be patient!

---

### Option 3: Use Mock Mode

**Setup Time: 30 seconds**

1. Rebuild and install app
2. Click "Activate Mock Mode"
3. ✅ **Ready immediately!**
4. Send messages - get predefined responses

**Good for:** UI testing, quick demos without waiting

---

## 🎯 For Your 12:00 Demo

**RECOMMENDED SETUP:**

1. **Primary:** Use Ollama (fast, reliable)
2. **Backup:** Have Mock Mode ready
3. **Optional:** Pre-load on-device model if time permits

---

## 🐛 Troubleshooting

### If model loading is stuck:

- **Wait the full 5 minutes first!**
- Check logcat for progress logs
- If still stuck after 5 minutes, use Ollama instead

### If app crashes on load:

- Device might not have enough RAM (need 500MB+)
- Switch to Ollama or Mock mode

### If "Model not found":

- Make sure download reached 100%
- Try clicking "Refresh Models"
- Use Ollama as backup

---

## 📊 What Was Fixed

The app was timing out after 30 seconds even though model loading takes 2-5 minutes. This is now
fixed:

- ✅ Removed premature timeouts
- ✅ Added better status messages
- ✅ Model loading now completes successfully

---

## 🎥 Demo Script

**For a smooth demo:**

1. Show the app UI (Mock mode for instant responses)
2. Demonstrate Ollama integration (fast, reliable)
3. Mention on-device capability (if pre-loaded)
4. Highlight the multi-mode flexibility

**Time each mode:**

- Mock: Instant ⚡
- Ollama: ~5 seconds per response 🚀
- On-device: ~10-30 seconds per response 📱

---

**Good luck! 🚀**

# ⚡ MAKE NEUROLENS LIVE IN 5 MINUTES!

## 🚀 **FASTEST PATH TO LIVE AI**

Your app is **READY** for live AI. Here's how to activate it RIGHT NOW:

---

## **STEP 1: Install Ollama (2 minutes)**

### **Windows:**

1. Download: https://ollama.com/download/windows
2. Run installer → Done! (Auto-starts)

### **Mac:**

```bash
brew install ollama
```

### **Linux:**

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

---

## **STEP 2: Download AI Model (2 minutes)**

Open terminal/PowerShell and run:

```bash
# FASTEST (700MB) - Recommended for hackathon
ollama run phi

# OR Medium (4GB) - Better quality
ollama run llama2

# OR Best (7GB) - Highest quality
ollama run llama3
```

**Wait for download** - You'll see progress. Model will auto-start when ready.

---

## **STEP 3: Configure in App (1 minute)**

1. **Open Neurolens** on your emulator
2. **Tap Settings** (gear icon, top right)
3. **Enter Ollama URL**:
   ```
   http://10.0.2.2:11434
   ```
   ☝️ This is how emulator accesses your PC's localhost

4. **Enter Model Name**:
    - Type: `phi` (or `llama2` or `llama3` - whatever you downloaded)

5. **Tap "Connect to Ollama"**
6. **Wait for green checkmark** ✓

---

## **STEP 4: Turn OFF Mock Mode**

Still in Settings:

1. **Toggle "Mock Mode" to OFF**
2. Status should show: **"Ollama model active: phi ✓"**

---

## **STEP 5: TEST IT! 🎉**

1. **Go Back to Home**
2. **Tap "Emotional Journal"**
3. **Type**: "I'm feeling stressed about my hackathon presentation"
4. **Hit Send**
5. **Watch REAL AI respond in real-time!**

---

## ✅ **VERIFICATION**

You should see:

- ✅ Text appearing word-by-word (streaming)
- ✅ Different response each time you ask
- ✅ Natural, conversational answers
- ✅ Status shows "Ollama model active"

---

## 🔧 **TROUBLESHOOTING**

### **Problem: Connection Failed**

**Check Ollama is running:**

```bash
ollama list
```

Should show your downloaded models.

**Check Ollama is serving:**

```bash
ollama ps
```

Should show active model.

**Restart Ollama service:**

```bash
# Windows: Close from system tray, reopen
# Mac/Linux:
ollama serve
```

---

### **Problem: Model Not Found**

```bash
# List downloaded models
ollama list

# Pull model if missing
ollama pull phi
```

---

### **Problem: Emulator Can't Connect**

**Wrong URL? Try these:**

1. **For Emulator**: `http://10.0.2.2:11434` (recommended)
2. **Find your PC IP**:
    - Windows: `ipconfig` → IPv4 Address
    - Mac/Linux: `ifconfig` → inet
3. **Use**: `http://YOUR_IP:11434`

---

### **Problem: Slow Responses**

**Solutions:**

1. Use smaller model: `phi` instead of `llama3`
2. Close other apps to free RAM
3. **OR** just use Mock Mode for demo (instant!)

---

## 🎯 **QUICK COMMANDS REFERENCE**

```bash
# Start Ollama server
ollama serve

# List installed models
ollama list

# Check running models
ollama ps

# Pull a model
ollama pull phi

# Run a model (also pulls if needed)
ollama run phi

# Stop all models
ollama stop phi
```

---

## 📊 **MODEL RECOMMENDATIONS**

| Model | Size | Speed | Quality | Best For |
|-------|------|-------|---------|----------|
| **phi** | 700MB | ⚡⚡⚡ | Good | **Hackathon demo** ✅ |
| **llama2** | 4GB | ⚡⚡ | Great | General use |
| **llama3** | 7GB | ⚡ | Excellent | Final presentation |
| **mistral** | 4GB | ⚡⚡ | Great | Alternative |

**For today (5pm deadline):** Use **phi** - fast downloads, fast responses!

---

## 🎬 **DEMO STRATEGY**

### **Plan A: Start with Live AI**

1. Have Ollama running BEFORE judges arrive
2. Show it's real AI from the start
3. Type unique questions judges suggest
4. Proves it's not pre-programmed

### **Plan B: Mock First, Then Live** (Safest)

1. Start with Mock Mode (zero risk)
2. Show all features quickly
3. Say: "Now let me show you with real AI..."
4. Switch to Ollama mode
5. If it fails, no problem - Mock Mode worked!

### **Plan C: Mock Only** (Backup)

1. If Ollama gives issues, stay in Mock Mode
2. Say: "We support multiple AI backends"
3. Mock Mode responses are still impressive!

---

## ⚡ **CURRENT STATUS OF YOUR APP**

✅ **Ollama integration COMPLETE**
✅ **OllamaService.kt** - Fully implemented
✅ **ChatViewModel** - Handles Ollama streaming
✅ **Settings screen** - Configuration UI ready
✅ **Mock Mode** - Perfect backup

**What's working:**

- Stream responses word-by-word
- Connection testing
- Model configuration
- Error handling
- Automatic fallback to Mock Mode

**You just need to:**

1. Install Ollama
2. Download a model
3. Configure in app
4. Turn off Mock Mode

---

## 🚀 **YOUR TURN!**

Open terminal RIGHT NOW and run:

```bash
ollama run phi
```

In 2-3 minutes you'll have a working AI model!

Then open your app and follow Step 3 above.

---

## 💡 **PRO TIPS**

1. **Pre-download model** before your presentation time
2. **Keep Ollama running** in background
3. **Test connection** 5 minutes before demo
4. **Know your backup** - Mock Mode is always ready
5. **Explain the tech** - "Runs on Ollama, works offline or with local models"

---

## 🎉 **YOU'RE ALMOST THERE!**

Your app is 100% ready. You just need to:

- [ ] Install Ollama (2 min)
- [ ] Download model (2 min)
- [ ] Configure in app (1 min)
- [ ] Test it works (1 min)

**Total time: 6 minutes to live AI! 🚀**

Go do it now! Then come back and test!

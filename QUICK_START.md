# 🚀 Quick Start - Test Ollama Connection Now

## ⚡ 5-Minute Setup

### **Step 1: Install Ollama (2 minutes)**

**Download and install:**

```
https://ollama.ai
```

### **Step 2: Pull Model (1 minute)**

Open terminal/PowerShell:

```bash
ollama pull llama2
```

### **Step 3: Start Server (instant)**

```bash
ollama serve
```

**Keep this window open!**

### **Step 4: Test in App (1 minute)**

1. Launch Neurolens app in emulator
2. Tap **Settings** (gear icon)
3. Scroll down to see **step-by-step instructions**
4. URL should already be: `http://10.0.2.2:11434`
5. Model: `llama2`
6. Tap **"Connect to Ollama"**
7. Wait for ✅ green checkmark

### **Step 5: Chat! (instant)**

1. Go back to Home
2. Tap **Emotional Journal**
3. Type: "I'm feeling stressed"
4. Send
5. Watch real AI respond! 🎉

---

## 🐛 If It Doesn't Work

### **Check Logcat**

```bash
adb logcat | grep -E "OllamaService|ChatVM"
```

Look for lines with:

- `❌` - Shows what's wrong
- `💡` - Shows how to fix it

### **Most Common Issues**

**Issue: "Connection refused"**

```bash
# Make sure Ollama is running
ollama list
```

**Issue: "Unknown host"**

- Check URL is exactly: `http://10.0.2.2:11434`
- No `https://`, no trailing `/`

**Issue: Still not working?**

- See `OLLAMA_SETUP_GUIDE.md` for detailed troubleshooting

---

## 📱 What You'll See

### **When Working:**

**Settings Screen:**

```
✅ Ollama connected ✓
```

**Status Message:**

```
Ollama model active: llama2 ✓
```

**Emotional Journal:**

- Type message
- See streaming response
- Real AI conversation!

---

## 💡 Pro Tip

If you just want to test quickly:

1. Keep Mock Mode ON
2. Test all features first
3. Then switch to Ollama
4. Show judges: "And here's real AI!"

Mock Mode is already fully functional with great responses! 🎯

---

## 📊 Verification

Your changes are ready:

- ✅ Build successful
- ✅ URL fixed to `10.0.2.2`
- ✅ Error handling added
- ✅ Instructions in UI
- ✅ Documentation complete

**Just need to:**

1. Start Ollama server
2. Connect in app
3. Done!

---

## 🎯 Next Steps

After confirming Ollama works:

1. ✅ Test emotional journal with various prompts
2. ✅ Try switching between Mock/Ollama/Local modes
3. ✅ Check all error messages are helpful
4. ✅ Review Settings UI looks good
5. ✅ Read OLLAMA_SETUP_GUIDE.md for full details

---

**Good luck! 🚀**

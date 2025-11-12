# 🔧 Ollama Setup Guide for Neurolens

This guide will help you connect the Neurolens app to Ollama for live AI responses.

---

## 📋 **Table of Contents**

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Troubleshooting](#troubleshooting)
5. [Advanced Topics](#advanced-topics)

---

## 🎯 **Prerequisites**

Before you begin, ensure you have:

- ✅ Ollama installed on your computer ([Download from ollama.ai](https://ollama.ai))
- ✅ Android emulator or physical device with Neurolens app installed
- ✅ Computer and device on same WiFi network (for physical device)
- ✅ At least one Ollama model installed (recommended: `llama2`)

---

## 📥 **Installation**

### **Step 1: Install Ollama**

**Windows/Mac/Linux:**

1. Visit [ollama.ai](https://ollama.ai)
2. Download the installer for your OS
3. Run the installer
4. Verify installation:
   ```bash
   ollama --version
   ```

### **Step 2: Pull a Model**

Download a model to use with the app:

**Recommended for beginners:**
```bash
ollama pull llama2
```

**Other options:**

```bash
# Smaller, faster (good for testing)
ollama pull tinyllama

# Better quality responses
ollama pull mistral

# Larger, more capable (requires more RAM)
ollama pull llama2:7b
```

### **Step 3: Verify Model Installation**

```bash
ollama list
```

You should see your installed models listed.

---

## ⚙️ **Configuration**

### **Starting Ollama Server**

**Option 1: Start as Service (Recommended)**

Ollama runs automatically on startup. If not running:

```bash
ollama serve
```

Keep this terminal window open while using the app.

**Option 2: Test Quickly**

```bash
ollama run llama2
```

This starts the server and opens a chat interface. You can type here to test the model.

### **Connecting from Android Emulator**

The Android emulator uses a special IP address to access your host machine.

**Configuration:**

1. Open Neurolens app
2. Tap **Settings** (gear icon in top right)
3. Scroll to "Ollama Configuration"
4. Enter server URL: `http://10.0.2.2:11434`
5. Enter model name: `llama2` (or your chosen model)
6. Tap "Connect to Ollama"

**Important:** Use `10.0.2.2` NOT `localhost` for emulators!

### **Connecting from Physical Android Device**

To connect from a real phone/tablet, you need your computer's IP address.

**Find Your IP Address:**

**Windows:**

```bash
ipconfig
```

Look for "IPv4 Address" (e.g., 192.168.1.100)

**Mac:**

```bash
ifconfig | grep "inet "
```

**Linux:**

```bash
ip addr show | grep "inet "
```

**Configuration:**

1. Open Neurolens app
2. Tap **Settings**
3. Enter server URL: `http://YOUR_IP:11434`
    - Example: `http://192.168.1.100:11434`
4. Enter model name: `llama2`
5. Tap "Connect to Ollama"

**Requirements:**

- ✅ Phone and PC must be on **same WiFi network**
- ✅ Firewall must allow connections (see troubleshooting)

---

## 🐛 **Troubleshooting**

### **Issue 1: "Connection Refused" or "Cannot Connect"**

**Symptoms:**

- App shows "❌ Connection failed"
- Logcat shows "Connection refused"

**Solutions:**

**A. Verify Ollama is Running**

```bash
# Test if Ollama responds
curl http://localhost:11434/api/tags
```

Should return JSON with your models. If it fails:

```bash
# Start Ollama
ollama serve
```

**B. Check URL Format**

- ✅ Correct: `http://10.0.2.2:11434`
- ❌ Wrong: `https://10.0.2.2:11434` (no HTTPS)
- ❌ Wrong: `http://localhost:11434` (use 10.0.2.2 for emulator)
- ❌ Wrong: `http://10.0.2.2:11434/` (no trailing slash)

**C. Test in Browser**

On your computer, open: `http://localhost:11434`

Should display: "Ollama is running"

### **Issue 2: "Unknown Host" Error**

**Symptoms:**

- Logcat shows "UnknownHostException"

**Solutions:**

1. **Double-check spelling** of URL
2. **Verify format**: Must be `http://` not `https://`
3. **For emulator**: Must use `10.0.2.2`
4. **For device**: Verify IP is correct (`ipconfig` / `ifconfig`)

### **Issue 3: Connection Timeout**

**Symptoms:**

- App hangs on "Testing connection..."
- Eventually times out

**Solutions:**

**A. Check Firewall (Windows)**

1. Open Windows Defender Firewall
2. Click "Allow an app through firewall"
3. Find "Ollama" and enable for both Private and Public networks
4. If not listed:
    - Click "Change settings"
    - Click "Allow another app"
    - Browse to Ollama installation
    - Add it

**B. Check Firewall (Mac)**

1. System Preferences → Security & Privacy
2. Firewall tab → Firewall Options
3. Ensure Ollama is allowed
4. If blocked, click "+" and add Ollama

**C. Restart Ollama**

```bash
# Kill existing process
pkill ollama

# Start fresh
ollama serve
```

### **Issue 4: "Server Connected but No Models"**

**Symptoms:**

- Connection succeeds
- But app says "no models available"

**Solution:**

Pull a model first:

```bash
ollama pull llama2
```

Then reconnect in the app.

### **Issue 5: Responses are Very Slow**

**Symptoms:**

- Connection works
- But responses take minutes

**Solutions:**

**A. Use Smaller Model**

```bash
ollama pull tinyllama
```

Then change model in app settings to `tinyllama`.

**B. Check System Resources**

Ollama requires:

- Minimum 8GB RAM (for llama2)
- 4-core CPU
- 10GB free disk space

**C. Monitor Ollama Performance**

While running:
```bash
# Check Ollama logs
ollama list
```

### **Issue 6: Physical Device Won't Connect**

**Symptoms:**

- Emulator works fine
- Physical device fails to connect

**Solutions:**

**A. Verify Same WiFi Network**

- Ensure phone and PC are on **same WiFi**
- Not cellular/mobile data
- Not guest WiFi network

**B. Find Correct IP**

```bash
# Windows
ipconfig

# Mac/Linux
ifconfig
```

Use the **local** IP (192.168.x.x or 10.0.x.x), not public IP.

**C. Test Connectivity**

On your phone's browser, visit: `http://YOUR_PC_IP:11434`

Should show "Ollama is running". If not, firewall is blocking.

**D. Disable Firewall Temporarily**

To test if firewall is the issue:

1. Temporarily disable firewall
2. Try connecting from app
3. If it works, firewall is blocking
4. Re-enable firewall and add Ollama exception

---

## 🚀 **Advanced Topics**

### **Using Custom Models**

You can use any GGUF model from HuggingFace:

1. Download model to: `~/.ollama/models/`
2. Create a Modelfile
3. Import with `ollama create`

Example:
```bash
ollama create my-custom-model -f Modelfile
```

Then use `my-custom-model` in the app.

### **Performance Optimization**

**For Faster Responses:**

1. **Use GPU acceleration** (if available)
    - Ollama automatically uses GPU if detected
    - NVIDIA CUDA or Apple Metal

2. **Adjust context length**
    - Smaller context = faster responses
    - Edit Modelfile: `PARAMETER num_ctx 2048`

3. **Use quantized models**
    - Q4 models are 4x smaller, faster
    - Example: `llama2:7b-q4_0`

### **Running Multiple Models**

You can have multiple models installed:

```bash
ollama pull llama2
ollama pull mistral
ollama pull codellama
```

Switch between them in app settings.

### **Monitoring Ollama**

Check what's running:

```bash
# List models
ollama list

# Check if server is running
ps aux | grep ollama

# View logs (if run as service)
journalctl -u ollama -f
```

---

## 📊 **Expected Performance**

### **Response Times (on typical laptop)**

| Model     | First Token | Full Response |
|-----------|-------------|---------------|
| tinyllama | ~100ms      | ~5s           |
| llama2    | ~200ms      | ~10s          |
| mistral   | ~300ms      | ~15s          |
| llama2:7b | ~500ms      | ~30s          |

*Actual times depend on your hardware*

### **System Requirements**

**Minimum:**

- 8GB RAM
- 4-core CPU
- 10GB free disk space

**Recommended:**

- 16GB RAM
- 6+ core CPU or dedicated GPU
- 20GB free disk space

---

## ✅ **Verification Checklist**

Before reporting issues, verify:

- [ ] Ollama is installed: `ollama --version`
- [ ] Model is pulled: `ollama list` shows model
- [ ] Server is running: `curl http://localhost:11434/api/tags`
- [ ] Firewall allows connections
- [ ] Using correct URL:
    - [ ] Emulator: `http://10.0.2.2:11434`
    - [ ] Device: `http://YOUR_PC_IP:11434`
- [ ] App shows correct model name
- [ ] Network connection is stable

---

## 🆘 **Still Having Issues?**

### **Check Logcat**

The app provides detailed error messages in Android logcat:

```bash
adb logcat | grep -E "ChatVM|OllamaService"
```

Look for lines starting with:

- `❌` - Errors with solutions
- `💡` - Troubleshooting tips
- `✅` - Successful operations

### **Common Error Messages**

| Error                | Meaning             | Solution                 |
|----------------------|---------------------|--------------------------|
| "Connection refused" | Ollama not running  | Run `ollama serve`       |
| "Unknown host"       | Wrong URL           | Check IP/URL format      |
| "Timeout"            | Firewall blocking   | Allow Ollama in firewall |
| "No models"          | Model not installed | Run `ollama pull llama2` |

---

## 🎉 **Success!**

Once connected, you should see:

- ✅ Green checkmark in settings
- ✅ "Ollama connected ✓" message
- ✅ Able to chat in Emotional Journal with live AI

Enjoy using Neurolens with live AI! 🚀

---

## 📚 **Additional Resources**

- [Ollama Official Docs](https://github.com/ollama/ollama)
- [Available Models](https://ollama.ai/library)
- [Ollama Discord Community](https://discord.gg/ollama)

---

*Last Updated: 2025-01-12*


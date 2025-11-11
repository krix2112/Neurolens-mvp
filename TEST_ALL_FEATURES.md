# ✅ QUICK FEATURE TEST CHECKLIST

**Test these before recording your demo - takes 5 minutes**

---

## 🚀 Setup

```bash
cd K:/demo
./gradlew clean assembleDebug
./gradlew installDebug
```

1. Open app
2. Click "Activate Mock Mode"
3. Verify status shows: "Mock Mode Active"

---

## 📋 Test Each Feature (Copy-Paste These)

### ✅ Test 1: Anxiety (ChatGPT-like response)

```
I'm stressed about my deadline
```

**Expected:** Detailed response with breathing techniques, grounding methods

---

### ✅ Test 2: Tasks

```
Help me organize my tasks
```

**Expected:** Task management strategies, Pomodoro, priority matrix

---

### ✅ Test 3: Reminders

```
I need reminders for self-care
```

**Expected:** Smart reminder strategies, timing tips, breaker reminders

---

### ✅ Test 4: Voice Journal

```
Tell me about voice journaling
```

**Expected:** Benefits, tips, when to use

---

### ✅ Test 5: Breathing

```
I need a breathing exercise
```

**Expected:** Multiple techniques with instructions

---

### ✅ Test 6: Goals

```
Help me achieve my goals
```

**Expected:** SMART goals, Kaizen approach, implementation

---

### ✅ Test 7: Happy

```
Today was amazing!
```

**Expected:** Savoring happiness, spreading energy

---

### ✅ Test 8: Sad

```
I've been feeling down
```

**Expected:** Compassionate response, behavioral activation

---

### ✅ Test 9: General Chat

```
Hi, how are you?
```

**Expected:** Menu of options, ways to help

---

## 🎯 Success Criteria

All tests should show:

- ✅ **Detailed responses** (not just 3 bullet points)
- ✅ **Natural conversation** (ChatGPT-quality)
- ✅ **Relevant advice** (matches the request)
- ✅ **Professional tone** (therapeutic but accessible)

---

## 🐛 If Something's Wrong

### Responses are short (just 3 tips):

- Check `ConversionState.kt` has `detailedResponse` field
- Check `HookService.kt` is updated
- Rebuild app

### App crashes:

- Activate Mock Mode first
- Check logs for errors

### Wrong responses:

- Check typing matches test cases
- Keywords trigger different handlers

---

**If all 9 tests pass → YOU'RE READY TO RECORD! 🎬**

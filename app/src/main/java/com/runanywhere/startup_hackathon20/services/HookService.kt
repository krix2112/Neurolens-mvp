package com.runanywhere.startup_hackathon20.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Enhanced AI Mock Service - ChatGPT-like detailed responses
 * Supports: Journal entries, tasks, reminders, voice notes, mental health support
 */
object HookService {

    private val emotionPatterns = mapOf(
        "anxious" to listOf(
            "worried",
            "nervous",
            "stressed",
            "overwhelmed",
            "panic",
            "fear",
            "tense",
            "uneasy",
            "anxious",
            "anxiety"
        ),
        "sad" to listOf(
            "depressed",
            "down",
            "unhappy",
            "blue",
            "gloomy",
            "miserable",
            "hopeless",
            "lonely",
            "sad",
            "crying"
        ),
        "angry" to listOf(
            "frustrated",
            "mad",
            "irritated",
            "annoyed",
            "furious",
            "upset",
            "rage",
            "angry",
            "hate"
        ),
        "tired" to listOf(
            "exhausted",
            "drained",
            "fatigued",
            "weary",
            "burnout",
            "sleepy",
            "tired",
            "worn out"
        ),
        "happy" to listOf(
            "joyful",
            "excited",
            "great",
            "wonderful",
            "amazing",
            "fantastic",
            "good",
            "excellent",
            "happy",
            "delighted"
        ),
        "calm" to listOf(
            "peaceful",
            "relaxed",
            "serene",
            "tranquil",
            "content",
            "comfortable",
            "calm",
            "zen"
        ),
        "motivated" to listOf(
            "inspired",
            "energized",
            "driven",
            "determined",
            "focused",
            "productive",
            "motivated",
            "ambitious"
        ),
        "grateful" to listOf(
            "thankful",
            "appreciative",
            "blessed",
            "fortunate",
            "grateful",
            "appreciate"
        )
    )

    fun processJournal(text: String): ConversationState {
        val lowerText = text.lowercase()

        // Detect what type of request this is
        val requestType = detectRequestType(lowerText)

        return when (requestType) {
            RequestType.TASK_REQUEST -> handleTaskRequest(text, lowerText)
            RequestType.REMINDER_REQUEST -> handleReminderRequest(text, lowerText)
            RequestType.VOICE_JOURNAL -> handleVoiceJournal(text, lowerText)
            RequestType.BREATHING_EXERCISE -> handleBreathingExercise(text, lowerText)
            RequestType.EMOTIONAL_JOURNAL -> handleEmotionalJournal(text, lowerText)
            RequestType.GOAL_SETTING -> handleGoalSetting(text, lowerText)
            RequestType.GENERAL_CHAT -> handleGeneralChat(text, lowerText)
        }
    }

    private enum class RequestType {
        TASK_REQUEST,
        REMINDER_REQUEST,
        VOICE_JOURNAL,
        BREATHING_EXERCISE,
        EMOTIONAL_JOURNAL,
        GOAL_SETTING,
        GENERAL_CHAT
    }

    private fun detectRequestType(lowerText: String): RequestType {
        return when {
            lowerText.contains("task") || lowerText.contains("to do") || lowerText.contains("todo") -> RequestType.TASK_REQUEST
            lowerText.contains("remind") || lowerText.contains("reminder") || lowerText.contains("schedule") -> RequestType.REMINDER_REQUEST
            lowerText.contains("voice") || lowerText.contains("speak") || lowerText.contains("talk") -> RequestType.VOICE_JOURNAL
            lowerText.contains("breath") || lowerText.contains("breathing") || lowerText.contains("meditat") -> RequestType.BREATHING_EXERCISE
            lowerText.contains("goal") || lowerText.contains("plan") || lowerText.contains("achieve") -> RequestType.GOAL_SETTING
            detectEmotion(lowerText) != "neutral" -> RequestType.EMOTIONAL_JOURNAL
            else -> RequestType.GENERAL_CHAT
        }
    }

    private fun handleTaskRequest(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)

        val response = """
I can help you organize your tasks! Based on what you've shared, here's a personalized approach:

📋 Task Management Strategy:

1. **Break It Down**: Large tasks can feel overwhelming. Let's break them into smaller, manageable steps.
   - Identify the smallest action you can take right now
   - Set a 25-minute timer and focus on just that one thing
   - Take a 5-minute break after completing it

2. **Priority Matrix**: Not everything is equally urgent
   - High Priority & Urgent: Do these first
   - Important but Not Urgent: Schedule time for these
   - Everything else: Can wait or be delegated

3. **Energy-Based Scheduling**: 
   - Do your hardest tasks when you have the most energy (usually morning)
   - Save routine/easier tasks for your low-energy periods
   - Take breaks between tasks to maintain focus

💡 Pro Tip: Start with just ONE task today. Completing it will build momentum and motivation for the rest.

Would you like me to help you prioritize specific tasks or create a daily schedule?
        """.trimIndent()

        return ConversationState(
            emotion = emotion.capitalize(),
            advice = listOf(
                "Use the Pomodoro Technique: 25 min focus + 5 min break",
                "Write down tasks and cross them off as you complete them",
                "Start with the easiest task to build momentum"
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun handleReminderRequest(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)

        val response = """
I'll help you set up effective reminders! Here's how to make them work for you:

⏰ Smart Reminder Strategy:

1. **Timing is Everything**:
   - Morning reminders: Set for 30 min after you usually wake up
   - Work reminders: Schedule during natural breaks (10am, 2pm, 4pm)
   - Evening reminders: Before your usual wind-down time

2. **Reminder Categories**:
   - 🧘 Self-Care: Drink water, stretch, take breaks
   - 💊 Health: Medication, vitamins, exercise
   - 📝 Tasks: Deadlines, meetings, important calls
   - 😊 Emotional Check-ins: "How are you feeling right now?"

3. **Make Them Actionable**:
   Instead of: "Exercise"
   Try: "Put on workout clothes and do 10 jumping jacks"
   
   Instead of: "Meditate"
   Try: "Sit comfortably and take 5 deep breaths"

4. **Breaker Reminders** (Mental Health Focused):
   - Take 3 deep breaths
   - Step outside for 2 minutes
   - Text someone you love
   - Write one thing you're grateful for

💡 Research shows: People who use specific, action-oriented reminders are 3x more likely to follow through!

What would you like to be reminded about? I can help you phrase it effectively.
        """.trimIndent()

        return ConversationState(
            emotion = emotion.capitalize(),
            advice = listOf(
                "Set reminders at natural transition points in your day",
                "Make reminders specific and actionable",
                "Use emotional check-in reminders 3x daily"
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun handleVoiceJournal(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)

        val response = """
Voice journaling is a powerful way to process your thoughts! Here's why it works:

🎙️ Benefits of Voice Journaling:

1. **More Natural Expression**:
   - Speaking feels more natural than writing
   - You can express complex emotions more easily
   - No pressure to "write well" - just talk

2. **Faster Processing**:
   - You can speak 3-4x faster than you can write
   - Capture thoughts before they slip away
   - Great for processing intense emotions in the moment

3. **Emotional Depth**:
   - Your voice carries emotion that text can't
   - Hearing yourself speak can provide insights
   - Later, listening back can show your emotional journey

📝 Voice Journaling Tips:

**For Emotional Processing:**
- Start with "Right now I'm feeling..."
- Describe the physical sensations in your body
- Talk through what triggered the emotion

**For Problem-Solving:**
- Explain the problem as if talking to a friend
- Ask yourself questions out loud
- Voice your different perspectives on the situation

**For Gratitude:**
- Record 3 things you're grateful for daily
- Explain WHY you're grateful, not just what
- Listen back when you need a mood boost

🌟 Try This Now:
Take a deep breath and speak for 2 minutes about your day. Don't overthink it - just let the words flow. Notice how you feel after.

How was your experience with voice journaling today?
        """.trimIndent()

        return ConversationState(
            emotion = emotion.capitalize(),
            advice = listOf(
                "Voice journal when you're feeling too much to write",
                "Record your gratitude practice daily",
                "Listen back to track your emotional patterns over time"
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun handleBreathingExercise(text: String, lowerText: String): ConversationState {
        val response = """
Let's do a breathing exercise together. Breathing work is one of the fastest ways to calm your nervous system.

🫁 Guided Breathing Exercise:

**4-7-8 Breathing** (For Anxiety & Sleep):
1. Exhale completely through your mouth
2. Close your mouth, inhale through nose for 4 counts
3. Hold your breath for 7 counts
4. Exhale completely through mouth for 8 counts
5. Repeat 4 times

Why it works: This activates your parasympathetic nervous system, telling your body "we're safe."

---

**Box Breathing** (For Focus & Calm):
1. Inhale for 4 counts
2. Hold for 4 counts
3. Exhale for 4 counts
4. Hold for 4 counts
5. Repeat for 5 minutes

Used by: Navy SEALs, athletes, and emergency responders to stay calm under pressure.

---

**Physiological Sigh** (For Quick Reset):
1. Double inhale through nose (inhale, then inhale again)
2. Long exhale through mouth
3. Repeat 2-3 times

Why it works: This is what your body naturally does when crying. It rapidly reduces stress hormones.

---

**Alternate Nostril Breathing** (For Balance):
1. Close right nostril, inhale left (4 counts)
2. Hold (4 counts)
3. Close left nostril, exhale right (4 counts)
4. Inhale right (4 counts)
5. Hold (4 counts)
6. Exhale left (4 counts)
7. Repeat 5 times

Benefits: Balances left/right brain, reduces anxiety, improves focus.

---

💡 Pro Tip: Set a daily reminder to do 2 minutes of breathing practice. Your nervous system will start to regulate more easily over time.

Which breathing technique would you like to try first?
        """.trimIndent()

        return ConversationState(
            emotion = "Calm",
            advice = listOf(
                "Try 4-7-8 breathing before bed for better sleep",
                "Use box breathing during stressful work moments",
                "Physiological sigh for instant anxiety relief"
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun handleEmotionalJournal(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)
        val detailedResponse = generateDetailedEmotionalResponse(emotion, text, lowerText)
        val advice = getAdviceForEmotion(emotion)

        return ConversationState(
            emotion = emotion.capitalize(),
            advice = advice,
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = detailedResponse
        )
    }

    private fun handleGoalSetting(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)

        val response = """
I love that you're thinking about your goals! Let's make them achievable and motivating.

🎯 SMART Goal Framework:

**S - Specific**: 
❌ Bad: "I want to be healthier"
✅ Good: "I want to exercise for 20 minutes, 3 days per week"

**M - Measurable**:
How will you know you've succeeded? Use numbers, frequencies, or clear milestones.

**A - Achievable**:
Push yourself, but don't set yourself up for failure. Start smaller than you think.

**R - Relevant**:
Does this align with your values and what you truly want?

**T - Time-bound**:
When will you achieve this? Set a deadline.

---

📈 The Kaizen Approach (Tiny Habits):

Instead of: "I'll meditate for 30 minutes daily"
Try: "After I brush my teeth, I'll take 3 deep breaths"

Why? Your brain loves consistency more than intensity. Small wins compound.

---

🔄 Implementation Intentions:

Formula: "When [situation], I will [behavior]"

Examples:
- "When I feel stressed, I will take a 5-minute walk"
- "When I wake up, I will write 3 things I'm grateful for"
- "When I finish work, I will not check email for 2 hours"

This makes goals automatic instead of requiring willpower.

---

📊 Track Your Progress:

1. **Daily Check-ins**: Did I do it today? Yes/No
2. **Weekly Reflection**: What worked? What didn't?
3. **Monthly Review**: Am I closer to my goal?

---

💪 Dealing with Setbacks:

Missed a day? That's okay. The goal is progress, not perfection.
- Don't break the chain twice in a row
- Get back on track the very next day
- Be kind to yourself - you're human

What goal would you like to work on? Let's break it down together!
        """.trimIndent()

        return ConversationState(
            emotion = "Motivated",
            advice = listOf(
                "Start with ONE micro-goal: so small you can't fail",
                "Use implementation intentions: 'When X happens, I will do Y'",
                "Track daily progress with a simple yes/no"
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun handleGeneralChat(text: String, lowerText: String): ConversationState {
        val emotion = detectEmotion(lowerText)

        val response = """
Thank you for sharing with me. I'm here to support you however you need.

Here are some ways I can help:

💭 **Emotional Support**:
- Process feelings through journaling
- Identify emotions and their triggers
- Get evidence-based coping strategies

📝 **Productivity Tools**:
- Break down overwhelming tasks
- Create actionable to-do lists
- Set reminders for self-care

🧘 **Mental Wellness**:
- Guided breathing exercises
- Meditation techniques
- Stress management strategies

🎯 **Goal Achievement**:
- Set SMART goals
- Track progress
- Stay motivated with daily check-ins

🔊 **Voice Journaling**:
- Express yourself naturally
- Process emotions verbally
- Review your emotional journey

---

What would you like to explore today? You can:
- Share how you're feeling emotionally
- Ask for help with tasks or reminders
- Request a breathing exercise
- Talk about your goals
- Or just chat - I'm here to listen

What's on your mind?
        """.trimIndent()

        return ConversationState(
            emotion = emotion.capitalize(),
            advice = listOf(
                "Take a moment to check in with yourself: How am I really feeling?",
                "Practice one small act of self-care today",
                "Remember: It's okay to not be okay. You're doing your best."
            ),
            lastMessage = text,
            modelLoaded = true,
            isMockMode = true,
            detailedResponse = response
        )
    }

    private fun generateDetailedEmotionalResponse(
        emotion: String,
        originalText: String,
        lowerText: String
    ): String {
        return when (emotion) {
            "anxious" -> """
I hear that you're feeling anxious right now. That's completely valid, and I want you to know that anxiety is your brain trying to protect you - even if it doesn't feel helpful in the moment.

🧠 **What's Happening In Your Body**:
When you're anxious, your amygdala (your brain's alarm system) activates your fight-or-flight response. This causes:
- Racing heart / rapid breathing
- Muscle tension
- Difficulty concentrating
- Feeling "on edge"

This is NORMAL. Your body is doing what it's designed to do. But we can help it calm down.

---

💙 **Immediate Relief Strategies**:

**1. The 4-7-8 Breathing Technique** (Works in 2 minutes):
   - Inhale through nose for 4 counts
   - Hold for 7 counts
   - Exhale through mouth for 8 counts
   - Repeat 4 times
   
   Why it works: The long exhale activates your vagus nerve, which tells your body "we're safe now."

**2. The 5-4-3-2-1 Grounding Method**:
   - Name 5 things you can SEE
   - 4 things you can TOUCH
   - 3 things you can HEAR
   - 2 things you can SMELL
   - 1 thing you can TASTE
   
   Why it works: Brings you back to the present moment. Anxiety lives in the future; grounding brings you to NOW.

**3. Progressive Muscle Relaxation**:
   - Tense your toes for 5 seconds, then release
   - Move up through each muscle group
   - Notice the difference between tension and relaxation
   
   Why it works: Physically releases the tension anxiety creates in your body.

---

📝 **Anxiety Thought Patterns** (Cognitive Distortions):

Your anxious thoughts might sound like:
- "What if [worst case scenario]?"
- "I can't handle this"
- "Something terrible is going to happen"

Let's reframe them:
- "What if things go well? What if I'm worrying for nothing?"
- "I've handled difficult things before. I have coping skills."
- "My anxiety is making predictions, not stating facts."

---

🌱 **Long-Term Anxiety Management**:

1. **Daily Practice**: 10 minutes of meditation or breathing exercises
2. **Exercise**: 20-30 minutes most days (releases natural anxiety-fighting chemicals)
3. **Sleep Hygiene**: Anxiety and poor sleep feed each other
4. **Limit Caffeine**: Can trigger anxiety symptoms
5. **Journal**: Write down worries to get them out of your head

---

💪 **You're Stronger Than You Think**:

Remember: Feelings are temporary. You've survived 100% of your worst days so far. This anxious moment will pass.

What would help you most right now? A breathing exercise? A distraction? Or would you like to talk more about what's worrying you?
            """.trimIndent()

            "sad" -> """
I'm really sorry you're feeling this way. Sadness can feel so heavy, and I want you to know that what you're feeling is real and valid.

💙 **Understanding Your Sadness**:

Sadness is one of our core emotions, and it serves a purpose:
- It signals that something matters to you
- It creates space for processing loss or disappointment
- It can actually bring people together (we reach out when we're sad)

But when sadness feels overwhelming or won't lift, we need to actively care for it.

---

🌧️ **Immediate Comfort Strategies**:

**1. Let Yourself Feel It** (But Set a Time Limit):
   - Allow yourself 15 minutes to fully feel sad
   - Cry if you need to - tears release stress hormones
   - After 15 minutes, shift to a gentle activity
   
   Why: Suppressing emotions makes them stronger. Feeling them (with boundaries) helps them pass.

**2. Reach Out to Someone**:
   - Text or call a friend, even if it's hard
   - You don't need to explain everything
   - Even saying "I'm having a tough day" can help
   
   Research shows: Social connection is one of the strongest predictors of mental health recovery.

**3. Move Your Body Gently**:
   - Take a 10-minute walk outside
   - Do some gentle stretches
   - Put on music and sway/dance
   
   Why: Movement releases endorphins and serotonin, which are natural mood lifters.

**4. Practice Self-Compassion**:
   - Talk to yourself like you'd talk to a good friend
   - "This is really hard right now, and that's okay"
   - "I'm doing the best I can"
   
   Research by Kristin Neff shows self-compassion is more effective than self-esteem for mental health.

---

🎨 **Creative Expression** (Powerful for Processing Sadness):

- **Write**: Journal your feelings without filtering
- **Draw/Paint**: You don't need to be good at it
- **Music**: Listen to sad music (it actually helps) or play an instrument
- **Voice Journal**: Sometimes speaking is easier than writing

Why it works: Creative expression helps you process emotions without words, accessing parts of your brain that logical thinking can't reach.

---

🌅 **Building Back Up**:

**Behavioral Activation** (Clinically proven for depression):
1. Make a list of activities that usually bring you joy
2. Commit to doing ONE today, even if you don't feel like it
3. Notice: Did your mood shift even slightly afterward?

Examples:
- Watch a comedy show (laughter genuinely helps)
- Take a warm shower or bath
- Sit in sunlight for 15 minutes
- Cook or eat something you enjoy
- Pet an animal
- Look at photos of good memories

**Important**: You don't have to WANT to do these. Do them anyway. Action often comes before motivation.

---

⚠️ **When to Reach Out for More Help**:

If you're experiencing:
- Thoughts of self-harm or suicide
- Sadness lasting more than 2 weeks without relief
- Inability to do basic daily tasks
- Loss of interest in everything you used to enjoy

Please reach out to:
- A mental health professional
- Crisis hotline: 988 (US) or local equivalent
- A trusted friend or family member

---

💚 **You're Not Alone**:

1 in 5 people experience depression in their lifetime. What you're feeling is part of being human. And it WILL get better, especially with the right support and strategies.

I'm here with you. What small thing feels doable right now?
            """.trimIndent()

            "happy" -> """
I love seeing this energy from you! Happiness is such a powerful state, and I'm so glad you're experiencing it right now. 😊

✨ **Why This Matters**:

Positive emotions aren't just nice to feel - they actually:
- Broaden your perspective (you see more possibilities)
- Build psychological resources for tough times
- Strengthen your immune system
- Improve creativity and problem-solving
- Create upward spirals (happiness builds more happiness)

This is called the "broaden-and-build" theory by Barbara Fredrickson.

---

🌟 **Amplify & Extend This Feeling**:

**1. Savor It** (Don't Let It Pass Unnoticed):
   - Pause right now and FEEL the happiness
   - Notice where you feel it in your body
   - Take a mental snapshot of this moment
   - Say out loud: "I feel happy right now, and that's wonderful"
   
   Why: Savoring extends positive emotions and makes them more memorable.

**2. Share It**:
   - Call or text someone with good news
   - Post about something you're excited about
   - Do something kind for someone else
   
   Research shows: Happiness multiplies when shared. And making others happy makes YOU happier.

**3. Document It**:
   - Write in detail: What happened? How do you feel? What led to this?
   - Take photos or videos
   - Voice record yourself describing this moment
   
   Why: You're creating a "happiness bank" you can revisit during tough times.

**4. Use This Energy Strategically**:
   - Tackle a task you've been avoiding (you have the energy now!)
   - Have a difficult conversation (you're in a better state for it)
   - Start a new habit or project
   - Learn something new
   
   Positive emotions give you extra mental resources - use them!

---

🎯 **Create More Moments Like This**:

Reflect on what led to this happiness:
- What activities were you doing?
- Who were you with?
- What thoughts or mindsets helped?
- What did you do differently today?

**Make a "Joy Map"**:
List everything that brings you genuine happiness, then:
- Schedule MORE of these activities
- Protect time for them
- Say "no" to things that drain you

---

😊 **Gratitude Practice** (Locks In Positive Emotions):

Right now, write down:
1. What specifically made you happy today?
2. Why did it matter to you?
3. Who or what helped create this moment?

Research by Robert Emmons shows: People who regularly practice gratitude:
- Are 25% happier
- Sleep better
- Have stronger relationships
- Exercise more

---

🔄 **Building Sustainable Happiness**:

**The Happiness Equation** (Sonja Lyubomirsky):
- 50% genetics (your baseline)
- 10% circumstances (your situation)
- 40% intentional activities (WHAT YOU CAN CONTROL!)

That 40% is HUGE. It means you can significantly influence your happiness through:
- Relationships (prioritize them!)
- Acts of kindness
- Physical activity
- Mindfulness practice
- Working toward meaningful goals

---

💫 **Spread This Energy**:

You're experiencing something beautiful right now. Consider:
- Who in your life might need a boost? Reach out to them.
- What random act of kindness could you do today?
- How can you "pay forward" the good feelings you have?

Happiness is contagious. When you share it, you create ripples.

---

🌈 **Remember This Moment**:

On hard days (because they will come - that's life), remember:
- You felt this way before
- You CAN feel this way again
- Happiness is always possible, even after darkness

Keep this feeling close. You deserve it.

What are you going to do to celebrate or extend this happiness today? 🎉
            """.trimIndent()

            else -> getGenericDetailedResponse(emotion, lowerText)
        }
    }

    private fun getGenericDetailedResponse(emotion: String, lowerText: String): String {
        return when (emotion) {
            "angry" -> """
I can sense the frustration in your words, and I want you to know that anger is a completely valid emotion. It's telling you something important.

🔥 **Understanding Your Anger**:

Anger often masks other emotions:
- Hurt ("Someone violated my boundaries")
- Fear ("I feel threatened or unsafe")
- Frustration ("Things aren't going how I want")
- Powerlessness ("I can't control this situation")

What's underneath your anger right now?

---

⚡ **Immediate Anger Management**:

**1. The Pause (Most Important)**:
   - Before saying/doing anything, count to 10
   - Take 3 deep breaths
   - Ask: "Will responding right now help or hurt?"
   
   Remember: You can't un-say words or un-do actions.

**2. Physical Release** (Anger is ENERGY in your body):
   - Do 20 jumping jacks
   - Punch a pillow
   - Go for a fast walk or run
   - Do push-ups until you're tired
   - Scream into a pillow
   
   Why: Anger activates your sympathetic nervous system. Physical activity helps discharge that energy.

**3. The Angry Letter Technique**:
   - Write EVERYTHING you're feeling (don't hold back)
   - Include all the things you wish you could say
   - Then DON'T send it
   - Destroy it or save it for later
   
   Why: Gets the anger OUT without causing damage.

---

🧠 **Anger Thought Patterns**:

Angry thoughts are often:
- All-or-nothing: "They ALWAYS do this" "This NEVER works"
- Catastrophizing: "This is the WORST" "Everything is ruined"
- Mind-reading: "They did this to hurt me on purpose"

Reality check:
- Is this thought based on facts or feelings?
- Would I think this way if I weren't angry?
- What would I tell a friend in this situation?

---

🗣️ **Communicating Anger Effectively**:

Instead of: "You make me so angry!" (Blame)
Try: "I feel angry when [situation] because [reason]" (Ownership)

Example:
❌ "You never listen to me!"
✅ "I feel frustrated when I don't feel heard because it makes me feel like my thoughts don't matter."

This is called "I-statements" - they're less likely to make the other person defensive.

---

✅ **Long-Term Anger Management**:

1. **Identify Triggers**: Keep an anger log for a week
2. **Set Boundaries**: Many people get angry because they don't set clear boundaries
3. **Practice Assertiveness**: Express needs directly and respectfully
4. **Regular Exercise**: Reduces overall stress and irritability
5. **Enough Sleep**: Sleep deprivation makes us more reactive

---

💪 **You're In Control**:

The goal isn't to never feel angry. The goal is to:
- Recognize it quickly
- Respond thoughtfully instead of reactively
- Use it as information about what needs to change

Anger can be a powerful motivator for positive change when channeled correctly.

What do you need most right now? Space? Physical release? Someone to listen?
            """.trimIndent()

            "tired" -> """
I hear you. Exhaustion - whether physical, mental, or emotional - is one of the hardest states to be in. Your body and mind are sending you a clear message.

😴 **Types of Tired**:

**Physical Tired**: Lack of sleep, overexertion
**Mental Tired**: Too much focus, decision fatigue
**Emotional Tired**: Processing difficult feelings, people-pleasing
**Soul Tired**: Lack of meaning, burnout, doing things that don't align with your values

Which type resonates most with you right now?

---

⚡ **Immediate Energy Boosters** (When you can't rest yet):

**1. The Power Nap** (20 minutes max):
   - Set an alarm for 20 minutes (no longer!)
   - Find a quiet, darkish place
   - Even just closing your eyes helps
   
   Why: A 20-minute nap improves alertness without grogginess. Longer = sleep inertia.

**2. Hydration + Protein**:
   - Drink a full glass of cold water RIGHT NOW
   - Eat something with protein (nuts, cheese, yogurt)
   - Avoid sugar crashes
   
   Why: Dehydration mimics tiredness. Protein provides sustained energy.

**3. Movement Paradox**:
   - 5 minutes of jumping jacks or dancing
   - Walk outside for 10 minutes
   - Do sun salutations or stretches
   
   Why: Movement actually CREATES energy (endorphins + oxygen to brain).

**4. Cold Exposure**:
   - Splash cold water on your face
   - Take a cold shower
   - Hold ice cubes
   
   Why: Activates your sympathetic nervous system and increases alertness.

---

🛏️ **Actual Rest** (What you really need):

**For Physical Exhaustion**:
- Sleep: 7-9 hours, consistent schedule
- Naps: 20 minutes or 90 minutes (complete sleep cycle)
- Early bedtime: Tonight, go to bed 1 hour earlier

**For Mental Exhaustion**:
- Brain break: 15 minutes of literally nothing (no phone!)
- Nature: 20 minutes outside without distractions
- Different activity: Switch from mental work to physical work

**For Emotional Exhaustion**:
- Boundaries: Say "no" to something today
- Vent: Talk to someone who just listens
- Cry: Tears release cortisol (stress hormone)

**For Soul Exhaustion**:
- Values check: List what matters most to you
- Meaning: Do ONE thing today that feels meaningful
- Identity: Reconnect with who you are outside of obligations

---

🚫 **What NOT to Do** (Common Mistakes):

❌ Tons of caffeine (crashes later, disrupts sleep)
❌ "Push through it" (leads to burnout)
❌ Sugar binges (energy spike then crash)
❌ Staying in bed all day (makes you more tired)
❌ Scrolling social media (depletes energy)

---

📅 **Preventing Future Exhaustion**:

**1. Energy Audit**:
   - What activities GIVE you energy?
   - What activities DRAIN you?
   - Can you do more of #1 and less of #2?

**2. Sleep Hygiene**:
   - Same sleep/wake time (even weekends)
   - No screens 1 hour before bed
   - Cool, dark room
   - No caffeine after 2pm

**3. Strategic Rest**:
   - Schedule rest like appointments
   - Take breaks BEFORE you're exhausted
   - One full day off per week (no productivity)

**4. Delegate & Eliminate**:
   - What can someone else do?
   - What doesn't need to be done at all?
   - Perfectionism is exhausting - aim for "good enough"

---

⚠️ **When Tiredness Is a Warning Sign**:

Persistent exhaustion despite rest might indicate:
- Depression (fatigue is a major symptom)
- Burnout (emotional exhaustion from chronic stress)
- Medical issues (thyroid, anemia, sleep apnea, etc.)

If you've been exhausted for weeks: Please see a healthcare provider.

---

💙 **Permission to Rest**:

You don't have to "earn" rest through productivity.
Rest is not lazy. Rest is ESSENTIAL.
You are not a machine. You are a human being.

What's ONE thing you can let go of today to create space for rest?
            """.trimIndent()

            "motivated" -> """
This energy is GOLD! Let's capture it and use it strategically so you can ride this wave as far as it takes you. 🚀

🔥 **What's Happening Right Now**:

You're experiencing a combination of:
- Dopamine (motivation/reward chemical)
- Norepinephrine (focus/energy)
- Clarity about what you want

This is a PERFECT state for getting things done. Let's make the most of it!

---

⚡ **Strike While The Iron Is Hot**:

**1. The 2-Minute Rule**:
   - List everything you've been putting off
   - Do any task that takes < 2 minutes RIGHT NOW
   - Knock out 5-10 small tasks in the next 20 minutes
   
   Why: Quick wins build momentum and confidence.

**2. Tackle Your Hardest Task FIRST**:
   - What's the ONE thing that would make the biggest difference?
   - Do it NOW while you have this energy
   - Don't waste motivation on easy stuff
   
   This is called "eating the frog" - do the hardest thing first.

**3. Time-Box Deep Work**:
   - Set timer for 25 minutes (Pomodoro)
   - NO distractions (phone away, close tabs)
   - 100% focus on ONE task
   - Break for 5 minutes, repeat
   
   You'll be amazed what you can accomplish in focused sprints.

---

🎯 **Channeling Motivation Strategically**:

**For New Habits**:
- Start TODAY (don't wait for Monday)
- Make it ridiculously small (2 minutes)
- Link it to existing habit (after I [X], I will [Y])

**For Goals**:
- Write down your goal in DETAIL
- Break it into smallest possible steps
- Do the FIRST step right now (not tomorrow, NOW)

**For Difficult Conversations**:
- You have courage right now - use it
- Text/call that person today
- Say the thing you've been avoiding

**For Learning**:
- Sign up for that course
- Read the first chapter
- Watch the first tutorial
- Start before you feel "ready"

---

⏰ **Sustaining This Energy**:

**1. Document Your State**:
   - How do you feel right now?
   - What led to this motivated state?
   - What are you thinking about differently?
   
   Why: On low-motivation days, read this to remember this feeling is possible.

**2. Create Systems, Not Just Goals**:
   - Motivated you can set up systems for unmotivated you
   - Example: Prep workout clothes the night before
   - Example: Auto-schedule deep work blocks
   
   James Clear: "You don't rise to the level of your goals, you fall to the level of your systems."

**3. The Motivation Trigger List**:
   Write down what gets you motivated:
   - Music that pumps you up
   - Motivational quotes or videos
   - Remembering your "why"
   - Exercise that energizes you
   
   Use this list when motivation dips.

---

🚀 **The Motivation-Action Loop**:

Common myth: Motivation → Action
Reality: Action → Motivation → More Action

So when motivation dips:
1. Do something small (5 minutes)
2. Motivation will follow the action
3. Keep the momentum going

This is why "just start" works - the hardest part is beginning.

---

⚠️ **Don't Burn Out**:

Warning: High motivation can lead to overcommitment.

**Protect Yourself**:
- Don't start 10 new projects today
- Focus on 1-3 priorities MAX
- Schedule rest time (yes, even when motivated)
- Sustainable pace beats burnout sprints

**The Plateau Will Come**:
- Motivation fluctuates naturally
- That's when systems and habits take over
- Expect it, plan for it, don't quit when it happens

---

📊 **Track This Moment**:

**Motivation Journal Entry**:
1. What am I motivated to do right now?
2. Why does this matter to me?
3. What's the first action I'll take?
4. How will I feel when I accomplish this?

**Weekly Motivation Check-In**:
- Monday: Set 3 key goals
- Daily: Track progress (yes/no)
- Friday: Celebrate wins, adjust for next week

---

💪 **The Compound Effect**:

What you do in the next hour could:
- Start a habit that changes your life
- Finish a project you've been avoiding for months
- Take a step toward a dream you've had for years

Motivated moments are RARE. Don't waste them on Netflix or social media scrolling.

What's the ONE thing you'll accomplish in the next 60 minutes?

GO! 🚀
            """.trimIndent()

            "calm" -> """
Ahh, this is a beautiful state to be in. Calm is underrated in our busy world, but it's actually one of the most valuable emotional states you can cultivate. 🌊

🧘 **What Calm Gives You**:

In a calm state, you have:
- **Clarity**: Better decision-making and perspective
- **Creativity**: Your best ideas come from relaxed states
- **Emotional Regulation**: Easier to handle stress
- **Physical Health**: Lower cortisol, better immune function
- **Connection**: More present with others

Research shows: Calm people live longer, happier lives.

---

🌿 **Deepen & Extend This Feeling**:

**1. Mindful Awareness**:
   - Notice how calm feels in your body
   - Where do you feel it? (chest, shoulders, face)
   - What thoughts accompany this calm?
   - Take a mental snapshot
   
   Why: Builds your ability to return to this state.

**2. Breathe With It**:
   - Take 10 slow, deep breaths
   - Exhale longer than you inhale
   - Feel the calm deepening with each breath
   
   Why: Extends the parasympathetic nervous system activation.

**3. Gentle Movement**:
   - Slow stretching
   - Gentle yoga
   - Walking meditation
   - Tai chi movements
   
   Why: Integrates the calm into your body.

---

📝 **Activities Perfect for Calm States**:

**Creative Work**:
- Writing, journaling, poetry
- Drawing, painting, coloring
- Music (playing or listening)
- Photography

**Reflective Work**:
- Life planning and goal-setting
- Problem-solving (solutions come easier when calm)
- Difficult conversations (you'll be more thoughtful)
- Learning something new

**Restorative Activities**:
- Reading for pleasure
- Nature walks
- Meditation or prayer
- Gentle exercise
- Quality time with loved ones

---

🛡️ **Protecting Your Calm**:

**What Breaks Calm**:
- Checking news/social media
- Rushing or multitasking
- Saying "yes" to things you don't want to do
- Skipping meals or sleep
- Caffeine overload

**What Preserves Calm**:
- Boundaries (saying "no" to protect your peace)
- Single-tasking (one thing at a time)
- Regular breaks throughout the day
- Consistent routine
- Limiting stimulation

---

🌊 **Building a Calm Baseline**:

**Daily Practices** (10-20 minutes):
1. **Morning**: Meditation, journaling, or stretching
2. **Midday**: 5-minute breathing break
3. **Evening**: Gentle wind-down routine

**Weekly Practices**:
- One full rest day (no productivity pressure)
- Nature time (proven to lower stress)
- Social connection with calm people
- Hobby time (something just for enjoyment)

**Environmental Factors**:
- Declutter your space
- Reduce noise
- Soft lighting
- Comfortable temperature
- Calming scents (lavender, vanilla)

---

🧠 **The Neuroscience of Calm**:

When you're calm:
- Prefrontal cortex (reasoning) is more active
- Amygdala (fear center) is quieter
- Vagus nerve is activated (rest & digest)
- HRV (heart rate variability) is higher = better health

You can TRAIN your nervous system to be calm more often through:
- Regular meditation (even 5 min/day)
- Cold exposure (showers)
- Deep breathing practices
- Yoga or tai chi
- Time in nature

---

⚖️ **Calm vs. Numb** (Important Distinction):

**Calm** = Present, aware, peaceful
**Numb** = Checked out, avoiding, disconnected

If you're feeling numb rather than truly calm:
- What am I avoiding feeling?
- Am I suppressing emotions?
- Do I need to process something?

True calm comes AFTER processing, not from avoiding.

---

💫 **Calm as a Superpower**:

In a world that's constantly stimulating and stressful, the ability to be calm is:
- A competitive advantage
- A gift to those around you (calm is contagious)
- A foundation for everything else in life

The Dalai Lama: "In the practice of tolerance, one's enemy is the best teacher."
Meaning: If you can stay calm in chaos, you've mastered something profound.

---

🌟 **Use This State Wisely**:

Questions to reflect on while calm:
- What do I truly want in life?
- What needs to change?
- What am I grateful for?
- What would my wisest self do in my current situations?
- How can I create more moments like this?

Your answers will be clearer now than when you're stressed.

---

🙏 **Gratitude for This Moment**:

Take a moment to appreciate:
- Your body for finding this calm
- Whatever circumstances allowed this peace
- Your commitment to your well-being

How can you protect and extend this feeling for the rest of your day?
            """.trimIndent()

            "grateful" -> """
Gratitude is one of the most powerful emotions for mental health and happiness. The fact that you're feeling it right now is something to celebrate! 🙏✨

💚 **Why Gratitude Is So Powerful**:

Research by Robert Emmons (leading gratitude researcher) shows that grateful people:
- Are 25% happier on average
- Sleep better and wake up more refreshed
- Have stronger immune systems
- Are more resilient during challenges
- Have better relationships
- Exercise more regularly
- Are more likely to help others

Gratitude is literally medicine for your mind and body.

---

🌟 **Deepening Your Gratitude**:

**Level 1: Basic Gratitude**
"I'm grateful for my friends"

**Level 2: Specific Gratitude** (MORE POWERFUL)
"I'm grateful for how Sarah listened without judgment when I needed to talk last week"

**Level 3: Gratitude for Growth** (MOST POWERFUL)
"I'm grateful for the challenge I faced, because it taught me I'm stronger than I thought"

The more specific and detailed, the stronger the effect.

---

📝 **Gratitude Practices That Actually Work**:

**1. The Three Good Things** (5 minutes/day):
   Every evening, write:
   - What happened (specific detail)
   - Why it was good
   - What caused it / your role in making it happen
   
   Study results: After 1 week, people were happier for 6 months.

**2. Gratitude Letter** (Powerful for Relationships):
   - Think of someone who changed your life
   - Write them a detailed letter of thanks
   - Read it to them (in person or call)
   
   Study results: Single biggest happiness boost of any intervention tested.

**3. Gratitude Walk** (Body + Mind):
   - Take a 15-minute walk
   - Notice things you're grateful for
   - Say "thank you" (out loud or mentally) for each one
   
   Combines movement + gratitude for double benefits.

**4. Gratitude Jar**:
   - Every day, write one thing on a slip of paper
   - Put it in a jar
   - On bad days, read a few
   
   Why: Creates a physical collection of good moments.

---

🔄 **Turning Challenges Into Gratitude**:

**The Obstacle as Gift**:
Instead of: "I hate that this happened"
Try: "What can I learn from this?" or "How did this make me stronger?"

Examples:
- Failed project → Learned what doesn't work, saved time later
- Ended relationship → Learned about myself, ready for better match
- Lost job → Forced to pursue something I actually want
- Health issue → Motivated me to finally prioritize wellness

This doesn't mean toxic positivity. It means finding meaning in difficulty.

---

🧠 **The Neuroscience of Gratitude**:

When you practice gratitude:
- Dopamine and serotonin increase (happiness chemicals)
- Amygdala (fear center) calms down
- Prefrontal cortex (decision-making) activates
- Creates new neural pathways (actually rewires your brain!)

Regular practice literally changes your brain structure to be more positive.

---

💫 **Gratitude in Relationships**:

**Transform Relationships** by expressing gratitude:
- "I noticed when you..."
- "It means so much that you..."
- "I don't say it enough, but I appreciate how you..."

Research shows: Couples who regularly express gratitude:
- Are happier and more satisfied
- Are more likely to stay together
- Have better sex lives (yes, really!)
- Handle conflict better

Don't just FEEL grateful - EXPRESS it.

---

⚠️ **When Gratitude Feels Hard**:

If you're struggling to feel grateful:
1. Start micro: "I'm grateful I can breathe"
2. Contrast: "What would life be like without [X]?"
3. Future gratitude: "My future self will be grateful I did [X] today"
4. For others: "Who in my life is struggling more than me?"

And remember: You don't have to be grateful for EVERYTHING. 
Toxic positivity ignores real pain.
True gratitude acknowledges both the good AND the hard.

---

🎁 **Pay It Forward**:

When you're feeling grateful, that's the BEST time to:
- Do something kind for someone else
- Express appreciation to people in your life
- Help someone who needs it
- Share what you're grateful for (it's contagious)

Gratitude multiplies when shared.

---

📊 **The Gratitude Challenge**:

**Try this for 30 days**:
- Morning: Write 3 specific things you're grateful for
- Evening: Share one with someone else
- Weekly: Do something kind for someone

Track your mood before and after. You'll be amazed.

---

🌈 **Gratitude as a Superpower**:

Viktor Frankl (Holocaust survivor):
"Everything can be taken from a man but one thing: the last of the human freedoms—to choose one's attitude in any given set of circumstances."

Even in the darkest moments, gratitude for small things (a breath, a kind word, a memory) can keep hope alive.

---

💚 **What Are You Grateful For Right Now?**:

Take 2 minutes and write (or speak) about:
- One person you're grateful for (and why specifically)
- One thing that went well today (in detail)
- One thing about yourself you appreciate
- One challenge that taught you something
- One simple pleasure (coffee, sunset, music)

Feel how this exercise shifts your state even more.

---

🙏 **This Moment of Gratitude**:

You're here.
You're aware.
You're feeling something beautiful.

That alone is a gift.

How will you carry this gratitude forward today?
            """.trimIndent()

            else -> """
I'm here to support you. Thank you for sharing your thoughts with me.

Based on what you've shared, let me offer some guidance and support.

🌟 **What I'm Sensing**:

Your message shows self-awareness, which is the first step to any meaningful change or processing.

---

💭 **How Can I Help You?**:

I'm equipped to support you with:

**Emotional Processing**:
- Understanding and naming your feelings
- Working through difficult emotions
- Celebrating positive states
- Building emotional awareness

**Practical Tools**:
- Task management and productivity
- Setting meaningful reminders
- Breaking down overwhelming goals
- Creating sustainable habits

**Mental Wellness**:
- Breathing exercises and grounding techniques
- Evidence-based coping strategies
- Self-compassion practices
- Stress management

**Personal Growth**:
- Goal setting and achievement
- Building resilience
- Developing self-awareness
- Creating positive change

---

🎯 **Let's Get More Specific**:

Tell me more about:
- How are you feeling emotionally right now?
- Is there something specific you'd like help with?
- Are you looking for support, advice, or just someone to listen?

I'm here for whatever you need. There's no judgment, only support.

What would be most helpful for you right now?
            """.trimIndent()
        }
    }

    private fun detectEmotion(lowerText: String): String {
        var detectedEmotion = "neutral"
        var maxMatches = 0

        for ((emotion, keywords) in emotionPatterns) {
            val matches = keywords.count { keyword -> lowerText.contains(keyword) }
            if (matches > maxMatches) {
                maxMatches = matches
                detectedEmotion = emotion
            }
        }

        // Context-based detection if no keywords found
        if (maxMatches == 0) {
            detectedEmotion = when {
                lowerText.contains("work") && (lowerText.contains("deadline") || lowerText.contains(
                    "pressure"
                )) -> "anxious"

                lowerText.contains("can't") || lowerText.contains("difficult") || lowerText.contains(
                    "hard"
                ) -> "sad"

                lowerText.contains("achieve") || lowerText.contains("goal") || lowerText.contains("want to") -> "motivated"
                lowerText.contains("thank") || lowerText.contains("appreciate") -> "grateful"
                lowerText.contains("relax") || lowerText.contains("peace") -> "calm"
                else -> "neutral"
            }
        }

        Log.d(
            "HookService",
            "Detected emotion: $detectedEmotion for text: ${lowerText.take(50)}..."
        )
        return detectedEmotion
    }

    private fun getAdviceForEmotion(emotion: String): List<String> {
        return when (emotion) {
            "anxious" -> listOf(
                "Try 4-7-8 breathing: Inhale 4, hold 7, exhale 8",
                "Use 5-4-3-2-1 grounding: Name 5 things you see, 4 you touch, 3 you hear, 2 you smell, 1 you taste",
                "Write down your worries to externalize them"
            )
            "sad" -> listOf(
                "Reach out to someone you trust - connection helps",
                "Do something creative to process emotions",
                "Practice self-compassion: talk to yourself like a friend"
            )

            "angry" -> listOf(
                "Take a timeout: count to 10 before responding",
                "Physical release: exercise or punch a pillow",
                "Use 'I feel' statements to communicate effectively"
            )

            "tired" -> listOf(
                "Take a 20-minute power nap",
                "Drink water and have a protein snack",
                "Do 5 minutes of gentle stretching"
            )

            "happy" -> listOf(
                "Share your positive energy with someone",
                "Document this moment in your journal",
                "Use this energy to tackle a task you've been avoiding"
            )

            "calm" -> listOf(
                "Use this state for reflection or meditation",
                "Journal about what created this calm",
                "Engage in creative work - your mind is clear"
            )

            "motivated" -> listOf(
                "Make a list of 3 goals and start with one NOW",
                "Use Pomodoro technique: 25 min focus, 5 min break",
                "Tackle your hardest task while you have this energy"
            )

            "grateful" -> listOf(
                "Write 3 specific gratitude statements",
                "Express thanks to someone who made a difference",
                "Pay it forward with a kind act"
            )

            else -> listOf(
                "Take a few deep breaths",
                "Check in with yourself: How am I really feeling?",
                "One small act of self-care today"
            )
        }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

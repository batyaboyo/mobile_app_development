import json
import random

# DEVOTIONS (365)
themes = ["Hope", "Peace", "Love", "Faith", "Strength", "Courage", "Grace", "Forgiveness", "Patience", "Joy", "Trust", "Wisdom", "Perseverance", "Humility", "Compassion", "Integrity", "Kindness", "Gratitude", "Obedience", "Service"]

verses = [
    ("Jeremiah 29:11", "For I know the plans I have for you..."),
    ("Romans 8:28", "And we know that in all things God works for the good..."),
    ("Philippians 4:13", "I can do all this through him who gives me strength."),
    ("Proverbs 3:5-6", "Trust in the LORD with all your heart..."),
    ("Isaiah 41:10", "So do not fear, for I am with you..."),
    ("Psalm 46:1", "God is our refuge and strength..."),
    ("Galatians 5:22-23", "But the fruit of the Spirit is love, joy, peace..."),
    ("Hebrews 11:1", "Now faith is confidence in what we hope for..."),
    ("James 1:2-3", "Consider it pure joy, my brothers and sisters..."),
    ("1 Peter 5:7", "Cast all your anxiety on him because he cares for you."),
    ("Matthew 11:28", "Come to me, all you who are weary and burdened..."),
    ("Romans 12:2", "Do not conform to the pattern of this world..."),
    ("Joshua 1:9", "Have I not commanded you? Be strong and courageous..."),
    ("Ephesians 2:8-9", "For it is by grace you have been saved..."),
    ("Psalm 23:1", "The LORD is my shepherd, I lack nothing."),
    ("John 14:27", "Peace I leave with you; my peace I give you."),
    ("Colossians 3:12", "Therefore, as God's chosen people, holy and dearly loved..."),
    ("1 Corinthians 13:4", "Love is patient, love is kind."),
    ("Micah 6:8", "He has shown you, O mortal, what is good. And what does the LORD require of you?"),
    ("Lamentations 3:22-23", "Because of the LORD's great love we are not consumed...")
]

paragraphs = [
    "In our daily walk, we often face challenges that test our resolve and focus. Yet, the message of the gospel constantly redirects us to the higher truth of God's unshakable character. When we pause and let the reality of His presence sink in, the difficulties of the moment begin to lose their grip. We are invited stepping out of our self-reliance and leaning fully into His everlasting arms. This shifts our perspective from what we lack to what He has already provided.",
    "The modern world demands our attention at every turn, pulling us in a thousand different directions. God, however, invites us into quiet trust and steady rhythm. It is not about doing more or achieving a certain status, but about abiding quietly in His steadfast love. When we remember who goes before us, we find the courage to face even the most daunting tasks with a serene confidence that we are never alone.",
    "Grace is the undeserved gift that changes everything. No matter our past mistakes or our current struggles, God's mercies are new every morning. This truth is deeply liberating. We do not have to earn our standing; we simply receive it with open hands. Knowing this allows us to walk in freedom and extend that same grace to others, transforming our relationships and our inner peace.",
    "To live a life of true purpose means aligning our hearts with God's desires. The Scriptures give us a blueprint, not of rigid rules, but of a vibrant relationship built on love and obedience. Following His commands leads to human flourishing. As we seek His wisdom in our choices, small and large, we discover that His path, while sometimes narrow and difficult, is the only one that leads to abundant life.",
    "Often, we find ourselves waiting on God—for an answer, for healing, for direction. The wilderness of waiting is difficult, yet it is profoundly formative. It is in the waiting that our character is refined and our trust deepened. If we can learn to embrace the season rather than rush through it, we will see that He is just as present in the silence as He is in the spectacular."
]

prayers = [
    "Lord, grant me the serenity to trust Your plan entirely today.",
    "Father, fill my heart with Your overwhelming peace and help me share it with others.",
    "God, teach me to lean not on my own understanding but on Your infinite wisdom.",
    "Jesus, thank You for Your endless grace; help me to walk in it firmly today.",
    "Holy Spirit, guide my steps and give me courage to face whatever comes.",
    "Lord, remind my anxious soul that You are always with me and fighting for me.",
]

def generate_devotions():
    devotions = []
    for i in range(1, 366):
        theme = random.choice(themes)
        verse = random.choice(verses)
        para = random.choice(paragraphs)
        prayer = random.choice(prayers)
        
        devotions.append(f"""
        Devotion(
            title = "A Reflection on {theme}",
            reference = "{verse[0]}",
            text = "As we consider {verse[0]}, we read: \\"{verse[1]}\\" This passage reminds us of the profound reality of {theme}. \\n\\n{para}",
            prayer = "{prayer} Amen."
        )""")
    return devotions

# PRAYERS (365)
prayer_themes = ["Strength", "Guidance", "Patience", "Peace", "Gratitude", "Forgiveness", "Healing", "Protection", "Wisdom", "Joy", "Provision", "Comfort"]

def generate_prayers():
    morning = []
    evening = []
    
    for i in range(1, 366):
        theme = random.choice(prayer_themes)
        verse = random.choice(verses)
        
        # Morning
        morning.append(f"""
        Prayer(
            time = "morning",
            title = "Morning Prayer for {theme}",
            verse = "{verse[1]}",
            verseRef = "{verse[0]}",
            text = "Lord, as I wake to begin this new day, my heart seeks Your {theme}. I know that the challenges ahead cannot be faced in my own strength. Guide my decisions, protect my mind, and give me a spirit of {theme.lower()} in all my interactions. May my life today reflect Your love and light.",
            closing = "In Jesus' name, Amen."
        )""")
        
        # Evening
        theme2 = random.choice(prayer_themes)
        verse2 = random.choice(verses)
        evening.append(f"""
        Prayer(
            time = "evening",
            title = "Evening Prayer for {theme2}",
            verse = "{verse2[1]}",
            verseRef = "{verse2[0]}",
            text = "Heavenly Father, as this day closes, I come to You to find rest and {theme2}. I cast all the anxieties and burdens of the day upon You, knowing You care for me. Forgive my shortcomings, restore my soul as I sleep, and surround my home with Your divine peace.",
            closing = "In Your holy name I pray, Amen."
        )""")
        
    return morning, evening

# STORIES (100)
def generate_stories():
    stories = []
    story_templates = [
        ("The Call of Abraham", "Genesis 12", "God calls Abram to leave everything and promises to make him a great nation.", "old-testament", "🐪"),
        ("Moses and the Burning Bush", "Exodus 3", "God speaks to Moses from a burning bush, calling him to free Israel.", "old-testament", "🔥"),
        ("David's Triumph", "1 Samuel 17", "A young shepherd defeats a giant warrior with faith and a sling.", "old-testament", "🗡️"),
        ("Daniel in the Lion's Den", "Daniel 6", "Daniel's unwavering faith protects him from hungry lions.", "old-testament", "🦁"),
        ("The Birth of Jesus", "Luke 2", "The Savior is born in a humble manger in Bethlehem.", "new-testament", "🌟"),
        ("The Feeding of the 5000", "Matthew 14", "Jesus multiplies five loaves and two fish to feed a massive crowd.", "new-testament", "🍞"),
        ("The Good Samaritan", "Luke 10", "A story teaching us that our neighbor is anyone in need.", "new-testament", "❤️"),
        ("The Resurrection", "Matthew 28", "Jesus defeats death and rises from the grave on the third day.", "new-testament", "✝️")
    ]
    
    # We will loop and generate 100 stories mathematically filling it out.
    for i in range(1, 101):
        idx = i % len(story_templates)
        template = story_templates[idx]
        
        stories.append(f"""
        BibleStory(
            id = "story_{i}",
            title = "{template[0]} (Part {i})",
            icon = "{template[4]}",
            testament = "{template[3]}",
            reference = "{template[1]}",
            snippet = "{template[2]}",
            sections = listOf(
                StorySection(title = "The Beginning", text = "This is the remarkable story from {template[1]} where we see God's providence unfold. {template[2]} It teaches us profound lessons about faith and courage."),
                StorySection(title = "The Conflict", text = "As the events progressed, great challenges arose. Yet, God was always present, guiding the faithful through the trials of their generation."),
                StorySection(title = "The Resolution", text = "In the end, victory belongs to the Lord. His promises are true and His love endures forever.")
            ),
            moral = "Trust in God through all circumstances, for He is faithful.",
            keyVerse = KeyVerse(text = "The Lord is faithful to all His promises.", ref = "Psalm 145:13")
        )""")
    return stories

def main():
    devos = generate_devotions()
    m_prayers, e_prayers = generate_prayers()
    stories = generate_stories()
    
    print("Writing DevotionData.kt...")
    with open(r"c:\\Users\\batzt\\Desktop\\b7b\\projects\\bible_app\\app\\src\\main\\java\\com\\theword\\app\\data\\embedded\\DevotionData.kt", "w", encoding="utf-8") as f:
        f.write("package com.theword.app.data.embedded\\n\\n")
        f.write("data class Devotion(val title: String, val reference: String, val text: String, val prayer: String)\\n\\n")
        f.write("object DevotionData {\\n    val list = listOf(\\n")
        f.write(",\\n".join(devos))
        f.write("\\n    )\\n}\\n")

    print("Writing PrayerData.kt...")
    with open(r"c:\\Users\\batzt\\Desktop\\b7b\\projects\\bible_app\\app\\src\\main\\java\\com\\theword\\app\\data\\embedded\\PrayerData.kt", "w", encoding="utf-8") as f:
        f.write("package com.theword.app.data.embedded\\n\\n")
        f.write("import com.theword.app.domain.model.Prayer\\n\\n")
        f.write("object PrayerData {\\n")
        f.write("    val morning = listOf(\\n")
        f.write(",\\n".join(m_prayers))
        f.write("\\n    )\\n\\n")
        f.write("    val evening = listOf(\\n")
        f.write(",\\n".join(e_prayers))
        f.write("\\n    )\\n}\\n")

    print("Writing StoriesData.kt...")
    with open(r"c:\\Users\\batzt\\Desktop\\b7b\\projects\\bible_app\\app\\src\\main\\java\\com\\theword\\app\\data\\embedded\\StoriesData.kt", "w", encoding="utf-8") as f:
        f.write("package com.theword.app.data.embedded\\n\\n")
        f.write("import com.theword.app.domain.model.BibleStory\\n")
        f.write("import com.theword.app.domain.model.KeyVerse\\n")
        f.write("import com.theword.app.domain.model.StorySection\\n\\n")
        f.write("val STORIES_DATA = listOf(\\n")
        f.write(",\\n".join(stories))
        f.write("\\n)\\n")

if __name__ == "__main__":
    main()

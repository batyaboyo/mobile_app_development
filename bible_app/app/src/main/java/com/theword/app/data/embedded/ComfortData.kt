package com.theword.app.data.embedded

import com.theword.app.domain.model.Verse

data class ComfortCategory(
    val id: String,
    val name: String,
    val icon: String,
    val verses: List<ComfortVerse>
)

data class ComfortVerse(
    val reference: String,
    val text: String,
    val bookId: String,
    val chapter: Int
)

object ComfortData {
    val categories = listOf(
        ComfortCategory(
            id = "anxiety",
            name = "Anxiety & Worry",
            icon = "🌿",
            verses = listOf(
                ComfortVerse("Philippians 4:6-7", "Do not be anxious about anything, but in every situation, by prayer and petition, with thanksgiving, present your requests to God.", "PHP", 4),
                ComfortVerse("1 Peter 5:7", "Cast all your anxiety on him because he cares for you.", "1PE", 5),
                ComfortVerse("Matthew 6:34", "Therefore do not worry about tomorrow, for tomorrow will worry about itself. Each day has enough trouble of its own.", "MAT", 6),
                ComfortVerse("Psalm 94:19", "When anxiety was great within me, your consolation brought me joy.", "PSA", 94),
                ComfortVerse("Joshua 1:9", "Have I not commanded you? Be strong and courageous. Do not be afraid; do not be discouraged, for the Lord your God will be with you wherever you go.", "JOS", 1)
            )
        ),
        ComfortCategory(
            id = "peace",
            name = "Peace & Rest",
            icon = "🕊️",
            verses = listOf(
                ComfortVerse("John 14:27", "Peace I leave with you; my peace I give you. I do not give to you as the world gives. Do not let your hearts be troubled and do not be afraid.", "JHN", 14),
                ComfortVerse("Matthew 11:28", "Come to me, all you who are weary and burdened, and I will give you rest.", "MAT", 11),
                ComfortVerse("Psalm 23:1-3", "The Lord is my shepherd, I lack nothing. He makes me lie down in green pastures, he leads me beside quiet waters, he refreshes my soul.", "PSA", 23),
                ComfortVerse("Isaiah 26:3", "You will keep in perfect peace those whose minds are steadfast, because they trust in you.", "ISA", 26),
                ComfortVerse("John 16:33", "I have told you these things, so that in me you may have peace. In this world you will have trouble. But take heart! I have overcome the world.", "JHN", 16)
            )
        ),
        ComfortCategory(
            id = "strength",
            name = "Strength & Courage",
            icon = "💪",
            verses = listOf(
                ComfortVerse("Isaiah 40:31", "But those who hope in the Lord will renew their strength. They will soar on wings like eagles; they will run and not grow weary, they will walk and not be faint.", "ISA", 40),
                ComfortVerse("Philippians 4:13", "I can do all this through him who gives me strength.", "PHP", 4),
                ComfortVerse("Psalm 46:1", "God is our refuge and strength, an ever-present help in trouble.", "PSA", 46),
                ComfortVerse("2 Timothy 1:7", "For the Spirit God gave us does not make us timid, but gives us power, love and self-discipline.", "2TI", 1),
                ComfortVerse("Isaiah 41:10", "So do not fear, for I am with you; do not be dismayed, for I am your God. I will strengthen you and help you; I will uphold you with my righteous right hand.", "ISA", 41)
            )
        ),
        ComfortCategory(
            id = "hope",
            name = "Hope & Future",
            icon = "⚓",
            verses = listOf(
                ComfortVerse("Jeremiah 29:11", "For I know the plans I have for you, declares the Lord, plans to prosper you and not to harm you, plans to give you hope and a future.", "JER", 29),
                ComfortVerse("Romans 15:13", "May the God of hope fill you with all joy and peace as you trust in him, so that you may overflow with hope by the power of the Holy Spirit.", "ROM", 15),
                ComfortVerse("Psalm 130:5", "I wait for the Lord, my whole being waits, and in his word I put my hope.", "PSA", 130),
                ComfortVerse("Hebrews 11:1", "Now faith is confidence in what we hope for and assurance about what we do not see.", "HEB", 11),
                ComfortVerse("Lamentations 3:22-23", "Because of the Lord's great love we are not consumed, for his compassions never fail. They are new every morning; great is your faithfulness.", "LAM", 3)
            )
        ),
        ComfortCategory(
            id = "love",
            name = "God's Love",
            icon = "❤️",
            verses = listOf(
                ComfortVerse("Romans 8:38-39", "For I am convinced that neither death nor life, neither angels nor demons, neither the present nor the future, nor any powers, neither height nor depth, nor anything else in all creation, will be able to separate us from the love of God that is in Christ Jesus our Lord.", "ROM", 8),
                ComfortVerse("1 John 4:19", "We love because he first loved us.", "1JN", 4),
                ComfortVerse("Psalm 36:7", "How priceless is your unfailing love, O God! People take refuge in the shadow of your wings.", "PSA", 36),
                ComfortVerse("John 3:16", "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life.", "JHN", 3),
                ComfortVerse("Zephaniah 3:17", "The Lord your God is in your midst, a mighty one who will save; he will rejoice over you with gladness; he will quiet you by his love; he will exult over you with loud singing.", "ZEP", 3)
            )
        ),
        ComfortCategory(
            id = "forgiveness",
            name = "Forgiveness",
            icon = "✨",
            verses = listOf(
                ComfortVerse("1 John 1:9", "If we confess our sins, he is faithful and just and will forgive us our sins and purify us from all unrighteousness.", "1JN", 1),
                ComfortVerse("Ephesians 4:32", "Be kind and compassionate to one another, forgiving each other, just as in Christ God forgave you.", "EPH", 4),
                ComfortVerse("Psalm 103:12", "As far as the east is from the west, so far has he removed our transgressions from us.", "PSA", 103),
                ComfortVerse("Micah 7:19", "You will again have compassion on us; you will tread our sins underfoot and hurl all our iniquities into the depths of the sea.", "MIC", 7),
                ComfortVerse("Colossians 3:13", "Bear with each other and forgive one another if any of you has a grievance against someone. Forgive as the Lord forgave you.", "COL", 3)
            )
        )
    )
}

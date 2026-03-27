const fs = require('fs');
const path = require('path');

// 1. Read stories
const storiesCode = fs.readFileSync('data/stories-data.js', 'utf8');
const storiesContext = { window: {} };
require('vm').createContext(storiesContext);
require('vm').runInContext(storiesCode, storiesContext);
const stories = storiesContext.window.BIBLE_STORIES_DATA;

// 2. Read prayers
const scriptCode = fs.readFileSync('script.js', 'utf8');
let mStart = scriptCode.indexOf('const MORNING_PRAYERS = [');
let mEnd = scriptCode.indexOf('];\n\nconst EVENING_PRAYERS = [');
let mStr = scriptCode.substring(mStart, mEnd + 1).replace('const MORNING_PRAYERS = ', '');

let eStart = scriptCode.indexOf('const EVENING_PRAYERS = [');
let eEnd = scriptCode.indexOf('];\n\nlet prayerTimerInterval = null;');
let eStr = scriptCode.substring(eStart, eEnd + 1).replace('const EVENING_PRAYERS = ', '');

const morningPrayers = eval(mStr);
const eveningPrayers = eval(eStr);

function escapeK(str) {
    if (str === null || str === undefined) return '""';
    return '"' + str.replace(/\\/g, '\\\\').replace(/"/g, '\\"').replace(/\n/g, '\\n').replace(/\$/g, '\\$') + '"';
}

// Generate StoriesData.kt
let storiesKt = `package com.theword.app.data.embedded

import com.theword.app.domain.model.BibleStory
import com.theword.app.domain.model.KeyVerse
import com.theword.app.domain.model.StorySection

val STORIES_DATA = listOf(
`;

for (let i = 0; i < stories.length; i++) {
    const s = stories[i];
    storiesKt += `    BibleStory(\n`;
    storiesKt += `        id = ${escapeK(s.id)},\n`;
    storiesKt += `        title = ${escapeK(s.title)},\n`;
    storiesKt += `        icon = ${escapeK(s.icon)},\n`;
    storiesKt += `        testament = ${escapeK(s.testament)},\n`;
    storiesKt += `        reference = ${escapeK(s.reference)},\n`;
    storiesKt += `        snippet = ${escapeK(s.snippet)},\n`;
    
    storiesKt += `        sections = listOf(\n`;
    for (let j = 0; j < s.sections.length; j++) {
        const sec = s.sections[j];
        storiesKt += `            StorySection(title = ${escapeK(sec.title)}, text = ${escapeK(sec.text)})${j < s.sections.length - 1 ? ',' : ''}\n`;
    }
    storiesKt += `        ),\n`;
    
    storiesKt += `        moral = ${escapeK(s.moral)},\n`;
    storiesKt += `        keyVerse = KeyVerse(text = ${escapeK(s.keyVerse.text)}, ref = ${escapeK(s.keyVerse.ref)})\n`;
    storiesKt += `    )${i < stories.length - 1 ? ',' : ''}\n`;
}
storiesKt += `)\n`;

fs.writeFileSync('../app/src/main/java/com/theword/app/data/embedded/StoriesData.kt', storiesKt, 'utf8');


// Generate PrayerData.kt
let prayerKt = `package com.theword.app.data.embedded

import com.theword.app.domain.model.Prayer

object PrayerData {
    val morning = listOf(
`;
for (let i = 0; i < morningPrayers.length; i++) {
    const p = morningPrayers[i];
    prayerKt += `        Prayer(\n`;
    prayerKt += `            time = "morning",\n`;
    prayerKt += `            title = ${escapeK(p.title)},\n`;
    prayerKt += `            verse = ${escapeK(p.verse)},\n`;
    prayerKt += `            verseRef = ${escapeK(p.verseRef)},\n`;
    prayerKt += `            text = ${escapeK(p.text)},\n`;
    prayerKt += `            closing = ${escapeK(p.closing)}\n`;
    prayerKt += `        )${i < morningPrayers.length - 1 ? ',' : ''}\n`;
}
prayerKt += `    )\n\n    val evening = listOf(\n`;

for (let i = 0; i < eveningPrayers.length; i++) {
    const p = eveningPrayers[i];
    prayerKt += `        Prayer(\n`;
    prayerKt += `            time = "evening",\n`;
    prayerKt += `            title = ${escapeK(p.title)},\n`;
    prayerKt += `            verse = ${escapeK(p.verse)},\n`;
    prayerKt += `            verseRef = ${escapeK(p.verseRef)},\n`;
    prayerKt += `            text = ${escapeK(p.text)},\n`;
    prayerKt += `            closing = ${escapeK(p.closing)}\n`;
    prayerKt += `        )${i < eveningPrayers.length - 1 ? ',' : ''}\n`;
}
prayerKt += `    )\n}\n`;

fs.writeFileSync('../app/src/main/java/com/theword/app/data/embedded/PrayerData.kt', prayerKt, 'utf8');

console.log('Conversion successful!');

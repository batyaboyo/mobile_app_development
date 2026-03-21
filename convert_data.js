const fs = require('fs');
const path = require('path');

// Mock window and other GLOBALS for eval
global.window = {};

function extractArray(content, arrayName) {
    const startMarker = `const ${arrayName} = [`;
    const startIndex = content.indexOf(startMarker);
    if (startIndex === -1) {
        console.warn(`Could not find ${arrayName} in script.js`);
        return [];
    }
    
    // Find the end of the array ];
    // We'll look for ]; starting from startIndex
    let endIndex = content.indexOf('];', startIndex);
    if (endIndex === -1) {
        console.warn(`Could not find end of ${arrayName} in script.js`);
        return [];
    }
    
    const arrayStr = content.substring(startIndex + startMarker.length - 1, endIndex + 2);
    try {
        // Eval only the isolated array string
        return eval(arrayStr);
    } catch (e) {
        console.error(`Error eval-ing ${arrayName}:`, e);
        return [];
    }
}

try {
    const stories_path = path.join('web app', 'data', 'stories-data.js');
    const quiz_path = path.join('web app', 'data', 'quiz-data.js');
    const script_path = path.join('web app', 'script.js');

    console.log('Reading source files...');
    const stories_src = fs.readFileSync(stories_path, 'utf8');
    const quiz_src = fs.readFileSync(quiz_path, 'utf8');
    const script_src = fs.readFileSync(script_path, 'utf8');

    console.log('Evaluating stories and quiz...');
    eval(stories_src);
    const clean_quiz_src = quiz_src.replace(/\];\s*\];/g, '];');
    eval(clean_quiz_src);

    // Process STORIES
    const storiesRaw = window.BIBLE_STORIES_DATA || [];
    const stories = storiesRaw.map(s => ({
        id: s.id,
        title: s.title,
        testament: s.testament,
        icon: s.icon,
        content: s.sections || s.content || [],
        moral: s.moral,
        keyVerse: s.keyVerse,
        snippets: s.snippets || ""
    }));

    // Process QUIZ
    const quizRaw = window.QUIZ_QUESTIONS_DATA || [];
    const quiz = quizRaw.map(q => ({
        category: q.category,
        question: q.q,
        options: q.options,
        answerIndex: q.answer,
        reference: q.ref
    }));

    // Process PRAYERS
    console.log('Extracting prayers from script.js...');
    const morningRaw = extractArray(script_src, 'MORNING_PRAYERS');
    const eveningRaw = extractArray(script_src, 'EVENING_PRAYERS');
    
    const prayers = [];
    console.log(`Found ${morningRaw.length} morning prayers.`);
    morningRaw.forEach(p => {
        prayers.push({
            type: 'morning',
            title: p.title,
            verse: p.verse,
            verseRef: p.ref || p.verseRef,
            text: p.prayer || p.text,
            closing: p.closing
        });
    });

    console.log(`Found ${eveningRaw.length} evening prayers.`);
    eveningRaw.forEach(p => {
        prayers.push({
            type: 'evening',
            title: p.title,
            verse: p.verse,
            verseRef: p.ref || p.verseRef,
            text: p.prayer || p.text,
            closing: p.closing
        });
    });

    // Write output files
    fs.writeFileSync('stories.json', JSON.stringify(stories, null, 2));
    fs.writeFileSync('quiz.json', JSON.stringify(quiz, null, 2));
    fs.writeFileSync('prayers.json', JSON.stringify(prayers, null, 2));
    
    console.log('Successfully generated JSON files: stories.json, quiz.json, prayers.json');

    // Move to assets
    const assetsDir = path.join('app', 'src', 'main', 'assets');
    if (fs.existsSync(assetsDir)) {
        fs.copyFileSync('stories.json', path.join(assetsDir, 'stories.json'));
        fs.copyFileSync('quiz.json', path.join(assetsDir, 'quiz.json'));
        fs.copyFileSync('prayers.json', path.join(assetsDir, 'prayers.json'));
        console.log('Moved files to Android assets directory.');
    } else {
        console.warn('Assets directory not found, files remain in root.');
    }

} catch (err) {
    console.error('Error during conversion:', err);
    process.exit(1);
}

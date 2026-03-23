const https = require('https');
const fs = require('fs');

const books = [
  "Genesis","Exodus","Leviticus","Numbers","Deuteronomy",
  "Joshua","Judges","Ruth","1 Samuel","2 Samuel",
  "1 Kings","2 Kings","1 Chronicles","2 Chronicles","Ezra",
  "Nehemiah","Esther","Job","Psalms","Proverbs",
  "Ecclesiastes","Song Of Solomon","Isaiah","Jeremiah","Lamentations",
  "Ezekiel","Daniel","Hosea","Joel","Amos",
  "Obadiah","Jonah","Micah","Nahum","Habakkuk",
  "Zephaniah","Haggai","Zechariah","Malachi",
  "Matthew","Mark","Luke","John","Acts",
  "Romans","1 Corinthians","2 Corinthians","Galatians","Ephesians",
  "Philippians","Colossians","1 Thessalonians","2 Thessalonians",
  "1 Timothy","2 Timothy","Titus","Philemon","Hebrews",
  "James","1 Peter","2 Peter","1 John","2 John","3 John",
  "Jude","Revelation"
];

const abbrevs = [
  "gn","ex","lv","nm","dt","js","jg","rt","1sm","2sm",
  "1kgs","2kgs","1ch","2ch","ezr","ne","et","job","ps","prv",
  "ec","so","is","jr","lm","ez","dn","ho","jl","am",
  "ob","jn","mi","na","hk","zp","hg","zc","ml",
  "mt","mk","lk","jo","act","rm","1co","2co","gl","eph",
  "ph","cl","1ts","2ts","1tm","2tm","tt","phm","hb",
  "jm","1pe","2pe","1jo","2jo","3jo","jd","re"
];

function fetchJSON(url) {
  return new Promise((resolve, reject) => {
    https.get(url, { headers: { 'User-Agent': 'Node' } }, (res) => {
      if (res.statusCode === 301 || res.statusCode === 302) {
        return fetchJSON(res.headers.location).then(resolve).catch(reject);
      }
      if (res.statusCode !== 200) {
        return reject(new Error(`HTTP ${res.statusCode}`));
      }
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => {
        try { resolve(JSON.parse(data)); } catch(e) { reject(e); }
      });
    }).on('error', reject);
  });
}

function delay(ms) { return new Promise(r => setTimeout(r, ms)); }

async function main() {
  const result = [];
  for (let i = 0; i < books.length; i++) {
    const bookName = books[i];
    const abbrev = abbrevs[i];
    const encoded = encodeURIComponent(bookName).replace(/%20/g, '%20');
    const url = `https://raw.githubusercontent.com/aruljohn/Bible-niv/main/${encoded}.json`;
    console.log(`[${i+1}/${books.length}] Fetching ${bookName}...`);
    try {
      const data = await fetchJSON(url);
      const chapters = data.chapters.map(ch => ch.verses.map(v => v.text));
      result.push({ abbrev, name: bookName, chapters });
      console.log(`  OK - ${chapters.length} chapters`);
    } catch(e) {
      console.error(`  FAILED: ${e.message}`);
    }
    await delay(100);
  }
  fs.writeFileSync('app/src/main/assets/bible_niv.json', JSON.stringify(result));
  console.log(`\nDone! Wrote ${result.length} books.`);
}

main();

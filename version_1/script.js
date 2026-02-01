// --- Data Model & Persistence ---
const STORAGE_KEY = 'sobriety_app_data_v1';

let appData = {
    startDate: null,
    checkIns: [],
    motivations: [],
    distractions: ['Go for a walk', 'Drink a glass of water', 'Call a friend', 'Read 5 pages', 'Do 10 pushups'], // Defaults
    contacts: [] // {name, info}
};

function loadData() {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
        // Merge defaults if needed
        const parsed = JSON.parse(stored);
        appData = { ...appData, ...parsed };
    }
}

function saveData() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(appData));
}

// --- Domain Logic ---

function getDaysSober() {
    if (!appData.startDate) return 0;
    const start = new Date(appData.startDate);
    const now = new Date();
    start.setHours(0, 0, 0, 0);
    now.setHours(0, 0, 0, 0);

    const diffTime = now - start;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays >= 0 ? diffDays : 0;
}

function getTodayString() {
    const d = new Date();
    return d.toISOString().split('T')[0];
}

// --- Check-In Logic ---

function handleCheckIn() {
    const today = getTodayString();
    if (!appData.checkIns.includes(today)) {
        appData.checkIns.push(today);
        saveData();
        updateCheckInUI();
        renderCalendar();
        setTimeout(() => alert('Great job! Day logged.'), 10);
    }
}

function updateCheckInUI() {
    const today = getTodayString();
    const btn = document.getElementById('check-in-btn');
    const msg = document.getElementById('check-in-msg');

    if (appData.checkIns.includes(today)) {
        btn.disabled = true;
        btn.textContent = 'Checked In for Today';
        btn.classList.add('success');

        const streak = getDaysSober(); // Simplified streak
        msg.textContent = `Great job! Day ${streak} logged.`;
        msg.classList.remove('hidden');
    } else {
        btn.disabled = false;
        btn.textContent = "I'm Sober Today";
        btn.classList.remove('success');
        msg.classList.add('hidden');
    }
}

// --- Visualizations ---

function renderCalendar() {
    const grid = document.getElementById('calendar-grid');
    grid.innerHTML = '';
    const streakDisplay = document.getElementById('streak-display');
    streakDisplay.textContent = `Current Streak: ${getDaysSober()} days`; // Simple streak logic

    for (let i = 29; i >= 0; i--) {
        const d = new Date();
        d.setDate(d.getDate() - i);
        const dateString = d.toISOString().split('T')[0];

        const cell = document.createElement('div');
        cell.classList.add('day-cell');
        cell.textContent = d.getDate();

        if (appData.checkIns.includes(dateString)) {
            cell.classList.add('checked-in');
            cell.title = `Checked in on ${dateString}`;
        } else if (i > 0) {
            cell.classList.add('missed');
        }

        grid.appendChild(cell);
    }
}

// --- Milestones ---

const MILESTONES = [7, 30, 100, 365];

function renderMilestones() {
    const container = document.getElementById('badges-container');
    container.innerHTML = '';
    const currentDays = getDaysSober();

    MILESTONES.forEach(m => {
        const badge = document.createElement('div');
        badge.classList.add('badge');
        badge.textContent = `${m} Days`;

        if (currentDays >= m) {
            badge.classList.add('earned');
        }
        container.appendChild(badge);
    });
}

function checkMilestones() {
    const currentDays = getDaysSober();
    const celebrationMsg = document.getElementById('celebration-msg');
    const modal = document.getElementById('celebration-modal');

    // In a real app, we'd check if this specific milestone was already seen.
    // Here, we just check if today is exactly a milestone day.
    if (MILESTONES.includes(currentDays) && !appData.checkIns.includes(getTodayString())) {
        // Only show if we haven't checked in yet? Or maybe just on load?
        // Let's show it if it matches exactly.
        // Better: Check if we just hit it.
    }
}

// --- Inspirations & Toolkit ---

function renderMotivations() {
    const list = document.getElementById('motivation-list');
    list.innerHTML = '';
    appData.motivations.forEach((item, index) => {
        const li = document.createElement('li');
        li.textContent = item;
        // Add delete button logic if needed
        list.appendChild(li);
    });

    const display = document.getElementById('random-motivation');
    if (appData.motivations.length > 0) {
        const random = appData.motivations[Math.floor(Math.random() * appData.motivations.length)];
        display.innerHTML = `<p>${random}</p>`;
    } else {
        display.innerHTML = '<p class="placeholder-text">Add your reasons for quitting to see them here.</p>';
    }
}

function renderDistractions() {
    const list = document.getElementById('distraction-list');
    list.innerHTML = '';
    appData.distractions.forEach(item => {
        const li = document.createElement('li');
        li.textContent = item;
        list.appendChild(li);
    });
}

function renderContacts() {
    const list = document.getElementById('contact-list');
    list.innerHTML = '';
    appData.contacts.forEach(item => {
        const li = document.createElement('li');
        li.innerHTML = `<strong>${item.name}</strong>: <a href="tel:${item.info}">${item.info}</a>`;
        list.appendChild(li);
    });
}

// Breathing Logic
let breathingInterval;
let isBreathing = false;

function toggleBreathing() {
    const btn = document.getElementById('start-breathing-btn');
    const display = document.getElementById('breathing-display');

    if (isBreathing) {
        clearInterval(breathingInterval);
        isBreathing = false;
        btn.textContent = 'Start';
        display.textContent = 'Ready';
        display.className = 'breathing-circle';
    } else {
        isBreathing = true;
        btn.textContent = 'Stop';

        let phase = 0; // 0: Inhale (4), 1: Hold (7), 2: Exhale (8)
        let count = 0;

        const cycle = () => {
            if (!isBreathing) return;

            if (phase === 0) { // Inhale
                display.textContent = 'Inhale';
                display.className = 'breathing-circle inhale';
                setTimeout(() => {
                    if (!isBreathing) return;
                    phase = 1;
                    display.textContent = 'Hold';
                    display.className = 'breathing-circle'; // stable
                    setTimeout(() => {
                        if (!isBreathing) return;
                        phase = 2;
                        display.textContent = 'Exhale';
                        display.className = 'breathing-circle exhale';
                        setTimeout(() => {
                            if (!isBreathing) return;
                            phase = 0;
                            cycle();
                        }, 8000);
                    }, 7000);
                }, 4000);
            }
        };
        cycle();
    }
}


// --- Main UI Updates ---

function updateCounterUI() {
    const daysDisplay = document.getElementById('days-display');
    const setupSection = document.getElementById('setup-section');
    const editBtn = document.getElementById('edit-date-btn');
    const dateInput = document.getElementById('start-date');

    if (appData.startDate) {
        const days = getDaysSober();
        daysDisplay.textContent = days;
        setupSection.classList.add('hidden');
        editBtn.classList.remove('hidden');
    } else {
        daysDisplay.textContent = '0';
        setupSection.classList.remove('hidden');
        editBtn.classList.add('hidden');
        dateInput.value = '';
    }
}

function init() {
    console.log('Sobriety App Initialized');
    loadData();

    // Initial Renders
    updateCounterUI();
    updateCheckInUI();
    renderCalendar();
    renderMilestones();
    renderMotivations();
    renderDistractions();
    renderContacts();

    // --- Event Listeners ---

    // Counter
    document.getElementById('save-date-btn').addEventListener('click', () => {
        const input = document.getElementById('start-date');
        if (input.value) {
            appData.startDate = input.value;
            saveData();
            updateCounterUI();
            renderMilestones();
            renderCalendar();
        }
    });

    document.getElementById('edit-date-btn').addEventListener('click', () => {
        if (confirm('Reset your start date? This will update your day count.')) {
            appData.startDate = null;
            saveData();
            updateCounterUI();
            renderMilestones();
        }
    });

    // Check-in
    document.getElementById('check-in-btn').addEventListener('click', handleCheckIn);

    // Motivations
    document.getElementById('add-motivation-btn').addEventListener('click', () => {
        const input = document.getElementById('new-motivation');
        if (input.value) {
            appData.motivations.push(input.value);
            saveData();
            renderMotivations();
            input.value = '';
        }
    });

    // Accordion for motivations
    const acc = document.querySelector('.accordion-header');
    if (acc) {
        acc.addEventListener('click', () => {
            acc.classList.toggle('active');
            const panel = acc.nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }
        });
    }

    // Distractions
    document.getElementById('add-distraction-btn').addEventListener('click', () => {
        const input = document.getElementById('new-distraction');
        if (input.value) {
            appData.distractions.push(input.value);
            saveData();
            renderDistractions();
            input.value = '';
        }
    });

    // Contacts
    document.getElementById('add-contact-btn').addEventListener('click', () => {
        const name = document.getElementById('contact-name');
        const info = document.getElementById('contact-info');
        if (name.value && info.value) {
            appData.contacts.push({ name: name.value, info: info.value });
            saveData();
            renderContacts();
            name.value = '';
            info.value = '';
        }
    });

    // Breathing
    document.getElementById('start-breathing-btn').addEventListener('click', toggleBreathing);

    // Modal
    document.querySelector('.close-modal').addEventListener('click', () => {
        document.getElementById('celebration-modal').classList.add('hidden');
    });
}

document.addEventListener('DOMContentLoaded', init);

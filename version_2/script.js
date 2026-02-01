// State Management
const STORAGE_KEY = 'sobrietyData';

let state = {
    startDate: null,
    checkins: [], // Array of date strings 'YYYY-MM-DD'
    cravings: [], // Array of objects { date: 'YYYY-MM-DD', level: 'String' }
    journal: [], // Array of objects { date: 'ISOString', text: 'String' }
    emergencyReasons: ''
};

// DOM Elements
const views = {
    setup: document.getElementById('setup-view'),
    dashboard: document.getElementById('dashboard-view')
};

const elements = {
    startInput: document.getElementById('start-date-input'),
    startBtn: document.getElementById('start-btn'),
    dayCounter: document.getElementById('day-counter'),
    startDateDisplay: document.getElementById('start-date-display'),
    bestStreak: document.getElementById('best-streak'),
    totalDays: document.getElementById('total-days'),
    checkinBtn: document.getElementById('checkin-btn'),
    checkinMsg: document.getElementById('checkin-msg'),
    cravingBtns: document.querySelectorAll('.craving-btn'),
    historyChart: document.getElementById('history-chart'),
    journalInput: document.getElementById('journal-entry'),
    journalSaveBtn: document.getElementById('journal-save-btn'),
    journalList: document.getElementById('journal-list'),
    emergencyBtn: document.getElementById('emergency-btn'),
    emergencyModal: document.getElementById('emergency-modal'),
    closeEmergencyBtn: document.querySelector('.close-modal'),
    closeEmergencyActionBtn: document.getElementById('close-emergency-btn'),
    reasonsInput: document.getElementById('reasons-input'),
    timerDisplay: document.getElementById('timer')
};

// Initialization
function init() {
    loadData();
    setupEventListeners();
    
    if (state.startDate) {
        showDashboard();
    } else {
        showSetup();
    }
}

function loadData() {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
        state = { ...state, ...JSON.parse(stored) };
    }
}

function saveData() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

// Navigation
function showSetup() {
    views.setup.classList.remove('hidden');
    views.dashboard.classList.add('hidden');
}

function showDashboard() {
    views.setup.classList.add('hidden');
    views.dashboard.classList.remove('hidden');
    updateDashboard();
}

// Dashboard Updates
function updateDashboard() {
    updateCounter();
    updateStats();
    updateCheckinStatus();
    renderJournal();
    renderCravingHistory();
    elements.reasonsInput.value = state.emergencyReasons || '';
}

function updateCounter() {
    if (!state.startDate) return;
    
    const start = new Date(state.startDate);
    const now = new Date();
    const diffTime = Math.abs(now - start);
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24)); 
    
    elements.dayCounter.textContent = diffDays;
    elements.startDateDisplay.textContent = `Since ${start.toLocaleDateString()}`;
}

function updateStats() {
    // Total days checked in
    elements.totalDays.textContent = state.checkins.length;

    // Calculate Best Streak
    // Sort dates
    const sortedDates = state.checkins
        .map(d => new Date(d))
        .sort((a, b) => a - b);
    
    let maxStreak = 0;
    let currentStreak = 0;
    
    for (let i = 0; i < sortedDates.length; i++) {
        if (i === 0) {
            currentStreak = 1;
        } else {
            const prev = sortedDates[i-1];
            const curr = sortedDates[i];
            const diffDays = (curr - prev) / (1000 * 60 * 60 * 24);
            
            if (diffDays === 1) {
                currentStreak++;
            } else if (diffDays > 1) {
                currentStreak = 1;
            }
        }
        maxStreak = Math.max(maxStreak, currentStreak);
    }
    
    // Also consider if current streak is active (including today)
    // If user hasn't checked in today, but did yesterday, streak is potentially active. 
    // This simple logic just counts consecutive check-in days.
    elements.bestStreak.textContent = maxStreak;
}

function getTodayStr() {
    return new Date().toISOString().split('T')[0];
}

function updateCheckinStatus() {
    const today = getTodayStr();
    const hasCheckedIn = state.checkins.includes(today);
    
    if (hasCheckedIn) {
        elements.checkinBtn.textContent = "Checked In ✓";
        elements.checkinBtn.disabled = true;
        elements.checkinBtn.classList.remove('btn-success');
        elements.checkinBtn.style.backgroundColor = '#10B981'; // Ensure it looks active/success
    } else {
        elements.checkinBtn.textContent = "I stayed sober today";
        elements.checkinBtn.disabled = false;
        elements.checkinBtn.classList.add('btn-success');
    }
}

// Event Listeners
function setupEventListeners() {
    // Setup
    elements.startBtn.addEventListener('click', () => {
        const dateVal = elements.startInput.value;
        if (dateVal) {
            state.startDate = dateVal;
            saveData();
            showDashboard();
        }
    });

    // Check-in
    elements.checkinBtn.addEventListener('click', () => {
        const today = getTodayStr();
        if (!state.checkins.includes(today)) {
            state.checkins.push(today);
            state.checkins.sort(); // Keep sorted
            saveData();
            
            updateCheckinStatus();
            updateStats();
            
            // Show message
            elements.checkinMsg.textContent = "Great job! Keep it up.";
            elements.checkinMsg.classList.remove('hidden');
            setTimeout(() => {
                elements.checkinMsg.classList.add('hidden');
            }, 3000);
        }
    });

    // Cravings
    elements.cravingBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const level = e.target.dataset.level;
            const today = getTodayStr();
            
            // Remove any existing entry for today to allow "updating"
            state.cravings = state.cravings.filter(c => c.date !== today);
            
            state.cravings.push({ date: today, level: level });
            saveData();
            
            // Visual feedback
            elements.cravingBtns.forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            
            renderCravingHistory();
        });
    });

    // Journal
    elements.journalSaveBtn.addEventListener('click', () => {
        const text = elements.journalInput.value.trim();
        if (text) {
            const entry = {
                date: new Date().toISOString(),
                text: text
            };
            state.journal.unshift(entry); // Add to top
            saveData();
            elements.journalInput.value = '';
            renderJournal();
        }
    });

    // Emergency
    elements.emergencyBtn.addEventListener('click', openEmergencyModal);
    elements.closeEmergencyBtn.addEventListener('click', closeEmergencyModal);
    elements.closeEmergencyActionBtn.addEventListener('click', closeEmergencyModal);
    
    // Save User Reasons
    elements.reasonsInput.addEventListener('input', (e) => {
        state.emergencyReasons = e.target.value;
        saveData();
    });
}

function renderJournal() {
    elements.journalList.innerHTML = '';
    state.journal.forEach(entry => {
        const li = document.createElement('li');
        const date = new Date(entry.date).toLocaleString([], { dateStyle: 'short', timeStyle: 'short' });
        li.innerHTML = `<span class="journal-date">${date}</span>${entry.text}`;
        elements.journalList.appendChild(li);
    });
}

function renderCravingHistory() {
    // Show last 7 days
    elements.historyChart.innerHTML = '';
    const today = new Date();
    
    for (let i = 6; i >= 0; i--) {
        const d = new Date(today);
        d.setDate(today.getDate() - i);
        const dateStr = d.toISOString().split('T')[0];
        
        const entry = state.cravings.find(c => c.date === dateStr);
        const bar = document.createElement('div');
        bar.className = 'chart-bar';
        bar.title = dateStr;
        
        // Height based on level
        let height = '10%'; // default min height
        if (entry) {
            switch(entry.level) {
                case 'None': height = '20%'; break;
                case 'Mild': height = '40%'; break;
                case 'Strong': height = '70%'; break;
                case 'Overwhelming': height = '100%'; break;
            }
            bar.style.backgroundColor = entry.level === 'None' ? '#10B981' : 
                                      entry.level === 'Mild' ? '#FBBF24' :
                                      entry.level === 'Strong' ? '#F59E0B' : '#EF4444';
        } else {
            bar.style.backgroundColor = '#E5E7EB'; // No data
        }
        
        bar.style.height = height;
        elements.historyChart.appendChild(bar);
    }
}

// Emergency Timer Logic
let timerInterval;

function openEmergencyModal() {
    elements.emergencyModal.classList.remove('hidden');
    startTimer(300); // 5 minutes = 300 seconds
}

function closeEmergencyModal() {
    elements.emergencyModal.classList.add('hidden');
    clearInterval(timerInterval);
}

function startTimer(duration) {
    let timer = duration, minutes, seconds;
    
    // Clear existing
    if(timerInterval) clearInterval(timerInterval);
    
    updateTimerDisplay(timer);
    
    timerInterval = setInterval(() => {
        timer--;
        updateTimerDisplay(timer);
        
        if (timer <= 0) {
            clearInterval(timerInterval);
        }
    }, 1000);
}

function updateTimerDisplay(timer) {
    let minutes = parseInt(timer / 60, 10);
    let seconds = parseInt(timer % 60, 10);

    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;

    elements.timerDisplay.textContent = minutes + ":" + seconds;
}

// Run
init();

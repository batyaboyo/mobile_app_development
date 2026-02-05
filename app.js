// --- Enhanced Sobriety Tracker (Version 3 Improved) ---
// Combines best features from all three versions

// --- Data Store ---
const DB_KEY = 'soberStreakData';

const Store = {
    data: {
        quitDate: null,
        weeklySpend: 50,
        personalReasons: [],
        dailyCheckIns: {}, // 'YYYY-MM-DD': 'sober' | 'slip'
        journalEntries: {},
        moodEntries: {},
        urgeEntries: {}, // NEW: 'YYYY-MM-DD': 'None' | 'Mild' | 'Strong' | 'High'
        longestStreak: 0,
        distractions: ['Go for a walk', 'Drink water', 'Call a friend', 'Read 5 pages', 'Do 10 pushups'], // NEW
        emergencyContacts: [] // NEW: [{name, info}]
    },

    load() {
        const saved = localStorage.getItem(DB_KEY);
        if (saved) {
            const parsed = JSON.parse(saved);
            // Merge with defaults to ensure new fields exist
            this.data = { ...this.data, ...parsed };
        }
        return this.data;
    },

    save() {
        localStorage.setItem(DB_KEY, JSON.stringify(this.data));
    },

    reset() {
        if (confirm("Are you sure you want to delete all data? This cannot be undone.")) {
            localStorage.removeItem(DB_KEY);
            location.reload();
        }
    }
};

// --- Utils ---
const Utils = {
    formatDate(date) {
        const offset = date.getTimezoneOffset();
        const local = new Date(date.getTime() - (offset * 60 * 1000));
        return local.toISOString().split('T')[0];
    },

    getToday() {
        return this.formatDate(new Date());
    },

    daysBetween(date1, date2) {
        const oneDay = 24 * 60 * 60 * 1000;
        return Math.floor(Math.abs((date1 - date2) / oneDay));
    },

    randomElement(arr) {
        return arr[Math.floor(Math.random() * arr.length)];
    }
};

// --- Core Application Logic ---
const app = {
    currentCheckInStatus: null,
    selectedMood: null,
    selectedUrge: null, // NEW
    breathingInterval: null, // NEW
    isBreathing: false, // NEW
    emergencyTimerInterval: null, // NEW

    init() {
        const data = Store.load();

        if (!data.quitDate) {
            this.router.showOnboarding();
        } else {
            document.getElementById('main-nav').classList.remove('hidden');
            document.getElementById('app').classList.remove('hidden');
            this.router.go('dashboard');
            this.startLiveTimer();
            this.initTheme(); // NEW
        }
    },

    completeOnboarding() {
        const qDateInput = document.getElementById('input-quit-date');
        const qDate = qDateInput.value;
        const spend = document.getElementById('input-spend').value;
        const reasons = document.getElementById('input-reasons').value;

        if (!qDate) return alert("Please select a date.");

        const selectedDate = new Date(qDate);
        const today = new Date();
        today.setHours(23, 59, 59, 999); // Allow today

        if (selectedDate > today) {
            return alert("The quit date cannot be in the future. Please select today or a past date.");
        }

        Store.data.quitDate = selectedDate.toISOString();
        Store.data.weeklySpend = Number(spend) || 0;
        Store.data.personalReasons = reasons.split('\n').filter(r => r.trim() !== '');

        Store.save();

        document.getElementById('view-onboarding').classList.add('hidden');
        document.getElementById('main-nav').classList.remove('hidden');
        document.getElementById('app').classList.remove('hidden');

        this.router.go('dashboard');
        this.startLiveTimer();
        this.initTheme();
    },

    // --- Theme Logic ---
    initTheme() {
        const savedTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', savedTheme);
        this.updateThemeIcon(savedTheme);
    },

    toggleTheme() {
        const current = document.documentElement.getAttribute('data-theme');
        const next = current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        localStorage.setItem('theme', next);
        this.updateThemeIcon(next);
        // Re-render chart to pick up new theme colors
        if (app.views.dashboard.render) app.views.dashboard.render();
    },

    updateThemeIcon(theme) {
        const btn = document.getElementById('theme-toggle');
        if (!btn) return;

        if (theme === 'dark') {
            btn.innerHTML = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/></svg>`;
        } else {
            btn.innerHTML = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>`;
        }
    },

    router: {
        go(viewName) {
            // Hide all views
            document.querySelectorAll('.view').forEach(el => el.classList.add('hidden'));

            // Show target
            const target = document.getElementById(`view-${viewName}`);
            if (target) {
                target.classList.remove('hidden');
                if (app.views[viewName] && app.views[viewName].render) {
                    app.views[viewName].render();
                }
            }

            // Update Nav
            document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
            const navItem = document.querySelector(`.nav-item[onclick*="'${viewName}'"]`);
            if (navItem) navItem.classList.add('active');

            window.scrollTo(0, 0);
        },

        showOnboarding() {
            document.getElementById('view-onboarding').classList.remove('hidden');
            document.getElementById('main-nav').classList.add('hidden');
            document.getElementById('app').classList.add('hidden');
        }
    },

    // --- Features ---

    getCurrentStreak() {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        let checkDate = new Date(today);
        let streak = 0;

        const qDate = new Date(Store.data.quitDate);
        qDate.setHours(0, 0, 0, 0);

        for (let i = 0; i < 3650; i++) {
            const dateStr = Utils.formatDate(checkDate);
            const status = Store.data.dailyCheckIns[dateStr];

            if (checkDate < qDate) break;

            if (status === 'slip') break;

            // Increment streak if 'sober' OR if there's no data for the day but it's >= qDate
            // This is more user-friendly for missing a check-in day
            if (status === 'sober') {
                streak++;
            } else if (status === 'slip') {
                break;
            } else {
                streak++;
            }

            checkDate.setDate(checkDate.getDate() - 1);
        }

        if (streak > Store.data.longestStreak) {
            Store.data.longestStreak = streak;
            Store.save();
        }

        return streak;
    },

    startLiveTimer() {
        const timerEl = document.getElementById('dash-timer');
        if (!timerEl) return;

        let lastResetDate = new Date(Store.data.quitDate);
        const sortedDates = Object.keys(Store.data.dailyCheckIns).sort();

        for (let d of sortedDates) {
            if (Store.data.dailyCheckIns[d] === 'slip') {
                const slipDate = new Date(d);
                slipDate.setHours(23, 59, 59, 999); // Reset happens after the slip day
                lastResetDate = new Date(slipDate.getTime() + 1);
            }
        }

        const update = () => {
            const now = new Date();
            const diff = now - lastResetDate;

            if (diff < 0) {
                timerEl.innerText = "00d 00h 00m";
                return;
            }

            const days = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
            const minutes = Math.floor((diff / (1000 * 60)) % 60);

            timerEl.innerText = `${days.toString().padStart(2, '0')}d ${hours.toString().padStart(2, '0')}h ${minutes.toString().padStart(2, '0')}m`;
        };

        update();
        if (this.timerInterval) clearInterval(this.timerInterval);
        this.timerInterval = setInterval(update, 30000); // Update every 30s
    },

    // --- Modals ---
    currentCheckInDate: null, // NEW: Track which date we're checking in for

    openCheckInModal(showDatePicker = false) {
        const today = Utils.getToday();
        this.currentCheckInDate = today;

        const dateSelector = document.getElementById('checkin-date-selector');
        const dateInput = document.getElementById('checkin-date-input');
        const dayLabel = document.getElementById('day-label');

        if (showDatePicker) {
            dateSelector.classList.remove('hidden');
            dateInput.value = today;

            const maxDate = new Date();
            const minDate = new Date();
            minDate.setDate(minDate.getDate() - 30); // Allow 30 days backfill

            dateInput.max = Utils.formatDate(maxDate);
            dateInput.min = Utils.formatDate(minDate);

            dateInput.onchange = () => {
                this.currentCheckInDate = dateInput.value;
                const selectedDate = new Date(dateInput.value);
                document.getElementById('checkin-date-display').innerText = selectedDate.toLocaleDateString();
                const isToday = dateInput.value === Utils.getToday();
                dayLabel.innerText = isToday ? 'today' : 'that day';
            };
        } else {
            dateSelector.classList.add('hidden');
            dayLabel.innerText = 'today';
        }

        document.getElementById('checkin-date-display').innerText = new Date().toLocaleDateString();
        document.getElementById('modal-checkin').classList.remove('hidden');
        document.getElementById('checkin-step-1').classList.remove('hidden');
        document.getElementById('checkin-step-2').classList.add('hidden');
        document.getElementById('checkin-slip-msg').classList.add('hidden');

        document.getElementById('checkin-note').value = '';
        document.querySelectorAll('.mood-opt').forEach(el => el.style.opacity = '0.4');
        document.querySelectorAll('.urge-btn').forEach(el => el.classList.remove('active'));
        this.selectedMood = null;
        this.selectedUrge = null;
    },

    processCheckIn(status) {
        this.currentCheckInStatus = status;

        if (status === 'slip') {
            document.getElementById('checkin-step-1').classList.add('hidden');
            document.getElementById('checkin-slip-msg').classList.remove('hidden');
        } else {
            this.showCheckInDetailsStep();
        }
    },

    showCheckInDetailsStep() {
        document.getElementById('checkin-step-1').classList.add('hidden');
        document.getElementById('checkin-slip-msg').classList.add('hidden');
        document.getElementById('checkin-step-2').classList.remove('hidden');
    },

    selectMood(el, mood) {
        this.selectedMood = mood;
        document.querySelectorAll('.mood-opt').forEach(opt => opt.style.opacity = '0.4');
        el.style.opacity = '1';
    },

    selectUrge(el, level) {
        this.selectedUrge = level;
        document.querySelectorAll('.urge-btn').forEach(btn => btn.classList.remove('active'));
        el.classList.add('active');
    },

    saveCheckInDetails() {
        const checkInDate = this.currentCheckInDate || Utils.getToday(); // Use selected date or today
        const note = document.getElementById('checkin-note').value;

        Store.data.dailyCheckIns[checkInDate] = this.currentCheckInStatus;
        if (note) Store.data.journalEntries[checkInDate] = note;
        if (this.selectedMood) Store.data.moodEntries[checkInDate] = this.selectedMood;
        if (this.selectedUrge) Store.data.urgeEntries[checkInDate] = this.selectedUrge; // NEW

        Store.save();
        this.closeModals();
        this.router.go('dashboard');

        if (this.currentCheckInStatus === 'sober') {
            this.checkMilestone();
        }
    },

    checkMilestone() {
        const streak = this.getCurrentStreak();
        const milestones = [7, 30, 90, 180, 365];
        if (milestones.includes(streak)) {
            Confetti.fire();
            setTimeout(() => {
                alert(`🎉 Congratulations! You've reached a ${streak}-day milestone!`);
            }, 500);
        }
    },

    closeModals() {
        document.querySelectorAll('.modal-overlay').forEach(el => el.classList.add('hidden'));
    },

    resetData() {
        Store.reset();
    },

    exportData() {
        const dataStr = JSON.stringify(Store.data, null, 2);
        const blob = new Blob([dataStr], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `sober-streak-backup-${Utils.getToday()}.json`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    },

    importData() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.json';

        input.onchange = e => {
            const file = e.target.files[0];
            if (!file) return;

            const reader = new FileReader();
            reader.onload = event => {
                try {
                    const imported = JSON.parse(event.target.result);
                    // Simple validation
                    if (!imported.quitDate || !imported.dailyCheckIns) {
                        throw new Error("Invalid data format");
                    }

                    if (confirm("This will overwrite your current data. Are you sure?")) {
                        Store.data = { ...Store.data, ...imported };
                        Store.save();
                        alert("Data imported successfully!");
                        location.reload();
                    }
                } catch (err) {
                    alert("Error importing data: " + err.message);
                }
            };
            reader.readAsText(file);
        };

        input.click();
    },

    resetQuitDate() {
        if (confirm("Change your quit date to today?")) {
            Store.data.quitDate = new Date().toISOString();
            Store.save();
            this.startLiveTimer();
            this.closeModals();
            this.router.go('dashboard');
        }
    },

    // --- NEW: Emergency Modal ---
    openEmergencyModal() {
        document.getElementById('modal-emergency').classList.remove('hidden');

        // Show user's reasons
        const reasonsDiv = document.getElementById('emergency-reasons');
        if (Store.data.personalReasons.length > 0) {
            reasonsDiv.innerHTML = '<ul>' +
                Store.data.personalReasons.map(r => `<li style="margin-bottom: 0.5rem;">• ${r}</li>`).join('') +
                '</ul>';
        } else {
            reasonsDiv.innerHTML = '<p style="font-style: italic;">Remember why you started this journey.</p>';
        }

        // Start 5-minute timer
        this.startEmergencyTimer(300); // 5 minutes
    },

    closeEmergencyModal() {
        document.getElementById('modal-emergency').classList.add('hidden');
        if (this.emergencyTimerInterval) {
            clearInterval(this.emergencyTimerInterval);
        }
    },

    startEmergencyTimer(duration) {
        let timer = duration;
        const timerEl = document.getElementById('emergency-timer');

        if (this.emergencyTimerInterval) {
            clearInterval(this.emergencyTimerInterval);
        }

        const updateDisplay = (t) => {
            let minutes = parseInt(t / 60, 10);
            let seconds = parseInt(t % 60, 10);
            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;
            timerEl.textContent = minutes + ":" + seconds;
        };

        updateDisplay(timer);

        this.emergencyTimerInterval = setInterval(() => {
            timer--;
            updateDisplay(timer);

            if (timer <= 0) {
                clearInterval(this.emergencyTimerInterval);
                timerEl.textContent = "Done!";
                timerEl.style.color = 'var(--color-success)';
            }
        }, 1000);
    },

    // --- NEW: Breathing Exercise ---
    toggleBreathing() {
        const btn = document.getElementById('breathing-btn');
        const circle = document.getElementById('breathing-circle');

        if (this.isBreathing) {
            this.stopBreathing();
        } else {
            this.startBreathing(btn, circle);
        }
    },

    startBreathing(btn, circle) {
        this.isBreathing = true;
        btn.textContent = 'Stop Exercise';
        btn.classList.replace('btn-primary', 'btn-outline');
        this.runBreathingCycle(circle);
    },

    stopBreathing() {
        const btn = document.getElementById('breathing-btn');
        const circle = document.getElementById('breathing-circle');

        this.isBreathing = false;
        if (this.breathingTimeout) clearTimeout(this.breathingTimeout);

        btn.textContent = 'Start Exercise';
        btn.classList.replace('btn-outline', 'btn-primary');
        circle.textContent = 'Ready';
        circle.className = 'breathing-circle';
    },

    runBreathingCycle(circle) {
        if (!this.isBreathing) return;

        // Inhale (4s)
        circle.textContent = 'Inhale...';
        circle.className = 'breathing-circle inhale';

        this.breathingTimeout = setTimeout(() => {
            if (!this.isBreathing) return;
            // Hold (7s)
            circle.textContent = 'Hold';
            circle.className = 'breathing-circle hold';

            this.breathingTimeout = setTimeout(() => {
                if (!this.isBreathing) return;
                // Exhale (8s)
                circle.textContent = 'Exhale...';
                circle.className = 'breathing-circle exhale';

                this.breathingTimeout = setTimeout(() => {
                    if (this.isBreathing) this.runBreathingCycle(circle);
                }, 8000);
            }, 7000);
        }, 4000);
    },

    // --- NEW: Coping Tools ---
    addDistraction() {
        const input = document.getElementById('new-distraction');
        const value = input.value.trim();

        if (value) {
            Store.data.distractions.push(value);
            Store.save();
            this.views.coping.render();
            input.value = '';
        }
    },

    removeDistraction(index) {
        if (confirm('Remove this distraction?')) {
            Store.data.distractions.splice(index, 1);
            Store.save();
            this.views.coping.render();
        }
    },

    addContact() {
        const nameInput = document.getElementById('contact-name');
        const infoInput = document.getElementById('contact-info');
        const name = nameInput.value.trim();
        const info = infoInput.value.trim();

        if (name && info) {
            Store.data.emergencyContacts.push({ name, info });
            Store.save();
            this.views.coping.render();
            nameInput.value = '';
            infoInput.value = '';
        }
    },

    removeContact(index) {
        if (confirm('Remove this contact?')) {
            Store.data.emergencyContacts.splice(index, 1);
            Store.save();
            this.views.coping.render();
        }
    },

    // --- Views ---
    views: {
        dashboard: {
            render() {
                const days = app.getCurrentStreak();
                const weeks = days / 7;
                const saved = Math.round(weeks * Store.data.weeklySpend);

                document.getElementById('dash-money').innerText = `$${saved}`;
                document.getElementById('dash-longest').innerText = Store.data.longestStreak;

                const reasons = Store.data.personalReasons;
                const quotes = [
                    "It always seems impossible until it's done.",
                    "Don't watch the clock; do what it does. Keep going.",
                    "The only way out is through.",
                    "Recovery is hard. Regret is harder.",
                    "Success is the sum of small efforts, repeated day in and day out.",
                    "The best time to plant a tree was 20 years ago. The second best time is now.",
                    "You are stronger than you think.",
                    "Believe you can and you're halfway there.",
                    "Every day is a fresh start.",
                    "Courage is resistance to fear, mastery of fear, not absence of fear.",
                    "Fall seven times, stand up eight.",
                    "Your past does not determine your future.",
                    "The journey of a thousand miles begins with one step.",
                    "Healing takes time, and asking for help is a courageous step.",
                    "You don't have to control your thoughts. You just have to stop letting them control you."
                ];

                let msg = "";
                if (reasons.length > 0 && Math.random() > 0.5) {
                    msg = "Remember: " + Utils.randomElement(reasons);
                } else {
                    msg = Utils.randomElement(quotes);
                }

                document.getElementById('dash-quote').innerText = msg;

                const qDate = new Date(Store.data.quitDate);
                document.getElementById('dash-start-date').innerText = `Started ${qDate.toLocaleDateString()}`;

                // Update Button Status
                const today = Utils.getToday();
                const checkInBtn = document.getElementById('btn-checkin-today');
                const logPastBtn = document.getElementById('btn-log-past');
                const hasCheckedInToday = Store.data.dailyCheckIns[today];

                if (hasCheckedInToday) {
                    checkInBtn.innerText = 'Checked In for Today ✓';
                    checkInBtn.classList.remove('btn-primary');
                    checkInBtn.classList.add('btn-success-solid');
                    checkInBtn.disabled = true;
                    checkInBtn.style.opacity = '0.8';
                    
                    if (logPastBtn) logPastBtn.style.opacity = '0.7';
                } else {
                    checkInBtn.innerText = 'Check In Today';
                    checkInBtn.classList.add('btn-primary');
                    checkInBtn.classList.remove('btn-success-solid');
                    checkInBtn.disabled = false;
                    checkInBtn.style.opacity = '1';
                    
                    if (logPastBtn) logPastBtn.style.opacity = '1';
                }

                // Render Chart
                setTimeout(() => app.renderAnalyticsChart(), 100);
            }
        },
        calendar: {
            currentDisplayDate: new Date(),
            nav(dir) {
                this.currentDisplayDate.setMonth(this.currentDisplayDate.getMonth() + dir);
                this.render();
            },
            render() {
                const year = this.currentDisplayDate.getFullYear();
                const month = this.currentDisplayDate.getMonth();

                const monthNames = ["January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"];

                document.getElementById('cal-month-year').innerText = `${monthNames[month]} ${year}`;

                const firstDay = new Date(year, month, 1);
                const lastDay = new Date(year, month + 1, 0);
                const daysInMonth = lastDay.getDate();
                const startDayIndex = firstDay.getDay();

                const grid = document.getElementById('cal-grid');
                grid.innerHTML = '';

                for (let i = 0; i < startDayIndex; i++) {
                    const e = document.createElement('div');
                    e.className = 'calendar-day day-empty';
                    grid.appendChild(e);
                }

                for (let d = 1; d <= daysInMonth; d++) {
                    const dateObj = new Date(year, month, d);
                    const dateStr = Utils.formatDate(dateObj);
                    const status = Store.data.dailyCheckIns[dateStr];

                    const cell = document.createElement('div');
                    cell.className = 'calendar-day';
                    cell.innerText = d;

                    if (dateObj > new Date()) {
                        cell.classList.add('day-future');
                    } else if (status === 'sober') {
                        cell.classList.add('day-sober');
                    } else if (status === 'slip') {
                        cell.classList.add('day-slip');
                    }

                    if (Store.data.moodEntries[dateStr]) {
                        const mood = document.createElement('span');
                        mood.innerText = Store.data.moodEntries[dateStr];
                        mood.style.position = 'absolute';
                        mood.style.bottom = '2px';
                        mood.style.right = '2px';
                        mood.style.fontSize = '0.7rem';
                        cell.appendChild(mood);
                    }

                    cell.onclick = () => {
                        const note = Store.data.journalEntries[dateStr];
                        const urge = Store.data.urgeEntries[dateStr];
                        if (note || status || urge) {
                            alert(`Date: ${dateStr}\nStatus: ${status || 'No Check-in'}\nMood: ${Store.data.moodEntries[dateStr] || ''}\nUrges: ${urge || ''}\nNote: ${note || ''}`);
                        }
                    };

                    grid.appendChild(cell);
                }
            }
        },
        timeline: {
            render() {
                const streak = app.getCurrentStreak();
                const benefits = [
                    { d: 1, t: "Better Hydration", desc: "Your body begins to rehydrate." },
                    { d: 3, t: "Reduced Anxiety", desc: "Hangover anxiety subsides, sleep may stabilize." },
                    { d: 7, t: "Better Sleep", desc: "Sleep patterns improve, increasing energy." },
                    { d: 14, t: "Clearer Skin", desc: "Skin looks brighter, bloating reduces." },
                    { d: 30, t: "Weight Loss", desc: "Liver fat decreases, mood stabilizes." },
                    { d: 90, t: "Liver Recovery", desc: "Significant liver healing and cognitive improvement." },
                    { d: 180, t: "Lower Cancer Risk", desc: "Risk of alcohol-related cancers drops." },
                    { d: 365, t: "Renewed Health", desc: "Major health risks significantly reduced." }
                ];

                const container = document.getElementById('health-list');
                container.innerHTML = '';

                benefits.forEach(b => {
                    const achieved = streak >= b.d;
                    const el = document.createElement('div');
                    el.className = `timeline-item ${achieved ? 'achieved' : ''}`;
                    el.innerHTML = `
                        <div class="timeline-dot"></div>
                        <h3 style="color: ${achieved ? 'var(--color-success)' : 'inherit'}">${b.t} (${b.d} days)</h3>
                        <p>${b.desc}</p>
                    `;
                    container.appendChild(el);
                });
            }
        },
        // NEW: Coping Tools View
        coping: {
            render() {
                // Render distractions
                const distList = document.getElementById('distraction-list');
                distList.innerHTML = '';
                Store.data.distractions.forEach((dist, idx) => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <span>${dist}</span>
                        <button class="btn btn-outline" style="width:auto; padding: 0.25rem 0.5rem;" onclick="app.removeDistraction(${idx})">Remove</button>
                    `;
                    distList.appendChild(li);
                });

                // Render contacts
                const contactList = document.getElementById('contact-list');
                contactList.innerHTML = '';
                if (Store.data.emergencyContacts.length === 0) {
                    contactList.innerHTML = '<li style="color: var(--color-text-muted); font-style: italic;">No contacts added yet</li>';
                } else {
                    Store.data.emergencyContacts.forEach((contact, idx) => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <div>
                                <strong>${contact.name}</strong><br>
                                <a href="tel:${contact.info}" style="color: var(--color-primary);">${contact.info}</a>
                            </div>
                            <button class="btn btn-outline" style="width:auto; padding: 0.25rem 0.5rem;" onclick="app.removeContact(${idx})">Remove</button>
                        `;
                        contactList.appendChild(li);
                    });
                }
            }
        },
        milestones: {
            render() {
                const streak = app.getCurrentStreak();
                const milestones = [7, 30, 60, 90, 180, 365, 730];
                const container = document.getElementById('milestone-list');
                container.innerHTML = '';

                milestones.forEach(m => {
                    const achieved = streak >= m;
                    const card = document.createElement('div');
                    card.className = 'card flex items-center justify-between';
                    if (achieved) card.style.borderColor = 'var(--color-success)';

                    card.innerHTML = `
                        <div>
                            <h3 style="color: ${achieved ? 'var(--color-success)' : 'inherit'}">${m} Days</h3>
                            <p>${achieved ? 'Unlocked!' : 'Upcoming'}</p>
                        </div>
                        <div style="font-size: 2rem;">
                            ${achieved ? '🏆' : '🔒'}
                        </div>
                    `;
                    container.appendChild(card);
                });
            }
        },
        journal: {
            render() {
                const entries = Store.data.journalEntries;
                const container = document.getElementById('journal-list');
                container.innerHTML = '';

                const dates = Object.keys(entries).sort().reverse();

                if (dates.length === 0) {
                    document.getElementById('journal-empty').classList.remove('hidden');
                } else {
                    document.getElementById('journal-empty').classList.add('hidden');
                    dates.forEach(d => {
                        const note = entries[d];
                        const mood = Store.data.moodEntries[d] || '';
                        const urge = Store.data.urgeEntries[d] || '';
                        const card = document.createElement('div');
                        card.className = 'card';
                        card.innerHTML = `
                            <div class="flex justify-between mb-2">
                                <strong>${d}</strong>
                                <span>${mood}</span>
                            </div>
                            ${urge ? `<div style="font-size: 0.8rem; color: var(--color-text-muted); margin-bottom: 0.5rem;">Urges: ${urge}</div>` : ''}
                            <p style="color: var(--color-text-main);">${note}</p>
                        `;
                        container.appendChild(card);
                    });
                }
            }
        }
    }
};

// --- Confetti (Simple Canvas impl) ---
const Confetti = {
    fire() {
        const canvas = document.getElementById('canvas-confetti');
        if (!canvas) return;
        canvas.classList.remove('hidden');
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
        const ctx = canvas.getContext('2d');

        let particles = [];
        for (let i = 0; i < 100; i++) {
            particles.push({
                x: canvas.width / 2, y: canvas.height / 2,
                dx: (Math.random() - 0.5) * 10, dy: (Math.random() - 0.5) * 10,
                c: `hsl(${Math.random() * 360}, 100%, 50%)`,
                s: Math.random() * 5 + 2
            });
        }

        function animate() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            particles.forEach(p => {
                p.x += p.dx;
                p.y += p.dy;
                p.dy += 0.1; // gravity
                ctx.fillStyle = p.c;
                ctx.fillRect(p.x, p.y, p.s, p.s);
            });
            particles = particles.filter(p => p.y < canvas.height);

            if (particles.length > 0) requestAnimationFrame(animate);
            else canvas.classList.add('hidden');
        }
        animate();
    }
};

// --- Analytics Chart ---
app.renderAnalyticsChart = function () {
    const canvas = document.getElementById('analytics-chart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    const rect = canvas.getBoundingClientRect();
    const dpr = window.devicePixelRatio || 1;
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    ctx.scale(dpr, dpr);

    const w = rect.width;
    const h = rect.height;
    ctx.clearRect(0, 0, w, h);

    const days = 14;
    const padding = { t: 20, b: 30, l: 30, r: 20 };
    const cw = w - padding.l - padding.r;
    const ch = h - padding.t - padding.b;

    const today = new Date();
    const moodData = [];
    const urgeData = [];
    const labels = [];

    for (let i = days - 1; i >= 0; i--) {
        const d = new Date();
        d.setDate(today.getDate() - i);
        const ds = Utils.formatDate(d);
        labels.push(d.getDate() + '/' + (d.getMonth() + 1));

        const m = Store.data.moodEntries[ds];
        const u = Store.data.urgeEntries[ds];

        moodData.push(m === '😊' ? 4 : m === '🙂' ? 3 : m === '😐' ? 2 : m === '😔' ? 1 : null);
        urgeData.push(u === 'High' ? 4 : u === 'Strong' ? 3 : u === 'Mild' ? 2 : u === 'None' ? 1 : null);
    }

    if (moodData.every(x => x === null) && urgeData.every(x => x === null)) {
        ctx.fillStyle = '#94a3b8';
        ctx.textAlign = 'center';
        ctx.fillText("Log your mood to see trends", w / 2, h / 2);
        return;
    }

    const getX = (i) => padding.l + (i / (days - 1)) * cw;
    const getY = (v) => padding.t + ch - ((v - 1) / 3) * ch;

    const bodyStyles = getComputedStyle(document.body);
    const borderColor = bodyStyles.getPropertyValue('--color-border').trim() || '#e2e8f0';
    const textColor = bodyStyles.getPropertyValue('--color-text-muted').trim() || '#64748b';
    const primaryColor = bodyStyles.getPropertyValue('--color-primary').trim() || '#0ea5e9';
    const dangerColor = bodyStyles.getPropertyValue('--color-danger').trim() || '#ef4444';

    // Grid
    ctx.strokeStyle = borderColor;
    ctx.lineWidth = 1;
    ctx.beginPath();
    for (let i = 0; i < 4; i++) {
        const y = padding.t + (i / 3) * ch;
        ctx.moveTo(padding.l, y);
        ctx.lineTo(w - padding.r, y);
    }
    ctx.stroke();

    // Labels X
    ctx.fillStyle = textColor;
    ctx.font = '10px sans-serif';
    ctx.textAlign = 'center';
    for (let i = 0; i < days; i += 2) {
        ctx.fillText(labels[i], getX(i), h - 10);
    }

    const drawLine = (data, color, dash) => {
        ctx.beginPath();
        ctx.strokeStyle = color;
        ctx.lineWidth = 2.5;
        if (dash) ctx.setLineDash([5, 5]);
        else ctx.setLineDash([]);

        let first = true;
        data.forEach((v, i) => {
            if (v !== null) {
                if (first) {
                    ctx.moveTo(getX(i), getY(v));
                    first = false;
                } else {
                    ctx.lineTo(getX(i), getY(v));
                }
            }
        });
        ctx.stroke();

        // Dots
        ctx.setLineDash([]);
        data.forEach((v, i) => {
            if (v !== null) {
                ctx.beginPath();
                ctx.arc(getX(i), getY(v), 3.5, 0, Math.PI * 2);
                ctx.fillStyle = bodyStyles.getPropertyValue('--color-surface').trim() || '#fff';
                ctx.fill();
                ctx.stroke();
            }
        });
    };

    drawLine(moodData, primaryColor, false);
    drawLine(urgeData, dangerColor, true);

    // Legend
    ctx.textAlign = 'left';
    ctx.font = '12px sans-serif';
    ctx.fillStyle = primaryColor;
    ctx.fillText('● Mood', padding.l, 10);
    ctx.fillStyle = dangerColor;
    ctx.fillText('--- Urge', padding.l + 60, 10);
};

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    // Fix for IOS height
    const setVh = () => {
        let vh = window.innerHeight * 0.01;
        document.documentElement.style.setProperty('--vh', `${vh}px`);
    };
    window.addEventListener('resize', setVh);
    setVh();

    app.init();
});

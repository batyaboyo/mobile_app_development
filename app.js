// --- Data Store ---
const DB_KEY = 'soberStreakData';

const Store = {
    data: {
        quitDate: null,
        weeklySpend: 50,
        personalReasons: [],
        dailyCheckIns: {}, // 'YYYY-MM-DD': 'sober' | 'slip' | null
        journalEntries: {},
        moodEntries: {},
        longestStreak: 0
    },

    load() {
        const saved = localStorage.getItem(DB_KEY);
        if (saved) {
            this.data = JSON.parse(saved);
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
        // Returns YYYY-MM-DD local
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

    init() {
        const data = Store.load();

        if (!data.quitDate) {
            this.router.showOnboarding();
        } else {
            this.router.go('dashboard');
            this.startLiveTimer();
        }
    },

    completeOnboarding() {
        const qDate = document.getElementById('input-quit-date').value;
        const spend = document.getElementById('input-spend').value;
        const reasons = document.getElementById('input-reasons').value;

        if (!qDate) return alert("Please select a date.");

        Store.data.quitDate = new Date(qDate).toISOString();
        Store.data.weeklySpend = Number(spend) || 0;
        Store.data.personalReasons = reasons.split('\n').filter(r => r.trim() !== '');

        Store.save();

        document.getElementById('view-onboarding').classList.add('hidden');
        document.getElementById('main-nav').classList.remove('hidden');
        document.getElementById('app').classList.remove('hidden');

        this.router.go('dashboard');
        this.startLiveTimer();
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
            // Note: Simple heuristic to highlight correct nav item
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
        const today = Utils.getToday();
        let checkDate = new Date(); // Start today
        let streak = 0;

        // Loop backwards
        // If today is tracked as 'slip', streak is 0. 
        // If today is not tracked, we check yesterday.
        // If consecutive 'sober', add to streak.
        // Stop at 'slip' or quitDate.

        const qDate = new Date(Store.data.quitDate);
        // Reset time for comparison
        qDate.setHours(0, 0, 0, 0);

        // Safety cap of 3650 days to prevent infinite loops in bad states
        for (let i = 0; i < 3650; i++) {
            const dateStr = Utils.formatDate(checkDate);
            const status = Store.data.dailyCheckIns[dateStr];

            if (checkDate < qDate) break; // Before quit date

            if (i === 0) { // Today
                if (status === 'slip') return 0;
                if (status === 'sober') streak++;
                // If null, we just continue to yesterday, streak is currently 0 from today's perspective but might be X from yesterday
            } else {
                if (status === 'slip') break;
                if (status === 'sober') streak++;
                else break; // missed check-in breaks streak logic usually, or we can be forgiving. Let's be strict: missed checkin breaks streak unless handled? 
                // User request: "current streak = consecutive sober days ending today (or yesterday if not checked in)"
                // So if today is null, we can count from yesterday.
                // If yesterday is also null, streak is 0? Or do we assume sober? 
                // Let's assume missed checkin = break for accurate tracking, but maybe user wants leniency.
                // Impl: If today is null, we don't increment streak for today, but check yesterday. 
                // If yesterday is sober, we continue.
                // If yesterday is null, streak ends.
            }

            checkDate.setDate(checkDate.getDate() - 1);
        }

        // Update longest
        if (streak > Store.data.longestStreak) {
            Store.data.longestStreak = streak;
            Store.save();
        }

        return streak;
    },

    startLiveTimer() {
        const timerEl = document.getElementById('dash-timer');

        // We calculate time from the last slip OR quit date.
        // Find most recent slip
        let lastResetDate = new Date(Store.data.quitDate);
        const sortedDates = Object.keys(Store.data.dailyCheckIns).sort();
        for (let d of sortedDates) {
            if (Store.data.dailyCheckIns[d] === 'slip') {
                // Reset date is the day AFTER the slip, or the slip day itself for tracking?
                // Usually clean time starts after the slip.
                const slipDate = new Date(d);
                lastResetDate = new Date(slipDate);
                lastResetDate.setDate(slipDate.getDate() + 1);
            }
        }

        // If last reset in future (e.g. today slipped), timer is 0

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

            timerEl.innerText = `${days}d ${hours}h ${minutes}m`;
        };

        update();
        setInterval(update, 60000);
    },

    // --- Modals ---
    openCheckInModal() {
        const today = Utils.getToday();
        if (Store.data.dailyCheckIns[today]) {
            // Already checked in, show edit/view?
            // For simplicity, allow overwrite
        }

        document.getElementById('checkin-date-display').innerText = new Date().toLocaleDateString();
        document.getElementById('modal-checkin').classList.remove('hidden');
        document.getElementById('checkin-step-1').classList.remove('hidden');
        document.getElementById('checkin-step-2').classList.add('hidden');
        document.getElementById('checkin-slip-msg').classList.add('hidden');

        // Reset form
        document.getElementById('checkin-note').value = '';
        document.querySelectorAll('.mood-opt').forEach(el => el.style.opacity = '0.5');
        this.selectedMood = null;
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

    selectMood(mood) {
        this.selectedMood = mood;
        document.querySelectorAll('.mood-opt').forEach(el => el.style.opacity = '0.5');
        event.target.style.opacity = '1';
    },

    saveCheckInDetails() {
        const today = Utils.getToday();
        const note = document.getElementById('checkin-note').value;

        Store.data.dailyCheckIns[today] = this.currentCheckInStatus;
        if (note) Store.data.journalEntries[today] = note;
        if (this.selectedMood) Store.data.moodEntries[today] = this.selectedMood;

        Store.save();
        this.closeModals();
        this.router.go('dashboard'); // Refresh dash

        if (this.currentCheckInStatus === 'sober') {
            // Check for milestone?
            this.checkMilestone();
        }
    },

    checkMilestone() {
        const streak = this.getCurrentStreak();
        const milestones = [7, 30, 90, 180, 365];
        if (milestones.includes(streak)) {
            Confetti.fire();
            alert(`Congratulations! You've reached a ${streak}-day milestone!`);
        }
    },

    closeModals() {
        document.querySelectorAll('.modal-overlay').forEach(el => el.classList.add('hidden'));
    },

    resetData() {
        if (confirm("Are you sure you want to delete all data? This cannot be undone.")) {
            Store.reset();
        }
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

                // Mix reasons and quotes
                let msg = "";
                if (reasons.length > 0 && Math.random() > 0.5) {
                    msg = "Remember: " + Utils.randomElement(reasons);
                } else {
                    msg = Utils.randomElement(quotes);
                }

                document.getElementById('dash-quote').innerText = msg;

                const qDate = new Date(Store.data.quitDate);
                document.getElementById('dash-start-date').innerText = `Started ${qDate.toLocaleDateString()}`;
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
                const startDayIndex = firstDay.getDay(); // 0 = Sun

                const grid = document.getElementById('cal-grid');
                grid.innerHTML = '';

                // Empty cells
                for (let i = 0; i < startDayIndex; i++) {
                    const e = document.createElement('div');
                    e.className = 'calendar-day day-empty';
                    grid.appendChild(e);
                }

                const todayYear = new Date().getFullYear();
                const todayMonth = new Date().getMonth();
                const todayDate = new Date().getDate();

                for (let d = 1; d <= daysInMonth; d++) {
                    const dateObj = new Date(year, month, d);
                    const dateStr = Utils.formatDate(dateObj);
                    const status = Store.data.dailyCheckIns[dateStr];

                    const cell = document.createElement('div');
                    cell.className = 'calendar-day';
                    cell.innerText = d;

                    // Check future
                    if (dateObj > new Date()) {
                        cell.classList.add('day-future');
                    } else if (status === 'sober') {
                        cell.classList.add('day-sober');
                    } else if (status === 'slip') {
                        cell.classList.add('day-slip');
                    }

                    // Indicator for journal/mood
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
                        // Could show mini modal with day details
                        const note = Store.data.journalEntries[dateStr];
                        if (note || status) {
                            alert(`Date: ${dateStr}\nStatus: ${status || 'No Check-in'}\nMood: ${Store.data.moodEntries[dateStr] || ''}\nNote: ${note || ''}`);
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
                    { d: 365, t: "Rennewed Health", desc: "Major health risks significantly reduced." }
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
                        const card = document.createElement('div');
                        card.className = 'card';
                        card.innerHTML = `
                            <div class="flex justify-between mb-2">
                                <strong>${d}</strong>
                                <span>${mood}</span>
                            </div>
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

        let frame = 0;
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

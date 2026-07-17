package com.uganda.learningapp.data

import com.uganda.learningapp.data.dao.RoadmapDao
import com.uganda.learningapp.data.entity.ModuleEntity
import com.uganda.learningapp.data.entity.TaskEntity
import com.uganda.learningapp.data.entity.WeekUnitEntity
import com.uganda.learningapp.data.entity.ProjectEntity
import com.uganda.learningapp.data.entity.QuizEntity
import com.uganda.learningapp.data.entity.ResourceEntity
import com.uganda.learningapp.data.entity.BadgeEntity
import com.uganda.learningapp.data.entity.UserSettingsEntity

/**
 * Populates the database with the complete 12-month roadmap for Cybersecurity, Blockchain, and Trading.
 * Based on the Uganda Edition roadmap, updated for 2025.
 */
object DataPopulator {
    suspend fun populate(dao: RoadmapDao) {
        // Initialize user settings
        dao.insertUserSettings(UserSettingsEntity())

        // Populate badges
        populateBadges(dao)

        // Populate resources
        populateResources(dao)

        // Populate modules (phases)
        populateModules(dao)

        // Populate sample projects
        populateProjects(dao)
    }

    private suspend fun populateBadges(dao: RoadmapDao) {
        val badges = listOf(
            BadgeEntity("first_login", "Welcome!", "You started your learning journey", "star"),
            BadgeEntity("week_1_complete", "Week 1 Champion", "Completed your first week", "emoji_events"),
            BadgeEntity("phase_1_complete", "Foundation Builder", "Completed Phase 1: Foundations", "school"),
            BadgeEntity("phase_2_complete", "Core Skills Master", "Completed Phase 2: Core Skills", "security"),
            BadgeEntity("phase_3_complete", "Project Pro", "Completed Phase 3: Intermediate Projects", "engineering"),
            BadgeEntity("phase_4_complete", "Expert Ready", "Completed Phase 4: Specialization", "workspace_premium"),
            BadgeEntity("first_project", "Project Starter", "Added your first project to portfolio", "folder"),
            BadgeEntity("five_projects", "Portfolio Builder", "Added 5 projects to portfolio", "collections"),
            BadgeEntity("quiz_master", "Quiz Master", "Scored 100% on 5 quizzes", "quiz"),
            BadgeEntity("all_tasks_week", "Perfectionist", "Completed all tasks in a week", "task_alt"),
            BadgeEntity("roadmap_complete", "Roadmap Champion", "Completed the entire 12-month roadmap", "military_tech")
        )
        dao.insertBadges(badges)
    }

    private suspend fun populateResources(dao: RoadmapDao) {
        val resources = listOf(
            // Phase 1 - Foundations
            ResourceEntity(title = "Ubuntu Linux Tutorials", url = "https://ubuntu.com/tutorials", type = "Documentation", topic = "Linux", difficulty = "Beginner", phaseId = 1, description = "Official Ubuntu tutorials for Linux beginners"),
            ResourceEntity(title = "LinuxCommand.org", url = "https://linuxcommand.org/", type = "Documentation", topic = "Linux", difficulty = "Beginner", phaseId = 1, description = "Learn the Linux command line"),
            ResourceEntity(title = "Cisco Networking Basics", url = "https://www.netacad.com/courses/networking", type = "Course", topic = "Networking", difficulty = "Beginner", phaseId = 1, description = "Free networking fundamentals from Cisco NetAcad"),
            ResourceEntity(title = "Cybrary Free Courses", url = "https://www.cybrary.it", type = "Course", topic = "Cybersecurity", difficulty = "Beginner", phaseId = 1, description = "Free cybersecurity courses and certifications"),
            ResourceEntity(title = "Blue Team Labs Online", url = "https://blueteamlabs.online/", type = "Lab", topic = "Cybersecurity", difficulty = "Beginner", phaseId = 1, description = "Free defensive security labs"),
            ResourceEntity(title = "Automate the Boring Stuff", url = "https://automatetheboringstuff.com/", type = "Course", topic = "Python", difficulty = "Beginner", phaseId = 1, description = "Free Python programming book and course"),
            ResourceEntity(title = "freeCodeCamp Python", url = "https://www.freecodecamp.org/", type = "Course", topic = "Python", difficulty = "Beginner", phaseId = 1, description = "Free interactive Python tutorials"),
            ResourceEntity(title = "Binance Academy", url = "https://academy.binance.com/en", type = "Course", topic = "Blockchain", difficulty = "Beginner", phaseId = 1, description = "Learn blockchain and crypto fundamentals"),
            ResourceEntity(title = "CryptoZombies", url = "https://cryptozombies.io/", type = "Course", topic = "Blockchain", difficulty = "Beginner", phaseId = 1, description = "Interactive Solidity game tutorial"),
            ResourceEntity(title = "Git Handbook", url = "https://guides.github.com/introduction/git-handbook/", type = "Documentation", topic = "Git", difficulty = "Beginner", phaseId = 1, description = "Official GitHub Git introduction"),

            // Phase 2 - Core Skills
            ResourceEntity(title = "Kali Linux Documentation", url = "https://www.kali.org/docs/", type = "Documentation", topic = "Cybersecurity", difficulty = "Intermediate", phaseId = 2, description = "Official Kali Linux documentation"),
            ResourceEntity(title = "TryHackMe", url = "https://tryhackme.com/", type = "Lab", topic = "Cybersecurity", difficulty = "Intermediate", phaseId = 2, description = "Hands-on cybersecurity training platform"),
            ResourceEntity(title = "Remix IDE", url = "https://remix.ethereum.org/", type = "Lab", topic = "Blockchain", difficulty = "Intermediate", phaseId = 2, description = "Online Solidity IDE for smart contracts"),
            ResourceEntity(title = "Consensys Smart Contract Best Practices", url = "https://consensys.github.io/smart-contract-best-practices/", type = "Documentation", topic = "Blockchain", difficulty = "Intermediate", phaseId = 2, description = "Security best practices for Solidity"),
            ResourceEntity(title = "GitHub Guides", url = "https://guides.github.com/", type = "Documentation", topic = "Git", difficulty = "Intermediate", phaseId = 2, description = "Advanced GitHub workflows"),

            // Phase 3 - Intermediate Projects
            ResourceEntity(title = "VulnHub Labs", url = "https://www.vulnhub.com/", type = "Lab", topic = "Cybersecurity", difficulty = "Intermediate", phaseId = 3, description = "Vulnerable VMs for penetration testing practice"),
            ResourceEntity(title = "Ethernaut", url = "https://ethernaut.openzeppelin.com/", type = "Lab", topic = "Blockchain", difficulty = "Intermediate", phaseId = 3, description = "Smart contract security challenges"),
            ResourceEntity(title = "Binance Testnet API", url = "https://binance-docs.github.io/apidocs/", type = "Documentation", topic = "Trading", difficulty = "Intermediate", phaseId = 3, description = "Practice trading with testnet funds"),

            // Phase 4 - Specialization
            ResourceEntity(title = "TryHackMe Intermediate Paths", url = "https://tryhackme.com/paths", type = "Lab", topic = "Cybersecurity", difficulty = "Advanced", phaseId = 4, description = "Advanced security learning paths"),
            ResourceEntity(title = "TradingView", url = "https://www.tradingview.com/", type = "Lab", topic = "Trading", difficulty = "Intermediate", phaseId = 4, description = "Free charting and market analysis"),
            ResourceEntity(title = "Upwork", url = "https://www.upwork.com/", type = "Course", topic = "Career", difficulty = "Beginner", phaseId = 4, description = "Find freelance cybersecurity and blockchain jobs"),
            ResourceEntity(title = "LinkedIn Learning", url = "https://www.linkedin.com/learning/", type = "Course", topic = "Career", difficulty = "Intermediate", phaseId = 4, description = "Professional development courses")
        )
        dao.insertResources(resources)
    }

    private suspend fun populateModules(dao: RoadmapDao) {
        // ==================== PHASE 1: Foundations (Weeks 1-12) ====================
        val phase1 = ModuleEntity(1, "Phase 1: Foundations", "Build core technical skills in Linux, networking, Python, and blockchain basics.", "Weeks 1-12")
        dao.insertModule(phase1)

        // Weeks 1-2: Linux Basics
        dao.insertWeek(WeekUnitEntity(1, 1, "Linux Basics", "Weeks 1-2", "Install Ubuntu/Kali VM; navigate file system, practice commands"))
        dao.insertTask(TaskEntity(weekId = 1, description = "Install Ubuntu or Kali Linux in a virtual machine (VirtualBox/VMware)"))
        dao.insertTask(TaskEntity(weekId = 1, description = "Learn basic commands: ls, cd, pwd, mkdir, rm, cp, mv"))
        dao.insertTask(TaskEntity(weekId = 1, description = "Practice file permissions: chmod, chown, chgrp"))
        dao.insertTask(TaskEntity(weekId = 1, description = "Explore the Linux file system structure (/etc, /var, /home)"))
        dao.insertTask(TaskEntity(weekId = 1, description = "Complete Ubuntu Linux Tutorials (ubuntu.com)"))
        dao.insertQuiz(QuizEntity(weekId = 1, question = "What command lists files in a directory?", optionA = "ls", optionB = "dir", optionC = "list", optionD = "show", correctAnswerIndex = 0))
        dao.insertQuiz(QuizEntity(weekId = 1, question = "What does 'chmod 755' do?", optionA = "Deletes file", optionB = "Sets rwx for owner, rx for group/others", optionC = "Hides file", optionD = "Encrypts file", correctAnswerIndex = 1))

        // Weeks 3-4: Networking Fundamentals
        dao.insertWeek(WeekUnitEntity(2, 1, "Networking Fundamentals", "Weeks 3-4", "TCP/IP, DNS, ports; set up home network lab"))
        dao.insertTask(TaskEntity(weekId = 2, description = "Learn TCP/IP fundamentals and the OSI model"))
        dao.insertTask(TaskEntity(weekId = 2, description = "Understand DNS, DHCP, and how networks communicate"))
        dao.insertTask(TaskEntity(weekId = 2, description = "Practice subnetting and IP addressing"))
        dao.insertTask(TaskEntity(weekId = 2, description = "Set up a home network lab with multiple VMs"))
        dao.insertTask(TaskEntity(weekId = 2, description = "Complete Cisco Networking Basics (NetAcad)"))
        dao.insertQuiz(QuizEntity(weekId = 2, question = "What is an IP address?", optionA = "Internet Protocol address", optionB = "Internal Post", optionC = "Indian Potato", optionD = "None", correctAnswerIndex = 0))
        dao.insertQuiz(QuizEntity(weekId = 2, question = "Which port is SSH?", optionA = "80", optionB = "22", optionC = "443", optionD = "21", correctAnswerIndex = 1))

        // Weeks 5-6: Cybersecurity Intro
        dao.insertWeek(WeekUnitEntity(3, 1, "Cybersecurity Introduction", "Weeks 5-6", "Security concepts, encryption, firewalls"))
        dao.insertTask(TaskEntity(weekId = 3, description = "Learn CIA triad: Confidentiality, Integrity, Availability"))
        dao.insertTask(TaskEntity(weekId = 3, description = "Understand encryption basics: symmetric vs asymmetric"))
        dao.insertTask(TaskEntity(weekId = 3, description = "Study firewall concepts and types"))
        dao.insertTask(TaskEntity(weekId = 3, description = "Complete introductory Cybrary courses"))
        dao.insertTask(TaskEntity(weekId = 3, description = "Try Blue Team Labs Online free challenges"))
        dao.insertQuiz(QuizEntity(weekId = 3, question = "What does CIA stand for in security?", optionA = "Central Intelligence Agency", optionB = "Confidentiality, Integrity, Availability", optionC = "Computer Information Access", optionD = "Cyber Internet Authority", correctAnswerIndex = 1))

        // Weeks 7-8: Python Basics
        dao.insertWeek(WeekUnitEntity(4, 1, "Python Basics", "Weeks 7-8", "Variables, loops, functions, file I/O"))
        dao.insertTask(TaskEntity(weekId = 4, description = "Install Python and set up development environment"))
        dao.insertTask(TaskEntity(weekId = 4, description = "Learn variables, data types, and operators"))
        dao.insertTask(TaskEntity(weekId = 4, description = "Master loops (for, while) and conditionals"))
        dao.insertTask(TaskEntity(weekId = 4, description = "Write functions and handle file I/O"))
        dao.insertTask(TaskEntity(weekId = 4, description = "Complete 'Automate the Boring Stuff' chapters 1-6"))
        dao.insertQuiz(QuizEntity(weekId = 4, question = "Which keyword defines a function in Python?", optionA = "function", optionB = "func", optionC = "def", optionD = "define", correctAnswerIndex = 2))
        dao.insertQuiz(QuizEntity(weekId = 4, question = "What is print('Hello') in Python?", optionA = "Variable", optionB = "Loop", optionC = "Function call", optionD = "Class", correctAnswerIndex = 2))

        // Weeks 9-10: Blockchain Fundamentals
        dao.insertWeek(WeekUnitEntity(5, 1, "Blockchain Fundamentals", "Weeks 9-10", "Bitcoin/Ethereum basics, wallets, consensus"))
        dao.insertTask(TaskEntity(weekId = 5, description = "Learn blockchain fundamentals: blocks, chains, hashing"))
        dao.insertTask(TaskEntity(weekId = 5, description = "Understand Bitcoin and Ethereum differences"))
        dao.insertTask(TaskEntity(weekId = 5, description = "Set up a MetaMask wallet"))
        dao.insertTask(TaskEntity(weekId = 5, description = "Learn consensus mechanisms: PoW vs PoS"))
        dao.insertTask(TaskEntity(weekId = 5, description = "Complete Binance Academy blockchain courses"))
        dao.insertQuiz(QuizEntity(weekId = 5, question = "What is a blockchain?", optionA = "A type of database", optionB = "A distributed ledger", optionC = "A cryptocurrency", optionD = "A wallet", correctAnswerIndex = 1))

        // Weeks 11-12: Git & Version Control
        dao.insertWeek(WeekUnitEntity(6, 1, "Git & Version Control", "Weeks 11-12", "Git commands, GitHub setup, first repo"))
        dao.insertTask(TaskEntity(weekId = 6, description = "Install Git and configure user settings"))
        dao.insertTask(TaskEntity(weekId = 6, description = "Learn git init, add, commit, push, pull"))
        dao.insertTask(TaskEntity(weekId = 6, description = "Create a GitHub account and first repository"))
        dao.insertTask(TaskEntity(weekId = 6, description = "Practice branching and merging"))
        dao.insertTask(TaskEntity(weekId = 6, description = "Push your Python projects to GitHub"))
        dao.insertQuiz(QuizEntity(weekId = 6, question = "What does 'git commit' do?", optionA = "Uploads to GitHub", optionB = "Saves staged changes locally", optionC = "Deletes files", optionD = "Creates a branch", correctAnswerIndex = 1))

        // ==================== PHASE 2: Core Skills (Weeks 13-24) ====================
        val phase2 = ModuleEntity(2, "Phase 2: Core Skills", "Master ethical hacking tools, blockchain development, and trading basics.", "Weeks 13-24")
        dao.insertModule(phase2)

        // Weeks 13-14: Kali Linux Tools
        dao.insertWeek(WeekUnitEntity(7, 2, "Kali Linux Tools", "Weeks 13-14", "Install Kali; explore Nmap, Wireshark, Netcat"))
        dao.insertTask(TaskEntity(weekId = 7, description = "Install Kali Linux (VM or dual boot)"))
        dao.insertTask(TaskEntity(weekId = 7, description = "Scan networks with Nmap"))
        dao.insertTask(TaskEntity(weekId = 7, description = "Capture and analyze packets with Wireshark"))
        dao.insertTask(TaskEntity(weekId = 7, description = "Use Netcat for creating connections"))
        dao.insertTask(TaskEntity(weekId = 7, description = "Document your home network scan results"))
        dao.insertQuiz(QuizEntity(weekId = 7, question = "What is Nmap used for?", optionA = "Password cracking", optionB = "Network scanning", optionC = "Web browsing", optionD = "Email", correctAnswerIndex = 1))

        // Weeks 15-16: Ethical Hacking Basics
        dao.insertWeek(WeekUnitEntity(8, 2, "Ethical Hacking Basics", "Weeks 15-16", "Scanning, enumeration, penetration testing"))
        dao.insertTask(TaskEntity(weekId = 8, description = "Learn reconnaissance and information gathering"))
        dao.insertTask(TaskEntity(weekId = 8, description = "Practice enumeration techniques"))
        dao.insertTask(TaskEntity(weekId = 8, description = "Complete TryHackMe free beginner rooms"))
        dao.insertTask(TaskEntity(weekId = 8, description = "Understand vulnerability scanning basics"))
        dao.insertTask(TaskEntity(weekId = 8, description = "Learn about common vulnerabilities (OWASP Top 10)"))
        dao.insertQuiz(QuizEntity(weekId = 8, question = "What is the first phase of ethical hacking?", optionA = "Exploitation", optionB = "Reconnaissance", optionC = "Reporting", optionD = "Maintenance", correctAnswerIndex = 1))

        // Weeks 17-18: Blockchain Development
        dao.insertWeek(WeekUnitEntity(9, 2, "Blockchain Development", "Weeks 17-18", "Hardhat, Truffle, first Solidity contract"))
        dao.insertTask(TaskEntity(weekId = 9, description = "Set up Solidity development environment"))
        dao.insertTask(TaskEntity(weekId = 9, description = "Learn Solidity syntax and data types"))
        dao.insertTask(TaskEntity(weekId = 9, description = "Write your first smart contract"))
        dao.insertTask(TaskEntity(weekId = 9, description = "Deploy to Ethereum testnet using Remix IDE"))
        dao.insertTask(TaskEntity(weekId = 9, description = "Complete CryptoZombies lessons 1-4"))
        dao.insertQuiz(QuizEntity(weekId = 9, question = "What language are Ethereum smart contracts written in?", optionA = "Python", optionB = "JavaScript", optionC = "Solidity", optionD = "Rust", correctAnswerIndex = 2))

        // Weeks 19-20: Web3 Security Basics
        dao.insertWeek(WeekUnitEntity(10, 2, "Web3 Security Basics", "Weeks 19-20", "Smart contract vulnerabilities"))
        dao.insertTask(TaskEntity(weekId = 10, description = "Learn about reentrancy attacks"))
        dao.insertTask(TaskEntity(weekId = 10, description = "Understand integer overflow/underflow"))
        dao.insertTask(TaskEntity(weekId = 10, description = "Study access control vulnerabilities"))
        dao.insertTask(TaskEntity(weekId = 10, description = "Review Consensys security best practices"))
        dao.insertTask(TaskEntity(weekId = 10, description = "Analyze real-world smart contract hacks"))
        dao.insertQuiz(QuizEntity(weekId = 10, question = "What is a reentrancy attack?", optionA = "Brute force login", optionB = "Calling back into contract before state update", optionC = "DDoS attack", optionD = "Phishing", correctAnswerIndex = 1))

        // Weeks 21-22: Python for Trading/Security
        dao.insertWeek(WeekUnitEntity(11, 2, "Python for Trading/Security", "Weeks 21-22", "Scripts to fetch crypto prices, basic backtesting"))
        dao.insertTask(TaskEntity(weekId = 11, description = "Learn to use Python requests library"))
        dao.insertTask(TaskEntity(weekId = 11, description = "Fetch crypto prices from Binance API"))
        dao.insertTask(TaskEntity(weekId = 11, description = "Parse JSON responses and store data"))
        dao.insertTask(TaskEntity(weekId = 11, description = "Create basic price alert script"))
        dao.insertTask(TaskEntity(weekId = 11, description = "Learn pandas for data analysis"))
        dao.insertQuiz(QuizEntity(weekId = 11, question = "What Python library is used for HTTP requests?", optionA = "pandas", optionB = "numpy", optionC = "requests", optionD = "matplotlib", correctAnswerIndex = 2))

        // Weeks 23-24: GitHub Portfolio
        dao.insertWeek(WeekUnitEntity(12, 2, "GitHub Portfolio", "Weeks 23-24", "Upload Python scripts & Solidity projects"))
        dao.insertTask(TaskEntity(weekId = 12, description = "Clean up and document your Python projects"))
        dao.insertTask(TaskEntity(weekId = 12, description = "Create README files for all repositories"))
        dao.insertTask(TaskEntity(weekId = 12, description = "Upload smart contracts with documentation"))
        dao.insertTask(TaskEntity(weekId = 12, description = "Organize GitHub profile with pinned repos"))
        dao.insertTask(TaskEntity(weekId = 12, description = "Create a portfolio website on GitHub Pages"))
        dao.insertQuiz(QuizEntity(weekId = 12, question = "What file describes a GitHub repository?", optionA = "index.html", optionB = "README.md", optionC = "main.py", optionD = "package.json", correctAnswerIndex = 1))

        // ==================== PHASE 3: Intermediate Projects (Weeks 25-36) ====================
        val phase3 = ModuleEntity(3, "Phase 3: Intermediate Projects", "Build real-world projects in security, blockchain, and trading.", "Weeks 25-36")
        dao.insertModule(phase3)

        // Weeks 25-26: Pen Testing Labs
        dao.insertWeek(WeekUnitEntity(13, 3, "Penetration Testing Labs", "Weeks 25-26", "Metasploit, DVWA, Metasploitable labs"))
        dao.insertTask(TaskEntity(weekId = 13, description = "Set up Metasploitable vulnerable VM"))
        dao.insertTask(TaskEntity(weekId = 13, description = "Learn Metasploit framework basics"))
        dao.insertTask(TaskEntity(weekId = 13, description = "Practice on DVWA (Damn Vulnerable Web App)"))
        dao.insertTask(TaskEntity(weekId = 13, description = "Complete VulnHub beginner machines"))
        dao.insertTask(TaskEntity(weekId = 13, description = "Document your penetration testing methodology"))
        dao.insertQuiz(QuizEntity(weekId = 13, question = "What is Metasploit?", optionA = "A web browser", optionB = "A penetration testing framework", optionC = "An operating system", optionD = "A text editor", correctAnswerIndex = 1))

        // Weeks 27-28: Smart Contract Auditing
        dao.insertWeek(WeekUnitEntity(14, 3, "Smart Contract Auditing", "Weeks 27-28", "Deploy contract with intentional bugs, practice auditing"))
        dao.insertTask(TaskEntity(weekId = 14, description = "Complete Ethernaut challenges 1-10"))
        dao.insertTask(TaskEntity(weekId = 14, description = "Write contracts with intentional vulnerabilities"))
        dao.insertTask(TaskEntity(weekId = 14, description = "Practice auditing your own code"))
        dao.insertTask(TaskEntity(weekId = 14, description = "Learn about formal verification"))
        dao.insertTask(TaskEntity(weekId = 14, description = "Create a sample audit report"))
        dao.insertQuiz(QuizEntity(weekId = 14, question = "What is Ethernaut?", optionA = "A cryptocurrency", optionB = "Smart contract security challenges", optionC = "A wallet", optionD = "A blockchain", correctAnswerIndex = 1))

        // Weeks 29-30: Advanced Python
        dao.insertWeek(WeekUnitEntity(15, 3, "Advanced Python", "Weeks 29-30", "Automate trade analysis; connect to Binance API"))
        dao.insertTask(TaskEntity(weekId = 15, description = "Use Binance testnet API for trading"))
        dao.insertTask(TaskEntity(weekId = 15, description = "Build a price tracking dashboard"))
        dao.insertTask(TaskEntity(weekId = 15, description = "Implement basic trading indicators (MA, RSI)"))
        dao.insertTask(TaskEntity(weekId = 15, description = "Create automated trade alerts"))
        dao.insertTask(TaskEntity(weekId = 15, description = "Backtest simple trading strategies"))
        dao.insertQuiz(QuizEntity(weekId = 15, question = "What is backtesting?", optionA = "Backup testing", optionB = "Testing strategy on historical data", optionC = "Testing backend", optionD = "Security testing", correctAnswerIndex = 1))

        // Weeks 31-32: Simulated DeFi Security
        dao.insertWeek(WeekUnitEntity(16, 3, "DeFi Security", "Weeks 31-32", "Testnet DeFi protocols; detect vulnerabilities"))
        dao.insertTask(TaskEntity(weekId = 16, description = "Understand DeFi protocols: lending, DEX, yield"))
        dao.insertTask(TaskEntity(weekId = 16, description = "Deploy a simple DeFi contract on testnet"))
        dao.insertTask(TaskEntity(weekId = 16, description = "Study flash loan attacks"))
        dao.insertTask(TaskEntity(weekId = 16, description = "Analyze oracle manipulation vulnerabilities"))
        dao.insertTask(TaskEntity(weekId = 16, description = "Document DeFi security best practices"))
        dao.insertQuiz(QuizEntity(weekId = 16, question = "What is DeFi?", optionA = "Digital Finance", optionB = "Decentralized Finance", optionC = "Defined Finance", optionD = "Default Finance", correctAnswerIndex = 1))

        // Weeks 33-34: Crypto Security Scripts
        dao.insertWeek(WeekUnitEntity(17, 3, "Crypto Security Scripts", "Weeks 33-34", "Python scripts to monitor wallet transactions"))
        dao.insertTask(TaskEntity(weekId = 17, description = "Connect to Etherscan API"))
        dao.insertTask(TaskEntity(weekId = 17, description = "Build wallet transaction monitor"))
        dao.insertTask(TaskEntity(weekId = 17, description = "Create alerts for unusual activity"))
        dao.insertTask(TaskEntity(weekId = 17, description = "Track token transfers and balances"))
        dao.insertTask(TaskEntity(weekId = 17, description = "Build a simple portfolio tracker"))
        dao.insertQuiz(QuizEntity(weekId = 17, question = "What is Etherscan?", optionA = "An antivirus", optionB = "A blockchain explorer", optionC = "A wallet", optionD = "A mining pool", correctAnswerIndex = 1))

        // Weeks 35-36: Portfolio Projects
        dao.insertWeek(WeekUnitEntity(18, 3, "Portfolio Projects", "Weeks 35-36", "Combine trading bot + smart contract + security audit"))
        dao.insertTask(TaskEntity(weekId = 18, description = "Build a complete trading bot project"))
        dao.insertTask(TaskEntity(weekId = 18, description = "Create a secure smart contract with tests"))
        dao.insertTask(TaskEntity(weekId = 18, description = "Write a professional security audit report"))
        dao.insertTask(TaskEntity(weekId = 18, description = "Record demo videos for each project"))
        dao.insertTask(TaskEntity(weekId = 18, description = "Update GitHub portfolio with all projects"))
        dao.insertQuiz(QuizEntity(weekId = 18, question = "What makes a good portfolio project?", optionA = "Just code", optionB = "Documentation, tests, and demo", optionC = "Only README", optionD = "Copy-pasted code", correctAnswerIndex = 1))

        // ==================== PHASE 4: Specialization & Portfolio (Weeks 37-52) ====================
        val phase4 = ModuleEntity(4, "Phase 4: Specialization & Portfolio", "Specialize, build reputation, and prepare for employment.", "Weeks 37-52")
        dao.insertModule(phase4)

        // Weeks 37-38: Advanced Security Labs
        dao.insertWeek(WeekUnitEntity(19, 4, "Advanced Security Labs", "Weeks 37-38", "SOC monitoring, vulnerability assessment"))
        dao.insertTask(TaskEntity(weekId = 19, description = "Learn SOC analyst fundamentals"))
        dao.insertTask(TaskEntity(weekId = 19, description = "Practice with SIEM tools"))
        dao.insertTask(TaskEntity(weekId = 19, description = "Complete TryHackMe intermediate paths"))
        dao.insertTask(TaskEntity(weekId = 19, description = "Conduct vulnerability assessments"))
        dao.insertTask(TaskEntity(weekId = 19, description = "Write professional vulnerability reports"))
        dao.insertQuiz(QuizEntity(weekId = 19, question = "What does SOC stand for?", optionA = "Software Operations Center", optionB = "Security Operations Center", optionC = "System Online Center", optionD = "Secure Output Control", correctAnswerIndex = 1))

        // Weeks 39-40: Solidity Advanced
        dao.insertWeek(WeekUnitEntity(20, 4, "Advanced Solidity", "Weeks 39-40", "Full smart contract project on testnet"))
        dao.insertTask(TaskEntity(weekId = 20, description = "Build a complete DApp backend"))
        dao.insertTask(TaskEntity(weekId = 20, description = "Implement complex contract interactions"))
        dao.insertTask(TaskEntity(weekId = 20, description = "Add comprehensive test coverage"))
        dao.insertTask(TaskEntity(weekId = 20, description = "Deploy on multiple testnets"))
        dao.insertTask(TaskEntity(weekId = 20, description = "Create front-end integration"))
        dao.insertQuiz(QuizEntity(weekId = 20, question = "What is a DApp?", optionA = "Digital Application", optionB = "Decentralized Application", optionC = "Database Application", optionD = "Dynamic Application", correctAnswerIndex = 1))

        // Weeks 41-42: Trading + Blockchain Integration
        dao.insertWeek(WeekUnitEntity(21, 4, "Trading + Blockchain Integration", "Weeks 41-42", "Automate trades based on smart contract events"))
        dao.insertTask(TaskEntity(weekId = 21, description = "Listen to blockchain events with Python"))
        dao.insertTask(TaskEntity(weekId = 21, description = "Create event-driven trading signals"))
        dao.insertTask(TaskEntity(weekId = 21, description = "Integrate on-chain and off-chain data"))
        dao.insertTask(TaskEntity(weekId = 21, description = "Build an automated response system"))
        dao.insertTask(TaskEntity(weekId = 21, description = "Test with paper trading"))
        dao.insertQuiz(QuizEntity(weekId = 21, question = "What are blockchain events?", optionA = "Conferences", optionB = "Logs emitted by smart contracts", optionC = "Price changes", optionD = "Transactions", correctAnswerIndex = 1))

        // Weeks 43-44: Security Audits
        dao.insertWeek(WeekUnitEntity(22, 4, "Security Audits", "Weeks 43-44", "Audit sample smart contract, document findings"))
        dao.insertTask(TaskEntity(weekId = 22, description = "Perform a complete audit on a sample project"))
        dao.insertTask(TaskEntity(weekId = 22, description = "Use automated tools: Slither, Mythril"))
        dao.insertTask(TaskEntity(weekId = 22, description = "Manual code review best practices"))
        dao.insertTask(TaskEntity(weekId = 22, description = "Write a professional audit report"))
        dao.insertTask(TaskEntity(weekId = 22, description = "Create audit report template"))
        dao.insertQuiz(QuizEntity(weekId = 22, question = "What is Slither?", optionA = "A game", optionB = "Static analysis tool for Solidity", optionC = "A blockchain", optionD = "A wallet", correctAnswerIndex = 1))

        // Weeks 45-46: Portfolio Refinement
        dao.insertWeek(WeekUnitEntity(23, 4, "Portfolio Refinement", "Weeks 45-46", "Upload all projects to GitHub & LinkedIn"))
        dao.insertTask(TaskEntity(weekId = 23, description = "Clean up all GitHub repositories"))
        dao.insertTask(TaskEntity(weekId = 23, description = "Create compelling project descriptions"))
        dao.insertTask(TaskEntity(weekId = 23, description = "Record demo videos for key projects"))
        dao.insertTask(TaskEntity(weekId = 23, description = "Update LinkedIn with projects"))
        dao.insertTask(TaskEntity(weekId = 23, description = "Create a personal portfolio website"))
        dao.insertQuiz(QuizEntity(weekId = 23, question = "What platform is best for showcasing code?", optionA = "Facebook", optionB = "GitHub", optionC = "Instagram", optionD = "Twitter", correctAnswerIndex = 1))

        // Weeks 47-48: Mock Freelance Projects
        dao.insertWeek(WeekUnitEntity(24, 4, "Mock Freelance Projects", "Weeks 47-48", "Offer free audits or small freelance tasks"))
        dao.insertTask(TaskEntity(weekId = 24, description = "Create profiles on Upwork and Fiverr"))
        dao.insertTask(TaskEntity(weekId = 24, description = "Offer 2-3 free audits for testimonials"))
        dao.insertTask(TaskEntity(weekId = 24, description = "Complete small paid gigs"))
        dao.insertTask(TaskEntity(weekId = 24, description = "Build client communication skills"))
        dao.insertTask(TaskEntity(weekId = 24, description = "Create service packages"))
        dao.insertQuiz(QuizEntity(weekId = 24, question = "Why offer free work initially?", optionA = "It's illegal to charge", optionB = "To build testimonials and reputation", optionC = "Clients don't pay", optionD = "It's not worth money", correctAnswerIndex = 1))

        // Weeks 49-50: Resume & Job Prep
        dao.insertWeek(WeekUnitEntity(25, 4, "Resume & Job Prep", "Weeks 49-50", "Cybersecurity + Blockchain + Trading CV"))
        dao.insertTask(TaskEntity(weekId = 25, description = "Create a professional resume"))
        dao.insertTask(TaskEntity(weekId = 25, description = "Write compelling cover letter templates"))
        dao.insertTask(TaskEntity(weekId = 25, description = "Prepare for technical interviews"))
        dao.insertTask(TaskEntity(weekId = 25, description = "Practice common interview questions"))
        dao.insertTask(TaskEntity(weekId = 25, description = "Apply to 5+ relevant positions"))
        dao.insertQuiz(QuizEntity(weekId = 25, question = "What should a tech resume emphasize?", optionA = "Hobbies", optionB = "Projects and skills", optionC = "Height", optionD = "Age", correctAnswerIndex = 1))

        // Weeks 51-52: Capstone Project
        dao.insertWeek(WeekUnitEntity(26, 4, "Capstone Project", "Weeks 51-52", "Deploy smart contract, simulate trades, secure system, document report"))
        dao.insertTask(TaskEntity(weekId = 26, description = "Plan comprehensive capstone project"))
        dao.insertTask(TaskEntity(weekId = 26, description = "Combine cybersecurity, blockchain, and trading skills"))
        dao.insertTask(TaskEntity(weekId = 26, description = "Deploy production-ready smart contract"))
        dao.insertTask(TaskEntity(weekId = 26, description = "Conduct full security audit"))
        dao.insertTask(TaskEntity(weekId = 26, description = "Create YouTube demo and documentation"))
        dao.insertQuiz(QuizEntity(weekId = 26, question = "What is a capstone project?", optionA = "A hat", optionB = "A comprehensive final project", optionC = "A small exercise", optionD = "A quiz", correctAnswerIndex = 1))
    }

    private suspend fun populateProjects(dao: RoadmapDao) {
        val projects = listOf(
            ProjectEntity(
                title = "Home Network Scan",
                description = "Scan home network for open ports using Nmap and document findings",
                phaseRef = "Phase 2",
                weekId = 7
            ),
            ProjectEntity(
                title = "Simple Smart Contract",
                description = "Deploy a Hello World contract on Ethereum testnet",
                phaseRef = "Phase 2",
                weekId = 9
            ),
            ProjectEntity(
                title = "Crypto Price Bot",
                description = "Python script to fetch prices from Binance API and send alerts",
                phaseRef = "Phase 2",
                weekId = 11
            ),
            ProjectEntity(
                title = "Penetration Testing Report",
                description = "Complete pentest on Metasploitable VM with professional report",
                phaseRef = "Phase 3",
                weekId = 13
            ),
            ProjectEntity(
                title = "Smart Contract Audit Report",
                description = "Security audit of sample smart contract with findings document",
                phaseRef = "Phase 3",
                weekId = 14
            ),
            ProjectEntity(
                title = "Trading Bot",
                description = "Automated trading bot with Binance testnet integration",
                phaseRef = "Phase 3",
                weekId = 15
            ),
            ProjectEntity(
                title = "DeFi Security Analysis",
                description = "Analysis of DeFi protocol vulnerabilities with mitigation suggestions",
                phaseRef = "Phase 3",
                weekId = 16
            ),
            ProjectEntity(
                title = "Wallet Transaction Monitor",
                description = "Python tool to monitor wallet transactions and detect unusual activity",
                phaseRef = "Phase 3",
                weekId = 17
            ),
            ProjectEntity(
                title = "Full DApp Project",
                description = "Complete decentralized application with frontend and smart contracts",
                phaseRef = "Phase 4",
                weekId = 20
            ),
            ProjectEntity(
                title = "Capstone: Security + Blockchain + Trading",
                description = "Comprehensive project combining all three domains with documentation",
                phaseRef = "Phase 4",
                weekId = 26
            )
        )
        for (project in projects) {
            dao.insertProject(project)
        }
    }
}

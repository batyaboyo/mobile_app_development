# UGTours: Video Demonstration Script

**Target Duration:** 5 Minutes  
**Demonstrator:** Batya Tonny Boyo  
**Format:** Screen Recording with Voiceover

---

## 0:00 - 0:35 | Introduction & Context

**Visual:**
- Title card: "UGTours - Your Digital Guide to Uganda"
- Subtitle: "Final Year Project by Batya Tonny Boyo"
- Fade to phone home screen showing UGTours app icon
- Tap icon to launch app

**Voiceover:**

"Welcome to UGTours, my final year project addressing a real challenge in Uganda's tourism industry.

Uganda is known as the Pearl of Africa, blessed with incredible natural beauty and wildlife. Yet tourists face a common problem: information is scattered across multiple websites, internet connectivity is unreliable in remote areas, and currency conversion creates constant confusion.

UGTours solves these challenges. It's an offline-first mobile application that serves as your complete digital guide to Uganda - from discovering attractions to booking accommodations, all without needing an internet connection."

---

## 0:35 - 1:10 | User Authentication & Security

**Visual:**
- App opens to login screen showing:
  - UG Tours app icon (launch icon)
  - "Welcome Back to UG Tours" heading
  - Email and password input fields
- Tap "Don't have an account? Register"
- Registration screen appears
- Fill in registration form:
  - Name: "Batya Tonny"
  - Email: "batya@example.com"
  - Password: (hidden characters)
- Tap "Sign Up" button
- Loading spinner appears
- Success message: "Account created successfully"
- Automatic login to home screen

**Voiceover:**

"The login screen welcomes users with the UG Tours app icon and a friendly 'Welcome Back to UG Tours' message, creating an immediate connection to the brand.

Let's create a new account by tapping the registration link. Security is fundamental to UGTours - I'll register with my name, email, and password. Behind the scenes, the app uses BCrypt hashing to securely store credentials. Plain text passwords are never saved to the database.

Once registered, I'm automatically logged in. This authentication system enables personalized features like saving favorites and managing bookings, all tied securely to my account."

---

## 1:10 - 1:50 | Home Dashboard & Discovery

**Visual:**
- Home screen fully loaded
- Slow scroll through "Featured Attractions" section
- Swipe horizontally through popular destinations carousel
- Show category chips: "National Parks", "Cultural Sites", "Waterfalls", "Adventure"
- Tap "National Parks" chip
- List filters to show only national parks
- Tap chip again to clear filter

**Voiceover:**

"Welcome to the home dashboard. The interface follows Material Design 3 principles for a clean, modern look.

At the top, we have featured attractions showcasing Uganda's most popular destinations. Users can swipe through this carousel to get inspired.

Below, attractions are organized by categories. If I'm specifically interested in National Parks, I simply tap the category chip and the list filters instantly. The smooth performance you're seeing is powered by the MVVM architecture pattern, which efficiently manages data and UI updates.

This makes discovering Uganda's treasures intuitive and enjoyable."

---

## 1:50 - 2:30 | Smart Search & Navigation

**Visual:**
- Tap search icon in toolbar
- Type "Falls" slowly
- List dynamically filters showing:
  - Murchison Falls National Park
  - Sipi Falls
  - Ssezibwa Falls
- Clear search
- Type "Gorilla"
- Results show Bwindi Impenetrable National Park
- Tap on "Bwindi Impenetrable National Park"
- Attraction detail page loads

**Voiceover:**

"Finding specific destinations is effortless with the smart search feature.

Watch as I type 'Falls' - the results update in real-time, searching through both attraction names and descriptions. I can see Murchison Falls, Sipi Falls, and more.

Let me search for something else - 'Gorilla'. Instantly, Bwindi Impenetrable National Park appears, home to Uganda's famous mountain gorillas.

I'll tap to explore this attraction in detail. This dynamic search ensures tourists can quickly find exactly what they're looking for, even with partial information."

---

## 2:30 - 3:20 | Attraction Details & Favorites

**Visual:**
- Attraction detail page for Bwindi showing:
  - Image gallery at top
- Swipe through 3-4 high-quality images
- Scroll down to show:
  - Detailed description
  - Unique features section
  - Location information
- Tap the heart/star icon (favorite button)
- Icon fills with color and brief animation
- Toast message: "Added to favorites"
- Navigate back
- Tap "Favorites" tab in bottom navigation
- Show Bwindi now listed in favorites

**Voiceover:**

"Inside each attraction, users get the complete picture. There's a beautiful image gallery showcasing the destination from multiple angles.

Below, we have comprehensive information - the history, unique features, and what makes this place special. For Bwindi, we highlight the mountain gorilla trekking experience, one of the most sought-after wildlife encounters in the world.

Now, imagine I'm planning a trip for next month. With one tap on this heart icon, I save Bwindi to my favorites.

When I navigate to the Favorites tab, there it is. This feature is perfect for building a personalized itinerary. And here's the key advantage - all this data is stored locally using Room database, so I can access my saved attractions even in remote areas without any internet connection."

---

## 3:20 - 4:10 | Accommodation & Dual Currency Pricing

**Visual:**
- Return to Bwindi attraction detail
- Scroll down to "Nearby Accommodations" section
- Show list of accommodations with dual pricing:
  - "Buhoma Lodge: $400-600 (UGX 1,416,000 - 2,124,000)"
  - "Gorilla Forest Camp: $350 (UGX 1,239,000)"
- Pause on pricing to highlight
- Tap "Book Now" on one accommodation
- Booking dialog appears showing:
  - Accommodation name and price
  - Check-in date selector
  - Check-out date selector
  - Number of guests field
  - Automatic total calculation in both currencies
  - Special requests field

**Voiceover:**

"Here's one of the most practical features of UGTours: the intelligent pricing engine.

Tourists constantly struggle with currency conversion. Lodges advertise prices in US Dollars, but payments are made in Ugandan Shillings. This creates confusion and uncertainty.

Look at Buhoma Lodge here. The app displays the price as four hundred dollars, and immediately shows the Uganda Shilling equivalent - one million, four hundred sixteen thousand shillings. The conversion uses the current exchange rate of one dollar equals three thousand five hundred forty shillings.

This dual-currency display eliminates mental math and helps tourists budget accurately on the spot. Whether you think in dollars or shillings, you have complete financial clarity.

When I tap 'Book Now', I can select my dates, specify the number of guests, and the app automatically calculates the total cost in both currencies. I can even add special requests like 'window view preferred' or dietary requirements."

---

## 4:10 - 4:50 | Booking Management & Drawer Navigation

**Visual:**
- Complete the booking (tap "Confirm Booking")
- Success message appears
- Navigate to "Bookings" tab (4th tab in bottom navigation)
- Show the newly created booking with:
  - Accommodation name
  - Dates
  - Total price in both currencies
  - Status: "Pending"
- Tap the hamburger menu icon (☰) in the top-left toolbar
- Navigation Drawer opens showing:
  - App Header
  - Profile
  - About
  - Settings
  - Logout
- Tap "Settings"
- Toggle "Dark Mode" switch
- App theme changes to dark mode
- Toggle back to light mode
- Open Drawer again and tap "Logout"
- User session clears and returns to login screen

**Voiceover:**

"After confirming the booking, it's saved to the Bookings tab, where I can track all my reservations.

For account management, we've implemented a modern Navigation Drawer. By tapping the menu icon, I can access my Profile, learn more About the app, or adjust Settings.

Let's check the Settings. Here I can toggle between Dark and Light modes, allowing users to customize their viewing experience - perfect for night-time planning or saving battery.

The drawer also houses the Logout option. This keeps the main interface clean while ensuring essential account features are always just a swipe away.

Tapping Logout securely clears my session and returns me to the login screen, ensuring my data remains private."

---

## 4:50 - 5:00 | Conclusion

**Visual:**
- Login screen visible
- Fade to black
- Display text:
  - "UGTours"
  - "Developed by Batya Tonny Boyo"
  - "GitHub: @batyaboyo"
- Fade out

**Voiceover:**

"UGTours is more than just a list of destinations. It's a complete travel companion that solves real problems - from offline accessibility to currency confusion to booking management.

By combining modern Android architecture with user-focused features, UGTours has the potential to transform how people experience Uganda's tourism industry.

Thank you for watching."

---

## Technical Notes for Recording

### Audio Quality
- Use a quality microphone in a quiet environment
- Speak clearly and at a moderate pace
- Add subtle background music (optional, low volume)

### Video Quality
- Record at 1080p minimum
- Use screen recording software (e.g., ADB screenrecord, scrcpy)
- Ensure smooth frame rate (30fps minimum)

### Editing Tips
- Add smooth transitions between sections
- Highlight UI elements with subtle circles/arrows when needed
- Include text overlays for key features:
  - "Offline-First Architecture"
  - "Dual Currency Pricing"
  - "Secure Authentication"
  - "Smart Search"
- Keep total duration under 5 minutes for engagement

### Pacing
- Don't rush through screens
- Pause briefly on important information
- Allow animations to complete
- Match voiceover timing with visual actions

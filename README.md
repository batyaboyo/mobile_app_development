# [UGTours: Integrated Mobile Tourism Guide]

## 1. Abstract
The tourism sector in Uganda faces challenges related to information accessibility and fragmented service delivery. **UGTours** is a native Android application developed to address these issues by providing a centralized, offline-first digital guide. This project leverages modern mobile computing paradigms, specifically the Model-View-ViewModel (MVVM) architecture and local data persistence, to offer a distinct solution for exploring national parks, cultural sites, and accommodations. This report outlines the design, implementation, and evaluation of the UGTours application.

## 2. Introduction & Problem Statement
### 2.1 Background
Uganda functions as a premier tourist destination; however, potential visitors often lack consolidated, reliable information regarding attractions and amenities. Existing web-based solutions require consistent internet connectivity, which is often unreliable in remote nature reserves.

### 2.2 Problem Statement
The current digital landscape for Ugandan tourism significantly lacks:
1.  **Offline Accessibility:** Critical for safari locations with poor network coverage.
2.  **Centralized Data:** Information is scattered across multiple disparate sources.
3.  **Integrated Booking:** A lack of unified systems for accommodation discovery and reservation.
4.  **Transparent Pricing:** Complexity in managing dual-currency (USD/UGX) pricing for international vs. local tourists.

## 3. Project Objectives
The primary objective was to design and implement a robust mobile application serving as a definitive digital tourism guide. Specific objectives included:
*   To develop a curated catalog of 16+ major tourist attractions with rich media content.
*   To implement an intelligent accommodation finder with proximity-based sorting.
*   To engineer a dual-currency pricing engine implementing real-time conversion (USD/UGX).
*   To ensure secure user authentication using industry-standard hashing algorithms (BCrypt).
*   To guarantee system availability in low-connectivity environments via an offline-first architecture.

## 4. Methodology & System Design
### 4.1 Architectural Pattern
The system adopts the **Model-View-ViewModel (MVVM)** architecture to ensure separation of concerns, enhance testability, and manage UI consistency.
*   **View Layer:** XML-based layouts and Fragments utilizing ViewBinding.
*   **ViewModel Layer:** Manages UI state and business logic using Kotlin Coroutines and LiveData/Flow.
*   **Model Layer:** Repository pattern mediating between the Room Database (local source) and remote data sources.

### 4.2 Technologies Employed
*   **Platform:** Android (Kotlin)
*   **Database:** Room Persistence Library (SQLite abstract layer)
*   **Asynchronous Processing:** Kotlin Coroutines & Flow
*   **Security:** BCrypt for password hashing; DataStore for session management.

## 5. Implementation Results
The development phase yielded a fully functional prototype with the following core modules:
1.  **Attraction Discovery Module:** Enables users to browse, filter, and search for destinations.
2.  **Accommodation & Booking Module:** Facilitates the viewing of lodging options and management of bookings with status tracking (Pending/Confirmed).
3.  **Currency Conversion Engine:** Automatically handles pricing displays in multiple currencies suitable for the target demographic.
4.  **User Profile Management:** Secure login, registration, and personalized history tracking (Favorites/Recently Viewed).

## 6. Conclusion
The UGTours project successfully demonstrates the efficacy of modern Android architecture in solving domain-specific problems within the tourism industry. By prioritizing offline accessibility and user-centric design, the application provides a significant improvement over existing fragmented solutions. Future work will focus on integrating real-time GPS navigation and third-party payment gateways.

---
**Developer:** Batya Tonny Boyo  
**Repository:** [https://github.com/batyaboyo/UGTours_App](https://github.com/batyaboyo/UGTours_App)  
**Date:** December 2025

package com.theword.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.theword.app.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val InterFont = GoogleFont("Inter")
val MerriweatherFont = GoogleFont("Merriweather")

val InterFamily = FontFamily(
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Bold),
)

val InterFallback = FontFamily.SansSerif

val MerriweatherFamily = FontFamily(
    Font(googleFont = MerriweatherFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = MerriweatherFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = MerriweatherFont, fontProvider = provider, weight = FontWeight.Bold),
)

val MerriweatherFallback = FontFamily.Serif

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    displayMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp),
    displaySmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    headlineLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    headlineMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    headlineSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp),
    titleLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = MerriweatherFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 28.sp),
    bodyMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 10.sp),
)

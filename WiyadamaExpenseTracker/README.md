# Wiyadama Expense Tracker

**Offline-first expense tracking app for Android** built with Kotlin, Jetpack Compose, and Room.

## Features

✅ **Fully Offline** - No internet required, all data stored locally  
✅ **LKR Currency** - Sri Lankan Rupees with proper formatting  
✅ **Categories & Subcategories** - Bills, Telephone, Food, Shopping, etc.  
✅ **Member Tracking** - Assign expenses to family members  
✅ **Shop Management** - Track which shops you spend at  
✅ **Transaction History** - View, edit, delete transactions by day  
✅ **Soft Delete & Trash** - Recover accidentally deleted transactions  
✅ **Data Safety** - Automatic backups, WAL journaling, checksums  

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Database**: Room (SQLite with WAL)
- **DI**: Hilt
- **Architecture**: MVVM + Repository pattern
- **Charts**: Vico Compose
- **Background**: WorkManager for periodic backups

## Project Structure

```
app/src/main/java/com/wiyadama/expensetracker/
├── data/
│   ├── entity/          # Room entities (Member, Category, Shop, Transaction)
│   ├── dao/             # Data Access Objects
│   ├── database/        # Database setup with seed data
│   └── repository/      # Repository layer
├── domain/              # Use cases (planned)
├── ui/
│   ├── screens/         # Compose screens (Home, Analytics, Members, History)
│   └── theme/           # Material 3 theme, colors, typography
├── util/                # Currency, date, validation utilities
├── di/                  # Hilt modules
└── WiyadamaApplication.kt

```

## Database Schema

### Entities

- **Member** - Family members who incur expenses
- **Category** - Hierarchical categories (parent-child subcategories)
- **Shop** - Shops/merchants where expenses occur
- **Transaction** - Expense records with soft-delete support
- **BackupMeta** - Backup metadata for recovery

### Seed Categories

On first launch, the app seeds:
- **Bills & Utilities** → Water, Electricity
- **Telephone Bills** → TV, Phone (member req.), Wi-Fi (member req.)
- Food & Dining
- Transport
- Shopping
- Grocery
- Entertainment
- Travel

## Build Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- Gradle 8.2+

### Steps

1. **Clone/Open Project**
   ```bash
   cd WiyadamaExpenseTracker
   ```

2. **Sync Gradle**
   - Open in Android Studio
   - Wait for Gradle sync to complete

3. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Or on Windows:
   ```powershell
   .\gradlew.bat assembleDebug
   ```

4. **Install on Device/Emulator**
   ```bash
   ./gradlew installDebug
   ```

5. **Run Tests**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

### Output Location

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

## Development Status

### ✅ Completed (Phase 1 & 2)
- [x] Project structure and Gradle setup
- [x] Room database with entities, DAOs, indexes
- [x] Hilt dependency injection
- [x] Seed data for categories
- [x] Repository layer
- [x] Currency formatter for LKR
- [x] Date utilities
- [x] Validation utilities
- [x] Material 3 theme matching TSX design
- [x] Home screen UI with category cards
- [x] Bottom navigation
- [x] MainActivity with device container simulation
- [x] CategoryDetailScreen with gradient hero and stats
- [x] AddExpenseScreen - 4-step flow (Amount → Category → Member/Shop → Details)
- [x] AnalyticsScreen with period selector and insights
- [x] MembersScreen with list and detail views
- [x] HistoryScreen with deleted transactions toggle

### 🚧 In Progress (Phase 3)
- [ ] Wire ViewModels with real data for all screens
- [ ] Implement transaction save/edit/delete operations
- [ ] Add Vico charts for Analytics screen
- [ ] Category management (add/edit/delete with reassignment)
- [ ] Member add/edit/delete dialogs
- [ ] Shop management
- [ ] Calendar date picker for History filters

### 📋 Planned (Phase 4)
- [ ] Backup/restore system (JSON + SQLite)
- [ ] WorkManager periodic backups
- [ ] Full validation and error handling
- [ ] Unit and instrumentation tests
- [ ] UI tests
- [ ] Shop autocomplete

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumentation tests (requires connected device/emulator):
```bash
./gradlew connectedAndroidTest
```

## Permissions

This app requires **NO INTERNET** permission - it's fully offline. Future versions may request storage permissions for manual backup export/import via Storage Access Framework.

## Contributing

This is a personal expense tracker project. Contributions welcome via pull requests.

## License

MIT License - See LICENSE file for details.

---

**Built with ❤️ for offline-first expense tracking in Sri Lanka**

# Coffee Bliss - Digital Membership Card

> **Evaluasi Akhir Semester (EAS) — Mata Kuliah Pemrograman Perangkat Bergerak C**
> Departemen Informatika, Institut Teknologi Sepuluh Nopember (ITS)

Aplikasi Android modern untuk manajemen kartu member digital coffee shop **Coffee Bliss**. Dibangun dengan pendekatan *full native Android* menggunakan **Kotlin**, **Jetpack Compose**, **Room Database**, dan arsitektur **MVVM** yang bersih dan terstruktur.

---

## Daftar Isi

- [Deskripsi Aplikasi](#deskripsi-aplikasi)
- [Tech Stack](#tech-stack)
- [Fitur Utama](#fitur-utama)
- [Arsitektur Aplikasi](#arsitektur-aplikasi)
- [Struktur Project](#struktur-project)
- [Skema Database](#skema-database)
- [Alur Navigasi](#alur-navigasi)
- [Aturan Bisnis Poin & Reward](#aturan-bisnis-poin--reward)
- [Cara Menjalankan](#cara-menjalankan)
- [Dependensi](#dependensi)
- [Panduan Penggunaan](#panduan-penggunaan)
- [Informasi Tim](#informasi-tim)

---

## Deskripsi Aplikasi

**Coffee Bliss** adalah aplikasi kartu member digital yang memungkinkan kasir atau admin coffee shop untuk:

- Mendaftarkan pelanggan sebagai member baru
- Mencatat setiap transaksi pembelian secara digital
- Menghitung dan mengakumulasi poin loyalitas secara otomatis
- Menampilkan kartu member digital beserta QR Code unik setiap member
- Memproses penukaran poin (*redemption*) dengan reward berupa minuman gratis
- Mengelola profil member termasuk foto profil

Seluruh data disimpan secara lokal menggunakan **Room Database** tanpa memerlukan koneksi internet.

---

## Tech Stack

| Teknologi | Versi | Kegunaan |
|---|---|---|
| **Kotlin** | 1.9.x | Bahasa pemrograman utama |
| **Jetpack Compose** | BOM 2024.02.00 | Framework UI deklaratif |
| **Material 3** | via Compose BOM | Desain sistem & komponen UI |
| **Room Database** | 2.6.1 | Persistensi data lokal (SQLite) |
| **KSP** (Kotlin Symbol Processing) | — | Code generation untuk Room |
| **Navigation Compose** | 2.7.7 | Navigasi antar layar |
| **ViewModel + Lifecycle** | 2.7.0 | Manajemen state & lifecycle-aware |
| **Kotlin Coroutines + Flow** | — | Pemrograman asinkron & reactive data |
| **ZXing** | 3.5.3 | Generate QR Code member |
| **Coil** | 2.5.0 | Loading & tampil gambar (foto profil) |

- **Min SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 34 (Android 14)
- **JVM Target**: Java 17

---

## Fitur Utama

### Splash Screen
- Tampilan awal animasi logo Coffee Bliss sebelum masuk ke halaman utama.

### Manajemen Member
- **Daftar Member**: Menampilkan seluruh member yang terdaftar beserta nomor member, nama, dan total poin.
- **Tambah Member**: Form pendaftaran member baru dengan validasi input (nama, email, no HP). Email bersifat unik — tidak boleh duplikat.
- **Nomor Member Otomatis**: Sistem membangkitkan nomor member secara otomatis dengan format `MBRxxxxx` (contoh: `MBR00001`).
- **Edit Profil**: Member dapat memperbarui nama, email, nomor HP, dan foto profil (dari galeri perangkat).

### Kartu Member Digital
- Menampilkan identitas lengkap member: nama, nomor member, total poin.
- **QR Code unik** per member yang digenerate menggunakan library ZXing.
- Tombol navigasi langsung ke Transaksi, Reward, dan Edit Profil.

### Transaksi & Poin
- Input nominal pembelian dalam Rupiah.
- Poin dihitung otomatis berdasarkan aturan bisnis (`Rp10.000 = 1 poin`).
- Setiap transaksi tersimpan ke database dengan timestamp.
- Total poin member diperbarui secara real-time.

### Redeem Reward
- Menampilkan katalog reward beserta jumlah poin yang dibutuhkan.
- Tombol *Redeem* hanya aktif jika poin member mencukupi.
- Setelah redeem, poin dikurangi otomatis dan riwayat redemption tersimpan.

### Riwayat Aktivitas
- Riwayat transaksi pembelian (tanggal, nominal, poin didapat).
- Riwayat penukaran reward (tanggal, nama reward, poin terpakai).

---

## Arsitektur Aplikasi

Aplikasi menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** dengan pemisahan lapisan yang jelas:

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│   Compose Screens + Reusable Components │
└────────────────────┬────────────────────┘
                     │ observes state
┌────────────────────▼────────────────────┐
│           ViewModel Layer               │
│  (AddMember, MemberDetail, Transaction, │
│   Redeem, MemberList, EditProfile VM)   │
└────────────────────┬────────────────────┘
                     │ calls suspend/Flow
┌────────────────────▼────────────────────┐
│          Repository Layer               │
│        CoffeeBlissRepository            │
└──────────┬─────────────────┬────────────┘
           │                 │
┌──────────▼──────┐  ┌───────▼──────────┐
│   Room DAOs     │  │   Utility Layer   │
│ (Member, Trans, │  │ (PointCalculator, │
│  Redemption)    │  │  ImageStorage,    │
└──────────┬──────┘  │  Formatters)      │
           │         └───────────────────┘
┌──────────▼──────────────────────────────┐
│         Room Database (SQLite)          │
│    members | transactions | redemptions │
└─────────────────────────────────────────┘
```

**Prinsip yang diterapkan:**
- **Unidirectional Data Flow (UDF)**: Data mengalir dari Repository → ViewModel → UI. Event mengalir dari UI → ViewModel → Repository.
- **Single Source of Truth**: Room Database sebagai sumber data tunggal yang dipancarkan via `Flow`.
- **Reactive UI**: Compose mengobservasi `StateFlow` dari ViewModel sehingga UI otomatis diperbarui saat data berubah.
- **Separation of Concerns**: Setiap lapisan hanya bertanggung jawab pada tugasnya masing-masing.

---

## Struktur Project

```
EAS_PPB/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/coffeebliss/app/
│           │
│           ├── CoffeeBlissApplication.kt    # Application class, init database
│           ├── MainActivity.kt              # Entry point, setup Compose
│           │
│           ├── data/
│           │   ├── model/
│           │   │   ├── Member.kt            # Entity: data member (Room)
│           │   │   ├── Transaction.kt       # Entity: transaksi pembelian (Room)
│           │   │   ├── Redemption.kt        # Entity: riwayat redeem (Room)
│           │   │   ├── Reward.kt            # Data class reward + RewardCatalog
│           │   │   └── OperationResults.kt  # Result wrapper (TransactionResult, RedeemResult)
│           │   │
│           │   ├── dao/
│           │   │   ├── MemberDao.kt         # CRUD + query member
│           │   │   ├── TransactionDao.kt    # Insert + query transaksi
│           │   │   └── RedemptionDao.kt     # Insert + query redemption
│           │   │
│           │   ├── database/
│           │   │   └── CoffeeBlissDatabase.kt  # Room database (v3), singleton
│           │   │
│           │   └── repository/
│           │       └── CoffeeBlissRepository.kt  # Logika bisnis, orkestrasi DAO
│           │
│           ├── viewmodel/
│           │   ├── MemberListViewModel.kt   # State daftar member & count
│           │   ├── AddMemberViewModel.kt    # Form tambah member
│           │   ├── MemberDetailViewModel.kt # Detail member, transaksi, redemption
│           │   ├── TransactionViewModel.kt  # Form tambah transaksi
│           │   ├── RedeemViewModel.kt       # Proses redeem reward
│           │   ├── EditProfileViewModel.kt  # Form edit profil member
│           │   └── ViewModelFactory.kt      # Factory untuk inject repository ke VM
│           │
│           ├── ui/
│           │   ├── screens/
│           │   │   ├── SplashScreen.kt      # Splash / loading screen
│           │   │   ├── MemberListScreen.kt  # Home: daftar semua member
│           │   │   ├── AddMemberScreen.kt   # Form pendaftaran member baru
│           │   │   ├── MemberCardScreen.kt  # Kartu member digital + QR Code
│           │   │   ├── TransactionScreen.kt # Form input transaksi pembelian
│           │   │   ├── RewardScreen.kt      # Katalog & proses redeem reward
│           │   │   └── EditProfileScreen.kt # Form edit data & foto profil
│           │   │
│           │   ├── components/
│           │   │   ├── CoffeeCard.kt        # Card member reusable component
│           │   │   ├── Dialogs.kt           # Dialog konfirmasi & info
│           │   │   └── ProfileAvatar.kt     # Komponen avatar foto profil
│           │   │
│           │   └── theme/
│           │       └── Theme.kt             # Material 3 color scheme (green theme)
│           │
│           ├── navigation/
│           │   └── CoffeeBlissNavGraph.kt   # NavHost + Routes object
│           │
│           └── util/
│               ├── PointCalculator.kt       # Kalkulasi poin dari nominal
│               ├── ImageStorage.kt          # Simpan & kelola foto profil lokal
│               └── Formatters.kt            # Format Rupiah, tanggal, dll.
│
├── docs/
│   └── UserManual.md                        # Panduan penggunaan aplikasi
├── build.gradle.kts                         # Project-level build config
├── app/build.gradle.kts                     # App-level build config + dependencies
└── settings.gradle.kts
```

---

## Skema Database

Database Room bernama `coffee_bliss_database` (versi 3) dengan tiga tabel:

### Tabel `members`

| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | Long (PK, autoGenerate) | ID unik member |
| `name` | String | Nama lengkap member |
| `email` | String | Email (unik) |
| `phone` | String | Nomor HP |
| `memberNumber` | String | Format `MBRxxxxx` (auto-generate) |
| `points` | Int | Total poin yang dimiliki (default: 0) |
| `photoPath` | String? | Path foto profil lokal (nullable) |

### Tabel `transactions`

| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | Long (PK, autoGenerate) | ID transaksi |
| `memberId` | Long (FK → members.id) | Referensi ke member |
| `date` | Long | Timestamp Unix (millis) |
| `amount` | Long | Nominal pembelian (Rupiah) |
| `pointsEarned` | Int | Poin yang diperoleh dari transaksi ini |

### Tabel `redemptions`

| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | Long (PK, autoGenerate) | ID redemption |
| `memberId` | Long (FK → members.id) | Referensi ke member |
| `date` | Long | Timestamp Unix (millis) |
| `rewardName` | String | Nama reward yang ditukar |
| `pointsUsed` | Int | Poin yang digunakan |

> Kedua tabel `transactions` dan `redemptions` memiliki **Foreign Key** ke `members.id` dengan `ON DELETE CASCADE` — jika member dihapus, semua data transaksi dan redemption-nya ikut terhapus.

---

## Alur Navigasi

```
[Splash Screen]
      │
      ▼
[Home / Member List] ──────────────── [+ Tambah Member]
      │                                      │
      │ (tap member)                         │ (simpan)
      ▼                                      │
[Member Card / Detail] ◄───────────────────-┘
      │         │         │
      │         │         └──► [Edit Profil]
      │         │
      │         └──────────► [Reward / Redeem]
      │
      └──────────────────► [Transaksi]
```

**Route yang tersedia:**

| Route | Deskripsi |
|---|---|
| `splash` | Splash screen awal |
| `home` | Daftar seluruh member |
| `add_member` | Form pendaftaran member baru |
| `member_card/{memberId}` | Kartu member digital |
| `edit_profile/{memberId}` | Edit data & foto profil |
| `transaction/{memberId}` | Input transaksi pembelian |
| `reward/{memberId}` | Katalog & redeem reward |

---

## Aturan Bisnis Poin & Reward

### Kalkulasi Poin

```
Poin = Nominal Pembelian (Rp) ÷ Rp10.000

Contoh:
  Rp 50.000  → 5 poin
  Rp 100.000 → 10 poin
  Rp 150.000 → 15 poin
  Rp 75.500  → 7 poin (dibulatkan ke bawah)
```

### Katalog Reward

| Poin Dibutuhkan | Reward | Deskripsi |
|---|---|---|
| 50 poin | Espresso | Espresso gratis 1 gelas |
| 100 poin | Cappuccino | Cappuccino gratis 1 gelas |
| 150 poin | Latte Gratis | Latte gratis 1 gelas |

### Validasi Redeem
- Tombol *Redeem* hanya bisa ditekan jika `poin_member >= poin_reward`.
- Setelah berhasil redeem, poin dikurangi: `poin_baru = poin_lama - poin_reward`.

---

## Cara Menjalankan

### Prasyarat

- **Android Studio** Hedgehog (2023.1.1) atau versi lebih baru
- **JDK 17**
- **Android SDK** dengan API Level 26–34 terinstall
- Emulator atau perangkat fisik Android (min Android 8.0)

### Langkah Menjalankan

**1. Clone repository**
```bash
git clone https://github.com/ulil1401/EAS_PPB.git
cd EAS_PPB
```

**2. Buka di Android Studio**
- Pilih **File → Open** → arahkan ke folder `EAS_PPB`
- Tunggu proses **Gradle Sync** selesai (membutuhkan koneksi internet pertama kali)

**3. Jalankan aplikasi**
- Pilih emulator atau perangkat fisik dari dropdown di toolbar
- Klik tombol **Run ▶** (Shift+F10)

**4. Build APK (opsional)**
```bash
# Windows
gradlew.bat assembleDebug

# Mac / Linux
./gradlew assembleDebug
```
APK output: `app/build/outputs/apk/debug/app-debug.apk`

---

## Dependensi

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Core & Lifecycle
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// QR Code Generator
implementation("com.google.zxing:core:3.5.3")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")
```

---

## Panduan Penggunaan

### 1. Splash Screen
Saat aplikasi pertama kali dibuka, akan muncul splash screen dengan logo **Coffee Bliss** selama beberapa detik sebelum otomatis berpindah ke halaman utama.

### 2. Home — Daftar Member
- Menampilkan **total member** terdaftar di bagian atas.
- Daftar member berisi nama, nomor member, dan total poin masing-masing.
- Jika belum ada member, tampil panduan *"Tap + untuk tambah member"*.
- Tekan **tombol +** (FAB) di pojok kanan bawah untuk mendaftarkan member baru.
- Tap salah satu member untuk melihat detail kartu member-nya.

### 3. Tambah Member
- Isi field **Nama**, **Email**, dan **Nomor HP**.
- Semua field wajib diisi — sistem akan menampilkan pesan error jika ada yang kosong.
- Email harus **unik** — tidak boleh sama dengan member yang sudah terdaftar.
- Tekan **Simpan** → nomor member otomatis dibuat, member tersimpan, dan langsung diarahkan ke halaman kartu member.

### 4. Kartu Member Digital
- Menampilkan: nama, nomor member (format `MBRxxxxx`), total poin, dan **QR Code** unik.
- Tombol **Transaksi** → untuk mencatat pembelian dan menambah poin.
- Tombol **Reward** → untuk melihat katalog dan melakukan redeem poin.
- Ikon **Edit** (pensil) di kanan atas → untuk mengubah data profil member.
- Menampilkan riwayat transaksi dan redemption di bagian bawah.

### 5. Tambah Transaksi
- Masukkan **nominal pembelian** dalam Rupiah.
- Aplikasi menampilkan **preview poin** yang akan didapat secara langsung.
- Tekan **Simpan** → transaksi tersimpan dan poin ditambahkan ke member.

### 6. Redeem Reward
- Menampilkan katalog reward: **Espresso (50 poin)**, **Cappuccino (100 poin)**, **Latte (150 poin)**.
- Tombol *Redeem* aktif hanya jika poin mencukupi.
- Setelah konfirmasi redeem → poin berkurang dan riwayat redemption tersimpan.

### 7. Edit Profil
- Ubah **nama**, **email**, dan **nomor HP**.
- Tap foto profil untuk memilih gambar dari **galeri** perangkat.
- Tekan **Simpan Perubahan** untuk memperbarui data.

---

## Informasi Tim

| | |
|---|---|
| **Mata Kuliah** | Pemrograman Mobile C |
| **Institusi** | Institut Teknologi Sepuluh Nopember (ITS) Surabaya |
| **Jenis Tugas** | Evaluasi Akhir Semester (EAS) |
| **Nama Aplikasi** | Coffee Bliss — Digital Membership Card |
| **Package ID** | `com.coffeebliss.app` |
| **Platform** | Android (Native Kotlin) |

---

<p align="center">
  Dibuat untuk EAS Pemrograman Mobile C — ITS Surabaya
</p>

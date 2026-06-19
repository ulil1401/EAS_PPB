# Demo Presentasi Coffee Bliss

## Slide 1 - Judul
**Coffee Bliss**
Aplikasi Kartu Member Digital untuk program loyalitas kopi.

## Slide 2 - Masalah
- Pencatatan member manual memakan waktu
- Sulit melacak poin dan reward
- Tidak ada kartu digital yang mudah dibawa

## Slide 3 - Solusi
Coffee Bliss menyediakan:
- Daftar member digital
- Kartu member dengan QR Code
- Perhitungan poin otomatis
- Redeem reward langsung di aplikasi

## Slide 4 - Fitur Utama
- Splash screen keren
- Home member list + total member
- Form tambah member
- Kartu member dengan poin dan QR Code
- Transaksi otomatis menghitung poin
- Reward redeem dengan batas poin
- Edit profil member

## Slide 5 - Arsitektur
- UI: Jetpack Compose
- ViewModel: MVVM
- Repository: data flow dari Room Database
- Database: Room dengan tabel member, transaction, redemption

## Slide 6 - Database
- `members` (id, name, email, phone, memberNumber, points, photoPath)
- `transactions` (id, memberId, date, amount, pointsEarned)
- `redemptions` (id, memberId, date, rewardName, pointsUsed)

## Slide 7 - User Flow
1. Splash → Home
2. Tambah member
3. Buka kartu member
4. Lakukan transaksi
5. Tukar reward

## Slide 8 - Demo Langsung
1. Buka aplikasi
2. Tambah member baru
3. Pilih member dan lihat kartu digital
4. Tambah transaksi untuk menambah poin
5. Buka reward dan redeem hadiah

## Slide 9 - Output Proyek
- Source code Kotlin lengkap
- Implementasi Room Database
- UI Jetpack Compose
- Arsitektur MVVM
- APK debug tersedia di `app/build/outputs/apk/debug/app-debug.apk`
- User manual: `docs/UserManual.md`
- Presentasi demo: `docs/PresentationDemo.md`

## Slide 10 - Catatan Teknis
- Minimum SDK: 26
- Compile SDK: 34
- Gradle wrapper: `gradle/wrapper/gradle-wrapper.properties`
- Java yang kompatibel: JDK 17

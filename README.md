
Coffee Bliss - Digital Membership Card
Aplikasi Android modern untuk coffee shop Coffee Bliss — kartu member digital berbasis Android.
Tech Stack
•
Kotlin
•
Jetpack Compose
•
Room Database
•
MVVM Architecture
•
Navigation Compose
•
Material 3 Design
Fitur
Fitur
Deskripsi
Registrasi Member
Input nama, email, no HP → disimpan ke Room DB
Login
Autentikasi dengan email + no HP
Kartu Member Digital
Nama, nomor member, status, total poin, QR Code
Riwayat Transaksi
Tanggal, nominal pembelian, poin didapat
Tambah Transaksi
Input nominal → poin dihitung otomatis
Redeem Poin
Tukar poin dengan reward
Aturan Poin
•
Rp10.000 = 1 Poin
•
Contoh: Pembelian Rp150.000 → 15 Poin
Daftar Reward
Poin
Reward
50
Espresso
100
Cappuccino
150
Latte Gratis
Struktur Project (MVVM)
app/src/main/java/com/coffeebliss/app/
├── data/
│   ├── model/       # Entity Room (Member, Transaction, Redemption)
│   ├── dao/         # Data Access Object
│   ├── database/    # Room Database
│   └── repository/  # Repository pattern
├── viewmodel/       # AuthViewModel, HomeViewModel, RedeemViewModel
├── ui/
│   ├── screens/     # Compose UI screens
│   ├── components/  # Reusable UI components
│   └── theme/       # Material 3 theme
├── navigation/      # Navigation Compose
└── util/            # PointCalculator, SessionManager, Formatters
Cara Menjalankan
1.
Buka folder project di Android Studio (Hedgehog atau lebih baru)
2.
Tunggu Gradle sync selesai
3.
Jalankan di emulator atau perangkat Android (min SDK 26)
4.
Atau via terminal: gradlew.bat assembleDebug
Alur Penggunaan
1.
Daftar — isi nama, email, dan no HP
2.
Login — masuk dengan email + no HP
3.
Kartu — lihat kartu member digital & QR Code
4.
Transaksi — tambah nominal pembelian untuk dapat poin
5.
Riwayat — lihat history transaksi & redeem
6.
Redeem — tukar poin dengan reward kopi
Login Demo
Setelah registrasi, gunakan email dan no HP yang sama untuk login kembali.

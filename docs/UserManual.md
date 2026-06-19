# Coffee Bliss - User Manual

## 1. Overview
Coffee Bliss adalah aplikasi kartu member digital yang dibangun dengan Kotlin, Jetpack Compose, Room Database, dan arsitektur MVVM.

## 2. Fitur Utama
- Splash screen untuk tampilan awal
- Home screen menampilkan daftar member dan jumlah total member
- Tambah member baru dengan nama, email, dan nomor HP
- Member card dengan ID member, QR code, poin, dan tombol navigasi
- Transaksi untuk menghitung poin dari pembelian
- Reward untuk menukarkan poin menjadi hadiah
- Edit profil member termasuk foto profil

## 3. Aturan Poin
- Rp 10.000 = 1 poin
- 50 poin → Espresso
- 100 poin → Cappuccino
- 150 poin → Latte gratis

## 4. Flow Aplikasi
1. Buka aplikasi.
2. Splash muncul selama beberapa detik.
3. Di Home, lihat daftar member dan total member.
4. Tekan tombol + untuk menambah member baru.
5. Isi Nama, Email, dan No HP lalu simpan.
6. Tap member untuk membuka kartu member.
7. Di halaman kartu member, pilih "Transaksi" untuk memasukkan nominal pembelian.
8. Poin akan dihitung otomatis dari nominal pembelian.
9. Pilih "Reward" untuk melihat hadiah yang tersedia dan lakukan redeem jika poin cukup.
10. Gunakan "Edit Profil" untuk memperbarui data member.

## 5. Tata Cara Menggunakan Setiap Layar
### Home
- Menampilkan nama member, nomor member, dan total poin.
- Jika belum ada member, tampil teks panduan "Tap + untuk tambah member".
- Tap ikon + untuk menambah member baru.
- Tap kartu member untuk membuka detail kartu member.

### Tambah Member
- Isi semua field Nama, Email, dan No HP.
- Aplikasi memvalidasi bahwa semua field wajib diisi.
- Email tidak boleh sama dengan member lain yang sudah terdaftar.
- Tekan "Simpan" untuk menyimpan member.

### Kartu Member
- Menampilkan detail member, jumlah poin, dan QR Code.
- Ada tombol "Transaksi" untuk menambah poin.
- Ada tombol "Reward" untuk menukarkan poin.
- Ada tombol edit profil di kanan atas.

### Transaksi
- Masukkan nominal pembelian dalam Rupiah.
- Aplikasi menghitung poin yang diperoleh otomatis.
- Tekan "Simpan" untuk menambah transaksi dan menambahkan poin ke member.

### Reward
- Menampilkan daftar reward berdasarkan threshold poin.
- Tombol "Redeem" hanya aktif jika poin member cukup.
- Setelah redeem berhasil, poin akan berkurang sesuai reward.

### Edit Profil
- Ubah nama, email, dan nomor HP.
- Pilih foto profil dari galeri.
- Tekan "Simpan Perubahan" untuk memperbarui data member.

## 6. Struktur Database
- `members`: menyimpan informasi member.
- `transactions`: menyimpan riwayat transaksi member.
- `redemptions`: menyimpan riwayat redeem reward.

## 7. File Penting
- `app/src/main/java/com/coffeebliss/app/ui/screens/` → UI Compose
- `app/src/main/java/com/coffeebliss/app/viewmodel/` → MVVM ViewModel
- `app/src/main/java/com/coffeebliss/app/data/database/` → Room database
- `app/src/main/java/com/coffeebliss/app/data/dao/` → Data access objects
- `app/src/main/java/com/coffeebliss/app/data/repository/` → Repository layer

## 8. Cara Menjalankan Aplikasi
1. Buka proyek di Android Studio.
2. Sinkronkan Gradle.
3. Jalankan `app` pada emulator atau perangkat.
4. Jika ingin menghasilkan APK, jalankan:
   - `./gradlew.bat app:assembleDebug`

## 9. Lokasi APK
- `app/build/outputs/apk/debug/app-debug.apk`

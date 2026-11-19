# PACIANA Android

Aplikasi Android native berbasis Kotlin dan Jetpack Compose. Versi prototipe: 0.1.0.

## Menjalankan proyek
1. Gunakan Android Studio stabil dengan dukungan Android Gradle Plugin 8.7.3.
2. Buka folder repositori PACIANA di Android Studio.
3. Tunggu Gradle Sync selesai (internet diperlukan untuk dependensi).
4. Jalankan pada emulator atau perangkat Android 8.0 (API 26) ke atas.

## Fitur yang tersedia pada fondasi saat ini
- Welcome page, beranda, dan navigasi dasar.
- Katalog demonstrasi (belum berisi katalog DCO).
- Pembaca teks demonstrasi.
- Ekspor teks demonstrasi ke TXT UTF-8 dan pembacaan offline.
- Pemilih berkas PDF/gambar untuk alur OCR (pemrosesan OCR belum aktif).
- Preferensi bahasa antarmuka tersimpan secara lokal menggunakan Preferences DataStore.
- Catatan penelitian tersimpan secara lokal dan bertahan setelah aplikasi ditutup.

## Belum diimplementasikan
- Katalog daring terkurasi dan tautan ke halaman dokumen Documenta Catholica Omnia.
- OCR aktual untuk Latin dan Yunani poltonik.
- Penerjemahan AI melalui backend yang aman.
- Database katalog terstruktur dengan Room.
- Akun, database cloud, dan sinkronisasi multi-perangkat.
- Pengelolaan edisi, metadata bibliografis, ekspor sitasi, dan anotasi dokumen.
- Ikon launcher final dan pengujian otomatis menyeluruh.

## Batasan integrasi Documenta Catholica Omnia
PACIANA harus menautkan ke halaman HTML per dokumen (bukan menanam mirror atau mengunduh koleksi secara massal). Setiap entri perlu mencatat sumber, editor/kompiler, edisi, dan status hak penggunaan. Ikuti ketentuan DCO dan hukum hak cipta yang berlaku.

## Keamanan
Jangan menyimpan API key AI atau kredensial backend rahasia di APK maupun repositori publik. Panggilan AI harus melalui fungsi backend yang memverifikasi autentikasi dan membatasi penggunaan.

## Identitas
- Application ID: `id.paciana.app`
- Minimum SDK: 26
- Target SDK: 35
- Warna: Parchment `#F0E8D8`, Ivory Mist `#E7DDCC`, Old Paper `#DDD1BD`, Dark Brown `#40372E`, Walnut `#655747`, Stone Grey `#897F70`, Antique Gold `#A88C5D`, Muted Bronze `#8C7655`, Aged Stone `#B9AA91`, Espresso `#302820`.

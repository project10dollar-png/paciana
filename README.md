# PACIANA Android

Proyek Android native (Kotlin + Jetpack Compose), versi prototipe 0.1.0.

## Membuka dan menjalankan
1. Pasang Android Studio versi stabil yang mendukung Android Gradle Plugin 8.7.3.
2. Ekstrak ZIP ini.
3. Android Studio → Open → pilih folder `PACIANA_Android`.
4. Tunggu Gradle Sync selesai (internet diperlukan untuk mengunduh dependensi).
5. Hubungkan perangkat Android dengan USB debugging aktif atau buat emulator.
6. Tekan Run.

Target SDK 35, minimum SDK 26 (Android 8.0).

## Fitur yang sudah berfungsi
- Welcome page dan navigasi dasar.
- Beranda, katalog contoh, pencarian katalog contoh.
- Pembaca teks contoh dengan tab teks asli/Indonesia.
- Simpan teks sebagai TXT UTF-8 ke penyimpanan internal aplikasi.
- Daftar unduhan dan membaca TXT secara offline di dalam aplikasi.
- Pengaturan pilihan bahasa (pilihan tersimpan sementara selama aplikasi berjalan).
- Halaman OCR, penerjemahan, dan catatan sebagai kerangka antarmuka.

## Belum terhubung
- Katalog daring dan sumber Documenta Catholica Omnia.
- OCR sungguhan dan penyedia OCR.
- Model/layanan AI penerjemah.
- Sinkronisasi, database daring, penyimpanan catatan permanen, ekspor sitasi.
- Logo final sebagai aset launcher dan identitas.

Fitur online belum diaktifkan karena belum dipilih penyedia yang mempunyai akses resmi, API yang sesuai, dan ketentuan biaya/izin yang dapat diterima. Aplikasi tidak mengirim dokumen atau teks ke pihak ketiga pada versi ini.

## Identitas
Paket: `id.paciana.app`
Warna resmi mengikuti spesifikasi PACIANA:
Parchment #F0E8D8, Ivory Mist #E7DDCC, Old Paper #DDD1BD,
Dark Brown #40372E, Walnut #655747, Stone Grey #897F70,
Antique Gold #A88C5D, Muted Bronze #8C7655, Aged Stone #B9AA91,
Espresso #302820.

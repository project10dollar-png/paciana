package id.paciana.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.documentfile.provider.DocumentFile
import java.io.File

private val Parchment = Color(0xFFF0E8D8)
private val IvoryMist = Color(0xFFE7DDCC)
private val OldPaper = Color(0xFFDDD1BD)
private val DarkBrown = Color(0xFF40372E)
private val Walnut = Color(0xFF655747)
private val StoneGrey = Color(0xFF897F70)
private val AntiqueGold = Color(0xFFA88C5D)
private val MutedBronze = Color(0xFF8C7655)
private val AgedStone = Color(0xFFB9AA91)
private val Espresso = Color(0xFF302820)

private data class Work(val author: String, val title: String, val period: String, val language: String, val sample: String)

private val sampleWorks = listOf(
    Work("Pacianus dari Barcelona", "Paraenesis ad Paenitentiam", "Abad IV", "Latin",
        "Fratres carissimi, paenitentia medicina est animae.\\n\\n[Contoh teks untuk memperlihatkan tata letak pembaca. Teks kritis atau transkripsi sumber yang sesungguhnya akan dimuat setelah sumber dan hak penggunaannya diverifikasi.]"),
    Work("Pacianus dari Barcelona", "Epistulae", "Abad IV", "Latin",
        "[Contoh teks Epistulae Paciani. Teks sumber sesungguhnya belum terhubung.]"),
    Work("Agustinus dari Hippo", "De Civitate Dei", "Abad V", "Latin",
        "[Contoh teks De Civitate Dei. Teks sumber sesungguhnya belum terhubung.]"),
    Work("Basilius Agung", "Regulae fusius tractatae", "Abad IV", "Yunani",
        "[Contoh teks Yunani. Teks sumber sesungguhnya belum terhubung.]")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PacianaApp() }
    }
}

@Composable
private fun PacianaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = MutedBronze,
            onPrimary = Parchment,
            secondary = AntiqueGold,
            onSecondary = DarkBrown,
            background = Parchment,
            surface = IvoryMist,
            onSurface = DarkBrown,
            outline = AgedStone
        ),
        content = content
    )
}

@Composable
private fun PacianaApp() {
    var page by remember { mutableStateOf("welcome") }
    var selected by remember { mutableStateOf(sampleWorks.first()) }
    var query by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("Bahasa Indonesia") }
    var readerTab by remember { mutableStateOf(0) }
    var note by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val context = LocalContext.current

    PacianaTheme {
        Surface(Modifier.fillMaxSize(), color = Parchment) {
            Column(Modifier.fillMaxSize()) {
                if (page != "welcome") TopBrand()
                Box(Modifier.weight(1f)) {
                    when (page) {
                        "welcome" -> WelcomePage { page = "home" }
                        "home" -> HomePage(
                            onLibrary = { page = "library" },
                            onOcr = { page = "ocr" },
                            onTranslate = { page = "translate" },
                            onDownloads = { page = "downloads" },
                            onNotes = { page = "notes" },
                            onAbout = { page = "about" }
                        )
                        "library", "search" -> LibraryPage(
                            query = query, onQuery = { query = it },
                            onOpen = { selected = it; readerTab = 0; page = "reader" }
                        )
                        "reader" -> ReaderPage(
                            work = selected, tab = readerTab, onTab = { readerTab = it },
                            onDownload = {
                                val fileName = selected.title.replace(Regex("[^A-Za-z0-9._-]"), "_") + ".txt"
                                val file = File(context.filesDir, fileName)
                                file.writeText(
                                    "${selected.title}\\n${selected.author}\\n${selected.period}\\n\\n${selected.sample}\\n",
                                    Charsets.UTF_8
                                )
                                status = "Tersimpan sebagai $fileName"
                            },
                            onTranslate = { page = "translate" },
                            status = status
                        )
                        "downloads" -> DownloadsPage(context)
                        "ocr" -> FeaturePage(
                            title = "OCR Dokumen",
                            description = "Pilih PDF atau gambar halaman untuk menyiapkan proses pengenalan teks.",
                            button = "Pilih dokumen",
                            onClick = { status = "Pemilih dokumen dibuka." },
                            status = status,
                            onChooseFile = { page = "ocr" }
                        )
                        "translate" -> TranslatePage()
                        "notes" -> NotesPage(note, { note = it })
                        "settings" -> SettingsPage(language, { language = it })
                        else -> AboutPage()
                    }
                }
                if (page != "welcome") BottomNavigation(page) { page = it; status = "" }
            }
        }
    }
}

@Composable
private fun TopBrand() {
    Column(Modifier.fillMaxWidth().background(Espresso).padding(horizontal = 20.dp, vertical = 13.dp)) {
        Text("PACIANA", color = Parchment, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, letterSpacing = 2.sp)
        Text("PATRISTICA CIVITAS INDONESIANA", color = Color(0xFFD6C5A8), fontSize = 9.sp, letterSpacing = 1.5.sp)
    }
}

@Composable
private fun WelcomePage(onEnter: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Parchment).padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size(220.dp).border(2.dp, AntiqueGold, CircleShape).padding(9.dp).border(1.dp, AgedStone, CircleShape), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Add, null, tint = MutedBronze, modifier = Modifier.size(38.dp))
                Text("PACIANA", color = DarkBrown, fontSize = 29.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, letterSpacing = 2.sp)
                Text("MMXXVI", color = Walnut, fontSize = 12.sp, letterSpacing = 2.sp)
            }
        }
        Spacer(Modifier.height(28.dp))
        Text("PER FONTES AD VERITATEM", color = DarkBrown, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif, textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text("Perpustakaan digital untuk mengenal, membaca, dan meneliti warisan para Bapa Gereja.", color = Walnut, textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 21.sp)
        Spacer(Modifier.height(22.dp))
        HorizontalDivider(color = AgedStone)
        Spacer(Modifier.height(14.dp))
        Text("HUMILITAS  •  FIDELITAS  •  SIMPLICITAS", color = MutedBronze, fontSize = 11.sp, letterSpacing = 1.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(30.dp))
        Button(onClick = onEnter, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MutedBronze), shape = RoundedCornerShape(8.dp)) {
            Text("MASUK KE PERPUSTAKAAN", color = Parchment, letterSpacing = 1.sp)
        }
        TextButton(onClick = onEnter) { Text("Tentang PACIANA", color = Walnut) }
    }
}

@Composable
private fun HomePage(onLibrary: () -> Unit, onOcr: () -> Unit, onTranslate: () -> Unit, onDownloads: () -> Unit, onNotes: () -> Unit, onAbout: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Selamat datang", fontSize = 25.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = DarkBrown)
            Text("Jelajahi sumber-sumber patristik dari berbagai tradisi Gereja.", color = Walnut)
        }
        item { SearchBar(value = "", onValue = {}, placeholder = "Cari pengarang atau karya…", enabled = false, onClick = onLibrary) }
        item { Text("Jelajahi koleksi", color = DarkBrown, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    HomeTile("Bapa Gereja Latin", "Karya berbahasa Latin", Icons.Default.MenuBook, onLibrary, Modifier.weight(1f))
                    HomeTile("Bapa Gereja Yunani", "Karya berbahasa Yunani", Icons.Default.Book, onLibrary, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    HomeTile("Konsili dan Sinode", "Dokumen gerejawi", Icons.Default.AccountBalance, onLibrary, Modifier.weight(1f))
                    HomeTile("Dokumen Gereja", "Surat dan dekret", Icons.Default.Description, onLibrary, Modifier.weight(1f))
                }
            }
        }
        item { Text("Akses cepat", color = DarkBrown, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickRow("Unduhan Saya", "Baca teks secara offline", Icons.Default.Download, onDownloads)
                QuickRow("OCR Dokumen", "Ekstraksi teks dari PDF dan gambar", Icons.Default.DocumentScanner, onOcr)
                QuickRow("Penerjemah AI", "Latin dan Yunani Kuno ke Indonesia", Icons.Default.Translate, onTranslate)
                QuickRow("Ruang Penelitian", "Catatan dan anotasi", Icons.Default.EditNote, onNotes)
                QuickRow("Tentang PACIANA", "Identitas dan motto", Icons.Default.Info, onAbout)
            }
        }
    }
}

@Composable
private fun HomeTile(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = OldPaper), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, null, tint = MutedBronze, modifier = Modifier.size(26.dp))
            Spacer(Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.SemiBold, color = DarkBrown)
            Text(subtitle, fontSize = 11.sp, color = Walnut)
        }
    }
}

@Composable
private fun QuickRow(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = IvoryMist), shape = RoundedCornerShape(10.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MutedBronze)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = DarkBrown, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = StoneGrey, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Walnut)
        }
    }
}

@Composable
private fun LibraryPage(query: String, onQuery: (String) -> Unit, onOpen: (Work) -> Unit) {
    var localQuery by remember { mutableStateOf(query) }
    val filtered = sampleWorks.filter { (it.author + it.title).contains(localQuery, ignoreCase = true) }
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Perpustakaan", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(localQuery, { localQuery = it; onQuery(it) }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Cari pengarang atau judul…") }, singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) })
        Spacer(Modifier.height(12.dp))
        Text("CONTOH KATALOG", color = StoneGrey, fontSize = 10.sp, letterSpacing = 1.2.sp)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(filtered) { work ->
                Card(Modifier.fillMaxWidth().clickable { onOpen(work) }, colors = CardDefaults.cardColors(containerColor = IvoryMist)) {
                    Column(Modifier.padding(15.dp)) {
                        Text("${work.author} · ${work.period}", color = Walnut, fontSize = 12.sp)
                        Text(work.title, color = DarkBrown, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
                        Text(work.language, color = StoneGrey, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReaderPage(work: Work, tab: Int, onTab: (Int) -> Unit, onDownload: () -> Unit, onTranslate: () -> Unit, status: String) {
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("PEMBACA TEKS", color = StoneGrey, fontSize = 10.sp, letterSpacing = 1.2.sp)
        Text(work.title, color = DarkBrown, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("${work.author} · ${work.period}", color = Walnut, fontSize = 13.sp)
        TabRow(selectedTabIndex = tab, containerColor = Parchment, contentColor = MutedBronze) {
            Tab(selected = tab == 0, onClick = { onTab(0) }, text = { Text("Teks asli") })
            Tab(selected = tab == 1, onClick = { onTab(1) }, text = { Text("Indonesia") })
        }
        Card(Modifier.weight(1f).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = IvoryMist)) {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text(if (tab == 0) "TEXTUS ORIGINALIS" else "TERJEMAHAN INDONESIA", color = MutedBronze, fontSize = 10.sp, letterSpacing = 1.sp)
                Spacer(Modifier.height(12.dp))
                Text(if (tab == 0) work.sample else "Terjemahan belum tersedia. Hubungkan penyedia AI penerjemah online untuk menghasilkan terjemahan yang dapat diverifikasi.", color = DarkBrown, fontSize = 16.sp, lineHeight = 25.sp)
            }
        }
        Button(onClick = onDownload, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MutedBronze)) {
            Icon(Icons.Default.Download, null); Spacer(Modifier.width(8.dp)); Text("Simpan teks sebagai TXT")
        }
        OutlinedButton(onClick = onTranslate, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Translate, null); Spacer(Modifier.width(8.dp)); Text("Penerjemah AI") }
        if (status.isNotBlank()) Text(status, color = MutedBronze, fontSize = 12.sp)
    }
}

@Composable
private fun DownloadsPage(context: Context) {
    var files by remember { mutableStateOf(downloadedFiles(context)) }
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Unduhan Saya", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("Teks TXT yang tersimpan di perangkat.", color = Walnut)
        Spacer(Modifier.height(14.dp))
        if (files.isEmpty()) {
            Text("Belum ada teks yang diunduh. Buka sebuah karya, lalu pilih “Simpan teks sebagai TXT”.", color = StoneGrey)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(files) { file ->
                    Card(colors = CardDefaults.cardColors(containerColor = IvoryMist), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(13.dp)) {
                            Text(file.name, color = DarkBrown, fontWeight = FontWeight.SemiBold)
                            Text("${file.length()} bytes · tersimpan lokal", color = StoneGrey, fontSize = 11.sp)
                            var content by remember(file.name) { mutableStateOf<String?>(null) }
                            TextButton(onClick = { content = file.readText(Charsets.UTF_8) }) { Text(if (content == null) "Baca offline" else "Tutup teks") }
                            content?.let { Text(it, color = DarkBrown, fontSize = 13.sp, lineHeight = 20.sp) }
                        }
                    }
                }
            }
        }
    }
}

private fun downloadedFiles(context: Context): List<File> =
    context.filesDir.listFiles()?.filter { it.extension.equals("txt", true) }?.sortedBy { it.name } ?: emptyList()

@Composable
private fun FeaturePage(title: String, description: String, button: String, onClick: () -> Unit, status: String, onChooseFile: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(title, fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text(description, color = Walnut)
        Card(colors = CardDefaults.cardColors(containerColor = IvoryMist)) {
            Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudUpload, null, tint = MutedBronze, modifier = Modifier.size(42.dp))
                Spacer(Modifier.height(10.dp))
                Text("Pemrosesan online", color = DarkBrown, fontWeight = FontWeight.SemiBold)
                Text("Modul layanan akan dihubungkan setelah penyedia OCR dipilih.", color = StoneGrey, textAlign = TextAlign.Center, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onClick) { Text(button) }
            }
        }
        if (status.isNotBlank()) Text(status, color = MutedBronze)
        Text("Catatan: fitur ini belum melakukan OCR sungguhan. Tidak ada dokumen yang dikirim ke layanan pihak ketiga pada versi ini.", color = StoneGrey, fontSize = 12.sp)
    }
}

@Composable
private fun TranslatePage() {
    var source by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Penerjemah AI", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("Latin / Yunani Kuno → Bahasa Indonesia", color = Walnut)
        OutlinedTextField(source, { source = it }, modifier = Modifier.fillMaxWidth().weight(1f), label = { Text("Masukkan teks sumber") }, placeholder = { Text("Tempel teks Latin atau Yunani di sini…") })
        Button(onClick = { result = true }, modifier = Modifier.fillMaxWidth(), enabled = source.isNotBlank()) { Text("Siapkan terjemahan") }
        if (result) Text("Belum terhubung ke model AI. Penyedia akan dipilih setelah uji mutu, lisensi, privasi, dan biaya.", color = MutedBronze)
        Text("Penerjemahan belum aktif pada versi ini; tidak ada klaim hasil AI tanpa layanan yang terhubung.", color = StoneGrey, fontSize = 12.sp)
    }
}

@Composable
private fun NotesPage(note: String, onNote: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Ruang Penelitian", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("Catatan pribadi untuk pembacaan dan penelitian.", color = Walnut)
        OutlinedTextField(note, onNote, modifier = Modifier.fillMaxWidth().weight(1f), label = { Text("Catatan") }, placeholder = { Text("Tuliskan kutipan, gagasan, atau pertanyaan…") })
        Text("Catatan ini sementara tersimpan selama aplikasi terbuka. Penyimpanan permanen akan ditambahkan pada tahap berikutnya.", color = StoneGrey, fontSize = 12.sp)
    }
}

@Composable
private fun SettingsPage(language: String, onLanguage: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Pengaturan", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("Bahasa antarmuka", color = Walnut, fontWeight = FontWeight.SemiBold)
        listOf("Bahasa Indonesia", "English", "Italiano", "Français", "Español", "Deutsch").forEach {
            Row(Modifier.fillMaxWidth().clickable { onLanguage(it) }.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = language == it, onClick = { onLanguage(it) })
                Text(it, color = DarkBrown)
            }
        }
        Text("Bahasa teks sumber dan bahasa terjemahan diatur terpisah dari bahasa antarmuka.", color = StoneGrey, fontSize = 12.sp)
    }
}

@Composable
private fun AboutPage() {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Tentang PACIANA", fontSize = 25.sp, color = DarkBrown, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text("PER FONTES AD VERITATEM", fontSize = 19.sp, color = MutedBronze, fontFamily = FontFamily.Serif)
        Text("PACIANA — Patristica Civitas Indonesiana — adalah gagasan perpustakaan digital untuk mengakses, membaca, dan meneliti warisan literatur para Bapa Gereja.", color = DarkBrown, lineHeight = 23.sp)
        HorizontalDivider(color = AgedStone)
        Text("HUMILITAS  •  FIDELITAS  •  SIMPLICITAS", color = Walnut, fontWeight = FontWeight.SemiBold)
        Text("2026", color = StoneGrey)
    }
}

@Composable
private fun SearchBar(value: String, onValue: (String) -> Unit, placeholder: String, enabled: Boolean, onClick: () -> Unit) {
    OutlinedTextField(value, onValue, modifier = Modifier.fillMaxWidth().clickable { onClick() }, placeholder = { Text(placeholder) }, enabled = enabled, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
}

@Composable
private fun BottomNavigation(page: String, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = Espresso, contentColor = Parchment) {
        val items = listOf(
            Triple("home", "Beranda", Icons.Default.Home),
            Triple("library", "Katalog", Icons.Default.MenuBook),
            Triple("downloads", "Unduhan", Icons.Default.Download),
            Triple("settings", "Setelan", Icons.Default.Settings)
        )
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                selected = page == route,
                onClick = { onNavigate(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AntiqueGold,
                    selectedTextColor = Parchment,
                    unselectedIconColor = Color(0xFFC8B99F),
                    unselectedTextColor = Color(0xFFC8B99F),
                    indicatorColor = Walnut
                )
            )
        }
    }
}

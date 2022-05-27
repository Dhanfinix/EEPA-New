package com.dhandev.eepa.materi

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.util.Linkify
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dhandev.eepa.R
import com.dhandev.eepa.databinding.ActivityMateriPengantarBinding
import com.dhandev.eepa.helper.customTab
import com.dhandev.eepa.ui.imageViewer.ImageViewerMateriActivity
import me.saket.bettermovementmethod.BetterLinkMovementMethod


class MateriPengantar : AppCompatActivity() {

    private lateinit var binding: ActivityMateriPengantarBinding
    private lateinit var sharedPred : SharedPreferences
    var URL : String = "https://cds.cern.ch/images/CERN-PHOTO-201802-030-10/file?size=medium"
    var URL2 = "https://cdn.mos.cms.futurecdn.net/7cvrrJxBe3N4Pfsv8q9oaM.jpg"
    var page = 1
    var desc = "Large Hadron Collider (Penubruk Hadron Raksasa) adalah pemercepat partikel berenergi tinggi terbesar di dunia, fasilitas percobaan paling kompleks yang pernah dibangun, dan mesin tunggal terbesar di dunia."
    var desc2 = "Fasilitas-fasilitas yang ada di CERN terdiri dari LHC (Large Hadron Collider), SPS (Super Proton Synchrotron), dan PS (Proton Synchrotron)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_pengantar)

        binding = ActivityMateriPengantarBinding.inflate(layoutInflater)
        sharedPred = this.getSharedPreferences("Tampilan", MODE_PRIVATE)
        loadUkuranbaru()
        loadLatarBaru()

        val fragment: Fragment
        fragment = MiniQuizFragment()
        loadFragmentQuiz(fragment)

        binding.apply{
            setContentView(root)
            aturTeks.visibility = View.GONE

            binding.arrowBack.setOnClickListener {
                onBackPressed()
            }

            setText.setOnClickListener {
                if (aturTeks.isVisible){
                    aturTeks.visibility = View.GONE
                } else {
                    aturTeks.visibility = View.VISIBLE
                }
            }

            toggleGroup.addOnButtonCheckedListener{toggleGroup, checkedId, isChecked ->
                if (isChecked){
                    when(checkedId){
                        R.id.btnSmall -> gantiUkuran(R.style.FontParagrafSmall, toggleGroup.checkedButtonId)
                        R.id.btnMedium -> gantiUkuran(R.style.FontParagraf, toggleGroup.checkedButtonId)
                        R.id.btnLarge -> gantiUkuran(R.style.FontParagrafLarge, toggleGroup.checkedButtonId)
                    }
                } else {
                    if (toggleGroup.checkedButtonId == View.NO_ID){
                        gantiUkuran(R.style.FontParagraf, toggleGroup.checkedButtonId)
                    }
                }
            }

            toggleGroupColor.addOnButtonCheckedListener{toggleGroup, checkedId, isChecked ->
                if (isChecked){
                    when(checkedId){
                        R.id.btnGreen ->  gantiLatar(R.color.greenRead, toggleGroupColor.checkedButtonId)
                        R.id.btnPeach -> gantiLatar(R.color.peachRead, toggleGroupColor.checkedButtonId)
                        R.id.btnOrange -> gantiLatar(R.color.orangeRead, toggleGroupColor.checkedButtonId)
                    }
                } else {
                    if (toggleGroup.checkedButtonId == View.NO_ID){
                        gantiLatar(R.color.white, toggleGroupColor.checkedButtonId)
                    }
                }
            }

            glideImage()
            glideImage2()
            gambar.setOnClickListener {
                openImageViewer(URL, desc)
            }
            gambar2.setOnClickListener {
                openImageViewer(URL2, desc2)
            }

            btnNext.setOnClickListener {
                when(page){
                    1 -> openPageTwo()
                    2 -> openPageThree()
                    3 -> openPageFour()
                }
            }

            btnPrev.visibility = View.GONE
            btnPrev.setOnClickListener {
                when(page){
                    2 -> openPageOne()
                    3 -> openPageTwo()
                }
            }

//            body1.transformationMethod = LinkTransformationMethod()
//            body1.movementMethod = LinkMovementMethod.getInstance()

            //buat tittle berjalan
            title.text = getString(R.string._2_teori_medan_kuantum)
            title.ellipsize = TextUtils.TruncateAt.MARQUEE
            title.isSingleLine = true
            title.marqueeRepeatLimit = -1
            title.isSelected = true

            tvCaption1.text = getString(R.string.m_tmq_caption)
            tvCaption2.text = getString(R.string.m_tmq_caption2)
            Linkify.addLinks(body1, Linkify.ALL)
//            body2.movementMethod = BetterLinkMovementMethod.getInstance()
            body2.movementMethod = BetterLinkMovementMethod.newInstance().apply {
                setOnLinkClickListener { textView, url ->
                    customTab.open(this@MateriPengantar, url)
                    true
                }
                setOnLinkLongClickListener { textView, url ->
                    Toast.makeText(this@MateriPengantar, getString(R.string.link_hint), Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun openImageViewer(url : String, desc : String) {
        val Editor:SharedPreferences.Editor = sharedPred.edit()
        Editor.putString("url", url)
        Editor.putString("desc", desc)
        Editor.putInt("id", 1)
        Editor.apply()
        val intent = Intent(this, ImageViewerMateriActivity::class.java)
        startActivity(intent)
    }

    private fun glideImage() {
        Glide.with(applicationContext)
            .load(URL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.memuatgambar)
            .centerCrop()
            .into(binding.gambar)
    }
    private fun glideImage2() {
        Glide.with(applicationContext)
            .load(URL2)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.memuatgambar)
            .centerCrop()
            .into(binding.gambar2)
    }

    private fun openPageOne() {
        recreate()
        focusOnTop()
        binding.btnPrev.visibility = View.GONE
    }

    private fun openPageTwo() {
        page = 2
        binding.apply {
            btnPrev.visibility = View.VISIBLE
            focusOnTop()
            URL = "https://s3-us-west-2.amazonaws.com/courses-images/wp-content/uploads/sites/1941/2017/05/30162034/daltons-symbols.gif"
            desc = "Struktur kimia dari Dalton's A New System of Chemical Philosophy"
            glideImage()
            body1.text = "\t Sayangnya, selama berabad-abad berlalu tidak ada perkembangan secara saintifik yang berarti mengenai atom karena sebagian besar filsuf sepakat dengan konsep Aristoteles. Baru pada abad ke-17, tepatnya tahun 1661, Robert Boyle memaparkan diskusinya mengenai atom pada literatur ilmiah yang berjudul The Sceptical Chymist. Namun, ahli kimia dan meteorologi Inggris John Dalton dikreditkan dengan teori atom modern pertama, seperti yang dijelaskan dalam bukunya A New System of Chemical Philosophy yang terbit pada tahun 1808 (bagian I) dan 1810 (bagian II). \tEksperimen Dalton dengan menggunakan gas tercatat sebagai pengukuran paling awal untuk mengetahui massa atom, konsep struktur atom, dan reaktivitas. Secara garis besar, Dalton menjelaskan bahwa:\n1. Semua atom dari unsur tertentu adalah identik\n2. Massa dan ukuran atom berbeda antara unsur satu dan yang lainnya\n3. Atom tidak bisa dihancurkan. Reaksi kimia bisa mengakibatkan penyusunan kembali, tapi tidak akan menghancurkan atau membuat atom baru."
        }
    }

    private fun openPageThree() {
        page = 3
        binding.apply {
            btnPrev.visibility = View.VISIBLE
            focusOnTop()
            URL = "https://cdn.kastatic.org/ka-perseus-images/79d080efbc996be3a96eb98195f77e5df00e8f95.png"
            desc = "Diagram tabung sinar katoda yang digunakan oleh J.J.Thomson ketika menemukan elektron"
            glideImage()
            val text = getString(R.string.bullet_3)
//            val spannableString = SpannableString(text)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                spannableString.setSpan(BulletSpan(40, getColor(R.color.blue_500), 20), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
            body1.text = "\t Pada akhir abad ke-19, tepatnya pada tahun 1897, J.J.Thomson bereksperimen dengan alat tabung sinar katoda (cathode ray tubes). Tabung sinar katoda adalah tabung gelas yang tertutup dari adanya udara masuk. Tegangan tinggi diberikan pada elektroda di salah satu ujung tabung, hal ini menyebabkan terpancarnya partikel dari katoda  (elektroda bermuatan negatif) ke anoda (elektroda bermuatan positif). Tabung ini disebut dengan tabung sinar katoda karena pancaran partikel (sinar katoda) berasal dari katoda. Sinar katoda dapat dideteksi dengan mewarnai ujung jauh tabung dari anoda dengan menggunakan fosfor. Fosfor tersebut akan memercik atau memancarkan cahaya ketika terkena sinar katoda. Untuk lebih jelas, perhatikan gambar pada atas halaman ini. " +
                    "\n\t Untuk menguji properti sinar katoda, Thomson meletakkan dua plat dengan muatan berlawanan di sekitar sinar katoda. Hasilnya, sinar katoda dibelokkan menjauhi plat bermuatan negatif dan mengarah ke plat bermuatan positif. Hal ini menunjukkan bahwa sinar katoda tersusun dari partikel bermuatan negatif." +
                    "\n\t Thomson juga meletakkan dua magnet di sisi lain tabung, kemudian mengamati bahwa magnet tersebut juga menyebabkan sinar katoda membelok. Hasil tersebut membantu Thomson untuk menentukan rasio massa terhadap muatan partikel sinar katoda, yang kemudian membawa pada penemuan besar yaitu setiap partikel sinar katoda memiliki massa yang jauh lebih kecil daripada atom lain yang diketahui. Karena merasa kurang yakin, Thomson juga menerapkan elektroda dari jenis logam lain dan hasilnya tetap saja sama. Dari temuannya tersebut, Thomson menyimpulkan:" +
                    text +
                    "\n\t Walaupun pada awalnya kontroversial, penemuan Thomson secara perlahan dapat diterima oleh pada ilmuwan. Pada akhirnya, partikel sinar katoda temuannya diberikan nama yang kini kita kenal dengan “elektron”. Adanya temuan Thomson menyangkal teori atom Dalton yang menyatakan bahwa atom tidak dapat dibagi lagi. Untuk menjelaskan keberadaan elektron, diperlukan suatu model atom yang baru."
            URL2 = "https://docs.google.com/uc?id=1yXr5duPTPbNz2vTlwuA5IQC21l5lrRN1"
            desc2 = "Model atom Thomson menunjukkan bahwa elektron tersebar didalam lautan muatan positif. Dianalogikan seperti kismis pada roti kismis (plum pudding)"
            glideImage2()
            gambar2.visibility = View.VISIBLE
            body2.visibility = View.VISIBLE
            body2.text = "\t Thomson tahu bahwa atom bermuatan netral, oleh karena itu Ia menganggap bahwa harus ada sumber dengan muatan positif pada atom untuk mengimbagi muatan negatif elektron. Hal ini mendorong Thomson pada tahun 1904 untuk mengajukan model bahwa atom dapat dideskripsikan sebagai partikel negatif yang mengambang di dalam sup bermuatan positif. Model ini dikenal dengan model roti kismis (plum pudding), karena deskripsi atom tersebut sangat mirip dengan roti kismis."
            containerQuiz.visibility = View.VISIBLE
        }
    }

    private fun openPageFour() {
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
    }

    private fun focusOnTop() {
        binding.latar.isFocusableInTouchMode = true
        binding.latar.smoothScrollTo(0,0)
        binding.latar.fullScroll(View.FOCUS_UP)
    }

    private fun loadFragmentQuiz(fragment: MiniQuizFragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerQuiz, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    private fun gantiLatar(greenRead: Int, pressed: Int) {
        val latarBaru: Int = greenRead
        val tombol: Int = pressed
        val Editor:SharedPreferences.Editor = sharedPred.edit()
        Editor.putInt("gantiLatar", latarBaru)
        Editor.putInt("tombolTerpilih", tombol)
        Editor.apply()
        loadLatarBaru()
    }

    private fun loadLatarBaru() {
        val sharedLatarId = sharedPred.getInt("gantiLatar", 0)
        val sharedTombolId = sharedPred.getInt("tombolTerpilih", 3)
        if (sharedLatarId.equals(0) && sharedTombolId.equals(3)){
            binding.latar.setBackgroundColor(getColor(R.color.white))
        } else {
            binding.latar.setBackgroundColor(getColor(sharedLatarId))
            binding.toggleGroupColor.check(sharedTombolId)
        }
    }

    private fun gantiUkuran(fontParagrafSmall: Int, checkedButtonId: Int) {
        val ukuranBaru: Int = fontParagrafSmall
        val tombolUkuran : Int = checkedButtonId
        val Editor:SharedPreferences.Editor = sharedPred.edit()
        Editor.putInt("ukuranBaru", ukuranBaru)
        Editor.putInt("tombolUkuranTerpilih", tombolUkuran)
        Editor.apply()
        loadUkuranbaru()
    }

    private fun loadUkuranbaru(){
        val sharedUkuranId = sharedPred.getInt("ukuranBaru", 0)
        val sharedTombolUkuranId = sharedPred.getInt("tombolUkuranTerpilih", 3)
        if (sharedUkuranId.equals(0)){
            binding.body1.setTextAppearance(R.style.FontParagraf)
            binding.body2.setTextAppearance(R.style.FontParagraf)
        } else {
            binding.body1.setTextAppearance(sharedUkuranId)
            binding.body2.setTextAppearance(sharedUkuranId)
            binding.toggleGroup.check(sharedTombolUkuranId)
        }
    }
}
package com.dhandev.eepa.materi

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dhandev.eepa.R
import com.dhandev.eepa.databinding.ActivityMateriPengantarBinding
import com.dhandev.eepa.ui.imageViewer.ImageViewerActivity
import java.net.URL


class MateriPengantar : AppCompatActivity() {

    private lateinit var binding: ActivityMateriPengantarBinding
    private lateinit var sharedPred : SharedPreferences
    var URL : String = "https://www.dictio.id/uploads/db3342/original/3X/0/b/0b8ddd6ce6f47c7b2c3e4de9310d128c547c97a8.jpeg"
    var page = 1
    var desc = "Demokritus tercatat sebagai orang pertama yang mencetuskan istilah atom"

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
            gambar.setOnClickListener {
                openImageViewer(URL, desc)
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

        }
    }

    private fun openImageViewer(url : String, desc : String) {
        val Editor:SharedPreferences.Editor = sharedPred.edit()
        Editor.putString("url", url)
        Editor.putString("desc", desc)
        Editor.apply()
        val intent = Intent(this, ImageViewerActivity::class.java)
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
            bagian.text = getString(R.string.intro_2)
            body1.text = "\tSayangnya, selama berabad-abad berlalu tidak ada perkembangan secara saintifik yang berarti mengenai atom karena sebagian besar filsuf sepakat dengan konsep Aristoteles. Baru pada abad ke-17, tepatnya tahun 1661, Robert Boyle memaparkan diskusinya mengenai atom pada literatur ilmiah yang berjudul The Sceptical Chymist. Namun, ahli kimia dan meteorologi Inggris John Dalton dikreditkan dengan teori atom modern pertama, seperti yang dijelaskan dalam bukunya A New System of Chemical Philosophy yang terbit pada tahun 1808 (bagian I) dan 1810 (bagian II). \tEksperimen Dalton dengan menggunakan gas tercatat sebagai pengukuran paling awal untuk mengetahui massa atom, konsep struktur atom, dan reaktivitas. Secara garis besar, Dalton menjelaskan bahwa:\n1. Semua atom dari unsur tertentu adalah identik\n2. Massa dan ukuran atom berbeda antara unsur satu dan yang lainnya\n3. Atom tidak bisa dihancurkan. Reaksi kimia bisa mengakibatkan penyusunan kembali, tapi tidak akan menghancurkan atau membuat atom baru."
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
            bagian.text = getString(R.string.intro_3)
            body1.text = "\t Pada akhir abad ke-19, tepatnya pada tahun 1897, J.J.Thomson bereksperimen dengan alat tabung sinar katoda (cathode ray tubes). Tabung sinar katoda adalah tabung gelas yang tertutup dari adanya udara masuk. Tegangan tinggi diberikan pada elektroda di salah satu ujung tabung, hal ini menyebabkan terpancarnya partikel dari katoda  (elektroda bermuatan negatif) ke anoda (elektroda bermuatan positif). Tabung ini disebut dengan tabung sinar katoda karena pancaran partikel (sinar katoda) berasal dari katoda. Sinar katoda dapat dideteksi dengan mewarnai ujung jauh tabung dari anoda dengan menggunakan fosfor. Fosfor tersebut akan memercik atau memancarkan cahaya ketika terkena sinar katoda. Untuk lebih jelas, perhatikan gambar pada atas halaman ini"
        }
    }

    private fun openPageFour() {
        TODO("Not yet implemented")
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
        } else {
            binding.body1.setTextAppearance(sharedUkuranId)
            binding.toggleGroup.check(sharedTombolUkuranId)
        }
    }
//TODO: Buat class khusus pengaturan warna latar dan ukuran
}
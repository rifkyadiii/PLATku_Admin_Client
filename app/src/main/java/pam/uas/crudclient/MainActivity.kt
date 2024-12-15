package pam.uas.crudclient

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pam.uas.crudclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val cariNopol: String = binding.cariNopol.text.toString()
            if (cariNopol.isNotEmpty()) {
                readData(cariNopol)
            } else {
                Toast.makeText(this, "Mohon masukkan nomor polisi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.keluar.setOnClickListener {
            finishAffinity()
        }

        binding.aboutButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readData(nomorPolisi: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Informasi Kendaraan")
        databaseReference.child(nomorPolisi).get().addOnSuccessListener {
            if (it.exists()) {
                val namaPemilik = it.child("namaPemilik").value
                val merkKendaraan = it.child("merkKendaraan").value
                val wilayah = it.child("wilayah").value
                Toast.makeText(this, "Data ditemukan", Toast.LENGTH_SHORT).show()
                binding.readNamaPemilik.text = namaPemilik.toString()
                binding.readMerkKendaraan.text = merkKendaraan.toString()
                binding.readWilayah.text = wilayah.toString()
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
        }
    }
}
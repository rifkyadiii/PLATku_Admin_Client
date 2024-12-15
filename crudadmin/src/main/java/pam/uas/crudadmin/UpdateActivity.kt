package pam.uas.crudadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pam.uas.crudadmin.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nomorPolisi = intent.getStringExtra("nomorPolisi")

        if (nomorPolisi != null) {
            binding.referenceNomorPolisi.setText(nomorPolisi)
            binding.referenceNomorPolisi.isEnabled = false
            fetchData(nomorPolisi)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.updateButton.setOnClickListener {
            val updateNamaPemilik: String = binding.updateNamaPemilik.text.toString()
            val updateMerkKendaraan: String = binding.updateMerkKendaraan.text.toString()
            val updateWilayah: String = binding.updateWilayah.text.toString()

            if (nomorPolisi != null) {
                updateData(nomorPolisi, updateNamaPemilik, updateMerkKendaraan, updateWilayah)
            }
        }
    }

    private fun fetchData(nomorPolisi: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Informasi Kendaraan")
        databaseReference.child(nomorPolisi).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val namaPemilik = snapshot.child("namaPemilik").value?.toString() ?: "Tidak tersedia"
                val merkKendaraan = snapshot.child("merkKendaraan").value?.toString() ?: "Tidak tersedia"
                val wilayah = snapshot.child("wilayah").value?.toString() ?: "Tidak tersedia"

                binding.updateNamaPemilik.setText(namaPemilik)
                binding.updateMerkKendaraan.setText(merkKendaraan)
                binding.updateWilayah.setText(wilayah)
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Terjadi kesalahan saat mengambil data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(nomorPolisi: String, namaPemilik: String, merkKendaraan: String, wilayah: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Informasi Kendaraan")
        val dataKendaraan = mapOf<String, String>("namaPemilik" to namaPemilik, "merkKendaraan" to merkKendaraan, "wilayah" to wilayah)

        databaseReference.child(nomorPolisi).updateChildren(dataKendaraan).addOnSuccessListener {
            binding.updateNamaPemilik.text.clear()
            binding.updateMerkKendaraan.text.clear()
            binding.updateWilayah.text.clear()

            Toast.makeText(this, "Data berhasil diupdate.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@UpdateActivity, ManageActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Tidak dapat melakukan update.", Toast.LENGTH_SHORT).show()
        }
    }
}
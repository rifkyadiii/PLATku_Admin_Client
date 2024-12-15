package pam.uas.crudadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pam.uas.crudadmin.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ManageActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.searchButton.setOnClickListener {
            val cariNopol: String = binding.cariNopol.text.toString()
            if (cariNopol.isNotEmpty()) {
                readData(cariNopol)
            } else {
                Toast.makeText(this, "Mohon masukkan nomor polisi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteButton.setOnClickListener {
            val cariNopol: String = binding.cariNopol.text.toString()
            if (cariNopol.isNotEmpty()) {
                deleteData(cariNopol)
            } else {
                Toast.makeText(this, "Mohon masukkan nomor polisi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.updateButton.setOnClickListener {
            val cariNopol: String = binding.cariNopol.text.toString()
            if (cariNopol.isNotEmpty()) {
                val intent = Intent(this@ManageActivity, UpdateActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Mohon masukkan nomor polisi", Toast.LENGTH_SHORT).show()
            }
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

                binding.updateButton.setOnClickListener {
                    val intent = Intent(this@ManageActivity, UpdateActivity::class.java)
                    intent.putExtra("nomorPolisi", nomorPolisi)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData(nomorPolisi: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus data dengan Nomor Polisi: $nomorPolisi?")

        builder.setPositiveButton("Ya") { _, _ ->
            databaseReference = FirebaseDatabase.getInstance().getReference("Informasi Kendaraan")
            databaseReference.child(nomorPolisi).removeValue().addOnSuccessListener {
                binding.cariNopol.text.clear()
                binding.readNamaPemilik.text = ""
                binding.readMerkKendaraan.text = ""
                binding.readWilayah.text = ""
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

}
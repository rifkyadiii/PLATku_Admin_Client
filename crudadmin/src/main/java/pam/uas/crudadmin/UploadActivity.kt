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
import pam.uas.crudadmin.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val namaPemilik = binding.uploadNamaPemilik.text.toString()
            val merkKendaraan = binding.uploadMerkKendaraan.text.toString()
            val wilayah = binding.uploadWilayah.text.toString()
            val nomorPolisi = binding.uploadNomorPolisi.text.toString()

            if (namaPemilik.isEmpty() || merkKendaraan.isEmpty() || wilayah.isEmpty() || nomorPolisi.isEmpty()) {
                Toast.makeText(this@UploadActivity, "Harap isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            databaseReference = FirebaseDatabase.getInstance().getReference("Informasi Kendaraan")

            databaseReference.child(nomorPolisi).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(this@UploadActivity, "Data sudah ada", Toast.LENGTH_SHORT).show()
                    } else {
                        val kendaraanData = KendaaraanData(namaPemilik, merkKendaraan, wilayah, nomorPolisi)
                        databaseReference.child(nomorPolisi).setValue(kendaraanData).addOnSuccessListener {
                            binding.uploadNamaPemilik.text?.clear()
                            binding.uploadMerkKendaraan.text?.clear()
                            binding.uploadWilayah.text?.clear()
                            binding.uploadNomorPolisi.text?.clear()

                            Toast.makeText(this@UploadActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@UploadActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@UploadActivity, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UploadActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


        binding.backButton.setOnClickListener {
            val intent = Intent(this@UploadActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
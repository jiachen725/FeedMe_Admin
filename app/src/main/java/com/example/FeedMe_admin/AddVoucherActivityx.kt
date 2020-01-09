package com.example.FeedMe_admin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_addvoucher.*
import java.util.*


class AddVoucherActivityx : AppCompatActivity() {

    lateinit var voucherName: EditText
    lateinit var voucherPoint: EditText
    lateinit var voucherExpireDate: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addvoucher)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        select_img.setOnClickListener{
            Log.d("AddVoucherActivityx", "Select Image")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        upload_pic_btn.setOnClickListener{
            upload_pic_btn.isEnabled=false
            uploadImg()

        }

        voucherName = findViewById(R.id.voucher_name)
        voucherPoint = findViewById(R.id.voucher_point)
        voucherExpireDate = findViewById(R.id.voucher_expire_date)

        val mStorage = FirebaseStorage.getInstance()


        val storeRef = mStorage.reference

    }



    private fun uploadDatatoFirebase(imgUrl:String){
        val vName = voucherName.text.toString().trim()
        val vPoint = voucherPoint.text.toString().trim()
        val expireD = voucherExpireDate.text.toString().trim()


        if(img_uri != null || expireD.isEmpty() || vName.isEmpty() || vPoint.isEmpty()){
            Toast.makeText(this,"Please not left empty", Toast.LENGTH_SHORT).show()


        }


        val reference = FirebaseDatabase.getInstance().getReference("Voucher")
        val voucherD = Voucher(expireD,vName, vPoint, imgUrl)


        reference.push().child("image/").setValue(voucherD).addOnSuccessListener {

            Toast.makeText(this,"Success Upload",Toast.LENGTH_SHORT).show()

        }

        Log.d("MainActicity","Voucher Name is :" +vName)
        Log.d("AddVoucherActivityx","Voucher Point is :" +vPoint)
        Log.d("AddVoucherActivityx","Voucher Point is :" +expireD)

    }



    private fun uploadImg() {

        if (img_uri == null) return


        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(img_uri!!).addOnSuccessListener {
            Log.d("AddVoucherActivityx", "Success Upload: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("AddVoucherActivityx", "File Location: $it")
                uploadDatatoFirebase(it.toString())
                Toast.makeText(this,"Success Upload",Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }

    }

    var img_uri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data == null){
            Log.d("AddVoucherActivityx","Image Error")
            return

        }
        if(requestCode == 0 && resultCode==Activity.RESULT_OK && data !=null){

            Log.d("AddVoucherActivityx","Image selected")
        }

        img_uri = data?.data
        val img_bitmap = MediaStore.Images.Media.getBitmap(contentResolver, img_uri)
        val bitmapDrawable = BitmapDrawable(img_bitmap)
        select_img.setBackgroundDrawable(bitmapDrawable)
    }

   }

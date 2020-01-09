package com.example.FeedMe_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class enterKg : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var currentPoint = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_kg)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val intent = intent //Asking "who called me?"
        val id = intent.getStringExtra("userid")

        val firebase = FirebaseDatabase.getInstance().getReference("member")
        val rootRef = firebase.child(id.toString())
        val confirmBtn = findViewById<Button>(R.id.confirmbtn)


                confirmBtn.setOnClickListener(){
                    confirm(rootRef)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.e("DataBase", "Start User")
        val name =findViewById<TextView>(R.id.name)
        val intent = intent //Asking "who called me?"
        val id = intent.getStringExtra("userid")

        val firebase = FirebaseDatabase.getInstance().getReference("member")
        val rootRef = firebase.child(id.toString())
        Log.e("DataBase", "Start User"+id+rootRef)
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val member = dataSnapshot.getValue(member::class.java)
                Log.e("DataBase", "Current User"+member!!.username +"   "+rootRef)
                name.text = member!!.username
                currentPoint = member!!.memPoint

            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }


    fun confirm(reference: DatabaseReference ){

        auth = FirebaseAuth.getInstance()

        val gram = findViewById<EditText>(R.id.gram)
        val numGram = gram.text.toString().toInt()
        val point= numGram*0.20

        Log.e("DataBase", "garm"+numGram)


        if(gram.text.isEmpty()==true){
            Toast.makeText(this,"Please enter a value", Toast.LENGTH_SHORT).show()
            return
        }
        if(numGram <=0  ){
            Toast.makeText(this,"Cannot be 0 or negative", Toast.LENGTH_SHORT).show()
            return
        }

        if(numGram>0) {


            Log.e("DataBase", "point User"+ point)

        reference.child("memPoint").setValue(currentPoint+point).addOnCompleteListener{task->
            if(task.isSuccessful) {
                this.finish()
                Toast.makeText(this,"Complete: "+String.format("%.0f",point)+" Points Earned", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this,"Check your connection and scan again", Toast.LENGTH_SHORT).show()

            }
        }

        }

    }



}

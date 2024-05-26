package com.example.instagramclonekotlin.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclonekotlin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val view = binding.root
        setContentView(view)
        val currentUser = auth.currentUser
        if (currentUser !=null){
            val intent = Intent(this , FeedActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
    fun SıgnİnClick(view : View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordTextT.text.toString()
        if (email.equals("")||password.equals("")){
            Toast.makeText(this , "Enter email and password" , Toast.LENGTH_LONG).show()

        }else{
            auth.signInWithEmailAndPassword(email , password).addOnSuccessListener {
                val intent = Intent(this@MainActivity , FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity , it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
    fun SignUpClicked (view : View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordTextT.text.toString()
        if (email.equals("") ||password.equals("")) {
            Toast.makeText(this , "Enter email and password " , Toast.LENGTH_LONG).show()

        }else{
            auth.createUserWithEmailAndPassword(email , password).addOnSuccessListener {
                //success
                val intent = Intent(this@MainActivity , FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()

            }

        }

    }
}
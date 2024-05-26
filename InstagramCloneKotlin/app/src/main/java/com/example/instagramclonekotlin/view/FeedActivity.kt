package com.example.instagramclonekotlin.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclonekotlin.R
import com.example.instagramclonekotlin.adapter.FeedRecyclerAdapter
import com.example.instagramclonekotlin.databinding.ActivityFeedBinding
import com.example.instagramclonekotlin.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // FirebaseAuth ve FirebaseFirestore başlatılıyor
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // RecyclerView ve adaptör işlemleri
        postArrayList = ArrayList()
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = feedAdapter

        // Verileri al
        getData()

        // FloatingActionButton tıklama olayını ayarlayın
        binding.floatingActionButton.setOnClickListener {
            PopUpMenuTıklandı(it)
        }
    }

    fun PopUpMenuTıklandı(view: View) {
        val popup = PopupMenu(this@FeedActivity, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.insta_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_post -> {
                    auth.signOut()
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        popup.show() // PopupMenu'yu göstermek için bu satırı eklemelisiniz
    }

    private fun getData() {
        db.collection("Posts")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (value != null) {
                    processPostData(value)
                }
            }
    }

    private fun processPostData(value: QuerySnapshot) {
        postArrayList.clear()
        for (document in value.documents) {
            val email = document.getString("email") ?: ""
            val comment = document.getString("comment") ?: ""
            val downloadUrl = document.getString("downloadUrl") ?: ""

            val post = Post(email, comment, downloadUrl)
            postArrayList.add(post)
        }
        feedAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.insta_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            auth.signOut()
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

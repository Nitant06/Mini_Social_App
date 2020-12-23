package com.nitant.socialapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.nitant.socialapp.daos.PostDao
import com.nitant.socialapp.models.Post
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PostAdapter.IPostAdapter {

    lateinit var adapter:PostAdapter
    lateinit var postDao:PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)

    fab.setOnClickListener {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
        finish()
    }

        setUpRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.menuLogout){
            Firebase.auth.signOut()
            startActivity(Intent(this,SignInActivity::class.java))
        }

        return true

    }

    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postCollections = postDao.postCollections
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {

        postDao.updateLikes(postId)
    }
}
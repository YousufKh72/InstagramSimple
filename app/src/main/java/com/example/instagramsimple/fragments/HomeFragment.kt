package com.example.instagramsimple.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramsimple.MainActivity
import com.example.instagramsimple.Post
import com.example.instagramsimple.PostAdapter
import com.example.instagramsimple.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class HomeFragment : Fragment() {
    lateinit var postsRecyclerView : RecyclerView

    lateinit var adapter: PostAdapter
//    var createdAt: String = ""
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This is where we set up our views and click listeners

        postsRecyclerView = view.findViewById(R.id.postRecyclerView)

        //Steps to populate RecylerView
        // 1. Create layout for each row in list
        // 2. Create data source for each row (this is the Post Class)
        // 3. Create adapter that will bridge data and row layout (PostAdapter)
        // 4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter
        // 5. Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    //Query for all posts in our server
    open fun queryPosts(){

        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all Post objects
        query.include(Post.KEY_USER)
        // Return posts in descending order: ie newer posts will appear first
        query.addDescendingOrder("createdAt")
        // Only return 20 posts.
        query.limit = 20
        query.findInBackground (object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e!= null){
                    //Something has went wrong
                    Log.e(TAG, "Error fetching posts")
                }else {
                    if (posts != null){
                        for (post in posts){
                            Log.i(TAG, "Post: " + post.getDescription())
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
    companion object {
        const val  TAG = "HomeFragment"
    }
}
package com.example.android.myinstagram.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myinstagram.Adapter.UserAdapter
import com.example.android.myinstagram.Model.User
import com.example.android.myinstagram.R
import com.google.firebase.database.*
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import io.grpc.Context

class SearchFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var search_bar:SocialAutoCompleteTextView
    lateinit var mUsers:ArrayList<User>
    lateinit var userAdapter:UserAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView= view.findViewById(R.id.recycler_view_user)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(context)
        search_bar=view.findViewById(R.id.search_bar)
        mUsers=ArrayList()
        userAdapter= UserAdapter(context, mUsers,true)
        recyclerView.adapter=userAdapter
        readUsers()
        return view
    }

    private fun readUsers() {
        val reference:DatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users")
        reference.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(TextUtils.isEmpty(search_bar.text.toString())){
                     mUsers.clear()
                     for(indexData:DataSnapshot in snapshot.children){
                          mUsers.add(indexData.getValue(User::class.java) as User)
                     }
                    userAdapter.notifyDataSetChanged()
                    Log.d("hello",mUsers.get(0).name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error","data not updated")
            }

        })
    }

}

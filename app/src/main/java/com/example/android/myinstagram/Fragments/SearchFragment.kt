package com.example.android.myinstagram.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.TelephonyCallback
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myinstagram.Adapter.TagAdapter
import com.example.android.myinstagram.Adapter.UserAdapter
import com.example.android.myinstagram.Model.User
import com.example.android.myinstagram.R
import com.google.firebase.database.*
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var search_bar:SocialAutoCompleteTextView
    lateinit var mUsers:ArrayList<User>
    lateinit var userAdapter:UserAdapter

    lateinit var recyclerViewTags: RecyclerView
    lateinit var mHashTag:ArrayList<String>
    lateinit var mHashTagsCount:ArrayList<String>
    lateinit var tagAdapter: TagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView= view.findViewById(R.id.recycler_view_user)
        recyclerViewTags=view.findViewById(R.id.recycler_view_tags)
        recyclerViewTags.setHasFixedSize(true)
        recyclerViewTags.layoutManager=LinearLayoutManager(context)
        mHashTag=ArrayList()
        mHashTagsCount=ArrayList()
        tagAdapter=TagAdapter(context,mHashTag,mHashTagsCount)
        recyclerViewTags.adapter=tagAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(context)
        search_bar=view.findViewById(R.id.search_bar)
        mUsers=ArrayList()
        userAdapter= UserAdapter(context, mUsers,true)
        recyclerView.adapter=userAdapter
        readUsers()
        readTags()
        search_bar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUser(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

        })
        return view
    }

    private fun readTags() {
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mHashTag.clear()
                mHashTagsCount.clear()
                for(itemData: DataSnapshot in snapshot.children){
                    mHashTag.add(itemData.key.toString())
                    mHashTagsCount.add(itemData.childrenCount.toString())
                }
                tagAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
                    Log.d("hello", mUsers[0].name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error","data not updated")
            }

        })
    }
    private fun searchUser(s:String) {
        val query:Query=FirebaseDatabase.getInstance().getReference().child("Users")
            .orderByChild("username").startAt(s).endAt(s+"\uf8ff")
        query.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsers.clear()
                for(indexData:DataSnapshot in snapshot.children){
                    mUsers.add(indexData.getValue(User::class.java) as User)
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun filter(text:String){
        val mSearchTags:ArrayList<String> =ArrayList()
        val mSearchTagsCount:ArrayList<String> =ArrayList()

        for(s:String in mHashTag){
            if(s.toLowerCase().contains(text.toLowerCase())){
                mSearchTags.add(s)
                mSearchTagsCount.add(mHashTagsCount.get(mHashTag.indexOf(s)))
            }
        }
        tagAdapter.filter(mSearchTags,mSearchTagsCount)
    }


}

package com.example.android.myinstagram.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.myinstagram.Model.User
import com.example.android.myinstagram.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(mContext: Context?, mUsers: ArrayList<User>, isFragment: Boolean) : RecyclerView.Adapter<UserAdapter.viewHolder>() {

    val mContext:Context?
    var mUsers:List<User>
    var isFragment: Boolean
    lateinit var firebaseUser:FirebaseUser

    init{
        this.mContext=mContext
        this.mUsers=mUsers
        this.isFragment=isFragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view: View=LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false)
        return viewHolder(view)
    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        firebaseUser= FirebaseAuth.getInstance().currentUser!!
        val user:User=mUsers.get(position)
        holder.btnFollow.visibility=View.VISIBLE
        holder.username.text=user.username
        holder.fullname.text=user.name
        Picasso.get().load(user.imageurl).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile)

        isFollowed(user.id,holder.btnFollow)

        if(user.id.equals(firebaseUser.uid)){
            holder.btnFollow.visibility=View.GONE
        }
    }

    private fun isFollowed(id: String, btnFollow: Button) {
        val reference: DatabaseReference=FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.uid).child("following")
       reference.addValueEventListener(object: ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.child(id).exists()){
                   btnFollow.text="following"
               }
               else
                   btnFollow.text="follow"
           }

           override fun onCancelled(error: DatabaseError) {

           }

       })
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageProfile:CircleImageView= itemView.findViewById(R.id.image_profile)
        var username:TextView=itemView.findViewById(R.id.username)
        var fullname:TextView=itemView.findViewById(R.id.fullName)
        var btnFollow:Button=itemView.findViewById(R.id.button_follow)
    }
}
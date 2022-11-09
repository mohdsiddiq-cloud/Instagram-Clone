package com.example.android.myinstagram.Adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myinstagram.R
import org.w3c.dom.Text

class TagAdapter(mContext:Context?,mTags:List<String>,mTagsCount:List<String>): RecyclerView.Adapter<TagAdapter.ViewHolder>(){
     var mContext:Context?
     var mTags:List<String>
     var mTagsCount:List<String>

    init {
        this.mContext=mContext
        this.mTags=mTags
        this.mTagsCount=mTagsCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(mContext).inflate(R.layout.tag_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tag.text="#"+mTags.get(position)
        holder.noOfPosts.text=mTagsCount.get(position)+ " posts"
    }

    override fun getItemCount(): Int {
        return mTags.size
    }
    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag:TextView=itemView.findViewById(R.id.hashtag)
        val noOfPosts:TextView=itemView.findViewById(R.id.number_of_posts)
    }
    fun filter(filterTags:ArrayList<String>,filterTagsCount:ArrayList<String>){
        this.mTags=filterTags
        this.mTagsCount=filterTagsCount
        notifyDataSetChanged()
    }
}
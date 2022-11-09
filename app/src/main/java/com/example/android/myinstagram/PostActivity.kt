package com.example.android.myinstagram

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.hendraanggrian.appcompat.socialview.Hashtag
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.theartofdev.edmodo.cropper.CropImage

class PostActivity : AppCompatActivity() {
    lateinit var post:TextView
    lateinit var close:ImageView
    lateinit var image_added:ImageView
    lateinit var description:SocialAutoCompleteTextView
    lateinit var imageUri:Uri
    lateinit var uploadtask:UploadTask
    lateinit var imageURL:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        close=findViewById(R.id.close)
        image_added=findViewById(R.id.emage_added)
        post=findViewById(R.id.post)
        description=findViewById(R.id.description)
        close.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        post.setOnClickListener {
            upload()
        }
        CropImage.activity().start(this)
    }
    private fun upload() {
        var pd:ProgressDialog= ProgressDialog(this)
        pd.setMessage("Uploading..")
        pd.show()

        if(imageUri!=null){
            var s=System.currentTimeMillis().toString()
            s=s+"."
            s=s+getFile(imageUri)
            val filepath:StorageReference=FirebaseStorage.getInstance().getReference("posts").
            child(s)
            uploadtask=filepath.putFile(imageUri)
            uploadtask.continueWithTask {
                if(!it.isSuccessful){
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                }
                return@continueWithTask filepath.downloadUrl
            }.addOnCompleteListener {
                var downloadUri: Uri=it.getResult()
                imageURL=downloadUri.toString()
               var ref: DatabaseReference= FirebaseDatabase.getInstance().getReference("posts")
               var postId: String= ref.push().key.toString()
                var map:HashMap<String,String> =HashMap<String,String>()
                map.put("postId",postId)
                map.put("imageURL",imageURL)
                map.put("description",description.text.toString())
                FirebaseAuth.getInstance().currentUser?.let { it1 -> map.put("publisher", it1.uid) }
                ref.child(postId).setValue(map)
                var mHashref:DatabaseReference=FirebaseDatabase.getInstance().getReference("HashTags")
                var hashTags:List<String> =description.hashtags
                if(!hashTags.isEmpty()){
                    var tag:String
                    for(tag in hashTags){
                        map.clear()
                        map.put("tag",tag.toLowerCase())
                        map.put("postId",postId)
                        mHashref.child(tag.toLowerCase()).child(postId).setValue(map)
                    }
                }
                pd.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"something Went wrong",Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this,"No image was selected",Toast.LENGTH_LONG).show()
        }
    }

    private fun getFile(uri: Uri): String {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.contentResolver.getType(uri)).toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK){
            var result:CropImage.ActivityResult=CropImage.getActivityResult(data);
            imageUri=result.uri
            image_added.setImageURI(imageUri)
        }
        else{
            Toast.makeText(this,"Try again",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val hashtagAdapter: ArrayAdapter<Hashtag> = HashtagArrayAdapter(applicationContext)
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(itemData: DataSnapshot in snapshot.children){
                    hashtagAdapter.add(itemData.key?.let { Hashtag(it,
                        itemData.childrenCount.toInt()
                    ) })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        description.hashtagAdapter=hashtagAdapter
    }
}